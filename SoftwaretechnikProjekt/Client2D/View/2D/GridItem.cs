using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;

namespace Client.Classes
{

    public class GridItem : ContentControl
    {
        public string instanceId
        {
            get;
            set;
        }

        public bool IsSelected
        {
            get { return (bool)GetValue(IsSelectedProperty); }
            set { SetValue(IsSelectedProperty, value); }
        }

        /** Die isSelected Property als DependencyProperty **/
        public static readonly DependencyProperty IsSelectedProperty = DependencyProperty.Register(
            "IsSelected", typeof(bool), typeof(GridItem), new FrameworkPropertyMetadata(false)
        );

        public int Angle
        {
            get { return (int)GetValue(AngleProperty); }
            set { SetValue(AngleProperty, value); }
        }

        public void set_angle(int i) { SetValue(AngleProperty, i); }

        /** Die Angle-Property als DependencyProperty **/
        public static readonly DependencyProperty AngleProperty = DependencyProperty.Register(
            "Angle", typeof(int), typeof(GridItem), new FrameworkPropertyMetadata(0)
        );

        public static readonly DependencyProperty GridThumbTemplateProperty =
            DependencyProperty.RegisterAttached("GridThumbTemplate", typeof(ControlTemplate), typeof(GridItem));

        public static ControlTemplate GetGridThumbTemplate(UIElement element)
        {
            return (ControlTemplate)element.GetValue(GridThumbTemplateProperty);
        }

        public static void SetGridThumbTemplate(UIElement element, ControlTemplate value)
        {
            element.SetValue(GridThumbTemplateProperty, value);
        }

        static GridItem()
        {
            FrameworkElement.DefaultStyleKeyProperty.OverrideMetadata(typeof(GridItem), new FrameworkPropertyMetadata(typeof(GridItem)));
        }

        public GridItem()
        {
            this.Loaded += new RoutedEventHandler(this.GridItem_Loaded);
            this.Focusable = true;
        }

        /** Der (hypersensible) MouseClicked Eventhandler (setzt die isSelected-Property) **/
        protected override void OnPreviewMouseDown(MouseButtonEventArgs e)
        {
            base.OnPreviewMouseDown(e);
            GridCanvas designer = VisualTreeHelper.GetParent(this) as GridCanvas;

            if (designer != null)
            {
                if ((Keyboard.Modifiers &
                (ModifierKeys.Shift | ModifierKeys.Control)) != ModifierKeys.None)
                {
                    this.IsSelected = !this.IsSelected;
                }
                else
                {
                    if (!this.IsSelected)
                    {
                        designer.DeselectAll();
                        this.IsSelected = true;
                    }
                }
            }

            e.Handled = false;
        }

        /** Diese Methode lädt das in GridItem.xaml definierte Template **/
        private void GridItem_Loaded(object sender, RoutedEventArgs e)
        {
            if (this.Template != null)
            {
                ContentPresenter contentPresenter =
                    this.Template.FindName("PART_ContentPresenter", this) as ContentPresenter;

                GridItem thumb =
                    this.Template.FindName("PART_GridThumb", this) as GridItem;

                if (contentPresenter != null && thumb != null)
                {
                    UIElement contentVisual =
                        VisualTreeHelper.GetChild(contentPresenter, 0) as UIElement;

                    if (contentVisual != null)
                    {
                        ControlTemplate template =
                            GridItem.GetGridThumbTemplate(contentVisual) as ControlTemplate;

                        if (template != null)
                        {
                            thumb.Template = template;
                        }
                    }
                }
            }
        }
    }
}
