using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
// Assemblies für ActiveMQ-Zugriff; 
// hierzu Verweise auf 
// "Apache.NMS.dll" und "Apache.NMS.ActiveMQ.dll"
// unter "Verweise" ins Projekt einbinden.

namespace Client
{

    public class ActiveMQListener : IMqObservable
    {
        //IMqObservable
        private List<IMqObserver> observers;
        // gewählter Topic name
        private string topicName;

        private string brokerUrl;
        private IConnection connection;
        private IConnectionFactory connectionFactory;
        private ISession session;
        private IMessageConsumer messageConsumer;

        private delegate void OneArgDelegate(string m);

        public ActiveMQListener() 
        {
            observers = new List<IMqObserver>();
        }

        // Verbindung zum  Messaging-Server aktualisieren
		public void UpdateConnection() {
            try
            {
                // eventuell früher belegte Ressourcen freigeben
                CleanupResources();

                // Verbindung / Session / MessageProducer und -Consumer instanziieren
                if (connectionFactory == null) connectionFactory = new ConnectionFactory(brokerUrl);
                AddLogMessage("creating connection...");
                connection = connectionFactory.CreateConnection();
                AddLogMessage("created connection");
                session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
                AddLogMessage("created session");
                messageConsumer = session.CreateConsumer(new ActiveMQTopic(topicName));
                AddLogMessage("created consumer");

                // MessageListener-Methode für eingehende Nachrichten registrieren
                messageConsumer.Listener += OnMessageReceived;

                // Thread zum Empfang eingehender Nachrichten starten
                AddLogMessage("starting connection...");
                connection.Start();
                AddLogMessage("started connection");
                AddLogMessage("Connection geändert, Broker-URL ist " + brokerUrl);
            }
            catch (Exception e)
            {
                AddLogMessage("*** EXCEPTION in updateConnection(): " + e.Message);
            }
		}


        // Methode wird als Eventhandler für Window.Closed-Event verwendet (benoetigt sender/e Argumente)
        // und von UpdateConnection() aus (parameterfrei). 
        public void CleanupResources(object sender=null, EventArgs e=null)
        {
            if (messageConsumer != null) messageConsumer.Dispose();
            if (session != null) session.Dispose();
            if (connection != null)
            {
                connection.Dispose();
                connection = null;
            }
            connectionFactory = null;
        }


        // OnMessageReceived - beim messageConsumer registrierte Callback-Methode,
        // wird bei Empfang einer neuen Nachricht vom messageConsumer aufgerufen
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                AddLogMessage("ACTIVEMQ-TextMessage: ID=" + tm.GetType() + "\n" + tm.Text);
                
                StringBuilder msgbuffer = new StringBuilder();
                msgbuffer.AppendFormat("MapMessage: ID={0}\n", msg.GetType());
                bool publisherContained = false;
                bool mqEventContained = false;
                foreach (string key in tm.Properties.Keys)
                {
                    AddLogMessage("actKey ist: "+key);
                    msgbuffer.AppendFormat("  - Property '{0}' = {1}\n", key, tm.Properties[key]);
                    if (key == "Publisher")
                    {
                        publisherContained = true;
                    }
                    else if (key == "Action")
                    {
                        mqEventContained = true;
                    }
                }

                AddLogMessage(msgbuffer.ToString());

                if (publisherContained && mqEventContained)
                {
                    object sessionId = tm.Properties["Publisher"];
                    object mqEvent = tm.Properties["Action"];
                    long publisher = Int64.Parse(sessionId.ToString());
                    string mqEventString = mqEvent.ToString();
                    string itemId = tm.Text;
                    NotifyObservers(itemId, publisher, mqEventString);
                }
                else
                {
                    Console.WriteLine("no keys contained");
                }
                
            }
            else if (msg is IMapMessage)
            {
                StringBuilder msgbuffer = new StringBuilder();

                IMapMessage mm = msg as IMapMessage;
                msgbuffer.AppendFormat("MapMessage: ID={0}\n", msg.GetType());
                foreach (string key in mm.Properties.Keys)
                {
                    msgbuffer.AppendFormat("  - Property '{0}' = {1}\n",key,mm.Properties[key]);
                }
                foreach (string key in mm.Body.Keys)
                {
                    msgbuffer.AppendFormat("  - Eintrag '{0}' = {1}\n", key,mm.Body[key]);
                }
                AddLogMessage(msgbuffer.ToString());
            }
            else
            {
                AddLogMessage("\nAnderer Message-Typ: " + msg);
            }
        }

        private void AddLogMessage(string msg) {
            Console.WriteLine(msg);
        }

        public void NotifyObservers(string itemId, long publisher, string mqEvent)
        {
            AddLogMessage("notifyObservers aufgerufen");
            foreach(IMqObserver actObserver in observers) {
                actObserver.Update(topicName, itemId, publisher, mqEvent);
            }
        }

        public void ClearAllObservers() {
            this.observers.Clear();
        }

        public void AddObserver(IMqObserver observer)
        {
            observers.Add(observer);
            Console.WriteLine("ACTIVEMQLISTENER, registered observer "+observer.ToString());
        }

        public void RemoveObserver(IMqObserver observer)
        {
            observers.Remove(observer);
            Console.WriteLine("ACTIVEMQLISTENER, deleted observer " + observer.ToString());
        }

        public void SetTopicName(string name)
        {
            topicName = name;
        }

        public void SetBrokerUrl(string url)
        {
            brokerUrl = url;
        }
    }
}

