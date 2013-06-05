namespace Client.View.Common
{
    partial class SettingWindow
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
            this.SettingLabel = new System.Windows.Forms.Label();
            this.SettingBox = new System.Windows.Forms.ComboBox();
            this.SettingOK = new System.Windows.Forms.Button();
            this.SettingCloseButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // SettingLabel
            // 
            this.SettingLabel.AutoSize = true;
            this.SettingLabel.Location = new System.Drawing.Point(13, 13);
            this.SettingLabel.Name = "SettingLabel";
            this.SettingLabel.Size = new System.Drawing.Size(93, 13);
            this.SettingLabel.TabIndex = 0;
            this.SettingLabel.Text = "Darstellungsplugin";
            // 
            // SettingBox
            // 
            this.SettingBox.FormattingEnabled = true;
            this.SettingBox.Location = new System.Drawing.Point(112, 10);
            this.SettingBox.Name = "SettingBox";
            this.SettingBox.Size = new System.Drawing.Size(160, 21);
            this.SettingBox.TabIndex = 1;
            // 
            // SettingOK
            // 
            this.SettingOK.Location = new System.Drawing.Point(197, 55);
            this.SettingOK.Name = "SettingOK";
            this.SettingOK.Size = new System.Drawing.Size(75, 23);
            this.SettingOK.TabIndex = 2;
            this.SettingOK.Text = "OK";
            this.SettingOK.UseVisualStyleBackColor = true;
            this.SettingOK.Click += new System.EventHandler(this.SettingOK_Click);
            // 
            // SettingCloseButton
            // 
            this.SettingCloseButton.Location = new System.Drawing.Point(99, 55);
            this.SettingCloseButton.Name = "SettingCloseButton";
            this.SettingCloseButton.Size = new System.Drawing.Size(75, 23);
            this.SettingCloseButton.TabIndex = 3;
            this.SettingCloseButton.Text = "Schließen";
            this.SettingCloseButton.UseVisualStyleBackColor = true;
            this.SettingCloseButton.Click += new System.EventHandler(this.SettingCloseButton_Click);
            // 
            // SettingWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 102);
            this.Controls.Add(this.SettingCloseButton);
            this.Controls.Add(this.SettingOK);
            this.Controls.Add(this.SettingBox);
            this.Controls.Add(this.SettingLabel);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Name = "SettingWindow";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Konfiguration";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label SettingLabel;
        private System.Windows.Forms.ComboBox SettingBox;
        private System.Windows.Forms.Button SettingOK;
        private System.Windows.Forms.Button SettingCloseButton;
    }
}