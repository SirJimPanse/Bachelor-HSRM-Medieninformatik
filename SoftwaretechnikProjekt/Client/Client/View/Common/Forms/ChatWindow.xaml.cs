using System;
using System.Windows;
using System.Text;
using System.Windows.Input;
using System.ComponentModel;
using System.Windows.Documents;
using Client.Communication;


namespace Client
{
    public partial class ChatWindow : Window
    {

        public ChatWindow()
        {
            this.InitializeComponent();
            
        }
        private void InputBoxKeyDown(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if (e.Key == System.Windows.Input.Key.Enter)
            {
                SendMessage();
            }
        }

        private void click_sendMessage(object sender, RoutedEventArgs e)
        {
            SendMessage();
        }

        private void SendMessage()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            csu.SendChatMessage(inputBox.Text);
            inputBox.Text = "";
        }

        public void AppendMessage(string text)
        {
            string[] message = text.Split(new string[]{":"}, 2, StringSplitOptions.None);
            string publisher = message[0];
            string messageText = message[1];

            if (!this.IsVisible)
            {
                this.Visibility = Visibility.Visible;
            }

            chatBlock.Inlines.Add(new Bold(new Run(publisher + ": ")));
            chatBlock.Inlines.Add(new Italic(new Run(messageText + "\n")));
            sv.ScrollToBottom();
        }

        public void ToggleChatWindow()
        {
            if (this.IsVisible)
            {
                this.Visibility = Visibility.Hidden;
            }
            else
            {
                this.Visibility = Visibility.Visible;
            }

        }

        private void OnKeyPressed(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if (e.Key == Key.F1)
            {
                ToggleChatWindow();
            }

        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
            e.Cancel = true;
        }
    }
}