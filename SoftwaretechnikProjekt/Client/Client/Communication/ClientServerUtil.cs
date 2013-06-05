using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Threading;
using Apache.NMS.ActiveMQ;
using Client.LoginServiceReference;
using Client.FactoryServiceReference;
using Sim = Client.SimulationServiceReference;


namespace Client.Communication
{
    public delegate void ClientServerEventHandler(object sender, ClientServerEventArgs e);

    public class ClientServerEventArgs : EventArgs
    {
        public String message { get; set; }
        public int typ { get; set; }

        public ClientServerEventArgs()
        {
            this.typ = 1; // Schwerwiegend -> Server nicht erreichbar
            this.message = "Server nicht erreichbar";
        }

        public ClientServerEventArgs(String message)
        {
            this.typ = 0; // Sonstiger Fehler -> Server existiert
            this.message = message;
        }
    }

    /// <summary>
    /// Bietet Zugriff auf den Server
    /// </summary>
    public class ClientServerUtil
    {
        LoginServiceClient loginService;
        FactoryServiceClient factoryService;
        Sim.SimulationServiceClient simulationService;
        long sessionID;
        
        public bool isSimulation {get; set;}
        private string username;
        private ConnectionUtils connUtil;
        private static ActiveMQListener factoryListener;
        private static ActiveMQListener userListener;
        private static ClientServerUtil instance;

        public event ClientServerEventHandler ClientServerEvent;

        /* ---------------------------------- INIT ------------------------------------ */
        public static ClientServerUtil getInstance() {
            if (instance == null)
                instance = new ClientServerUtil();
            return instance;
        }

        private ClientServerUtil()
        {
            connUtil = new ConnectionUtils();
            factoryListener = new ActiveMQListener();
            userListener = new ActiveMQListener();
            isSimulation = false;
        }

        public void SetUpServer(string ip, string port)
        {
                connUtil.SetUpServer(ip, port);
                CloseServices();
                loginService = connUtil.createLoginService();
                factoryService = connUtil.createFactoryService();
                simulationService = connUtil.createSimulationService();
        }

        private void CloseServices()
        {
            if (loginService != null)
            {
                loginService.Close();
            }
            if (factoryService != null)
            {
                factoryService.Close();
            }
            if (simulationService != null)
            {
                simulationService.Close();
            }
        }

