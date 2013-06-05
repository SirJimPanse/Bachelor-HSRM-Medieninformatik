using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media.Imaging;
using System.Windows.Media;

namespace Client.Classes
{
    /** Diese Klasse repräsentiert ein Element aus der Toolbox **/
    public class ToolboxItem : ContentControl
    {
        private Point? dragStartPoint = null;
        public BitmapImage img;

        public bool isPressed
        {
            get { return (bool)GetValue(IsPressedProperty); }
            set { SetValue(IsPressedProperty, value); }
        }

        public static readonly DependencyProperty IsPressedProperty = DependencyProperty.Register(
            "IsPressed", typeof(bool), typeof(bool));

        public int ClassId
        {
            get;
            set;
        }

        static ToolboxItem()
        {
            FrameworkElement.DefaultStyleKeyProperty.OverrideMetadata(typeof(ToolboxItem), new FrameworkPropertyMetadata(typeof(ToolboxItem)));
        }

        protected override void OnPreviewMouseDown(MouseButtonEventArgs e)
        {
            base.OnPreviewMouseDown(e);
            this.dragStartPoint = new Point?(e.GetPosition(this));
        }

        protected override void OnMouseDown(MouseButtonEventArgs e)
        {
            base.OnMouseDown(e);
            if(isPressed == true){
                isPressed = false; 
            } else {
                isPressed = true;
            }
   

            /* if (e.LeftButton != MouseButtonState.Pressed)
            {
                this.dragStartPoint = null;
            }

            if (this.dragStartPoint.HasValue)
            {
                Point position = e.GetPosition(this);
                if ((SystemParameters.MinimumHorizontalDragDistance <=
                    Math.Abs((double)(position.X - this.dragStartPoint.Value.X))) ||
                    (SystemParameters.MinimumVerticalDragDistance <=
                    Math.Abs((double)(position.Y - this.dragStartPoint.Value.Y))))
                {

                    Image contentImg = this.Content as Image;
                    BitmapImage contentBitmapImg = contentImg.Source as BitmapImage;
                    DataObject dataObject = new DataObject("GRID_ITEM", contentImg);
                    dataObject.SetData("CLASS_ID", this.ClassId);

                    if (dataObject != null)
                    {
                        #if DEBUG
                            Console.WriteLine("ToolboxItem.OnMouseMove: GridItem sollte erstellt werden: " + dataObject.ToString());
                            Console.WriteLine("ToolboxItem.OnMouseMove: TBI.ClassID: " + this.ClassId.ToString());
                            Console.WriteLine("ToolboxItem.OnMouseMove: BitmapImage " + contentBitmapImg.ToString());
                        #endif
                        DragDrop.DoDragDrop(this, dataObject, DragDropEffects.Copy);
                    }
                }
                e.Handled = true;*/
        }

        protected override void OnMouseMove(MouseEventArgs e)
        {

            base.OnMouseMove(e);
            if (e.LeftButton != MouseButtonState.Pressed)
            {
                this.dragStartPoint = null;
            }

            if (this.dragStartPoint.HasValue)
            {
                Point position = e.GetPosition(this);
                if ((SystemParameters.MinimumHorizontalDragDistance <=
                    Math.Abs((double)(position.X - this.dragStartPoint.Value.X))) ||
                    (SystemParameters.MinimumVerticalDragDistance <=
                    Math.Abs((double)(position.Y - this.dragStartPoint.Value.Y))))
                {

                    Image contentImg = this.Content as Image;
                    BitmapImage contentBitmapImg = contentImg.Source as BitmapImage;
                    DataObject dataObject = new DataObject("GRID_ITEM", contentImg);
                    dataObject.SetData("CLASS_ID", this.ClassId);

                    if (dataObject != null)
                    {
                        #if DEBUG
                            Console.WriteLine("ToolboxItem.OnMouseMove: GridItem sollte erstellt werden: " + dataObject.ToString());
                            Console.WriteLine("ToolboxItem.OnMouseMove: TBI.ClassID: " + this.ClassId.ToString());
                            Console.WriteLine("ToolboxItem.OnMouseMove: BitmapImage " + contentBitmapImg.ToString());
                        #endif
                        DragDrop.DoDragDrop(this, dataObject, DragDropEffects.Copy);
                    }
                }
                e.Handled = true;
            }
        }


        public BitmapImage GetItemImage()
        {
            Image contentImg = this.Content as Image;
            return contentImg.Source as BitmapImage;
        }
    }
}
