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
    public partial class NewFactoryWindow : Form
    {

        public string FactoryName { get; set; }
        public string FactorySizeX { get; set; }
        public string FactorySizeY { get; set; }

        public NewFactoryWindow()
        {
            InitializeComponent();
        }

        private void CreateProjectButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
            this.FactoryName = FactoryNameInput.Text;
            this.FactorySizeX = FactorySizeXInput.Text;
            this.FactorySizeY = FactorySizeYInput.Text;

        }

        private void CloseConfigButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
