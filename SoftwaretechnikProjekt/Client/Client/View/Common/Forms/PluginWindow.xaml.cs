using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.ComponentModel;

namespace Client
{
    /// <summary>
    /// Interaktionslogik für PluginWindow.xaml
    /// </summary>
    public partial class PluginWindow : Window
    {
        public static readonly int CANCEL = 0;
        public static readonly int INFO = 1;
        public static readonly int APPLY = 2;

        public event EventHandler pluginEvent;
        public PluginEvent cancel;
        public PluginEvent info;
        public PluginEvent apply;
        public string SelectedPlugin;

        public PluginWindow()
        {
            InitializeComponent();
            cancel = new PluginEvent();
            cancel.value = CANCEL;
            info = new PluginEvent();
            info.value = INFO;
            apply = new PluginEvent();
            apply.value = APPLY;
            Closing += OnWindowClosing;
        }

        public void InitializeComboBox(string[] entries)
        {
            foreach (string entry in entries) DefinedIconSets.Items.Add(entry);
        }

        public class PluginEvent : EventArgs
        {
            public int value { get; set; }
        }

        // wird das Fenster geschlossen (mit [X]), kann es nicht wieder verwendet werden -> abfangen
        public void OnWindowClosing(object sender, CancelEventArgs e)
        {
            e.Cancel = true; // Event als "behandelt" markieren
            this.pluginEvent(this, cancel); // eigenes cancel event setzen
        }

        private void CancelClicked(object sender, RoutedEventArgs e)
        {
            this.pluginEvent(this, cancel);
        }

        private void ApplyClicked(object sender, RoutedEventArgs e)
        {
            this.pluginEvent(this, apply);
        }

        private void InfoClicked(object sender, RoutedEventArgs e)
        {
            this.pluginEvent(this,info);
        }

        private void DefinedIconSets_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            string text = (sender as ComboBox).SelectedItem as string;
            this.SelectedPlugin = text;
        }
    }
}
