using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
using Newtonsoft.Json;

namespace ObjectTest {
    
    class Consumer{

        private String brokerURL;
        private String topicName;
        private IConnection connection;
        private IConnectionFactory connectionFactory;
        private ISession session;
        private IMessageConsumer messageConsumer;

        public Consumer(String topicName, String brokerURL){
            this.brokerURL = brokerURL;
            this.topicName = topicName;
        }

        // Methode wird als Eventhandler für Window.Closed-Event verwendet (benoetigt sender/e Argumente)
        // und von UpdateConnection() aus (parameterfrei). 
        void CleanupResources(object sender = null, EventArgs e = null)
        {
            if (messageConsumer != null)
            {
                messageConsumer.Dispose();
            }
            if (session != null) session.Dispose();
            if (connection != null)
            {
                connection.Dispose();
                connection = null;
            }
            connectionFactory = null;
        }

        // Verbindung zum  Messaging-Server aktualisieren
        public void UpdateConnection()
        {
            try
            {
                // eventuell früher belegte Ressourcen freigeben
                CleanupResources();

                // Verbindung / Session / MessageProducer und -Consumer instanziieren
                if (connectionFactory == null)
                {
                    Console.WriteLine(brokerURL);
                    connectionFactory = new ConnectionFactory(brokerURL);
                }
                connection = connectionFactory.CreateConnection();
                session = connection.CreateSession(AcknowledgementMode.AutoAcknowledge);
                messageConsumer = session.CreateConsumer(new ActiveMQTopic(topicName));

                // MessageListener-Methode für eingehende Nachrichten registrieren
                messageConsumer.Listener += OnMessageReceived;

                // Thread zum Empfang eingehender Nachrichten starten
                connection.Start();

                Console.WriteLine("Connection geändert, Broker-URL ist " + brokerURL);
            }
            catch (Exception e)
            {
                Console.WriteLine("*** EXCEPTION in updateConnection(): " + e.Message);
            }
        }

        // OnMessageReceived - beim messageConsumer registrierte Callback-Methode,
        // wird bei Empfang einer neuen Nachricht vom messageConsumer aufgerufen
        public void OnMessageReceived(IMessage msg) {
            
            if (msg is ITextMessage) 
            {
                ITextMessage tm = msg as ITextMessage;
                //Console.WriteLine("TextMessage: ID=" + tm.GetType() + "\n" + tm.Text);
                String s = tm.Text;
                Console.WriteLine(s);
                Schnorchel schnorchel = JsonConvert.DeserializeObject<Schnorchel>(s);
                Console.WriteLine(schnorchel);
            }
            else if (msg is IMapMessage)
            {
                StringBuilder msgbuffer = new StringBuilder();
                IMapMessage mm = msg as IMapMessage;
                msgbuffer.AppendFormat("MapMessage: ID={0}\n", msg.GetType());
                foreach (string key in mm.Properties.Keys)
                {
                    msgbuffer.AppendFormat("  - Property '{0}' = {1}\n", key, mm.Properties[key]);
                }
                foreach (string key in mm.Body.Keys)
                {
                    msgbuffer.AppendFormat("  - Eintrag '{0}' = {1}\n", key, mm.Body[key]);
                }
                Console.WriteLine(msgbuffer.ToString());
            }
            else
            {
                Console.WriteLine("\nAnderer Message-Typ: " + msg);
            }
        }
    }
}
