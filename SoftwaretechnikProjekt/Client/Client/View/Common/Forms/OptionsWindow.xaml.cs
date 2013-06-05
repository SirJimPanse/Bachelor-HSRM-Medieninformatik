using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.Windows.Forms;
using System.ComponentModel;
namespace Client
{
	/// <summary>
	/// Interaktionslogik für Window1.xaml
	/// </summary>
	public partial class OptionsWindow : Window
	{
        public static int OK = 1;
        public static int CANCEL = 0;
        public static int REGISTER = 2;
        public event EventHandler configEvent;
        public OptionsEvent ok;
        public OptionsEvent cancel;
        public OptionsEvent register;

		public OptionsWindow()
		{
			this.InitializeComponent();
            ok = new OptionsEvent();
            ok.value = OptionsWindow.OK;
            cancel = new OptionsEvent();
            cancel.value = OptionsWindow.CANCEL;
            register = new OptionsEvent();
            register.value = OptionsWindow.REGISTER;
            Closing += OnWindowClosing;
            setData();
		}

        // wird das Fenster geschlossen (mit [X]), kann es nicht wieder verwendet werden -> abfangen
        public void OnWindowClosing(object sender, CancelEventArgs e)
        {
            e.Cancel = true; // Event als "behandelt" markieren
            this.configEvent(this, cancel); // eigenes cancel event setzen
        }

        private void setData()
        {
            if (ConfigHelper.ServerFileExists()) 
            {
                string[] server = ConfigHelper.GetServerAddress();
                string address = server[0];
                string port = server[1];
                setServer(address, port);
            }

            if (ConfigHelper.LoginFileExists())
            {
                string[] loginInfo = ConfigHelper.GetLoginInformations();
                bool autoconnect = ConfigHelper.GetAutoConnect();
                setLoginData(loginInfo[0], loginInfo[1], autoconnect);
            }   
        }

        public void setLoginData(string user, string pass, bool auto){
            this.username.Text = user;
            this.password.Text = pass;
            this.autoconnect.IsChecked = auto;
        }

        public void setServer(string server, string port)
        {
            this.url.Text = server;
            this.port.Text = port;
        }

        private void OkClicked(object sender, RoutedEventArgs e)
        {
            this.configEvent(this, ok);
        }

        private void CancelClicked(object sender, RoutedEventArgs e)
        {
            this.configEvent(this, cancel);
        }

        public class OptionsEvent : EventArgs
        {
            public int value { get; set; }
        }

        private void RegisterClicked(object sender, RoutedEventArgs e)
        {
            this.configEvent(this, register);
        }
	}
}