        /* -------------------------------- Factory Services ------------------------------------ */
        public int[] GetScriptableMachines() {
            try 
            {
                List<int> result = new List<int>();
                System.Nullable<int>[] remote = factoryService.getScriptableMachines();
                foreach (int? rId in remote)
                {
                    result.Add(rId.Value);
                }
                return result.ToArray<int>();
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public void CreateItem(int classID, int x, int y, direction direction)
        {
            try
            {
                if (!isSimulation)
                {
                    factoryService.createMachine(sessionID, classID, x, y, direction);
                }
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void MoveItem(string instanceID, int x, int y) 
        {
            try
            {
                if (!isSimulation)
                {
                    factoryService.moveMachine(sessionID, instanceID, x, y);
                }
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void RotateItemsLeft(string[] instanceIDs)
        {
            try
            {
                if (!isSimulation)
                {
                    factoryService.rotateMachinesLeft(sessionID, instanceIDs);
                }
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void RotateItemsRight(string[] instanceIDs)
        {
            try
            {
                if (!isSimulation)
                {
                    factoryService.rotateMachinesRight(sessionID, instanceIDs);
                }
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void DeleteItems(string[] instanceIDs)
        {
            try
            {
                if (!isSimulation)
                {
                    factoryService.deleteMachines(sessionID, instanceIDs);
                }
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void DeleteFactory(int factoryID)
        {
            try
            {
                factoryService.deleteFactory(sessionID, factoryID);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public position GetPosition(string instanceID)
        {
            try
            {
                position p = factoryService.getMachinePosition(sessionID, instanceID);
                if (p.x == -1 && p.y == -1)
                    return null;
                return p;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public machineMap GetFactory()
        {
            try
            {
                return factoryService.getFactory(sessionID);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public int GetAreaWidth()
        {
            try
            {
                int width = factoryService.getAreaWidth(sessionID);
                return width;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return -1;
        }

        public int GetAreaHeight()
        {
            try
            {
                int length = factoryService.getAreaHeight(sessionID);
                return length;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return -1;
        }

        public void ScriptMachine(String instanceID, String script)
        {
            try
            {
                factoryService.scriptMachine(sessionID, instanceID, script);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public string GetMachineScript(String instanceID)
        {
            try
            {
                return factoryService.getMachineScript(sessionID, instanceID);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return "";
        }

        public bool ResizeFactory(int width, int height)
        {
            try
            {
                return factoryService.resizeFactory(sessionID, width, height);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return false;
        }

        public bool AddMemberToMemberAccess(int factoryID, string username)
        {
            try
            {
                return factoryService.addMemberToMemberAccess(sessionID, factoryID, username);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return false;
        }

        public void Save()
        {
            try
            {
                factoryService.saveFactory(sessionID);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            catch (NullReferenceException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Nicht zu Fabrik verbunden"));
            }
        }

        /* -------------------------------- Login Services ------------------------------------ */
        public bool Login(string username, string password)
        {
            try
            {
                this.username = username;
                this.sessionID = loginService.connect(username, password);

                if (sessionID != -1)
                {
                    return true;
                }
                else {
                    ClientServerEvent(this, new ClientServerEventArgs("Ungültiger Username oder Passwort."));
                }
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            return false;
        }

        public bool Register(string username, string password)
        {
            try
            {
                return loginService.register(username, password);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return false;
        }

        public bool ConnectToFactory(int factoryID)
        {
            factoryListener.ClearAllObservers();
            try
            {
                return loginService.connectToFactory(sessionID, factoryID);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return false;
        }

        public void Logout()
        {
            try
            {
                loginService.disconnect(sessionID);
                factoryListener.ClearAllObservers();
                userListener.ClearAllObservers();
                username = null;
                sessionID = -1;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }


        public Dictionary<int, string> GetFactories()
        {
            Dictionary<int, string> factoryMap = new Dictionary<int, string>();
            try
            {
                factoryMap factories = loginService.getFactories(sessionID);
                foreach (factoryMapEntry1 ele in factories.factories.AsEnumerable<factoryMapEntry1>())
                {
                    factoryMap.Add(ele.key, ele.value);
                }
                return factoryMap;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public bool CheckServerAddress(string address, string port)
        {
            SetUpServer(address, port);
            try
            {
                return loginService.welcome();
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            catch (NullReferenceException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            return false;
        }

        public int createFactory(string name, int width, int height)
        {
            try
            {
                return loginService.createFactory(sessionID, name, width, height);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return -1;
        }

        /* -------------------------------- Sonstiges ------------------------------------ */

        public void RegisterFactoryMQListener(int factoryID, IMqObserver observer)
        {
            string topicName = "" + factoryID;
            RegisterMQListener(topicName, observer, factoryListener);
        }

        public void RegisterInvitationMqListener(IMqObserver obs)
        {
            RegisterMQListener(this.username, obs, userListener);
        }

        private void RegisterMQListener(string topicName, IMqObserver observer, IMqObservable lis)
        {
            lis.SetTopicName(topicName);
            lis.SetBrokerUrl(connUtil.mqBrokerUrl);
            lis.AddObserver(observer);
            lis.UpdateConnection();
        }

        public void RemoveMQFactoryListener(IMqObserver observer)
        {
            factoryListener.RemoveObserver(observer);
        }

        public void RemoveMQUserListener(IMqObserver observer)
        {
            userListener.RemoveObserver(observer);
        }

        /* -------------------------------- Simulation Services ------------------------------------ */
        public string CreateSimulation()
        {
            try{
            return simulationService.createSimulation(sessionID);
            }
                catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public void AddMemberToSimulation(int simulationFactoryId, string username)
        {
            try{
                simulationService.addMemberToSimulation(sessionID, simulationFactoryId, username);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void Produce(int simulationFactoryId, int timeInMillis)
        {
            try{
                simulationService.produce(sessionID, simulationFactoryId, timeInMillis);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public int DeleteSimulationFactory(int simulationFactoryId)
        {
            try
            {
                return simulationService.deleteSimulation(sessionID, simulationFactoryId);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return -2;
        }

        public void StopSimulation(int simulationFactoryId)
        {
            try
            {
                simulationService.stopSimulation(simulationFactoryId);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public Sim.productMap GetProducts(int simulationFactoryId)
        {
            try
            {
                return simulationService.getProducts(simulationFactoryId);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public Sim.machineMap GetSimulation(int simulationId)
        {
            try
            {
                return simulationService.getSimulation(simulationId);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public Dictionary<int, string> GetSimulationFactories()
        {
            Dictionary<int, string> simulationFactoryMap = new Dictionary<int, string>();
            try
            {
                Sim.factoryMap simulations = simulationService.getSimulations(sessionID);
                foreach (Sim.factoryMapEntry1 ele in simulations.factories.AsEnumerable<Sim.factoryMapEntry1>())
                {
                    simulationFactoryMap.Add(ele.key, ele.value);
                }
                return simulationFactoryMap;
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return null;
        }

        public void SetMachineCapacity(int capacityInNumberOfProducts, string[] instanceIds)
        {
            try
            {
                factoryService.setMachineCapacity(sessionID, instanceIds, capacityInNumberOfProducts);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public int GetMachineCapacity(string instanceId)
        {
            try
            {
                return factoryService.getMachineCapacity(sessionID, instanceId);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
            return -1;
        }

        public void SendChatMessage(string text)
        {

            try
            {
                factoryService.sendChatMessage(sessionID, text);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }

        public void SetEmptyConsumerTickCount(string machineID, int noOfTicks)
        {
            try
            {
                factoryService.setEmptyConsumerTickCount(sessionID, machineID, noOfTicks);
            }
            catch (EndpointNotFoundException)
            {
                ClientServerEvent(this, new ClientServerEventArgs());
            }
            catch (FaultException)
            {
                ClientServerEvent(this, new ClientServerEventArgs("Fehler auf Server"));
            }
        }
    
        public void DestroySession()
        {
            this.sessionID = -1;
            this.username = "";
            this.isSimulation = false;
        }
    }
}
