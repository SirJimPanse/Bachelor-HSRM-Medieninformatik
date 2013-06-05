using System;
using System.Collections.Generic;
using Coordinate = System.Tuple<int, int>; // Eine Koordinate ist ein Integerpaar
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;
using System.Windows.Markup;
using System.Xml;
using System.IO;
using System.Windows.Media;
using Client.Classes;
using System.Windows.Media.Imaging;

namespace Client.Classes
{
    /** Ein GridCanvas ist ein Canvas, das aus Tiles (!= Teile) besteht, die an einem Grid ausgerichtet sind. **/
    class GridCanvas : Canvas
    {
        /** Die Anzahl der Tiles in x Richtung **/
        public int gridX;

        /** Die Anzahl der Tiles in y Richtung **/
        public int gridY;

        public List<UIElement> products;

        private double _width;
        public double width {
            get { return _width; }
            set { _width = value; }
        }

        private double _height;
        public double height {
            get { return _height; }
            set { _height = value; }
        }

        /** Eine Liste aller selektierten Items **/
        public IEnumerable<GridItem> SelectedItems
        {
            get
            {
                var selectedItems = from item in this.Children.OfType<GridItem>()
                                    where item.IsSelected == true
                                    select item;

                return selectedItems;
            }
        }

        /** Default Konstruktor mit vorgegebener Anzahl Tiles **/
        public GridCanvas() : this(MainWindow.GRIDCANVAS_COLUMNS, MainWindow.GRIDCANVAS_ROWS) { this.products = new List<UIElement>(); }

        /** Setter Konstruktor für die Anzahl Tiles in x und y Richtung **/
        public GridCanvas(int x, int y) : base() {
            this.gridX = x;
            this.gridY = y;
        }

        /** Gibt die Größe eines Tiles an (basiert auf Fenstergröße) **/
        public Size GetTileSize() {
            Size result   = new Size();
            #if DEBUG
                //Console.WriteLine("GridCanvas.GetTileSize: RenderSize = " + RenderSize.Width + ", " + RenderSize.Height);
            #endif
            result.Width  = RenderSize.Width / gridX;
            result.Height = RenderSize.Height / gridY;
            return result;
        }

        /** Gibt die Koordinate des Tile an auf der das UIElement liegt **/
        public Coordinate CalcGridCoord(Point pos)
        {
            Size s = this.GetTileSize();
            int x = (int)(pos.X / s.Width);
            int y = (int)(pos.Y / s.Height);
            return new Coordinate(x, y);
        }

        /** Errechnet die pixelgenaue Position für ein UIElement (liegt auf dem Raster) **/
        private Point CalcGridPos(UIElement element) {
            Size s = this.GetTileSize();
            Point p = new Point(Canvas.GetLeft(element),Canvas.GetTop(element));
            return CalcGridPos(p);
        }

        /** Errechnet die pixelgenaue Position für einen Punkt (liegt auf dem Raster) **/
        private Point CalcGridPos(Point p)
        {
            Size s = this.GetTileSize();

            #if DEBUG
                Console.WriteLine("GridCanvas.CalcGridPos: p = " + p);
            #endif

            // erste Näherung für die Position an die gesnappt werden soll
            double xSnap = p.X % s.Width;
            double ySnap = p.Y % s.Height;

            // x Richtung auf Basis der Tilesize errechnen
            if (xSnap <= s.Width / 2.0)
                xSnap *= -1;
            else
                xSnap = s.Width - xSnap;

            // y Richtung auf Basis der Tilesize errechnen
            if (ySnap <= s.Height / 2.0)
                ySnap *= -1;
            else
                ySnap = s.Height - ySnap;

            xSnap += p.X;
            ySnap += p.Y;

            return new Point(xSnap, ySnap);
        }

        /** Korrigiert die Position des UIElements dahingehend, dass dieses auf das naheliegendste Tile geschoben wird **/
        public void SnapToGrid(UIElement element) {
            Point       p = this.CalcGridPos(element);
            Coordinate  c = this.CalcGridCoord(p);
            Size        s = this.GetTileSize();

            #if DEBUG
                Console.WriteLine("GridCanvas.SnapToGrid: posX = " + p.X + "\nposY = " + p.Y);
                Console.WriteLine("GridCanvas.SnapToGrid: gridX = " + c.Item1 + "\ngridY = " + c.Item2);
                Console.WriteLine("GridCanvas.SnapToGrid: width = " + s.Width + "\nheight = " + s.Height);
            #endif
        
            Canvas.SetLeft(element, p.X);
            Canvas.SetTop(element, p.Y);
        }

        /** Ändert die Pixelgröße eines Tiles (z.B. bei Änderung der Fenstergröße) **/
        public void ResizeToGrid(UIElement element) {
           //TODO: Resize implementieren
        }

        /** Deselektiert alle Items in der Selektionsliste **/
        internal void DeselectAll()
        {
            foreach (GridItem item in this.SelectedItems)
            {
                item.IsSelected = false;
            }
        }

