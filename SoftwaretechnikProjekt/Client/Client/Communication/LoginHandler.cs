using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Client.Communication;

namespace Client
{

    public delegate void LoginEventHandler(object sender, LoginEventArgs e);

    public class LoginEventArgs : EventArgs
    {
        public bool success { get; set; }
        public String username { get; set; }
        public String password { get; set; }
        public int[] scriptables { get; set; }

        public LoginEventArgs(bool sucess, String username, String password, int[] scriptables)
        {
            this.success = sucess;
            this.username = username;
            this.password = password;
            this.scriptables = scriptables;
        }
    }

    public class LoginHandler
    {

        private String username;
        private String password;
        private bool success;
        private IMqObserver obs;

        public event LoginEventHandler LoginEvent;

        public LoginHandler(String username, String password, IMqObserver obs)
        {
            this.success = false;
            this.obs = obs;
            this.username = username;
            this.password = password;
        }

        public void login()
        {
            int[] scriptableIDs = null;
            ClientServerUtil csu = ClientServerUtil.getInstance();
            bool login = csu.Login(username, password);
            if (login)
            {
                success = true;
                csu.RegisterInvitationMqListener(obs);
                scriptableIDs = csu.GetScriptableMachines();
            }
            LoginEvent(this, new LoginEventArgs(success, username, password, scriptableIDs));
        }
    }
}
