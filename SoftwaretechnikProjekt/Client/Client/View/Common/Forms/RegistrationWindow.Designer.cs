namespace Client.View.Common
{
    partial class RegistrationWindow
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
            this.RegUsernameLabel = new System.Windows.Forms.Label();
            this.RegPasswordLabel = new System.Windows.Forms.Label();
            this.RegUsernameInput = new System.Windows.Forms.TextBox();
            this.RegPasswordInput = new System.Windows.Forms.TextBox();
            this.RegistButton = new System.Windows.Forms.Button();
            this.RegistrationCloseButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // RegUsernameLabel
            // 
            this.RegUsernameLabel.AutoSize = true;
            this.RegUsernameLabel.Location = new System.Drawing.Point(12, 32);
            this.RegUsernameLabel.Name = "RegUsernameLabel";
            this.RegUsernameLabel.Size = new System.Drawing.Size(75, 13);
            this.RegUsernameLabel.TabIndex = 0;
            this.RegUsernameLabel.Text = "Benutzername";
            // 
            // RegPasswordLabel
            // 
            this.RegPasswordLabel.AutoSize = true;
            this.RegPasswordLabel.Location = new System.Drawing.Point(12, 68);
            this.RegPasswordLabel.Name = "RegPasswordLabel";
            this.RegPasswordLabel.Size = new System.Drawing.Size(50, 13);
            this.RegPasswordLabel.TabIndex = 1;
            this.RegPasswordLabel.Text = "Passwort";
            // 
            // RegUsernameInput
            // 
            this.RegUsernameInput.Location = new System.Drawing.Point(111, 29);
            this.RegUsernameInput.Name = "RegUsernameInput";
            this.RegUsernameInput.Size = new System.Drawing.Size(161, 20);
            this.RegUsernameInput.TabIndex = 2;
            // 
            // RegPasswordInput
            // 
            this.RegPasswordInput.Location = new System.Drawing.Point(111, 68);
            this.RegPasswordInput.Name = "RegPasswordInput";
            this.RegPasswordInput.Size = new System.Drawing.Size(161, 20);
            this.RegPasswordInput.TabIndex = 3;
            // 
            // RegistButton
            // 
            this.RegistButton.Location = new System.Drawing.Point(111, 112);
            this.RegistButton.Name = "RegistButton";
            this.RegistButton.Size = new System.Drawing.Size(75, 23);
            this.RegistButton.TabIndex = 4;
            this.RegistButton.Text = "OK";
            this.RegistButton.UseVisualStyleBackColor = true;
            this.RegistButton.Click += new System.EventHandler(this.RegistButton_Click);
            // 
            // RegistrationCloseButton
            // 
            this.RegistrationCloseButton.Location = new System.Drawing.Point(197, 112);
            this.RegistrationCloseButton.Name = "RegistrationCloseButton";
            this.RegistrationCloseButton.Size = new System.Drawing.Size(75, 23);
            this.RegistrationCloseButton.TabIndex = 5;
            this.RegistrationCloseButton.Text = "Schließen";
            this.RegistrationCloseButton.UseVisualStyleBackColor = true;
            this.RegistrationCloseButton.Click += new System.EventHandler(this.RegistrationCloseButton_Click);
            // 
            // Registration
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 166);
            this.Controls.Add(this.RegistrationCloseButton);
            this.Controls.Add(this.RegistButton);
            this.Controls.Add(this.RegPasswordInput);
            this.Controls.Add(this.RegUsernameInput);
            this.Controls.Add(this.RegPasswordLabel);
            this.Controls.Add(this.RegUsernameLabel);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Name = "Registration";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Registrierung";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label RegUsernameLabel;
        private System.Windows.Forms.Label RegPasswordLabel;
        private System.Windows.Forms.TextBox RegUsernameInput;
        private System.Windows.Forms.TextBox RegPasswordInput;
        private System.Windows.Forms.Button RegistButton;
        private System.Windows.Forms.Button RegistrationCloseButton;
    }
}