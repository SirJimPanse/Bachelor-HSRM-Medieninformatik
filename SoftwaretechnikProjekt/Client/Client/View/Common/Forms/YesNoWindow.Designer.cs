namespace Client.View.Common
{
    partial class YesNoWindow
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
            this.InvitationTrueButton = new System.Windows.Forms.Button();
            this.InvitationFalseButton = new System.Windows.Forms.Button();
            this.firstLine = new System.Windows.Forms.Label();
            this.secondLine = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // InvitationTrueButton
            // 
            this.InvitationTrueButton.Location = new System.Drawing.Point(50, 92);
            this.InvitationTrueButton.Name = "InvitationTrueButton";
            this.InvitationTrueButton.Size = new System.Drawing.Size(75, 23);
            this.InvitationTrueButton.TabIndex = 2;
            this.InvitationTrueButton.Text = "Ja";
            this.InvitationTrueButton.UseVisualStyleBackColor = true;
            this.InvitationTrueButton.Click += new System.EventHandler(this.InvitationTrueButton_Click);
            // 
            // InvitationFalseButton
            // 
            this.InvitationFalseButton.Location = new System.Drawing.Point(151, 92);
            this.InvitationFalseButton.Name = "InvitationFalseButton";
            this.InvitationFalseButton.Size = new System.Drawing.Size(75, 23);
            this.InvitationFalseButton.TabIndex = 3;
            this.InvitationFalseButton.Text = "Nein";
            this.InvitationFalseButton.UseVisualStyleBackColor = true;
            this.InvitationFalseButton.Click += new System.EventHandler(this.InvitationFalseButton_Click);
            // 
            // firstLine
            // 
            this.firstLine.AutoSize = true;
            this.firstLine.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.firstLine.Location = new System.Drawing.Point(19, 23);
            this.firstLine.Name = "firstLine";
            this.firstLine.Size = new System.Drawing.Size(28, 13);
            this.firstLine.TabIndex = 4;
            this.firstLine.Text = "Text";
            // 
            // secondLine
            // 
            this.secondLine.AutoSize = true;
            this.secondLine.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.secondLine.Location = new System.Drawing.Point(19, 53);
            this.secondLine.Name = "secondLine";
            this.secondLine.Size = new System.Drawing.Size(28, 13);
            this.secondLine.TabIndex = 5;
            this.secondLine.Text = "Text";
            // 
            // YesNoWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 135);
            this.Controls.Add(this.secondLine);
            this.Controls.Add(this.firstLine);
            this.Controls.Add(this.InvitationFalseButton);
            this.Controls.Add(this.InvitationTrueButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Name = "YesNoWindow";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Info";
            this.Load += new System.EventHandler(this.YesNoWindow_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button InvitationTrueButton;
        private System.Windows.Forms.Button InvitationFalseButton;
        private System.Windows.Forms.Label firstLine;
        private System.Windows.Forms.Label secondLine;
    }
}