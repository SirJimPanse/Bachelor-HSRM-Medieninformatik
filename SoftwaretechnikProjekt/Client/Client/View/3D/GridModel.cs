using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using HelixToolkit.Wpf;
using System.ComponentModel;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Media3D;
using System.Windows.Forms;
using System.Threading;
using Client.Classes;
using Client.Communication;
using Client.FactoryServiceReference;
using Sim = Client.SimulationServiceReference;

namespace Client.View._3D
{
    class GridModel : INotifyPropertyChanged
    {
        public Model3DGroup Model { get; set; }

        public double width { get; set; }
        public double height { get; set; }

        public double xOffset;
        public double yOffset;

        public List<GridItem3D> modelList { get; set; }
        public List<Product> productList { get; set; }
        public List<GridItem3D> selectedItems { get; set; }

        public GridItem3D ghostItem { get; set; }

        public event PropertyChangedEventHandler PropertyChanged;
        private IconSet icons;

        public GridModel(double height, double width, IconSet icons)
        {
            this.Model = new Model3DGroup();
            this.icons = icons;
            this.width = width;
            this.height = height;
            this.modelList = new List<GridItem3D>();
            this.productList = new List<Product>();
            this.selectedItems = new List<GridItem3D>();
            UpdateModel();
        }

        /* --------------------- INIT & CLEAR ---------------------------*/

        public void InitFactoryGrid(machineMap machines)
        {
            ClearAndDeselectAllItems();
            ClearGrid();

            if (machines == null || machines.map == null)
            {
                return;
            }
            foreach (machineMapEntry machine in machines.map)
            {
                string instanceID = machine.key;
                int classID = HelperFunctions.GetClassIdFromItemId(instanceID);
                position pos = machine.value;
                Point3D point = TransformServerToClientCoords(new Point3D(pos.x, pos.y, 0));
                GridItem3D newItem = CreateGridItem3D(classID, point, pos.direction.ToString());

                newItem.instanceId = instanceID;
                PlaceItem(newItem);

            }
            SetItemOffset();
        }

        public void InitSimulationGrid(Sim.machineMap machines)
        {
            ClearAndDeselectAllItems();
            ClearGrid();

            if (machines == null || machines.map == null)
            {
                return;
            }
            foreach (Sim.machineMapEntry machine in machines.map)
            {
                string instanceID = machine.key;
                int classID = HelperFunctions.GetClassIdFromItemId(instanceID);
                Sim.position pos = machine.value;
                Point3D point = TransformServerToClientCoords(new Point3D(pos.x, pos.y, 0));
                GridItem3D newItem = CreateGridItem3D(classID, point, pos.direction.ToString());

                newItem.instanceId = instanceID;
                PlaceItem(newItem);
            }
            SetItemOffset();
        }

        public void ClearGrid()
        {
            modelList.Clear();
            productList.Clear();
            UpdateModel();
            UpdateProductModel();
        }

        /* --------------------- GUI-Methoden ---------------------------*/

        public void PlaceItem(GridItem3D item)
        {
            modelList.Add(item);
            UpdateModel();
        }

        public void PlaceProduct(Product product)
        {
            productList.Add(product);
            AddProductToModel(product);
        }

        public void MoveItem(String instanceId, Point3D point)
        {
            GridItem3D item = GetItemByID(instanceId);
            if (item != null)
            {
                item.position = point;
                UpdateModel();
            }
        }

        public void RemoveItem(string instanceId)
        {
            GridItem3D itemToBeRemoved = GetItemByID(instanceId);

            if (itemToBeRemoved != null)
            {
                Model.Children.Remove(itemToBeRemoved.model);
                modelList.Remove(itemToBeRemoved);

                ClearAndDeselectAllItems();
                UpdateModel();
            }
        }

        public void RemoveProduct(long id)
        {
            Product productToBeRemoved = GetProductByID(id);
            if (productToBeRemoved != null)
            {
                Model.Children.Remove(productToBeRemoved.model);
                productList.Remove(productToBeRemoved);

                ClearAndDeselectAllItems();
                UpdateProductModel();
            }
        }

        public void RemoveAllProducts()
        {
            foreach(Product product in productList)
            {
                this.Model.Children.Remove(product.model);
            }
            productList.Clear();
        }

        public void RotateItem(string itemId, string dir)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            position p = csu.GetPosition(itemId);
            if (p != null)
            {
                GridItem3D item = GetItemByID(itemId);
                
                if (dir == "right") 
                {
                    item.rotateRight();
                }
                else
                {
                    item.rotateLeft();
                }
                UpdateModel();
            }
        }

        internal void Resize(double height, double width)
        {
            this.height = height;
            this.width = width;
            SetItemOffset();
            UpdateModel();
        }

        /* --------------------- Hilfs-Methoden ---------------------------*/

        public void UpdateModel()
        {
            Model.Children.Clear();
            foreach (GridItem3D item in modelList)
            {
                AddItemToModel(item);
            }
        }