        /** Der DropEventHandler platziert ein GridItem auf dem Canvas **/
        // TODO: Refactoring.. viel zu lang die Methode
        protected override void OnDrop(DragEventArgs e)
        {
            Console.WriteLine("####### OnDrop-2D ######");
            base.OnDrop(e);
            Image imgObj = e.Data.GetData("GRID_ITEM") as Image;
            int classID = int.Parse(e.Data.GetData("CLASS_ID").ToString());

            BitmapImage contentImg = imgObj.Source as BitmapImage;

            #if DEBUG
                Console.WriteLine("GridCanvas.OnDrop: DragEventArgs.GetData('GRID_ITEM') lieferte " + contentImg.ToString());
            #endif
            
            if (contentImg != null)
            {
                GridItem newItem = null;

                if (contentImg != null)
                {
                    newItem = getGridItem(contentImg);

                    #if DEBUG
                        Console.WriteLine("GridCanvas.OnDrop: neues GridItem erstellt mit Contentelement " + newItem.Content.ToString());
                    #endif

                    Point position = e.GetPosition(this);

                    //TODO: Item darf nur hinzugefügt werden, wenn die Position frei ist..
                    double newX = Math.Max(0, position.X - newItem.Width / 2);
                    double newY = Math.Max(0, position.Y - newItem.Height / 2);

                    Point p1 = new Point(newX,newY);
                    Point p = this.CalcGridPos(p1);
                    Coordinate coord = this.CalcGridCoord(p);

                    #if DEBUG
                        Console.WriteLine("GridCanvas.OnDrop: neue Maschine auf dem Server erstellen lassen mit X = " + coord.Item1 + " und Y = " + coord.Item2);
          
                    #endif

                    // GO server..
                    newItem.instanceId = ClientServerUtil.getInstance().createItem(classID, coord.Item1, coord.Item2, FactoryServiceReference.direction.NORTH);
                    if (newItem.instanceId != "")
                    {
                        //placeItem(newItem, coord.Item1, coord.Item2);
                        this.DeselectAll();
                        newItem.IsSelected = true;
                    }
                    else
                    {
                        this.DeselectAll();
                    }
                }

                e.Handled = true;
            }
        }

        /** Platziert eine Liste von Produkten als UIElement auf einem durch x und y bestimmten Feld des Grids **/
        public void addProducts(List<UIElement> products, int x, int y)
        {
            foreach (UIElement product in products)
            {
                this.products.Add(product);
                this.placeItem(product,x,y);
            }
        }

        /** Entfernt alle Produkte aus dem Grid **/
        public void clearProducts()
        {
            foreach (UIElement product in products)
            {
                this.Children.Remove(product);
            }
            this.products = new List<UIElement>();
        }

        /** Gibt eine TextBox für einen bestimmten text zurück **/
        public static TextBox getTextBox(string text)
        {
            TextBox tb = new TextBox();
            tb.Text = text;
            return tb;
        }
        
        /** Gibt ein ins Grid passende GridItem zurück, das das BitmapImage bitImg anzeigt **/
        public GridItem getGridItem(BitmapImage bitImg) 
        {
            Image img = new Image();
            img.Source = bitImg;
            GridItem newItem = new GridItem();
            newItem.Content = img;

            if (img.MinHeight != 0 && img.MinWidth != 0)
            {
                newItem.Width = img.MinWidth * 2;
                newItem.Height = img.MinHeight * 2;
            }
            else
            {
                Size s = GetTileSize();
                newItem.Width = s.Width;
                newItem.Height = s.Height;
            }

            return newItem;
        }


        /* Platziert ein neues Item auf dem Grid (erstes Platzieren des jeweiligen Items) */
        public void placeItem(UIElement item, int x, int y)
        {
            Children.Add(item);
            moveItem(item, x, y);            
        }

        /* Bewegt ein bereits bestehendes Item item an eine andere Position (x,y) */
        public void moveItem(UIElement item, int x, int y)
        {
            Size s = GetTileSize();
            double dX = x * s.Width;
            double dY = y * s.Height;
            GridCanvas.SetLeft(item, dX);
            GridCanvas.SetTop(item, dY);
            SnapToGrid(item);
            item.InvalidateVisual();
            InvalidateVisual();
        }

        /** Entfernt alle selektierten Items vom Grid **/
        public void removeSelected() {
            List<GridItem> sel = new List<GridItem>();

            foreach (GridItem item in SelectedItems) {
                sel.Add(item);
            }

            foreach (GridItem item in sel) {
                ClientServerUtil.getInstance().deleteItem(item.instanceId);
            }
        }

        /** Entfernt das item mit der ID itemId vom Grid **/
        public void remove(string itemId)
        {
            GridItem itemToBeRemoved = getItemForId(itemId);
            this.Children.Remove(itemToBeRemoved);
        }

        /** gibt das GridItem mit der ID itemId zurück **/
        public GridItem getItemForId(string itemId)
        {
            foreach (GridItem item in this.Children)
            {
                if (item.instanceId == itemId)
                {
                    return item;
                }
            }
            return null;
        }

        /** Entfernt alle GridItems vom Grid **/
        public void Clear()
        {
            this.Children.Clear();
        }
      
    }
}
