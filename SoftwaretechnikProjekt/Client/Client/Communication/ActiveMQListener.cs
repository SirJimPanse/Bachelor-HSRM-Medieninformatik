using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
/*
  Assemblies für ActiveMQ-Zugriff; 
  hierzu Verweise auf "Apache.NMS.dll" und 
  "Apache.NMS.ActiveMQ.dll" unter 
  "Verweise" ins Projekt einbinden.
*/



namespace Client.Communication
{

    public class ActiveMQListener : IMqObservable
    {
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

        /// <summary>Verbindung zum  Messaging-Server aktualisieren</summary>
		public void UpdateConnection() {
            try
            {
                CleanupResources();

                // Verbindung / Session / MessageProducer und -Consumer instanziieren
                if (connectionFactory == null) connectionFactory = new ConnectionFactory(brokerUrl);
                connection = connectionFactory.CreateConnection();
                session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
                messageConsumer = session.CreateConsumer(new ActiveMQTopic(topicName));

                // MessageListener-Methode für eingehende Nachrichten registrieren
                messageConsumer.Listener += OnMessageReceived;

                // Thread zum Empfang eingehender Nachrichten starten
                connection.Start();
                AddLogMessage("Connection geändert, Broker-URL ist " + brokerUrl);
            }
            catch (Exception e)
            {
                AddLogMessage("*** EXCEPTION in updateConnection(): " + e.Message);
            }
		}


        /// <summary>
        /// Methode wird als Eventhandler für Window.Closed-Event verwendet (benoetigt sender/e Argumente)
        /// und von UpdateConnection() aus (parameterfrei).
        /// </summary>
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


        /// <summary>
        /// OnMessageReceived - beim messageConsumer registrierte Callback-Methode,
        /// wird bei Empfang einer neuen Nachricht vom messageConsumer aufgerufen.
        /// Informiert alle angemeldeten Observer über empfangene ITextMessages.
        /// </summary>
        /// <param name="msg"></param>
        public void OnMessageReceived(IMessage msg)
        {
            if (msg is ITextMessage)
            {
                ITextMessage tm = msg as ITextMessage;
                AddLogMessage("ACTIVEMQ-TextMessage: ID=" + tm.GetType() + "\n" + tm.Text);
                
                bool publisherContained = false;
                bool mqEventContained = false;
                foreach (string key in tm.Properties.Keys)
                {
                    AddLogMessage("actKey ist: "+key);
                    if (key == "Publisher")
                    {
                        publisherContained = true;
                    }
                    else if (key == "Action")
                    {
                        mqEventContained = true;
                    }
                }


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
                    AddLogMessage("no keys contained");
                }
                
            }
            else
            {
                AddLogMessage("\nAnderer Message-Typ: " + msg);
            }
        }

        private void AddLogMessage(string msg) {
            //Console.WriteLine(msg);
        }

        public void NotifyObservers(string itemId, long publisher, string mqEvent)
        {
            foreach(IMqObserver actObserver in observers) {
                actObserver.Update(topicName, itemId, publisher, mqEvent);
            }
        }

        public void ClearAllObservers() {
            this.observers.Clear();
        }

        public void AddObserver(IMqObserver observer)
        {
            if (!observers.Contains(observer))
            {
                observers.Add(observer);
            }
        }

        public void RemoveObserver(IMqObserver observer)
        {
            observers.Remove(observer);
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

