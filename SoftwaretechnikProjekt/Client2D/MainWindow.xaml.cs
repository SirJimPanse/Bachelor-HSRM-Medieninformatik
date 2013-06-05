using System;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Collections.ObjectModel;
using Client.Classes;
using System.Windows.Media.Media3D;
using HelixToolkit.Wpf;
using Client.FactoryServiceReference;
using Client.LoginServiceReference;
using System.Windows.Threading;
using System.Threading.Tasks;

namespace Client
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window, IMqObserver
    {
        public static string SPLIT_FACTORY_NAME = " - ";
        public static int GRIDITEM_WIDTH = 80;
        public static int GRIDITEM_HEIGHT = 80;
        public static int GRIDCANVAS_COLUMNS = 10;
        public static int GRIDCANVAS_ROWS = 10;

        List<ToolboxItem> toolboxITEMS = new List<ToolboxItem>();

        IconSet icons;
        string defaultPlugin;
        
        /** Die Toolbox, in der die Bilder für die Maschinen dargestellt werden (hat Drag&Drop Funktionalität) **/
        private Toolbox toolbox;

        public int SelectedToolboxItem
        {
            get { return (int)GetValue(SelectedToolboxItemProperty); }
            set { SetValue(SelectedToolboxItemProperty, value); }
        }

        public static readonly DependencyProperty SelectedToolboxItemProperty = DependencyProperty.Register(
	            "SelectedToolboxItem", typeof(int), typeof(int)
        );

	        public int ToolboxItems
        {
            get { return (int)GetValue(ToolboxItemsProperty); }
	            set { SetValue(ToolboxItemsProperty, value); }
        }
	
        /** Die Angle-Property der Toolboxitems als DependencyProperty **/
	        public static readonly DependencyProperty ToolboxItemsProperty = DependencyProperty.Register(
            "ToolboxItems", typeof(Image), typeof(MainWindow), new FrameworkPropertyMetadata(null)
	        );

        public MainWindow()
        {
            InitializeComponent();
            ConsoleManager.Show();

            LoginPanel.IsOpen = true;
            ScriptingPanel.IsOpen = false;
            this.Focusable = false;

            // ein vordefiniertes IconSet Plugin in "Client/Plugins/iconsets/" (TODO: plugins wie zwischen Clients verteilen?)
            defaultPlugin = "machines";
            icons = new IconSet(defaultPlugin);

            createToolbox();
        }

        private void createToolbox()
        {
            this.toolbox = new Toolbox();
            this.toolbox.DefaultItemSize = new Size(GRIDITEM_WIDTH, GRIDITEM_HEIGHT); // TODO Größe variabel??
            this.toolbox.SnapsToDevicePixels = true;
            ExpanderShapes.Content = this.toolbox;
        }

        private void clickListener(object sender, System.Windows.RoutedEventArgs e)
        {
            NewProjectConfigPanel.IsOpen = true;
            OpenChatWindowButton.FocusVisualStyle = null;
        }

        private void konfigSchließen(object sender, System.Windows.RoutedEventArgs e)
        {
#if DEBUG
            Console.WriteLine("MainWindow.konfigSchließen: create Factory abgebrochen.");
#endif
            NewProjectConfigPanel.IsOpen = false;
        }

        private void handCurserListener(object sender, System.Windows.RoutedEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Hand;
        }

        private void arrowCurserListener(object sender, System.Windows.RoutedEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Arrow;
        }

        private void chatOpenEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            ChatwindowPanel.Visibility = Visibility.Visible;
            OpenChatWindowButton.Visibility = Visibility.Hidden;
            CloseChatWindowButton.Visibility = Visibility.Visible;
        }

        private void chatCloseEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            ChatwindowPanel.Visibility = Visibility.Hidden;
            OpenChatWindowButton.Visibility = Visibility.Hidden;
            CloseChatWindowButton.Visibility = Visibility.Visible;
        }

        private void DrehenButton_ClickLeft(object sender, RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            //2D
            foreach (Client.Classes.GridItem item in GridC.SelectedItems)
            {
                csu.rotateItemLeft(item.instanceId);                
            }
        }

        private void DrehenButton_ClickRight(object sender, RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            //2D
            foreach (Client.Classes.GridItem item in GridC.SelectedItems)
            {
                csu.rotateItemRight(item.instanceId);
            }
        }

        private void KeyDownHandler(object sender, System.Windows.Input.KeyEventArgs e)
        {
            Console.WriteLine(e);
            if (e.Key == Key.Delete)
            {
                Console.WriteLine("Key.Delete gedrückt");
                GridC.removeSelected();
            }
        }

        private void NewProjectEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            int height = int.Parse(FactorySizeYInput.Text);
            int width = int.Parse(FactorySizeXInput.Text);
            string name = FactoryNameInput.Text;
            int facId = ClientServerUtil.getInstance().createFactory(name, width, height);
            Height.Text = height.ToString();
            Width.Text = width.ToString();
            
            clearFactoryPulldown();
            initFactoryPulldown(ClientServerUtil.getInstance());
            FactoryList.SelectedItem = ToString(facId, name);

            NewProjectConfigPanel.IsOpen = false;
        }

        private void UserLoginCheck(object sender, System.Windows.RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.serverPc = ServerInput.Text;
            var username = UsernameInput.Text;
            var password = PasswordInput.Text;

            try
            {
                csu.login(username, password, this);                   
                LoginPanel.IsOpen = false;
                UsernameIfLoggedIn.Content = "Eingeloggt als \n" + UsernameInput.Text;
                initFactoryPulldown(csu);
            }
            catch (LoginException ex)
            {
                Console.WriteLine(ex.StackTrace);
                showPopUp(ex.Message);
            }
        }

        private void initFactoryPulldown(ClientServerUtil loginService)
        {
            var data = new Dictionary<int, string>();
            var factories = loginService.getFactories();
            foreach (int actId in factories.Keys)
            {
                FactoryList.Items.Add(ToString(actId, factories[actId]));
            }
        }


        private void LogoutEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            ClientServerUtil usr = ClientServerUtil.getInstance();
            usr.logout();
            LoginPanel.IsOpen = true;
            UsernameIfLoggedIn.Content = "";
            clearToolbox();
            clearGrid();
            clearFactoryPulldown();
        }

       
        private void FactorySelected(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            if (FactoryList.Items.Count > 0)
            {
                ClientServerUtil usr = ClientServerUtil.getInstance(); // TODO ServerInput.Text setzen
                string selectedFactory = (string)FactoryList.SelectedItem;
                int id = getIdFromFactoryString(selectedFactory);

                usr.connectToFactory(id);
                usr.registerFactoryMQListener(id, this);

                clearToolbox();
                initToolbox();

                // 2D
                clearGrid();
                initGrid();

                Width.Text = usr.getAreaWidth().ToString();
                Height.Text = usr.getAreaLength().ToString();
            }
        }


        /*  ---------------------------- GUI - Allgemein ---------------------------- */
        private void initToolbox()
        {
            // IDs der Maschinenklassen (TODO: vom Server holen)
            List<int> classIds = new List<int> { 1, 2, 3, 4, 5 };
            foreach (int classId in classIds)
            {
                loadIcon(icons, classId);
            }
        }

        private void clearFactoryPulldown()
        {
            FactoryList.Items.Clear();
        }

        private void clearToolbox()
        {
            toolbox.Items.Clear();
        }

        /*  ---------------------------- 2D ---------------------------- */
        private void initGrid()
        {
            machineMap machines = ClientServerUtil.getInstance().getFactory();
            if (machines == null || machines.map == null)
            {
                return;
            }
            foreach (machineMapEntry machine in machines.map)
            {
                string instanceID = machine.key;
                int classID = getClassIdFromItemId(instanceID);
                position pos = machine.value;
                GridItem newItem = new GridItem();
                Image img = icons.getImage(classID);
                newItem.Content = img;

#if DEBUG
                Console.WriteLine("MainWindow.initGrid: instanceID = " + instanceID);
                Console.WriteLine("MainWindow.initGrid: classID = " + classID);
#endif

                Size s = GridC.GetTileSize();
                newItem.Width = s.Width;
                newItem.Height = s.Height;
                newItem.Angle = getAngleFromDirection(pos.direction);
                newItem.instanceId = instanceID;
                GridC.placeItem(newItem, pos.x, pos.y);
            }
        }

        private void clearGrid()
        {
            GridC.Clear();
        }

        /*  ---------------------------- Hilfs - Methoden ---------------------------- */
        private static int getClassIdFromItemId(string instanceID)
        {
            int classID = int.Parse(instanceID.Split('-')[0]);
            return classID;
        }

        private static int getAngleFromDirection(direction dir)
        {
            if (dir == direction.NORTH)
                return 0;
            if (dir == direction.EAST)
                return 90;
            if (dir == direction.SOUTH)
                return 180;
            return 270;
        }

        /* Plugin laden und ein Bild zur Toolbox hinzufügen */
        private void loadIcon(IconSet ics, int classid)
        {
            Image img = ics.getImage(classid);
            ToolboxItem tbi = new ToolboxItem();
            toolboxITEMS.Add(tbi);
            tbi.Content = img;
            tbi.ClassId = classid;
            this.toolbox.Items.Add(tbi);
        }

        private string ToString(int key, string value)
        {
            return key + SPLIT_FACTORY_NAME + value;
        }

        private int getIdFromFactoryString(string factory)
        {
            if (factory == null)
            {
                return -1;
            }
            string[] parts = factory.Split(new string[] { SPLIT_FACTORY_NAME }, StringSplitOptions.None);
            return int.Parse(parts[0]);
        }

        private void SetScriptingButton(int classID)
        {
            if (classID == 5 || classID == 1)
            {
                ScriptingButton.Visibility = Visibility.Visible;
            }
            else
            {
                ScriptingButton.Visibility = Visibility.Hidden;
            }
        }

        protected void OnSaveButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.Save();
        }

        public void Update(string factory, string itemId, long publisher, string mqEvent)
        {
            Console.WriteLine("_____________MQ-Event: "+mqEvent+"______________");
            Console.WriteLine("InstanceID: " + itemId);
            Console.WriteLine("Factory: " + factory);
            Console.WriteLine("Publisher: " + publisher);

            switch (mqEvent)
            {
                case "create":
                    CreateUIElement(itemId);
                    break;
                case "move":
                    MoveUIElement(itemId);
                    break;
                case "rotate-left":
                case "rotate-right":
                    RotateUIElement(itemId);
                    break;
                case "delete":
                    DeleteUIElement(itemId);
                    break;
                case "added":
                    String message = "Du wurdest erfolgreich zu " + factory + " hinzugefügt";
                    this.Dispatcher.Invoke(new ShowPopUpDelegate(showPopUp), new object[] { message });
                    Console.WriteLine("Regist-Nachricht beim Eingeladenen geworfen!");
                    refreshFactoryPulldownAsynchron();
                    break;
                case "updateProducts":
                    GridC.Dispatcher.Invoke(new ClearProductsDelegate(GridC.clearProducts), new object[] { });
                    this.Dispatcher.Invoke(new PopulateGridDelegate(populateGrid), new object[] { ClientServerUtil.getInstance().getProducts() });
                    break;
            }
        }

        public delegate void PopulateGridDelegate(SimulationServiceReference.productMap productMap);
        public delegate void ClearProductsDelegate();
        public void populateGrid(SimulationServiceReference.productMap productMap)
        {
            foreach (SimulationServiceReference.productMapEntry entry in productMap.productMap1.AsEnumerable())
            {
                SimulationServiceReference.position position = entry.key;
                SimulationServiceReference.product[] products = entry.value;
                string displayText = products.Length+"";
                for (int i = 0; i < products.Length; i += 1)
                {
                    if (products[i] != null) {
                        displayText = displayText + "\n" + products[i].name;
                    }
                }
                UIElement textBox = GridCanvas.getTextBox(displayText);
                List<UIElement> uiProducts = new List<UIElement>();
                uiProducts.Add(textBox);
                GridC.addProducts(uiProducts, position.x, position.y);
            }
        }

        public delegate void ShowPopUpDelegate(String message);
        public static void showPopUp(String message)
        {
            Window w = new Window();
            w.Content = message;
            w.SizeToContent = SizeToContent.WidthAndHeight;
            w.Show();
        }

            public delegate void ClearFactoryPulldownDelegate();
        public delegate void InitFactoryPulldownDelegate(ClientServerUtil clientServerUtil);
        private void refreshFactoryPulldownAsynchron()
        {
            this.Dispatcher.Invoke(new ClearFactoryPulldownDelegate(this.clearFactoryPulldown), new object[] { });
            this.Dispatcher.Invoke(new InitFactoryPulldownDelegate(this.initFactoryPulldown), new object[] { ClientServerUtil.getInstance() });
        }


        /* concurrent gui magic starts here */
        public GridItem GetItemFromID(string instId)
        {
            GridItem it = null;
            GridC.Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                it = (from item in GridC.Children.OfType<GridItem>() where item.instanceId.Equals(instId) select item).First()
            ));       
            return it;
        }

        //2D
        public delegate void SetAngleDelegate(int angle);
        public delegate void MoveUIElementDelegate(GridItem item, int x, int y);
        public delegate void CreateUIElementDelegate(GridItem it, int x, int y);
        public delegate ToolboxItem GetItemForIdDelegate(int classId);
        public delegate BitmapImage GetItemImageDelegate();
        public delegate GridItem GetGridItemDelegate(BitmapImage bitImg);


        //2D
        private void RotateUIElement(string itemId)
        {
            Console.WriteLine("MainWindow: Rotate called.");
            ClientServerUtil csu = ClientServerUtil.getInstance();
            position p = csu.getPosition(itemId);
            if (p != null)
            {
                int angle = getAngleFromDirection(p.direction);
                GridItem it = GetItemFromID(itemId);
                // Invoke ChangeTextBox with 1 parameter (TextForTextBox)
                GridC.Dispatcher.BeginInvoke(new SetAngleDelegate(it.set_angle), new object[] { angle });
            }
        }

        //2D
        private void MoveUIElement(string itemId)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            position p = csu.getPosition(itemId);
            if (p != null)
            {
                GridItem it = GetItemFromID(itemId);
                // Invoke ChangeTextBox with 1 parameter (TextForTextBox)
                GridC.Dispatcher.BeginInvoke(new MoveUIElementDelegate(GridC.moveItem), new object[] { it, p.x, p.y });
                #if DEBUG
                Console.WriteLine("MoveUIElement: x = " + p.x + " und y = " + p.y);
                #endif
            }
        }

        //2D
        private void CreateUIElement(string itemId)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            position p = csu.getPosition(itemId);
            if (p != null)
            {
                ToolboxItem toolboxItem = toolbox.Dispatcher.Invoke(new GetItemForIdDelegate(toolbox.getItemForId), new object[] { getClassIdFromItemId(itemId) }) as ToolboxItem;
                BitmapImage bimg = toolboxItem.Dispatcher.Invoke(new GetItemImageDelegate(toolboxItem.GetItemImage), new object[] { }) as BitmapImage;
                GridItem it = GridC.Dispatcher.Invoke(new GetGridItemDelegate(GridC.getGridItem), new object[] { bimg }) as GridItem;
                it.instanceId = itemId;
                GridC.Dispatcher.BeginInvoke(new CreateUIElementDelegate(GridC.placeItem), new object[] { it, p.x, p.y });
                #if DEBUG
                    Console.WriteLine("CreateUIElement: new GridItem created at x = " + p.x + " y = " + p.y);
                #endif
            }
        }

        public delegate void CreateSimulationElementDelegate(GridItem it, int x, int y);

        private void CreateSimulationElement(string itemId)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            position p = csu.getPosition(itemId);
            if (p != null)
            {
                ToolboxItem toolboxItem = toolbox.Dispatcher.Invoke(new GetItemForIdDelegate(toolbox.getItemForId), new object[] { getClassIdFromItemId(itemId) }) as ToolboxItem;
                //TextBox it = GridC.Dispatcher.Invoke(new GetGridItemDelegate(GridC.getGridItem), new object[] {  }) as GridItem;
                //it.instanceId = itemId;
                //GridC.Dispatcher.BeginInvoke(new CreateSimulationElementDelegate(GridC.placeItem), new object[] { it, p.x, p.y });
                #if DEBUG
                    Console.WriteLine("CreateSimulationElement: new TextBox created at x = " + p.x + " y = " + p.y);
                #endif
            }
        }

        public delegate void RemoveDelegate(string itemId);
        private void DeleteUIElement(string itemId)
        {
            GridC.Dispatcher.Invoke(new RemoveDelegate(GridC.remove), new object[] { itemId }); 
        }

        /* ends here */

        private void DeleteFactoryClick(object sender, System.Windows.RoutedEventArgs e)
        {
            string factoryString = (string)FactoryList.SelectedItem;
            int factoryID = getIdFromFactoryString(factoryString);
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.deleteFactory(factoryID);
            FactoryList.Items.Clear();
            initFactoryPulldown(csu);
        }

        private void RegistrationButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
        	RegistrationPanel.IsOpen = true;
        }
		
		private void CloseRegistrationEvent(object sender, System.Windows.RoutedEventArgs e)
        {
        	RegistrationPanel.IsOpen = false;
        }

        private void RegistradeEvent(object sender, System.Windows.RoutedEventArgs e)
        {
        	var username = RegUsernameInput.Text;
            var password = RegPasswordInput.Text;

            LoginServiceClient lsc = new LoginServiceClient();
            lsc.register(username,password);

            RegistrationPanel.IsOpen = false;
        }

        private void StartSimulationEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.simulate();
        }

        private void UserInviteClick(object sender, System.Windows.RoutedEventArgs e)
        {
            var username = UserInvite.Text;

            string factoryString = (string)FactoryList.SelectedItem;
            int factoryID = getIdFromFactoryString(factoryString);

        	ClientServerUtil csu = ClientServerUtil.getInstance();
            String message = csu.addMemberToMemberAccess(factoryID,username, factoryString);
            showPopUp(message);
            UserInvite.Clear();
        }

        private void ProduceButtonClickEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.produce();
        }

        private void ScriptingSaveClick(object sender, System.Windows.RoutedEventArgs e)
        {
            string script = ScriptingBox.Text.ToString();
            string machineID = null;
            Console.WriteLine("Script: " + script);
            Console.WriteLine("Maschine: " + machineID);

            ScriptingPanel.IsOpen = false;
        }

        private void ScriptingCloseClick(object sender, System.Windows.RoutedEventArgs e)
        {
        	ScriptingPanel.IsOpen = false;
        }

        private void ScriptingButtonClick(object sender, System.Windows.RoutedEventArgs e)
        {
            ScriptingPanel.IsOpen = true;
            string script = null;
            string machineID = null;
            string instanceID = null;
            Console.WriteLine("ID: " + instanceID);
            ScriptingBox.Text = script;
            Console.WriteLine("textausgabe: " + script);


        }
    }
}
 