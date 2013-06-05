using System;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SoapClientTest.LoginServiceReference;
using SoapClientTest.FactoryServiceReference;
using Apache.NMS;

namespace SoapClientTest
{
    public class TestMQObserver : IMqObserver
    {
        public void update(string factory, string itemId, long publisher, string mqEvent)
        {
            //TODO: falls publisher nicht eigene sessionId
            //TODO: factoryService.getPosition(itemId);
            //TODO in GUI erzeugen/setzen/...
            Console.WriteLine("received: "+mqEvent+" "+itemId);
        }
    }


    public class ConnectionUtils
    {
        const string SERVER_PC = "pc13-118";
        public const string MQ_BROKER_URL = "tcp://"+SERVER_PC+".intern.mi.hs-rm.de:61616";
        const string SERVICEURL = "http://"+SERVER_PC+".intern.mi.hs-rm.de:8080/";
        const string FACTORY_ENDING = "FactoryService";
        const string LOGIN_ENDING = "LoginService";

        private static EndpointAddress getEndpointAddress(string customUrlEnding)
        {
            string customURL = SERVICEURL + customUrlEnding;
            Console.WriteLine("Factory-Service wird aufgerufen: " + customURL);
            return new EndpointAddress(customURL);
        }

        public static FactoryServiceClient getFactoryService()
        {
            Binding httpBinding = new BasicHttpBinding();
            FactoryServiceClient factoryService = new FactoryServiceClient(httpBinding, getEndpointAddress(FACTORY_ENDING));
            return factoryService;
        }


        public static LoginServiceClient getLoginService()
        {
            Binding httpBinding = new BasicHttpBinding();
            LoginServiceClient service = new LoginServiceClient(httpBinding, getEndpointAddress(LOGIN_ENDING));
            return service;
        }


    }


    class LoginTest
    {
        static LoginServiceClient loginService;
        static FactoryServiceClient factoryService;
        static long sessionID;
        private static ActiveMQListener listener;


        static string[] getFactories(LoginServiceClient service, long sessionId)
        {
            try
            {
                Console.WriteLine("Versuche Factories zu laden...");
                string[] factories = service.getFactories(sessionId);
                Console.WriteLine("Factories erhalten: ");
                foreach (string ele in factories)
                {
                    Console.WriteLine("  '" + ele + "' ");
                }
                return factories;
            }
            catch (FaultException e)
            {
                Console.WriteLine("Problem beim Laden der Factory-Liste.");
            }
            return null;
        }


        static long login(string username, string password)
        {
            try
            {
                Console.WriteLine("\nwird eingeloggt...");
                long sessionID = loginService.connect(username, password);
                Console.WriteLine("...uuuund die ID ist: "+ sessionID);

                return sessionID;
            }
            catch (FaultException e)
            {
                Console.WriteLine("Could not log in "+username+", "+password);
                return -1;
            }
        }


        static bool connectToFactory(string factoryName)
        {
            return loginService.connectToFactory(sessionID, factoryName);
        }


        static void printFactory(factory connectedFactory)
        {
            Console.WriteLine("\nfactory contains:");
            foreach (factoryEntry actEntry in connectedFactory.itemMap.AsEnumerable())
            {
                Console.WriteLine("item: "+actEntry.key);
                Console.WriteLine("position: "+actEntry.value.x+","+actEntry.value.y+", "+actEntry.value.direction);
            }
        }

        static String createItem(int classID, int x, int y, direction direction)
        {
            position ourPosition = getPosition(x, y, direction);
            return factoryService.createItem(sessionID, classID, ourPosition);
        }

        private static long testLogin()
        {
            string rightUsername = "Client_Eastwood";
            string rightPassword = "ninjaflex";
            string wrongUsername = "user";
            string wrongPassword = "123";

            Console.WriteLine("\ntry: invalid user " + wrongUsername + ", invalid password " + wrongPassword);
            login(wrongUsername, wrongPassword);
            
            Console.WriteLine("try: valid user " + rightUsername + ", valid password " + rightPassword);
            return login(rightUsername, rightPassword);
        }

        private static position getPosition(int x, int y, direction direction)
        {
            position newPosition = new position();
            newPosition.x = x;
            newPosition.y = y;
            newPosition.direction = direction;
            return newPosition;
        }


        private static void registerMQListener(String factoryID)
        {
            listener = new ActiveMQListener();
            listener.topicName = factoryID;
            listener.brokerUrl = ConnectionUtils.MQ_BROKER_URL;
            listener.addObserver(new TestMQObserver());
            listener.UpdateConnection();
        }


        static void Main(string[] args)
        {
            string factoryID;
            string [] factories;

            loginService = ConnectionUtils.getLoginService();
            sessionID = testLogin();
            

            factories = getFactories(loginService, sessionID);
            factoryID = factories[0];
            

            bool connected = connectToFactory(factoryID);
            registerMQListener(factoryID);
            Console.WriteLine("Connected to factory: "+connected);


            loginService.Close();
            factoryService = ConnectionUtils.getFactoryService();
            factory connectedFactory = factoryService.getFactory(sessionID);
            printFactory(connectedFactory);
            

            string itemId1 = createItem(1, 17, 42, FactoryServiceReference.direction.SOUTH);
            string itemId2 = createItem(89, 0, 0, FactoryServiceReference.direction.NORTH);

            connectedFactory = factoryService.getFactory(sessionID);
            printFactory(connectedFactory);


            int x = 20;
            int y = 20;
            direction dir = direction.WEST;
            position pos = getPosition(20, 20, direction.WEST);
            Console.WriteLine("Moving item " + itemId1 + " to " + x + "," + y + ", direction:" + dir);
            factoryService.moveItem(sessionID, itemId1, pos);

            connectedFactory = factoryService.getFactory(sessionID);
            printFactory(connectedFactory);


            Console.WriteLine("deleting item " + itemId1);
            factoryService.deleteItem(sessionID, itemId1);

            connectedFactory = factoryService.getFactory(sessionID);
            printFactory(connectedFactory);


            Console.WriteLine("Warte auf input");
            Console.ReadKey();
            
        }

        
    }
}