        public void UpdateProductModel() 
        {
            foreach (Product product in productList)
            {
                AddProductToModel(product);
            }
        }

        private void AddProductToModel(Product product)
        {
            product.UpdateModelProperties(xOffset, yOffset);
            Model.Children.Add(product.model);
        }

        private void AddItemToModel(GridItem3D item)
        {
            item.UpdateModelProperties(xOffset, yOffset);
            item.isSet = true;
            Model.Children.Add(item.model);
        }

        public GridItem3D GetItemByID(string instanceId)
        {
            foreach (GridItem3D item in modelList)
            {
                if (item.instanceId == instanceId)
                {
                    return item;
                }
            }
            return null;
        }

        public Product GetProductByID(long id)
        {
            foreach (Product product in productList)
            {
                if (product.id == id)
                {
                    return product;
                }
            }
            return null;
        }

        public GridItem3D GetItemAtPosition(Point3D position)
        {
            foreach (GridItem3D item in modelList)
            {
                if (item.position == position)
                {
                    return item;
                }
            }
            return null;
        }

        public void SetItemOffset()
        {
            xOffset = (width % 2 == 0) ? -0.5 : 0;
            yOffset = (height % 2 == 0) ? -0.5 : 0;
        }

        public bool IsProductInList(long id) 
        {
            foreach(Product product in productList)
            {
                if(product.id == id)
                {
                    return false;
                }
            }
            return true;
        }

        public GridItem3D CreateGridItem3D(int classID, Point3D p, String direction)
        {
            return new GridItem3D(classID, direction, p, icons);
        }

        public Product CreateProduct(long id, Point3D p, Point3D nextP, String name)
        {
            return new Product(id, name, p, nextP, icons);
        }

