using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Client.View._3D;
using Client.Classes;

namespace Client.View.Common
{
    public partial class ScriptingWindow : Form
    {

        public string script { get; set; }

        public ScriptingWindow(string content)
        {
            InitializeComponent();
            ScriptingBox.Text = content;
            this.script = ScriptingBox.Text.ToString();
        }

        private void ScriptingSaveButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
            this.script = ScriptingBox.Text.ToString();
        }

        private void ScriptingCloseButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void ScriptingBox_TextChanged(object sender, EventArgs e)
        {

        }

        private void Scripting_Load(object sender, EventArgs e)
        {

        }

    }
}
