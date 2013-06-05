using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;
using System.IO;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;
using System.Windows;

namespace ObjectTest
{
    class ObjectSending
    {
        
        private const String brokerURL = "tcp://pc12-116.intern.mi.hs-rm.de:61616";
        private const String TOPIC_NAME = "FactoryFactory";

        static void Main(string[] args)
        {
            Producer producer = new Producer(TOPIC_NAME, brokerURL);
            Consumer consumer = new Consumer(TOPIC_NAME, brokerURL);
            Console.WriteLine("Connecting to" + brokerURL + " ...");

            consumer.UpdateConnection();
            producer.UpdateConnection();

            Schnorchel schnorch = new Schnorchel();
            schnorch.alter = 17;
            schnorch.name = "Odd";
            schnorch.hobbies.Add(new Hobby("reiten", false));
            schnorch.hobbies.Add(new Hobby("schwimmen", false));
            schnorch.hobbies.Add(new Hobby("lesen", true));
            schnorch.faehigkeiten.Add("huepfen", true);
            schnorch.faehigkeiten.Add("weidomieren", false);
            schnorch.faehigkeiten.Add("folloppen", true);
            
            string s = JsonConvert.SerializeObject(schnorch);
            //producer.Send(s);
            Console.ReadKey();
        }
    }
}
