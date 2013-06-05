using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using Coordinate = System.Tuple<int, int>; // Eine Koordinate ist ein Integerpaar
using System.Windows.Input;
using System.Windows.Media;
using System.Windows;

namespace Client.Classes
{
    /** Ein GridThumb der Teil eines GridItems, dessen Verhalten das Objekt beweglich macht **/
    class GridThumb : Thumb
    {

        /** Default Konstruktor **/
        public GridThumb()
        {
            DragDelta += new DragDeltaEventHandler(this.GridThumb_DragDelta);
        }

        /** Der MouseDrag Eventhandler für das GridThumb **/
        private void GridThumb_DragDelta(object sender, DragDeltaEventArgs e)
        {
            Console.WriteLine("GridThumb");
            GridItem item = this.DataContext as GridItem;
            if (item != null)
            {
                GridCanvas GridC = VisualTreeHelper.GetParent(VisualTreeHelper.GetParent(VisualTreeHelper.GetParent(this))) as GridCanvas;
                Point p = new Point(GridCanvas.GetLeft(item) + e.HorizontalChange,GridCanvas.GetTop(item) + e.VerticalChange);
                Coordinate coord = null;
                if (p != null) // wenn objekt gelöscht wurde, wärend wir verschieben ist p == null
                {
                    coord = GridC.CalcGridCoord(p);
                    // GO server..
                    if (coord != null)
                    {
                        ClientServerUtil.getInstance().moveItem(item.instanceId, coord.Item1, coord.Item2);
                        #if DEBUG
                            Console.WriteLine("\nObject Moved:");
                        #endif
                    }
                }
            }
        }
    }
}
