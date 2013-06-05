using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace Client.Classes
{
    public class Toolbox : ListView
    {
        public static int GRIDITEM_WIDTH = 50;
        public static int GRIDITEM_HEIGHT = 50;
        private Size defaultItemSize = new Size(50, 50);
        private IconSet icons;
        
        public Toolbox(IconSet icons)
        { 
            this.defaultItemSize = new Size(GRIDITEM_WIDTH, GRIDITEM_HEIGHT);
            this.Width = 156;
            GridView gv = new GridView();
            GridViewColumn gvc = new GridViewColumn();
            this.icons = icons;
            gvc.Header = icons;
        }

        public int GetSelectedClassID()
        {
            return this.SelectedIndex + 1;
        }

        public void Init()
        {
            Clear();
            List<int> classIds = new List<int> { 1, 2, 3, 4, 5 };
            foreach (int classId in classIds)
            {
                LoadIcon(classId);
            }
        }

        public void Clear()
        {
            Items.Clear();
        }

        /* Plugin laden und ein Bild zur Toolbox hinzufügen */
        private void LoadIcon(int classid)
        {
            ListViewItem item = new ListViewItem();
            item.ToolTip = icons.getTooltip(classid);
            item.MaxWidth = 75;
            item.Content = icons.getImage(classid);
            
            this.Items.Add(item);
        }
    }
}


