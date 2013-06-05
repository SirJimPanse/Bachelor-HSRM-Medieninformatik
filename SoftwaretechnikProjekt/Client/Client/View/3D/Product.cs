using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Media.Media3D;
using Client.Classes;
using HelixToolkit.Wpf;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Threading;

namespace Client.View._3D
{
    class Product : DependencyObject
    {
        /* ----------------------------- Klassen-Attribute ------------------------------------ */

        public long id
        {
            get { return (long)GetValue(IdProperty); }
            set { SetValue(IdProperty, value); }
        }

        public string name
        {
            get { return (string)GetValue(NameProperty); }
            set { SetValue(NameProperty, value); }
        }

        public Material color
        {
            get { return (Material)GetValue(ColorProperty); }
            set
            {
                SetValue(ColorProperty, value);
                SetColor(value);
            }
        }

        public Model3DGroup model
        {
            get { return (Model3DGroup)GetValue(ModelProperty); }
            set { SetValue(ModelProperty, value); }
        }

        public Point3D position
        {
            get { return (Point3D)GetValue(PositionProperty); }
            set { SetValue(PositionProperty, value); }
        }

        public Point3D nextPosition
        {
            get { return (Point3D)GetValue(NextPositionProperty); }
            set { SetValue(NextPositionProperty, value); }
        }

        /* ----------------------------- DependencyProperties ------------------------------------ */

        public static readonly DependencyProperty IdProperty = DependencyProperty.Register(
            "Id", typeof(long), typeof(Product)
        );

        public static readonly DependencyProperty NameProperty = DependencyProperty.Register(
            "Name", typeof(string), typeof(Product)
        );

        public static readonly DependencyProperty ColorProperty = DependencyProperty.Register(
            "Color", typeof(Material), typeof(Product)
        );

        public static readonly DependencyProperty ModelProperty = DependencyProperty.Register(
            "Model", typeof(Model3DGroup), typeof(Product)
        );

        public static readonly DependencyProperty PositionProperty = DependencyProperty.Register(
            "Position", typeof(Point3D), typeof(Product)
        );

        public static readonly DependencyProperty NextPositionProperty = DependencyProperty.Register(
            "NextPosition", typeof(Point3D), typeof(Product)
        );

        /* ----------------------------- Konstruktor ------------------------------------ */

        public Product(long id, string name, Point3D pos, Point3D nextPos, IconSet icons)
        {
            this.id = id;
            this.name = name;
            this.position = pos;
            this.nextPosition = nextPos;
            this.model = icons.getProductModel(name);
            this.color = icons.getProductColor(name);
        }

        private void SetColor(Material material)
        {
            foreach (var child in this.model.Children)
            {
                ((GeometryModel3D)child).Material = material;
            }
        }

        public void UpdateModelProperties(double xOffset, double yOffset)
        {
            Point3D p = this.position;
            p.X = position.X + xOffset;
            p.Y = position.Y + yOffset;
            TranslateTransform3D translateTransform = new TranslateTransform3D(p.X, p.Y, p.Z);
            Transform3DGroup transformGroup = new Transform3DGroup();
            
            transformGroup.Children.Add(translateTransform);
            this.model.Transform = transformGroup;
        }
    }
}
