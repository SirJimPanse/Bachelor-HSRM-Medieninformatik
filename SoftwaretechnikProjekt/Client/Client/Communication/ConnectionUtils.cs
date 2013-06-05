using System;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Linq;
using Client.LoginServiceReference;
using Client.FactoryServiceReference;
using Sim = Client.SimulationServiceReference;

namespace Client.Communication
{
    /// <summary>
    /// Speichert URLs zum Zugriff auf den Server und generiert daraus 
    /// ServiceClients
    /// </summary>
    public class ConnectionUtils
    {
        public string mqBrokerUrl;
        string SERVICEURL;

        const string FACTORY_ENDING = "FactoryService";
        const string LOGIN_ENDING = "LoginService";
        const string SIM_ENDING = "SimulationService";

        const string TCP_PORT = "61616";

        public ConnectionUtils()
        {

        }

        public void SetUpServer(string ip, string port)
        {
            ip = (ip == "") ? "localhost" : ip;
            port = (port == "") ? "8080" : port;

            mqBrokerUrl = "tcp://" + ip + ":" + TCP_PORT;
            SERVICEURL = "http://" + ip + ":" + port + "/";
        }

        private EndpointAddress getEndpointAddress(string customUrlEnding)
        {
            try
            {
                string customURL = SERVICEURL + customUrlEnding;
                Console.WriteLine("Service wird aufgerufen: " + customURL);
                return new EndpointAddress(customURL);
            }
            catch (UriFormatException)
            {
                return null;
            }
        }

        public FactoryServiceClient createFactoryService()
        {
            try
            {
                Binding httpBinding = new BasicHttpBinding();
                FactoryServiceClient factoryService = new FactoryServiceClient(httpBinding, getEndpointAddress(FACTORY_ENDING));
                return factoryService;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public LoginServiceClient createLoginService()
        {
            try
            {
                Binding httpBinding = new BasicHttpBinding();
                LoginServiceClient service = new LoginServiceClient(httpBinding, getEndpointAddress(LOGIN_ENDING));
                return service;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public Sim.SimulationServiceClient createSimulationService()
        {
            try
            {
                Binding httpBinding = new BasicHttpBinding();
                Sim.SimulationServiceClient simulationService = new Sim.SimulationServiceClient(httpBinding, getEndpointAddress(SIM_ENDING));
                return simulationService;
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}
