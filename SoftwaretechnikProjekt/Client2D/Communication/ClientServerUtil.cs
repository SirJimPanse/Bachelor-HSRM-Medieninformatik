using System;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Apache.NMS;
using Coordinate = System.Tuple<int, int>;
using Apache.NMS.ActiveMQ;
using Client.LoginServiceReference;
using Client.FactoryServiceReference;
using Sim = Client.SimulationServiceReference;
using System.Windows;


namespace Client
{
    public class ConnectionUtils
    {
        public string SERVER_PC { set{
            
            if (value == "")
                value = "localhost";
            else if (!value.Contains('.') && value != "localhost")
                value += "." + DEFAULT_DOMAIN;

            mqBrokerUrl = "tcp://" + value + ":" + TCP_PORT;
            SERVICEURL = "http://" + value + ":" + SERVICE_PORT + "/";
        }}
        public string mqBrokerUrl;
        string SERVICEURL;

        const string FACTORY_ENDING = "FactoryService";
        const string LOGIN_ENDING = "LoginService";
        const string SIM_ENDING = "SimulationService";

        const string TCP_PORT = "61616";
        const string SERVICE_PORT = "8080";
        const string DEFAULT_DOMAIN = "intern.mi.hs-rm.de";

        public ConnectionUtils()
        {
            SERVER_PC = "pc13-108";
        }

        private EndpointAddress getEndpointAddress(string customUrlEnding)
        {
            string customURL = SERVICEURL + customUrlEnding;
            Console.WriteLine("Factory-Service wird aufgerufen: " + customURL);
            return new EndpointAddress(customURL);
        }

        public FactoryServiceClient createFactoryService()
        {
            Binding httpBinding = new BasicHttpBinding();
            FactoryServiceClient factoryService = new FactoryServiceClient(httpBinding, getEndpointAddress(FACTORY_ENDING));
            return factoryService;
        }

        public LoginServiceClient createLoginService()
        {
            Binding httpBinding = new BasicHttpBinding();
            LoginServiceClient service = new LoginServiceClient(httpBinding, getEndpointAddress(LOGIN_ENDING));
            return service;
        }

        public Sim.SimulationServiceClient createSimulationService()
        {
            Binding httpBinding = new BasicHttpBinding();
            Sim.SimulationServiceClient simulationService = new Sim.SimulationServiceClient(httpBinding, getEndpointAddress(SIM_ENDING));
            return simulationService;
        }
    }

    class LoginException : Exception
    {
        public LoginException() : base("Could not login.") {}
        public LoginException(String message) : base(message) {}
    }

    public class ClientServerUtil
    {
        LoginServiceClient loginService;
        FactoryServiceClient factoryService;
        Sim.SimulationServiceClient simulationService;
        long sessionID;
        private string username; // TODO topicname evtl ersetzen auf serverseite durch session ID
        private ConnectionUtils connUtil;
        private static ActiveMQListener factoryListener;
        private static ActiveMQListener userListener;
        private static ClientServerUtil instance;

        public static ClientServerUtil getInstance() {
            if (instance == null)
                instance = new ClientServerUtil();
            return instance;
        }

        public string serverPc {set {
                connUtil.SERVER_PC = value;
                closeServices();
                loginService = connUtil.createLoginService();
                factoryService = connUtil.createFactoryService();
                simulationService = connUtil.createSimulationService();
        }}

        private void closeServices()
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
              
        public ClientServerUtil()
        {
            connUtil = new ConnectionUtils();
            factoryListener = new ActiveMQListener();
            userListener = new ActiveMQListener();
        }

        public Dictionary<int, string> getFactories()
        {
            Dictionary<int, string> factoryMap = new Dictionary<int, string>();
            try
            {
                Console.WriteLine("Versuche Factories zu laden...");
                factoryMap factories = loginService.getFactories(sessionID);
                Console.WriteLine("Factories erhalten: ");
                foreach (factoryMapEntry1 ele in factories.factories.AsEnumerable<factoryMapEntry1>())
                {
                    factoryMap.Add(ele.key, ele.value);
                }
                return factoryMap;
            }
            catch (FaultException e)
            {
                Console.WriteLine("Problem beim Laden der Factory-Liste.");
            }
            catch(EndpointNotFoundException ex)
            {
                Console.WriteLine("Falsche Server-IP");
            }
            return factoryMap;
        }

