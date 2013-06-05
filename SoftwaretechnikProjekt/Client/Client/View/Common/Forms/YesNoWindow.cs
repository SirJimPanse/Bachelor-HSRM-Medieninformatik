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
    public partial class YesNoWindow : Form
    {
        public YesNoWindow(string firstLine, string secondLine)
        {
            InitializeComponent();
            this.firstLine.Text = firstLine;
            this.secondLine.Text = secondLine;
        }

        private void InvitationTrueButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
        }

        private void InvitationFalseButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void YesNoWindow_Load(object sender, EventArgs e)
        {

        }
    }
}