        protected void RaisePropertyChanged(string property)
        {
            var handler = PropertyChanged;
            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(property));
            }
        }

        /* ------------------------------------------ Server-Methoden ----------------------------------------------*/

        public void PopulateGrid(int simulationFactoryId)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            Sim.productMap productMap = csu.GetProducts(simulationFactoryId);

            if (productMap != null)
            {
                foreach (Sim.productMapEntry entry in productMap.productMap1.AsEnumerable())
                {
                    Sim.position position = entry.key;
                    Sim.product[] products = entry.value;

                    for (int i = 0; i < products.Count(); i++)
                    {
                        Point3D point = TransformServerToClientCoords(ChangeProductPoints(i, position));
                        Product product = GetProductByID(products[i].id);
                        if (product == null)
                        {
                            product = CreateProduct(products[i].id, point, point, products[i].name);
                            PlaceProduct(product);
                        }
                        else
                        {
                            product.position = point;
                            product.UpdateModelProperties(xOffset, yOffset);
                        }
                    }
                }
            }
        }

        public void CreateItemOnServer(Point3D p, int classID)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            int x = (int)TransformClientToServerCoords(p).X;
            int y = (int)TransformClientToServerCoords(p).Y;
            csu.CreateItem(classID, x, y, FactoryServiceReference.direction.NORTH);
        }
        
        public void MoveItemOnServer(Point3D position)
        {
            if (selectedItems.Count > 0)
            {
                GridItem3D item = selectedItems[0];
                Point3D serverPosition = TransformClientToServerCoords(position);
                item.MoveItemOnServer(new Point3D(serverPosition.X, serverPosition.Y, 0));
            }
        }

        public void RemoveSelectedItemsOnServer()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            if (selectedItems.Count > 0)
            {
                csu.DeleteItems(selectedItems.Select(item => item.instanceId).ToArray());
            }
        }

        public void RemoveAllItemsOnServer()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            if (modelList.Count > 0)
            {
                csu.DeleteItems(modelList.Select(item => item.instanceId).ToArray());
            }
        }

        public void RotateItemsOnServer(string direction)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            if (selectedItems.Count > 0)
            {
                if (direction == "left")
                {
                    csu.RotateItemsLeft(selectedItems.Select(item => item.instanceId).ToArray());
                }
                else // (direction == "right")
                {
                    csu.RotateItemsRight(selectedItems.Select(item => item.instanceId).ToArray());
                }
            }
        }

        /* ------------------------------------------ Hilfs-Methoden - Item Selektieren ----------------------------------------------*/
        internal void DeselectItem(GridItem3D item)
        {
            if (selectedItems.Count() == 1)
            {
                if (selectedItems[0] != item)
                {
                    ClearAndDeselectAllItems();
                }
                UnsetGhost();
            }
            else if (selectedItems.Count() > 1)
            {
                ClearAndDeselectAllItems();
            }
        }

        public void SelectItem(GridItem3D item)
        {
            if (!item.isSelected)
            {
                item.isSelected = true;
                selectedItems.Add(item);
                if (selectedItems.Count == 1)
                {
                    SetGhost();
                }
                else
                {
                    UnsetGhost();
                }
            }
            else
            {
                ClearAndDeselectAllItems();
            }
        }

        public void ClearAndDeselectAllItems()
        {
            DeselectAllItems();
            selectedItems.Clear();
        }

        private void DeselectAllItems()
        {
            foreach (var item in selectedItems)
            {
                item.isSelected = false;
            }
        }

        /* ------------------------------------------ Hilfs-Methoden - Koordinaten umrechnen ----------------------------------------------*/
        public Point3D TransformClientToServerCoords(Point3D p)
        {
            Point3D retP;
            var x = p.X;
            var y = p.Y * (-1);

            // CoordSystem Hoehe X Breite
            if (CheckClientCoordsWidthEven())
            {
                x += (Math.Truncate(width / 2) - 1);
                y += Math.Truncate(height / 2);
            }
            else if (CheckClientCoordsHeightOdd())
            {
                x += Math.Truncate(width / 2);
                y += Math.Truncate(height / 2);
            }
            retP = new Point3D(x, y, p.Z);

            return retP;
        }

        private bool CheckClientCoordsHeightOdd()
        {
            bool ungeradeUngerade = (height % 2 != 0 && width % 2 != 0); //CoordSystem == Ungerade X Ungerade
            bool geradeUngerade = (height % 2 == 0 && width % 2 != 0); // CoordSystem == Gerade(h) X Ungerade(w)

            return (ungeradeUngerade || geradeUngerade);
        }

        private bool CheckClientCoordsWidthEven()
        {
            bool geradeGerade = (height % 2 == 0 && width % 2 == 0); // CoordSystem == Gerade X Gerade
            bool ungeradeGerade = (height % 2 != 0 && width % 2 == 0); // CoordSystem == Ungerade(h) X Gerade(w)
            return (geradeGerade || ungeradeGerade);
        }

        public Point3D TransformServerToClientCoords(Point3D p)
        {
            Point3D retP;
            var x = p.X;
            var y = p.Y * (-1);

            if (CheckServerCoordsWidthEven()) 
            {
                x -= Math.Truncate(width / 2) - 1;
                y += Math.Truncate(height / 2);
            }
            else if (CheckServerCoordsHeightOdd()) 
            {
                x -= Math.Truncate(width / 2);
                y += Math.Truncate(height / 2);
            }

            retP = new Point3D(x, y, p.Z);

            return retP;
        }

        private bool CheckServerCoordsHeightOdd()
        {
            bool ungeradeUngerade = (height % 2 != 0 && width % 2 != 0); // CoordSystem == Ungerade X Ungerade
            bool ungeradeGerade = (height % 2 == 0 && width % 2 != 0); // CoordSystem == Ungerade(w) X Gerade(h)
            return (ungeradeUngerade || ungeradeGerade);
        }

        private bool CheckServerCoordsWidthEven()
        {
            bool geradeGerade = (height % 2 == 0 && width % 2 == 0); // CoordSystem == Gerade X Gerade
            bool geradeUngerade = (height % 2 != 0 && width % 2 == 0); // CoordSystem == Gerade(w) X Ungerade(h)
            return (geradeGerade || geradeUngerade);
        }

        public Point3D ChangeProductPoints(int counter, SimulationServiceReference.position position) 
        {
            double x = position.x;
            double y = position.y;
            double z = 0;

            if(counter > 8 )
            {
                z = (counter / 9) * 0.3;
                counter -= ((counter / 9) * 9);
            }

            switch (counter)
            {
                case 0:
                    x -= 0.3;
                    y -= 0.3;
                    break;
                case 1:
                    y -= 0.3; 
                    break;
                case 2:
                    x += 0.3;
                    y -= 0.3;
                    break;
                case 3:
                    x -= 0.3;
                    break;
                case 5:
                    x += 0.3;
                    break;
                case 6:
                    x -= 0.3;
                    y += 0.3;
                    break;
                case 7:
                    y += 0.3;
                    break;
                case 8:
                    x += 0.3;
                    y += 0.3;
                    break;
            }
            return new Point3D(x, y, z);
        }

        /* ----------------------- GHOST METHODS --------------------------- */

        public void SetGhost()
        {
            GridItem3D item = selectedItems[0];
            ghostItem = item.Clone();
        }

        public void UnsetGhost()
        {
            HideGhost();
            ghostItem = null;
        }

        private void HideGhost()
        {
            if(IsSetGhost())
            {
                Model.Children.Remove(ghostItem.model);
            }
        }

        public bool IsSetGhost()
        {
            return (ghostItem != null);
        }

        public void MoveGhost(Point3D position)
        {
            if (IsSetGhost())
            {
                if (position.Y > height / 2 || position.X > width / 2 || position.Y <= -(height / 2) || position.X <= -(width/ 2))
                {
                    ghostItem.SetOutOfGridGhostColor();
                }
                else
                {
                    ghostItem.SetDefaultGhostColor();
                }
                ghostItem.position = position;
                RenderGhost();
            }
        }

        private void RenderGhost()
        {
            if (IsSetGhost())
            {
                Model.Children.Remove(ghostItem.model);
                ghostItem.UpdateModelProperties(xOffset, yOffset);
                Model.Children.Add(ghostItem.model);
            }
        }
    }
}