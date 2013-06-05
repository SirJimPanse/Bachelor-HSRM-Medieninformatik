using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Client
{
    class ConfigHelper
    {
        private static string LOGIN_CONFIG_PATH = Path.GetFullPath("../..") + "\\login.cfg";
        private static string SERVER_CONFIG_PATH = Path.GetFullPath("../..") + "\\server.cfg";

        public static bool LoginFileExists()
        {
            return File.Exists(LOGIN_CONFIG_PATH);
        }

        public static bool ServerFileExists()
        {
            return File.Exists(SERVER_CONFIG_PATH);
        }

        public static string[] GetLoginInformations()
        {
            string[] loginInfos = new string[3];

            TextReader loginConfigReader = new StreamReader(LOGIN_CONFIG_PATH);

            loginInfos[0] = loginConfigReader.ReadLine();
            loginInfos[1] = loginConfigReader.ReadLine();
            loginInfos[2] = loginConfigReader.ReadLine();
            loginConfigReader.Close();
            return loginInfos;
        }

        public static void WriteLoginFile(string username, string password, bool auto)
        {
            
            TextWriter loginWriter = new StreamWriter(LOGIN_CONFIG_PATH);
            loginWriter.WriteLine(username);
            loginWriter.WriteLine(password);
            loginWriter.WriteLine(auto);

            loginWriter.Close();
        }

        public static string[] GetServerAddress()
        {
            TextReader serverConfigReader = new StreamReader(SERVER_CONFIG_PATH);

            string[] serverInfos = new string[2];

            serverInfos[0] = serverConfigReader.ReadLine();
            serverInfos[1] = serverConfigReader.ReadLine();
            serverConfigReader.Close();
            return serverInfos;
        }

        public static void WriteServerFile(string serverAddress, string port)
        {
            TextWriter serverConfigWriter = new StreamWriter(SERVER_CONFIG_PATH);
            serverConfigWriter.WriteLine(serverAddress);
            serverConfigWriter.WriteLine(port);
            serverConfigWriter.Close();
        }

        public static bool GetAutoConnect()
        {
            //TODO
            if (LoginFileExists())
            {
                string[] loginInfo = GetLoginInformations();
                return loginInfo[2] == "True";
            }
            return false;
        }
    }
}
