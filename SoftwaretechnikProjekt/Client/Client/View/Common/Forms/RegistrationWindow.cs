using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Client.View.Common
{
    public partial class RegistrationWindow : Form
    {

        public string RegUsername { get; set; }
        public string RegPassword { get; set; }

        public RegistrationWindow()
        {
            InitializeComponent();
        }

        private void RegistrationCloseButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void RegistButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
            this.RegUsername = RegUsernameInput.Text;
            this.RegPassword = RegPasswordInput.Text;
        }
    }
}
