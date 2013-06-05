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
    public partial class SettingWindow : Form
    {
        public SettingWindow()
        {
            InitializeComponent();
        }

        private void SettingOK_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
        }

        private void SettingCloseButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