        public void login(string username, string password, IMqObserver obs)
        {
            try
            {
                Console.WriteLine("\nwird eingeloggt...");
                this.username = username;
                this.sessionID = loginService.connect(username, password);
                Console.WriteLine("...uuuund die ID ist: "+ sessionID);
                if (sessionID == -1)
                    throw new LoginException();
                _registerMQListener(username,obs,userListener);
            }
            catch (FaultException e)
            {
                Console.WriteLine("Could not log in "+username+", "+password);
                throw new LoginException("Ungültiger Username oder Passwort.");
            }
            catch(EndpointNotFoundException ex)
            {
                Console.WriteLine("Falsche IP");
                throw new LoginException("Ungültige Server-Adresse.");
            }
        }
     
        public bool connectToFactory(int factoryId)
        {
            factoryListener.ClearAllObservers();
            return loginService.connectToFactory(sessionID, factoryId);
        }

        public int createFactory(string name, int width, int height)
        {
            return loginService.createFactory(sessionID, name, width, height);
        }

        public String createItem(int classID, int x, int y, direction direction)
        {
            Console.WriteLine("Creating item on server. Position is " + x + "," + y+", direction:"+direction);
            return factoryService.createMachine(sessionID, classID, x, y, direction);
        }

        public bool moveItem(string instanceID, int x, int y) 
        {
            return factoryService.moveMachine(sessionID, instanceID, x, y);
        }

        public void rotateItemLeft(string instanceID)
        {
            factoryService.rotateMachineLeft(sessionID, instanceID);
        }

        public void rotateItemRight(string instanceID)
        {
            factoryService.rotateMachineRight(sessionID, instanceID);
        }

        private static position createPosition(int x, int y, direction direction)
        {
            position newPosition = new position();
            newPosition.x = x;
            newPosition.y = y;
            newPosition.direction = direction;
            return newPosition;
        }

        public void register(string username, string password)
        {
            loginService.register(username, password);
        }

        public void deleteFactory(int factoryID)
        {
            factoryService.deleteFactory(sessionID, factoryID);
        }

        public bool deleteItem(String instanceID)
        {
            return factoryService.deleteMachine(sessionID, instanceID);
        }

        public position getPosition(string itemID)
        {
            position p = factoryService.getMachinePosition(sessionID, itemID);
            if (p.x == -1 && p.y == -1)
                return null;
            return p;
        }

        public machineMap getFactory()
        {
            return factoryService.getFactory(sessionID);
        }

        public Sim.productMap getProducts()
        {
            return simulationService.getProducts(sessionID);
        }

        public void logout()
        {
            loginService.disconnect(sessionID);
            factoryListener.ClearAllObservers();
            userListener.ClearAllObservers();
            username = null;
            sessionID = -1;
        }

        public void Save()
        {
            factoryService.saveFactory(sessionID);
        }

        public void registerFactoryMQListener(int factoryID, IMqObserver observer)
        {
            string topicName = "" + factoryID;
            _registerMQListener(topicName,observer,factoryListener);
        }

        private void _registerMQListener(string topicName, IMqObserver observer, IMqObservable lis)
        {
            lis.SetTopicName(topicName);
            lis.SetBrokerUrl(connUtil.mqBrokerUrl);
            lis.AddObserver(observer);
            lis.UpdateConnection();
        }

        public void simulate()
        {
            simulationService.simulate(sessionID);
        }

        public void produce()
        {
            simulationService.produce(sessionID);
        }
        
        public String addMemberToMemberAccess(int FactoryID, string username, string factoryName)
        {
            Boolean succes = factoryService.addMemberToMemberAccess(sessionID, FactoryID, username);
            if (succes == true)
            {
                return "User " + username + " wurde erfolgreich zu " + factoryName + " hinzugefügt";        
            }
            return "User "+username+" konnte nicht hinzugefügt werden. :(";
        }

        public int getAreaWidth()
        {
            int width = factoryService.getAreaWidth(sessionID);
            return width;
        }

        public int getAreaLength()
        {
            int length = factoryService.getAreaHeight(sessionID);
            return length;
        }

        public void scriptMachine(String machineID, String script)
        {
            factoryService.scriptMachine(sessionID, machineID, script);
        }

        public string getMachineScript(String machineID)
        {
            return factoryService.getMachineScript(sessionID, machineID);
        }

    }
}
