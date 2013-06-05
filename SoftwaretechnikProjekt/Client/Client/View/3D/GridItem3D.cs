using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Media3D;
using HelixToolkit.Wpf;
using Client.Communication;


namespace Client.Classes
{
    public class GridItem3D : DependencyObject
    {
        /* ----------------------------- Klassen-Attribute ------------------------------------- */
        private Material selectedMaterial = MaterialHelper.CreateMaterial(Colors.Green);
        private Material defaultMaterial = MaterialHelper.CreateMaterial(Colors.Yellow);
        private Material glasMaterial = MaterialHelper.CreateMaterial(Colors.WhiteSmoke, 75);
        private Material outOfGridMaterial = MaterialHelper.CreateMaterial(Colors.Red, 75);
        private Material tailbackMaterial = MaterialHelper.CreateMaterial(Colors.Red);

        public string instanceId { get; set; }

        public Client.DirectionEnum direction
        {
            get { return (Client.DirectionEnum)GetValue(DirectionProperty); }
            set { SetValue(DirectionProperty, value); }
        }

        public bool isSet
        {
            get { return (bool)GetValue(IsSetProperty); }
            set { SetValue(IsSetProperty, value); }
        }

        public bool isSelected
        {
            get { return (bool)GetValue(IsSelectedProperty); }
            set { 
                SetValue(IsSelectedProperty, value);
                if (isSelected)
                {
                    this.color = selectedMaterial;
                }
                else
                {
                    this.color = defaultMaterial;
                }
            }
        }

        public int classId
        {
            get { return (int)GetValue(ClassIdProperty); }
            set { SetValue(ClassIdProperty, value); }
        }

        public Material color
        {
            get { return (Material)GetValue(ColorProperty); }
            set { SetValue(ColorProperty, value);
            SetColor(value);
            }
        }

        public int width
        {
            get { return (int)GetValue(WidthProperty); }
            set { SetValue(WidthProperty, value); }
        }

