using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
using Newtonsoft.Json;

namespace ObjectTest
{
    class Producer
    {
        private String brokerURL;
        private String topicName;
        private IConnection connection;
        private IConnectionFactory connectionFactory;
        private ISession session;
        private IMessageProducer messageProducer;

        public Producer(String topicName, String brokerURL)
        {
            this.brokerURL = brokerURL;
            this.topicName = topicName;
        }

        public IMessageProducer MessageProducer{
            get {
                return messageProducer;
            }  
        }

        public ISession Session
        {
            get
            {
                return session;
            }
        }

        // Methode wird als Eventhandler für Window.Closed-Event verwendet (benoetigt sender/e Argumente)
        // und von UpdateConnection() aus (parameterfrei). 
        void CleanupResources(object sender = null, EventArgs e = null)
        {
            if (messageProducer != null)
            {
                messageProducer.Dispose();
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
                messageProducer = session.CreateProducer(new ActiveMQTopic(topicName));


                // Thread zum Empfang eingehender Nachrichten starten
                connection.Start();

                Console.WriteLine("Connection geändert, Broker-URL ist " + brokerURL);
            }
            catch (Exception e)
            {
                Console.WriteLine("*** EXCEPTION in updateConnection(): " + e.Message);
            }
        }

        public void Send(String s)
        {
            Console.WriteLine(s);
            IMessage msg = session.CreateTextMessage(s);
            messageProducer.Send(msg);
        }
    
    }
}