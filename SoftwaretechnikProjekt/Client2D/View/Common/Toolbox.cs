using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;

namespace Client.Classes
{
    public class Toolbox : ItemsControl
    {
        private Size defaultItemSize = new Size(50, 50);
        public Size DefaultItemSize
        {
            get { return this.defaultItemSize; }
            set { this.defaultItemSize = value; }
        }

        protected override DependencyObject GetContainerForItemOverride()
        {
            return new ToolboxItem();
        }

        protected override bool IsItemItsOwnContainerOverride(object item)
        {
            return (item is ToolboxItem);
        }

        public ToolboxItem getItemForId(int classId)
        {
            foreach(ToolboxItem item in this.Items)
            {
                if (item.ClassId == classId)
                    return item;
            }
            return null;
        }
    }
}
