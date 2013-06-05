using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Windows;
using Client.Communication;

namespace Client
{
    /// <summary>
    /// Interaktionslogik für "App.xaml"
    /// </summary>
    public partial class App : Application
    {
        private void Application_Startup(object sender, StartupEventArgs e)
        {
            // create the main window and assign your datacontext
            MainWindow main;
            main = new MainWindow();
            main.Show();
            main.ShowOptionsWindow();
        }
    }
}
