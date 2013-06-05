using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Client.Classes;
using System.Windows.Media.Media3D;
using HelixToolkit.Wpf;
using Client.FactoryServiceReference;
using System.Windows.Threading;
using Client.View._3D;
using System.ServiceModel;
using System.IO;
using System.Windows.Media;
using System.Windows.Forms;
using Client.Communication;
using Client.View.Common;
using Client.View.Common.Forms;
using System.Threading;

namespace Client
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window, IMqObserver
    {
        private int[] scriptableItems;

        private GridModel grid;
        private Toolbox toolbox;
        private IconSet icons;
        private bool loggedIn = false;

        private OptionsWindow optionWindow;
        private PluginWindow pluginWindow;
        private ChatWindow chatWindow;
        private ClientServerUtil csu;

        private delegate void SetMaschinePositionBoxValueDelegate(Point3D point);
        private delegate void AppendMessageDelegate(string m);
        public delegate void RotateDelegate(string itemId, string dir);
        public delegate void MoveUIElement3DDelegate(String instanceID, Point3D point);
        public delegate void CreateUIElement3DDelegate(GridItem3D it);
        public delegate GridItem3D GetGridItem3DDelegate(int classID, Point3D point, String direction);
        public delegate void RemoveDelegate(string itemId);
        public delegate void InitFactoryPulldownDelegate();
        public delegate void ShowPopUpDelegate(String message);
        public delegate void TailbackDelegate(String message);
        public delegate void PopulateGridDelegate(int simulationFactoryId);
        public delegate void ClearProductsDelegate();
        private delegate void InitGridDelegate(machineMap machineMap);
        private delegate void ResizeGridDelegate(int height, int width);
        private delegate void LoadDelegate(int factoryID);
        private delegate void SetFactorySelectedDelegate(int factoryId);
        private delegate void SetSimulationSelectedDelegate(int simulationId);
        private delegate bool ShowInvitationWindowDelegate(int factoryID, string firstLine, string secondLine);
        private delegate void ToggleStateChangerDelegate(int toggelius);
        private delegate void DropdownSelectionDelegate(System.Windows.Controls.ComboBox dropdown, string selectedItem);
        public delegate void FactoryUIUpdater(int height, int width, FactoryServiceReference.machineMap factory);
        public delegate void SimulationUIUpdater(int height, int width, SimulationServiceReference.machineMap factory);

        
        public MainWindow()
        {
            InitializeComponent();

            //ConsoleManager.Show();

            icons = LoadIconSet();
            this.Focusable = false;

            Width.Text = lines.Length.ToString();
            Height.Text = lines.Width.ToString();

            grid = new GridModel(lines.Width, lines.Length, icons);
            Loaded += new RoutedEventHandler(OnGridLoaded);

            SimulationSpeedTextBox.Text = "" + 3;
            ConsumerEmptyTextBox.Text = "" + 0;

            DataContext = grid;
            view.ClipToBounds = false;

            CreateToolbox();
            Hide3DGrid();

            optionWindow = new OptionsWindow();
            optionWindow.configEvent += new EventHandler(optionWindow_configEvent);

            pluginWindow = new PluginWindow();
            pluginWindow.InitializeComboBox(icons.GetAvailablePlugins());
            pluginWindow.pluginEvent += new EventHandler(pluginWindow_pluginEvent);

            chatWindow = new ChatWindow();

            csu = ClientServerUtil.getInstance();
            csu.ClientServerEvent += new ClientServerEventHandler(csu_clientServerEvent);
            
            FactoryControll.IsEnabled = false;
            LoggedInControll.IsEnabled = false;

        }

        private IconSet LoadIconSet()
        {
            try
            {
                return new IconSet();
            }
            catch (IconSetException ise)
            {
                ShowPopUp(ise.Message + "\nBitte passen Sie die Pluginstruktur an.");
                this.Close();
                return null;
            }
        }


        /*  ------------------------------------------------- EVENTS -------------------------------------------------- */

        private void OnClick(object sender, MouseButtonEventArgs e)
        {
            
            if (!csu.isSimulation)
            {
                HideScriptingButton();
                HideConsumerEmptyButton();
                var position = GetPosition(e.GetPosition(view));

                GridItem3D item = grid.GetItemAtPosition(position);

                if (item != null) // Wenn auf Position Item liegt - ITEM SELECT
                {
                    SelectItem(item);
                    SetItemInformations(item);
                }
                else // Wenn Feld frei ist -> immer deselektieren und evtl Item setzen
                {
                    grid.ClearAndDeselectAllItems();
                    MachinePositionXBox.Text = "";
                    MachinePositionYBox.Text = "";
                    LoadTextBox.Text = "";
                    int id = toolbox.GetSelectedClassID();
                    if (id > 0 && id < 6)
                    {
                        grid.CreateItemOnServer(position, id);
                    }
                }
                ShowSelectedItems();
            }
        }

        private void SetItemInformations(GridItem3D item)
        {
            ShowScriptingButton(item);
            ShowConsumerEmptyButton(item);
            Point3D point = grid.TransformClientToServerCoords(item.position);
            SetMaschinePositionBoxValue(point);
            if (grid.selectedItems.Count > 0)
            {
                SetMachineCapacityTextbox(grid.selectedItems[0].instanceId);
            }
        }


        private void MouseReleased(object sender, MouseButtonEventArgs e)
        {
            var position = GetPosition(e.GetPosition(view));
            GridItem3D item = grid.GetItemAtPosition(position);

            if (grid.IsSetGhost()) // Item verschieben
            {
                grid.MoveItemOnServer(position);
                grid.UnsetGhost();
            }
            ShowSelectedItems();
        }

        private void PreviewItemMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            if (grid.selectedItems.Count > 0 & MousePressed()) // Wenn ALT nicht gedrückt und mehrere markiert sind
            {
                var position = GetPosition(e.GetPosition(view));
                grid.MoveGhost(position);
            }
        }

        private bool MousePressed()
        {
            return (Mouse.LeftButton == MouseButtonState.Pressed);
        }

        private bool CtrlPressed()
        {
            return ((Keyboard.Modifiers & (ModifierKeys.Control)) != ModifierKeys.None);
        }


        /* --------------------------------- Key Listener für komplettes MainWindow --------------------------------*/
        private void OnKeyPressedMainWindow(object sender, System.Windows.Input.KeyEventArgs e)
        {
            
            switch (e.Key)
            {
                case Key.S:
                    if (CtrlPressed())
                    {
                        SaveFactory();
                    }
                    break;
                case Key.F1:
                    if (FactoryDropdown.SelectedItem != null)
                    {
                        chatWindow.ToggleChatWindow();
                    }
                    break;
            }
        }

        /* --------------------------------- Key Listener für Helix-Ansicht --------------------------------*/
        private void OnKeyPressedHelixView(object sender, System.Windows.Input.KeyEventArgs e)
        {
            switch (e.Key)
            {
                case Key.Z:
                    view.ZoomExtents(500);
                    break;
                case Key.C:
                    grid.RemoveAllItemsOnServer();
                    break;
                case Key.Delete:
                    grid.RemoveSelectedItemsOnServer();
                    break;
            }
        }


        private void OnGridSizeChangeButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            try
            {
                int width = Convert.ToInt32(Width.Text);
                int height = Convert.ToInt32(Height.Text); 

                bool successful = csu.ResizeFactory(width, height);
                if (!successful)
                {
                    ShowPopUp("Grid kann nicht verkleinert werden. Maschinen würden außerhalb des gültigen Bereichs liegen. ");
                    Width.Text = "" + csu.GetAreaWidth();
                    Height.Text = "" + csu.GetAreaHeight();
                }
            }
            catch (FormatException)
            {
                ShowPopUp("Falsche Eingabe");
            }
        }


        private void OnFactorySelected(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            if (FactoryDropdown.Items.Count > 0)
            {
                string selectedFactory = (string)FactoryDropdown.SelectedItem;
                if (selectedFactory != null)
                {
                    int factoryID = HelperFunctions.GetIdFromFactoryString(selectedFactory);
                    LoadFactory(factoryID);
                }
            }
        }

        private void OnSimulationFactorySelected(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            if (SimulationFactoryDropdown.Items.Count > 0)
            {
                int simulationFactoryId = GetSelectedSimulationId();
                if (simulationFactoryId != 0)
                {
                    SimulationFactoryDropdown.SelectedItem = simulationFactoryId;
                    LoadSimulation(simulationFactoryId);
                }
            }
        }

        private void OnTabChanged(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            
            grid.ClearGrid();
            Hide3DGrid();
            toolbox.Clear();

            if (StateChanger.SelectedIndex == 0) // Factory-Tab
            {
                csu.isSimulation = false;
                toolbox.Init();
                OnFactorySelected(sender, e);
                BorderArea.Visibility = Visibility.Visible;
            }
            else // Simulation-Tab
            {
                csu.isSimulation = true;
                OnSimulationFactorySelected(sender, e);
                BorderArea.Visibility = Visibility.Hidden;
            }
        }

        private void LoadSimulation(int id)
        {
            SimulationLoader loader = new SimulationLoader(id, this);
            loader.SimulationDownload += new SimulationDownloadHandler(OnSimulationDownloadEvent);
            new Thread(loader.load).Start();
        }


        private void LoadFactory(int id)
        {
            FactoryLoader loader = new FactoryLoader(id, this);
            loader.FactoryDownload += new FactoryDownloadHandler(OnFactoryDownloadEvent);
            new Thread(loader.load).Start();
        }


        private void OnFactoryDownloadEvent(object sender, FactoryLoaderEventArgs e)
        {
            if (e.success)
            {
                Dispatcher.Invoke(new FactoryUIUpdater(updateUIAfterLoadingFatory), e.height, e.width, e.factory);
            }
            else
            {
                Dispatcher.Invoke(new ShowPopUpDelegate(ShowPopUp), "Server fehler");
            }
        }

        private void OnSimulationDownloadEvent(object sender, SimulationLoaderEventArgs e)
        {
            if (e.success)
            {
                Dispatcher.Invoke(new SimulationUIUpdater(updateUIAfterLoadingSimulation), e.height, e.width, e.factory);
            }
            else
            {
                Dispatcher.Invoke(new ShowPopUpDelegate(ShowPopUp), "Server fehler");
            }
        }

        private void updateUIAfterLoadingSimulation(int height, int width, SimulationServiceReference.machineMap simulation)
        {
            ResizeGrid(height, width);
            grid.InitSimulationGrid(simulation);
            Show3DGrid();
        }

        private void updateUIAfterLoadingFatory(int height, int width, FactoryServiceReference.machineMap Factory)
        {
            ResizeGrid(height, width);
            toolbox.Init();
            grid.InitFactoryGrid(Factory);
            Show3DGrid();
            EnableSidebarOnFactoryLoaded();
        }

        private void OnLogoutLoginButton(object sender, System.Windows.RoutedEventArgs e)
        {

            if (loggedIn == true)
            {
                Logout();
            }
            else if (loggedIn == false)
            {
                optionWindow.Show();
            }
        }

        private void OnRotateLeft(object sender, RoutedEventArgs e)
        {
            grid.RotateItemsOnServer("left");
        }

        private void OnRotateRight(object sender, RoutedEventArgs e)
        {
            grid.RotateItemsOnServer("right");
        }

        private void OnDeleteMachine(object sender, System.Windows.RoutedEventArgs e)
        {
            grid.RemoveSelectedItemsOnServer();
        }

        private void OnGridLoaded(object sender, RoutedEventArgs e)
        {
            view.ZoomExtents(500);
            view.Focus();
        }

        protected void OnSaveButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            SaveFactory();
        }

        private void SaveFactory()
        {
            csu.Save();
        }

        private void OnDeleteFactory(object sender, System.Windows.RoutedEventArgs e)
        {
            
            string factoryString = (string)FactoryDropdown.SelectedItem;
            int factoryID = HelperFunctions.GetIdFromFactoryString(factoryString);
            csu.DeleteFactory(factoryID);
            InitFactoryPulldown();
            Hide3DGrid();
            DisableSidebarOnFactoryCleared();
        }

        private void OnSimulationStart(object sender, System.Windows.RoutedEventArgs e)
        {
            
            if (FactoryDropdown.SelectedItem != null)
            {
                string errorID = csu.CreateSimulation();
                if (errorID == null)
                {
                    return;
                }
                else if (errorID.Equals(""))
                {
                    ShowPopUp("Simulation konnte nicht erzeugt werden.");
                }
                string[] parts = errorID.Split(new char[] { ':' });

                if (parts.Length == 2)
                {
                    string code = parts[0];
                    string simulationIdString = parts[1];
                    if (code.Equals("ERROR"))
                    {
                        ShowPopUp("Simulation konnte nicht erzeugt werden, weil Maschine " + parts[1] + " nicht richtig positioniert ist.");
                    }

                    else if (code.Equals("ID"))
                    {
                        int simulationID = int.Parse(simulationIdString);
                        string firstLine = "Simulation unter der ID " + simulationID + " erstellt.";
                        string secondLine = "Jetzt anzeigen?";

                        ClearSimulationPulldown();
                        InitSimulationPulldown();

                        object showSimulation = this.Dispatcher.Invoke(new ShowInvitationWindowDelegate(ShowSimulationInvitationWindow), new object[] { simulationID, firstLine, secondLine });
                        if ((bool)showSimulation)
                        {
                            Dictionary<int, string> simulations = csu.GetSimulationFactories();
                            if (simulations != null)
                            {
                                string simulationName = simulations[simulationID];
                                string simulationPulldownItemString = HelperFunctions.ToString(simulationID, simulationName);
                                SimulationFactoryDropdown.SelectedItem = simulationPulldownItemString;
                                LoadSimulation(simulationID);
                            }
                        }
                    }
                }
            }
        }

        private void OnUserInviteButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            var username = UserTextBox.Text;

            string factoryString = (string)FactoryDropdown.SelectedItem;
            int factoryID = HelperFunctions.GetIdFromFactoryString(factoryString);

            
            String message = "User " + username + " konnte nicht hinzugefügt werden.";
            Boolean success = csu.AddMemberToMemberAccess(factoryID, username);
            if (success)
            {
                message = "User " + username + " wurde erfolgreich zu " + factoryString + " hinzugefügt";
            }
            ShowPopUp(message);
            UserTextBox.Clear();
        }

        private void OnProduceButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            try
            {
                
                int simulationFactoryID = GetSelectedSimulationId();
                double timeInSeconds = 3;
                timeInSeconds = double.Parse(SimulationSpeedTextBox.Text);

                int timeInMils = (int)(timeInSeconds * 1000.0);
                csu.Produce(simulationFactoryID, timeInMils);
            }
            catch (Exception)
            {
                ShowPopUp("Ungültiger Wert");
            }
        }

        private void OnScriptButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            if (grid.selectedItems.Count == 1)
            {
                GridItem3D item = grid.selectedItems[0];
                ShowScriptingWindow(item);
            }
        }

        private void SimulationStopEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            
            int simulationFactoryId = GetSelectedSimulationId();
            csu.StopSimulation(simulationFactoryId);
        }

        private void SimulationDeleteEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            
            int simulationFactoryId = GetSelectedSimulationId();
            int stateOfDeletion = csu.DeleteSimulationFactory(simulationFactoryId);

            if (stateOfDeletion == -1)
            {
                ShowPopUp("Die Simulation muss erst angehalten werden, bevor sie gelöscht werden kann. ");
            }
            else if (stateOfDeletion == 0)
            {
                csu.RemoveMQFactoryListener(this);
                grid.ClearAndDeselectAllItems();
                grid.ClearGrid();
                Hide3DGrid();
                ClearSimulationPulldown();
                InitSimulationPulldown();
            }
            else
            {
                ShowPopUp("Es ist etwas schief gelaufen. Bitte versuchen Sie es erneut");
            }
        }

        private void AddUserToSimulationEvent(object sender, System.Windows.RoutedEventArgs e)
        {
            
            int simulationFactoryId = GetSelectedSimulationId();
            string username = SimulationUserTextBox.Text;
            csu.AddMemberToSimulation(simulationFactoryId, username);
        }

        /* --------------------------------------- LISTENER ------------------------------------------- */
        private void ClickListener(object sender, System.Windows.RoutedEventArgs e)
        {
            ShowFactoryWindow();
        }

        private void HandCurserListener(object sender, System.Windows.RoutedEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Hand;
        }

        private void ArrowCurserListener(object sender, System.Windows.RoutedEventArgs e)
        {
            Mouse.OverrideCursor = System.Windows.Input.Cursors.Arrow;
        }

        private void OnPositionChangeClick(object sender, System.Windows.RoutedEventArgs e)
        {
            if (grid.selectedItems.Count == 1)
            {
                GridItem3D item = grid.selectedItems[0];

                try
                {
                    double x = Convert.ToDouble(MachinePositionXBox.Text);
                    double y = Convert.ToDouble(MachinePositionYBox.Text);
                    Point3D point = new Point3D(x, y, 0);
                    item.MoveItemOnServer(point);
                }
                catch (FormatException)
                {
                    Point3D point = grid.TransformClientToServerCoords(item.position);
                    SetMaschinePositionBoxValue(point);
                }
            }
        }

        private void SetMaschinePositionBoxValue(Point3D point)
        {
            if (grid.selectedItems.Count == 1)
            {
                MachinePositionXBox.Text = ((int)point.X) + "";
                MachinePositionYBox.Text = ((int)point.Y) + "";
            }
            else
            {
                MachinePositionXBox.Text = "";
                MachinePositionYBox.Text = "";
            }
        }

        private void OnChatButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            if (FactoryDropdown.SelectedItem != null)
            {
                chatWindow.ToggleChatWindow();
            }
        }

        private void OnExitClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            Environment.Exit(0);
        }

        /* --------------------------------------- MQ-Listener ------------------------------------------- */
        public void Update(string topicName, string messageText, long publisher, string mqEvent)
        {
            Console.WriteLine("### MQ-Event: " + mqEvent + " - Text: " + messageText + " - Topic: " + topicName + " - Publisher: " + publisher);
            switch (mqEvent)
            {
                case "create":
                    CreateUIElement3D(messageText);
                    break;
                case "move":
                    MoveUIElement3D(messageText);
                    break;
                case "rotate-left":
                    RotateUIElement3D(messageText, "left");
                    break;
                case "rotate-right":
                    RotateUIElement3D(messageText, "right");
                    break;
                case "delete":
                    DeleteUIElement3D(messageText);
                    break;
                case "added-Factory":
                    AddedToFactory(messageText);
                    break;
                case "added-Simulation":
                    AddedToSimulation(messageText);
                    break;
                case "resize":
                    OnResizeMessage();
                    break;
                case "updateProducts":
                    int simulationId = int.Parse(topicName);
                    this.Dispatcher.Invoke(new PopulateGridDelegate(grid.PopulateGrid), new object[] { simulationId });
                    break;
                case "tailback":
                    this.Dispatcher.Invoke(new TailbackDelegate(Tailback), new object[] { messageText });
                    break;
                case "statistic":
                    ShowPopUp(messageText);
                    WriteFile(topicName, messageText);
                    break;
                case "shutdown":
                    Logout();
                    ShowPopUp(messageText);
                    break;
                case "chat":
                    this.Dispatcher.Invoke(new AppendMessageDelegate(chatWindow.AppendMessage), new object[] { messageText });
                    break;
            }

        }


        /* --------------------------------------- MAGIC START - METHODS ------------------------------------------- */
        private void OnResizeMessage()
        {
            
            int width = csu.GetAreaWidth();
            int height = csu.GetAreaHeight();

            if (width != -1 && height != -1)
            {
                this.Dispatcher.Invoke(new ResizeGridDelegate(this.ResizeGrid), new object[] { height, width });
                this.Dispatcher.Invoke(new InitGridDelegate(grid.InitFactoryGrid), new object[] { csu.GetFactory() });
            }
        }

        private void AddedToFactory(string factoryString)
        {
            int factoryID = HelperFunctions.GetIdFromFactoryString(factoryString);
            string firstLine = "Sie wurden zur Fabrik " + factoryString + " hinzugefügt.";
            string secondLine = "Jetzt anzeigen?";
            object showFactory = this.Dispatcher.Invoke(new ShowInvitationWindowDelegate(ShowFactoryInvitationWindow), new object[] { factoryID, firstLine, secondLine });
            if ((bool)showFactory)
            {
                this.Dispatcher.Invoke(new DropdownSelectionDelegate(SetSelected), new object[] { FactoryDropdown, factoryString });
                this.Dispatcher.Invoke(new LoadDelegate(LoadFactory), new object[] { factoryID });
            }
        }

        private void AddedToSimulation(string simulationString)
        {
            int simulationID = HelperFunctions.GetIdFromFactoryString(simulationString);
            string firstLine = "Sie wurden zur Simulation " + simulationString + " hinzugefügt.";
            string secondLine = "Jetzt anzeigen?";
            object showSimulation = this.Dispatcher.Invoke(new ShowInvitationWindowDelegate(ShowSimulationInvitationWindow), new object[] { simulationID, firstLine, secondLine });
            if ((bool)showSimulation)
            {
                this.Dispatcher.Invoke(new DropdownSelectionDelegate(SetSelected), new object[] { SimulationFactoryDropdown, simulationString });
                this.Dispatcher.Invoke(new LoadDelegate(LoadSimulation), new object[] { simulationID });
            }
        }

        private void SetSelected(System.Windows.Controls.ComboBox dropdown, string selectedItem)
        {
            dropdown.SelectedItem = selectedItem;
        }

        private void RefreshFactoryPulldownAsynchron()
        {
            this.Dispatcher.Invoke(new InitFactoryPulldownDelegate(this.InitFactoryPulldown), new object[] { });
        }

        private void RefreshSimulationPulldownAsynchron()
        {
            this.Dispatcher.Invoke(new InitFactoryPulldownDelegate(this.InitSimulationPulldown), new object[] { });
        }

        private GridItem3D GetItem3DFromID(string instId)
        {
            GridItem3D it = null;
            grid.Model.Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                it = (from item in grid.modelList where item.instanceId.Equals(instId) select item).First()
            ));
            return it;
        }

        private void CreateUIElement3D(string itemId)
        {
            position p = csu.GetPosition(itemId);
  
            if (p != null)
            {
                Point3D point = grid.TransformServerToClientCoords(new Point3D(p.x, p.y, 0));
                String direction = p.direction.ToString();
                int classID = HelperFunctions.GetClassIdFromItemId(itemId);

                GridItem3D it = grid.Model.Dispatcher.Invoke(new GetGridItem3DDelegate(grid.CreateGridItem3D), new object[] { classID, point, direction }) as GridItem3D;
                it.instanceId = itemId;
                grid.Model.Dispatcher.BeginInvoke(new CreateUIElement3DDelegate(grid.PlaceItem), new object[] { it });
            }

        }

        private void MoveUIElement3D(string itemId)
        {
            
            position p = csu.GetPosition(itemId);

            if (p != null)
            {
                Point3D serverPoint = new Point3D(p.x, p.y, 0);
                Point3D point = grid.TransformServerToClientCoords(serverPoint);

                grid.Model.Dispatcher.BeginInvoke(new SetMaschinePositionBoxValueDelegate(SetMaschinePositionBoxValue), new object[] { serverPoint });
                grid.Model.Dispatcher.BeginInvoke(new MoveUIElement3DDelegate(grid.MoveItem), new object[] { itemId, point });
            }
        }

        private void RotateUIElement3D(string itemId, string dir)
        {
            grid.Model.Dispatcher.Invoke(new RotateDelegate(grid.RotateItem), new object[] { itemId, dir });
        }

        private void DeleteUIElement3D(string itemId)
        {
            grid.Model.Dispatcher.Invoke(new RemoveDelegate(grid.RemoveItem), new object[] { itemId });
        }

        private void Tailback(String message)
        {
            
            int simulationFactoryId = GetSelectedSimulationId();
            GridItem3D item = grid.GetItemByID(message);
            if (item != null)
            {
                grid.GetItemByID(message).tailback = true;
            }
        }
        /* ends here */

        /* --------------------------------------- CLASS - UI - METHODS ------------------------------------------- */
        private void InitFactoryPulldown()
        {
            ClearFactoryPulldown();
            var factories = csu.GetFactories();

            if (factories != null)
            {
                foreach (int actId in factories.Keys)
                {
                    FactoryDropdown.Items.Add(HelperFunctions.ToString(actId, factories[actId]));
                }
            }
        }

        private void InitSimulationPulldown()
        {
            ClearSimulationPulldown();
            
            var data = new Dictionary<int, string>();
            var simulationFactories = csu.GetSimulationFactories();

            if (simulationFactories != null)
            {
                foreach (int actId in simulationFactories.Keys)
                {
                    SimulationFactoryDropdown.Items.Add(HelperFunctions.ToString(actId, simulationFactories[actId]));
                }
            }
        }

        private void ClearSimulationPulldown()
        {
            SimulationFactoryDropdown.Items.Clear();
        }

        private void ClearFactoryPulldown()
        {
            FactoryDropdown.Items.Clear();
        }

        private Point3D GetPosition(Point mousePosition)
        {
            var rayPosition = Viewport3DHelper.Point2DtoRay3D(view.Viewport, mousePosition);
            var tempPosition = rayPosition.PlaneIntersection(new Point3D(0, 0, 0.5), new Vector3D(0, 0, 1));
            var position = new Point3D(Math.Ceiling(tempPosition.Value.X - grid.xOffset), Math.Ceiling(tempPosition.Value.Y - grid.yOffset), 0);
            return position;
        }

        private void ShowSelectedItems()
        {
            InstaceIdBox.Text = "";
            foreach (GridItem3D item in grid.selectedItems)
            {
                InstaceIdBox.Text += item.instanceId + ", ";
            }
        }

        private void HideConsumerEmptyButton()
        {
            ConsumerCanvas.Visibility = Visibility.Hidden;
        }

        private void ShowConsumerEmptyButton(GridItem3D item)
        {
            if (grid.selectedItems.Count == 1 & item.isSelected)
            {
                if (grid.selectedItems[0].classId == 2)
                {
                    ConsumerCanvas.Visibility = Visibility.Visible;
                }
            }
        }

        private void HideScriptingButton()
        {
            ScriptingButton.Visibility = Visibility.Hidden;
        }

        private void ShowScriptingButton(GridItem3D item)
        {
            if (grid.selectedItems.Count == 1 & item.isSelected & scriptableItems.Contains(item.classId))
            {
                ScriptingButton.Visibility = Visibility.Visible;
            }
        }

        private void SelectItem(GridItem3D item)
        {
            if (!CtrlPressed()) // Wenn STRG nicht gedrückt und mehrere markiert sind
            {
                grid.DeselectItem(item);
                MachinePositionXBox.Text = "";
                MachinePositionYBox.Text = "";
            }
            grid.SelectItem(item);
        }

        private void WriteFile(string id, String message)
        {
            StreamWriter file = File.CreateText(Path.GetFullPath("../..") + "\\statistic" + id + ".txt");
            file.WriteLine(message);
            file.Close();
        }

        private void CreateToolbox()
        {
            this.toolbox = new Toolbox(icons);
            MachineExpander.Content = this.toolbox;
        }

        private void Hide3DGrid()
        {
            view.Visibility = System.Windows.Visibility.Hidden;
        }

        private void Show3DGrid()
        {
            view.Visibility = System.Windows.Visibility.Visible;
        }

        private void ResizeGrid(int height, int width)
        {
            Width.Text = width.ToString();
            Height.Text = height.ToString();

            grid.Resize(height, width);

            lines.Width = height;
            lines.Length = width;
        }

        private void CleanUI()
        {
            toolbox.Clear();
            grid.ClearGrid();
            ClearFactoryPulldown();
            Hide3DGrid();
        }

        /* --------------------------------------- HELP ------------------------------------------- */
        private int GetSelectedSimulationId()
        {
            if (SimulationFactoryDropdown.SelectedItem != null)
            {
                string simulationFactoryString = SimulationFactoryDropdown.SelectedItem.ToString();
                return HelperFunctions.GetIdFromFactoryString(simulationFactoryString);
            }
            return 0;
        }

        private void Logout()
        {
            csu.Logout();
            csu.RemoveMQUserListener(this);
            CleanUI();
            loggedIn = false;
            UsernameIfLoggedIn1.Content = "";
            DisableSidebarOnLogout();
        }

        /* --------------------------------------- FORM - METHODS FOR MODAL WINDOWS ------------------------------------------- */

        public static void ShowPopUp(String message)
        {
            System.Windows.Forms.MessageBox.Show(message);
        }

        public void ShowOptionsWindow()
        {
            if (!ConfigHelper.GetAutoConnect())
            {
                optionWindow.Show();
            }
            else // autoconnect -> automatisch server überprüfen und login
            {
                string[] server = ConfigHelper.GetServerAddress();
                string address = server[0];
                string port = server[1];
                string[] loginInfo = ConfigHelper.GetLoginInformations();

                if (CheckServerAddress(address, port))
                {
                    TryLogin(loginInfo[0], loginInfo[1]);
                }
            }
        }

        private bool CheckServerAddress(string address, string port)
        {
            bool connected = csu.CheckServerAddress(address, port);
            ConfigHelper.WriteServerFile(optionWindow.url.Text, optionWindow.port.Text);
            return connected;
        }

        private void TryLogin(string username, string password)
        {
            LoginHandler login = new LoginHandler(username,password,this);
            login.LoginEvent += new LoginEventHandler(OnLoginEvent);
            new Thread(login.login).Start();
        }

        public delegate void LoginSuccessDelegate(String username, String password, int[] scriptables);
        private void OnLoginEvent(object sender, LoginEventArgs e)
        {
            if (e.success)
            {
                Dispatcher.Invoke(new LoginSuccessDelegate(OnLoginSuccess), e.username, e.password, e.scriptables);
            }
            else
            {
                Dispatcher.Invoke(new ShowPopUpDelegate(ShowPopUp), "Login fehlgeschlagen! Ungültiger Username oder Passwort.");
                Dispatcher.Invoke(new Action(optionWindow.Show));
            }
        }

        private void OnLoginSuccess(String username, String password, int[] scriptables)
        {
            ConfigHelper.WriteLoginFile(username, password, optionWindow.autoconnect.IsChecked.Value);
            optionWindow.username.Text = username;
            optionWindow.password.Text = password;
            scriptableItems = scriptables;
            InitFactoryPulldown();
            InitSimulationPulldown();
            EnableSidebarOnLogin();
            optionWindow.Hide();
            UsernameIfLoggedIn1.Content = "Sie sind eingeloggt als " + username;
            loggedIn = true;
        }

        private void ShowRegistrationWindow()
        {
            RegistrationWindow registrationWindow = new RegistrationWindow();
            DialogResult result = registrationWindow.ShowDialog();
            if (result == System.Windows.Forms.DialogResult.OK)
            {
                if (RegisterUser(registrationWindow.RegUsername, registrationWindow.RegPassword))
                {
                    TryLogin(registrationWindow.RegUsername, registrationWindow.RegPassword);
                }
                else
                {
                    ShowPopUp("Registrierung fehlgeschlagen");
                }
            }
            else
            {
                DisableSidebarOnLogout();
                DisableSidebarOnFactoryCleared();
            }
            registrationWindow.Close();
        }

        private bool RegisterUser(string username, string password)
        {
            
            return csu.Register(username, password);
        }

        private void ShowFactoryWindow()
        {
            NewFactoryWindow factoryWindow = new NewFactoryWindow();
            DialogResult result = factoryWindow.ShowDialog();
            if (result == System.Windows.Forms.DialogResult.OK)
            {
                try
                {
                    DelegateFactoryData(factoryWindow.FactoryName, int.Parse(factoryWindow.FactorySizeX), int.Parse(factoryWindow.FactorySizeY));
                }
                catch (Exception)
                {
                    ShowPopUp("Falsche Eingabe");
                }
            }
        }

        private void DelegateFactoryData(string name, int height, int width)
        {
            int factoryId = csu.createFactory(name, width, height);
            if (factoryId != -1)
            {
                ResizeGrid(height, width);

                InitFactoryPulldown();

                FactoryDropdown.SelectedItem = HelperFunctions.ToString(factoryId, name);
                InitSimulationPulldown();
                grid.UpdateModel();
            }
        }

        private void ShowScriptingWindow(GridItem3D item)
        {
            string script = item.GetScriptFromServer();
            ScriptingWindow scriptingWindow = new ScriptingWindow(script);
            DialogResult result = scriptingWindow.ShowDialog();

            if (result == System.Windows.Forms.DialogResult.OK)
            {
                item.ScriptObjectOnServer(scriptingWindow.script);
            }
        }

        private bool ShowFactoryInvitationWindow(int factoryID, string firstLine, string secondLine)
        {
            YesNoWindow invitation = new YesNoWindow(firstLine, secondLine);
            DialogResult result = invitation.ShowDialog();
            bool yes = false;

            if (result == System.Windows.Forms.DialogResult.OK)
            {
                this.Dispatcher.Invoke(new LoadDelegate(this.LoadFactory), new object[] { factoryID });
                this.Dispatcher.Invoke(new ToggleStateChangerDelegate(this.ToggleStateChanger), new object[] { 0 });
                yes = true;
            }
            RefreshFactoryPulldownAsynchron();
            return yes;
        }

        private bool ShowSimulationInvitationWindow(int simulationID, string firstLine, string secondLine)
        {
            YesNoWindow invitation = new YesNoWindow(firstLine, secondLine);
            DialogResult result = invitation.ShowDialog();
            bool yes = false;

            if (result == System.Windows.Forms.DialogResult.OK)
            {
                this.Dispatcher.Invoke(new LoadDelegate(this.LoadSimulation), new object[] { simulationID });
                this.Dispatcher.Invoke(new ToggleStateChangerDelegate(this.ToggleStateChanger), new object[] { 1 });
                yes = true;
            }
            RefreshSimulationPulldownAsynchron();
            return yes;
        }

        private void ToggleStateChanger(int toggelius)
        {
            StateChanger.SelectedIndex = toggelius;
        }

        private void setFactorySelected(int factoryId)
        {
            
            var factories = csu.GetFactories();
            if (factories != null)
            {
                string factoryString = HelperFunctions.ToString(factoryId, factories[factoryId]);
                this.FactoryDropdown.SelectedItem = factoryString;
            }
        }

        private void setSimulationSelected(int simulationId)
        {
            
            var simulations = csu.GetSimulationFactories();
            if (simulations != null)
            {
                string simulationString = HelperFunctions.ToString(simulationId, simulations[simulationId]);
                this.SimulationFactoryDropdown.SelectedItem = simulationString;
            }
        }

        /* --------------------------------------------- ENABLE / DISABLE BUTTONS ---------------------------------------- */
        private void EnableSidebarOnFactoryLoaded()
        {
            FactoryControll.IsEnabled = true;
            Toolbox.IsEnabled = true;
            SaveChatToolbar.IsEnabled = true;
        }

        private void EnableSidebarOnLogin()
        {
            LoggedInControll.IsEnabled = true;
            Toolbox.IsEnabled = false;
            SaveChatToolbar.IsEnabled = false;
        }


        private void DisableSidebarOnLogout()
        {
            LoggedInControll.IsEnabled = false;
            FactoryControll.IsEnabled = false;
            Toolbox.IsEnabled = false;
            SaveChatToolbar.IsEnabled = false;
        }

        private void DisableSidebarOnFactoryCleared()
        {
            LoggedInControll.IsEnabled = true;
            FactoryControll.IsEnabled = false;
            Toolbox.IsEnabled = false;
            SaveChatToolbar.IsEnabled = false;
        }

        /* --------------------------------------- EVENT_HANDlER ------------------------------------------- */
        void optionWindow_configEvent(object sender, EventArgs e)
        {
            Client.OptionsWindow.OptionsEvent opt = (Client.OptionsWindow.OptionsEvent)e;
            if (opt.value == OptionsWindow.OK)
            {

                if (CheckServerAddress(optionWindow.url.Text, optionWindow.port.Text))
                {
                    TryLogin(optionWindow.username.Text, optionWindow.password.Text);
                }

            }
            else if (opt.value == OptionsWindow.CANCEL)
            {
                optionWindow.Visibility = Visibility.Hidden;
            }
            else if (opt.value == OptionsWindow.REGISTER)
            {
                if (CheckServerAddress(optionWindow.url.Text, optionWindow.port.Text))
                {
                    ConfigHelper.WriteServerFile(optionWindow.url.Text, optionWindow.port.Text);
                    optionWindow.Visibility = Visibility.Hidden;
                    ShowRegistrationWindow();
                }
            }
        }

        void pluginWindow_pluginEvent(object sender, EventArgs e)
        {
            Client.PluginWindow.PluginEvent plugins = (Client.PluginWindow.PluginEvent)e;
            if (plugins.value == PluginWindow.APPLY)
            {
                IconSet.WriteFolderName(pluginWindow.SelectedPlugin);
                icons = LoadIconSet();
                pluginWindow.Visibility = Visibility.Hidden;
                ShowPopUp("IconSet gewechselt, Änderung beim nächsten Programmstart aktiv!");
            }
            else if (plugins.value == PluginWindow.CANCEL)
            {
                pluginWindow.Visibility = Visibility.Hidden;
            }
            else if (plugins.value == PluginWindow.INFO)
            {
                PluginInfoWindow piw = new PluginInfoWindow();
                piw.Show();
            }
        }

        void csu_clientServerEvent(object sender, ClientServerEventArgs e)
        {
            ShowPopUp(e.message);
            if (e.typ == 0)
            {
                csu.DestroySession();
                csu.RemoveMQUserListener(this);
                CleanUI();
                DisableSidebarOnLogout();
                loggedIn = false;
                UsernameIfLoggedIn1.Content = "";
            }
        }

        void OnClose(object sender, System.ComponentModel.CancelEventArgs e)
        {
            Environment.Exit(0);
        }

        /* -------------------------------- Menü-Einträge -----------------------------------------*/


        private void OnSystemCloseClick(object sender, System.Windows.RoutedEventArgs e)
        {
            SaveFactory();
            App.Current.Shutdown();
        }


        private void OnShortcutsInfoClick(object sender, System.Windows.RoutedEventArgs e)
        {
            ShortcutInfoWindow shortcutinfo = new ShortcutInfoWindow();
            shortcutinfo.ShowDialog();
        }


        private void OnScriptingInfoClick(object sender, System.Windows.RoutedEventArgs e)
        {
            ScriptingInfoWindow scriptinginfo = new ScriptingInfoWindow();
            scriptinginfo.ShowDialog();
        }

        private void OnOptionsClick(object sender, System.Windows.RoutedEventArgs e)
        {
            optionWindow.Show();
        }

        private void OnPluginClick(object sender, System.Windows.RoutedEventArgs e)
        {
            pluginWindow.Show();
        }

        private void OnInfoClick(object sender, System.Windows.RoutedEventArgs e)
        {
            InfoWindow info = new InfoWindow();
            info.ShowDialog();
        }

        private void OnLoadButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            try
            {
                int anzahl = Convert.ToInt32(LoadTextBox.Text);
                SetMachineCapacity(anzahl);
            }
            catch (FormatException)
            {
                SetMachineCapacityTextbox(grid.selectedItems[0].instanceId);
            }
        }

        private void SetMachineCapacity(int anzahl)
        {
            

            if (grid.selectedItems.Count > 0)
            {
                csu.SetMachineCapacity(anzahl, grid.selectedItems.Select(item => item.instanceId).ToArray());                
            }
        }

        private void SetMachineCapacityTextbox(string instanceId)
        {
            LoadTextBox.Text = csu.GetMachineCapacity(instanceId).ToString();
        }

        private void OnConsumerButtonClicked(object sender, System.Windows.RoutedEventArgs e)
        {
            if ((grid.selectedItems.Count == 1) & grid.selectedItems[0].classId == 2)
            {
                SetEmptyConsumerTick();
            }
        }

        private void SetEmptyConsumerTick()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            string machineID = grid.selectedItems[0].instanceId;
            int consumerEmptyCount = Convert.ToInt32(ConsumerEmptyTextBox.Text);
            csu.SetEmptyConsumerTickCount(machineID, consumerEmptyCount);
        }
    }
}
