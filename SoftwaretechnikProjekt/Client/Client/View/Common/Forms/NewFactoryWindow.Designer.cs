namespace Client.View.Common
{
    partial class NewFactoryWindow
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.FactoryNameLabel = new System.Windows.Forms.Label();
            this.CreateProjectButton = new System.Windows.Forms.Button();
            this.CloseConfigButton = new System.Windows.Forms.Button();
            this.FactorySizeLabel = new System.Windows.Forms.Label();
            this.FactoryNameInput = new System.Windows.Forms.TextBox();
            this.FactorySizeXInput = new System.Windows.Forms.TextBox();
            this.FactorySizeYInput = new System.Windows.Forms.TextBox();
            this.FactorySizeXLabel = new System.Windows.Forms.Label();
            this.FactorySizeYLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // FactoryNameLabel
            // 
            this.FactoryNameLabel.AutoSize = true;
            this.FactoryNameLabel.Location = new System.Drawing.Point(12, 23);
            this.FactoryNameLabel.Name = "FactoryNameLabel";
            this.FactoryNameLabel.Size = new System.Drawing.Size(62, 13);
            this.FactoryNameLabel.TabIndex = 5;
            this.FactoryNameLabel.Text = "Fabrikname";
            // 
            // CreateProjectButton
            // 
            this.CreateProjectButton.Location = new System.Drawing.Point(172, 125);
            this.CreateProjectButton.Name = "CreateProjectButton";
            this.CreateProjectButton.Size = new System.Drawing.Size(75, 23);
            this.CreateProjectButton.TabIndex = 3;
            this.CreateProjectButton.Text = "Erstellen";
            this.CreateProjectButton.UseVisualStyleBackColor = true;
            this.CreateProjectButton.Click += new System.EventHandler(this.CreateProjectButton_Click);
            // 
            // CloseConfigButton
            // 
            this.CloseConfigButton.Location = new System.Drawing.Point(265, 125);
            this.CloseConfigButton.Name = "CloseConfigButton";
            this.CloseConfigButton.Size = new System.Drawing.Size(75, 23);
            this.CloseConfigButton.TabIndex = 4;
            this.CloseConfigButton.Text = "Schließen";
            this.CloseConfigButton.UseVisualStyleBackColor = true;
            this.CloseConfigButton.Click += new System.EventHandler(this.CloseConfigButton_Click);
            // 
            // FactorySizeLabel
            // 
            this.FactorySizeLabel.AutoSize = true;
            this.FactorySizeLabel.Location = new System.Drawing.Point(12, 54);
            this.FactorySizeLabel.Name = "FactorySizeLabel";
            this.FactorySizeLabel.Size = new System.Drawing.Size(67, 13);
            this.FactorySizeLabel.TabIndex = 6;
            this.FactorySizeLabel.Text = "Fabrikgrösse";
            this.FactorySizeLabel.Click += new System.EventHandler(this.CloseConfigButton_Click);
            // 
            // FactoryNameInput
            // 
            this.FactoryNameInput.Location = new System.Drawing.Point(114, 20);
            this.FactoryNameInput.Name = "FactoryNameInput";
            this.FactoryNameInput.Size = new System.Drawing.Size(226, 20);
            this.FactoryNameInput.TabIndex = 0;
            // 
            // FactorySizeXInput
            // 
            this.FactorySizeXInput.Location = new System.Drawing.Point(172, 51);
            this.FactorySizeXInput.Name = "FactorySizeXInput";
            this.FactorySizeXInput.Size = new System.Drawing.Size(68, 20);
            this.FactorySizeXInput.TabIndex = 1;
            // 
            // FactorySizeYInput
            // 
            this.FactorySizeYInput.Location = new System.Drawing.Point(172, 80);
            this.FactorySizeYInput.Name = "FactorySizeYInput";
            this.FactorySizeYInput.Size = new System.Drawing.Size(68, 20);
            this.FactorySizeYInput.TabIndex = 2;
            // 
            // FactorySizeXLabel
            // 
            this.FactorySizeXLabel.AutoSize = true;
            this.FactorySizeXLabel.Location = new System.Drawing.Point(111, 54);
            this.FactorySizeXLabel.Name = "FactorySizeXLabel";
            this.FactorySizeXLabel.Size = new System.Drawing.Size(40, 13);
            this.FactorySizeXLabel.TabIndex = 7;
            this.FactorySizeXLabel.Text = "X-Wert";
            // 
            // FactorySizeYLabel
            // 
            this.FactorySizeYLabel.AutoSize = true;
            this.FactorySizeYLabel.Location = new System.Drawing.Point(111, 83);
            this.FactorySizeYLabel.Name = "FactorySizeYLabel";
            this.FactorySizeYLabel.Size = new System.Drawing.Size(40, 13);
            this.FactorySizeYLabel.TabIndex = 8;
            this.FactorySizeYLabel.Text = "Y-Wert";
            // 
            // NewProjectConfig
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(365, 176);
            this.Controls.Add(this.FactorySizeYLabel);
            this.Controls.Add(this.FactorySizeXLabel);
            this.Controls.Add(this.FactorySizeYInput);
            this.Controls.Add(this.FactorySizeXInput);
            this.Controls.Add(this.FactoryNameInput);
            this.Controls.Add(this.FactorySizeLabel);
            this.Controls.Add(this.CloseConfigButton);
            this.Controls.Add(this.CreateProjectButton);
            this.Controls.Add(this.FactoryNameLabel);
            this.AcceptButton = this.CreateProjectButton;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Name = "NewProjectConfig";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Neues Projekt erstellen";
            this.ResumeLayout(false);
            this.PerformLayout();
        }

        #endregion

        private System.Windows.Forms.Label FactoryNameLabel;
        private System.Windows.Forms.Button CreateProjectButton;
        private System.Windows.Forms.Button CloseConfigButton;
        private System.Windows.Forms.Label FactorySizeLabel;
        private System.Windows.Forms.TextBox FactoryNameInput;
        private System.Windows.Forms.TextBox FactorySizeXInput;
        private System.Windows.Forms.TextBox FactorySizeYInput;
        private System.Windows.Forms.Label FactorySizeXLabel;
        private System.Windows.Forms.Label FactorySizeYLabel;
    }
}