        public int length
        {
            get { return (int)GetValue(LengthProperty); }
            set { SetValue(LengthProperty, value); }
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

        public bool tailback
        {
            get { return (bool)GetValue(TailbackProperty); }
            set { SetValue(TailbackProperty, value);
            SetColor(tailbackMaterial);
            }
        }

        /* ----------------------------- DependencyProperties ------------------------------------ */
        public static readonly DependencyProperty WidthProperty = DependencyProperty.Register(
            "Width", typeof(int), typeof(GridItem3D)
        );

        public static readonly DependencyProperty LengthProperty = DependencyProperty.Register(
            "Length", typeof(int), typeof(GridItem3D)
        );

        public static readonly DependencyProperty ColorProperty = DependencyProperty.Register(
            "Color", typeof(Material), typeof(GridItem3D)
        );

        public static readonly DependencyProperty IsSetProperty = DependencyProperty.Register(
            "IsSet", typeof(bool), typeof(GridItem3D)
        );

        public static readonly DependencyProperty ClassIdProperty = DependencyProperty.Register(
            "ClassId", typeof(int), typeof(GridItem3D)
        );

        public static readonly DependencyProperty IsSelectedProperty = DependencyProperty.Register(
            "IsSelected", typeof(bool), typeof(GridItem3D), new FrameworkPropertyMetadata(false)
        );

        public static readonly DependencyProperty DirectionProperty = DependencyProperty.Register(
            "Direction", typeof(Client.DirectionEnum), typeof(GridItem3D)
        );

        public static readonly DependencyProperty ModelProperty = DependencyProperty.Register(
            "Model", typeof(Model3DGroup), typeof(GridItem3D)            
        );

        public static readonly DependencyProperty PositionProperty = DependencyProperty.Register(
            "Position", typeof(Point3D), typeof(GridItem3D)
        );

        public static readonly DependencyProperty TailbackProperty = DependencyProperty.Register(
            "Tailback", typeof(bool), typeof(GridItem3D)
        );

        /* ----------------------------- Konstruktor ------------------------------------ */
        private GridItem3D()
        {

        }
        
        public GridItem3D(int ClassId, String dir, Point3D pos, IconSet icons) {
            this.classId = ClassId;
            this.isSet = false;
            this.width = 1;
            this.length = 1;
            this.position = pos;
            LoadObj(icons);
            SetDirectionByString(dir);
            this.isSelected = false;
        }

        /* ---------------------------------------- MODEL-SETTER -------------------------------------- */
        private void SetColor(Material material)
        {
            foreach (var child in this.model.Children)
            {
                GeometryModel3D model = ((GeometryModel3D)child);
                model.Material = material;
                model.BackMaterial = material;
            }
        }

        /* ---------------------------------------- HILFSMETHODEN -------------------------------------- */

        public void UpdateModelProperties(double xOffset, double yOffset)
        {
            RotateTransform3D rotateTransform = new RotateTransform3D();
            AxisAngleRotation3D angleRotation = new AxisAngleRotation3D();

            int a = HelperFunctions.GetAngleFromDirection(this.direction);

            angleRotation.Axis = new Vector3D(0, 0, 1);
            angleRotation.Angle = a;

            rotateTransform.CenterX = this.position.X - 0.5 + xOffset;
            rotateTransform.CenterY = this.position.Y - 0.5 + yOffset;
            rotateTransform.CenterZ = 0;
            rotateTransform.Rotation = angleRotation;

            Point3D p = this.position;
            p.X = position.X + xOffset;
            p.Y = position.Y + yOffset;
            TranslateTransform3D translateTransform = new TranslateTransform3D(p.X, p.Y, p.Z);
            Transform3DGroup transformGroup = new Transform3DGroup();

            transformGroup.Children.Add(translateTransform);
            transformGroup.Children.Add(rotateTransform);

            this.model.Transform = transformGroup;
        }

        private void LoadObj(IconSet icons)
        {
            this.model = icons.getObj(classId);
        }

        /* ----------------------------- Direction - Methods ------------------------------------ */
        private void SetDirectionByString(string d)
        {
            switch (d)
            {
                case "NORTH": this.direction = Client.DirectionEnum.NORTH; break;
                case "EAST": this.direction = Client.DirectionEnum.EAST; break;
                case "SOUTH": this.direction = Client.DirectionEnum.SOUTH; break;
                case "WEST": this.direction = Client.DirectionEnum.WEST; break;
            }
        }

        private void SetPreviousDirection()
        {
            switch (this.direction)
            {
                case DirectionEnum.NORTH:
                    this.direction = Client.DirectionEnum.WEST;
                    break;
                case DirectionEnum.WEST:
                    this.direction = Client.DirectionEnum.SOUTH;
                    break;
                case DirectionEnum.SOUTH:
                    this.direction = Client.DirectionEnum.EAST;
                    break;
                case DirectionEnum.EAST:
                    this.direction = Client.DirectionEnum.NORTH;
                    break;
            }
        }

        private void SetNextDirection()
        {
            switch (this.direction)
            {
                case DirectionEnum.NORTH:
                    this.direction = Client.DirectionEnum.EAST;
                    break;
                case DirectionEnum.EAST:
                    this.direction = Client.DirectionEnum.SOUTH;
                    break;
                case DirectionEnum.SOUTH:
                    this.direction = Client.DirectionEnum.WEST;
                    break;
                case DirectionEnum.WEST:
                    this.direction = Client.DirectionEnum.NORTH;
                    break;
            }
        }

        /* ----------------------------- Rotate - Methods ------------------------------------ */
        public void rotateLeft()
        {
            SetPreviousDirection();
        }

        public void rotateRight()
        {
            SetNextDirection();
        }

        /* ----------------------------- Server - Methods ------------------------------------ */
        public void MoveItemOnServer(Point3D serverP)
        {
            ClientServerUtil.getInstance().MoveItem(this.instanceId, (int)serverP.X, (int)serverP.Y);
        }

        public void ScriptObjectOnServer(string script)
        {
            ClientServerUtil.getInstance().ScriptMachine(this.instanceId, script);
        }

        public string GetScriptFromServer()
        {
            return ClientServerUtil.getInstance().GetMachineScript(this.instanceId);
        }

        /* ---------------------------------------- GHOST-METHODS -------------------------------------- */
        public GridItem3D Clone()
        {
            GridItem3D item = new GridItem3D();
            item.classId = this.classId;
            item.instanceId = "ghost";
            item.model = this.model.Clone();
            item.isSet = true;
            item.width = this.width;
            item.length = this.length;
            item.direction = this.direction;
            item.position = new Point3D(this.position.X, this.position.Y, this.position.Z);
            item.SetColor(glasMaterial);

            return item;
        }

        public void SetDefaultGhostColor(){
            this.color = glasMaterial;
        }

        public void SetOutOfGridGhostColor(){
            this.color = outOfGridMaterial;
        }
    }
}