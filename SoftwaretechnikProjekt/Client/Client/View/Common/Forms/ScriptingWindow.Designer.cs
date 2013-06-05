namespace Client.View.Common
{
    partial class ScriptingWindow
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
            this.ScriptingBox = new System.Windows.Forms.RichTextBox();
            this.ScriptingCloseButton = new System.Windows.Forms.Button();
            this.ScriptingSaveButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // ScriptingBox
            // 
            this.ScriptingBox.AcceptsTab = true;
            this.ScriptingBox.Location = new System.Drawing.Point(12, 11);
            this.ScriptingBox.Name = "ScriptingBox";
            this.ScriptingBox.Size = new System.Drawing.Size(465, 489);
            this.ScriptingBox.TabIndex = 0;
            this.ScriptingBox.Text = "";
            this.ScriptingBox.TextChanged += new System.EventHandler(this.ScriptingBox_TextChanged);
            // 
            // ScriptingCloseButton
            // 
            this.ScriptingCloseButton.Location = new System.Drawing.Point(402, 506);
            this.ScriptingCloseButton.Name = "ScriptingCloseButton";
            this.ScriptingCloseButton.Size = new System.Drawing.Size(75, 23);
            this.ScriptingCloseButton.TabIndex = 1;
            this.ScriptingCloseButton.Text = "Schließen";
            this.ScriptingCloseButton.UseVisualStyleBackColor = true;
            this.ScriptingCloseButton.Click += new System.EventHandler(this.ScriptingCloseButton_Click);
            // 
            // ScriptingSaveButton
            // 
            this.ScriptingSaveButton.Location = new System.Drawing.Point(309, 506);
            this.ScriptingSaveButton.Name = "ScriptingSaveButton";
            this.ScriptingSaveButton.Size = new System.Drawing.Size(75, 23);
            this.ScriptingSaveButton.TabIndex = 2;
            this.ScriptingSaveButton.Text = "Speichern";
            this.ScriptingSaveButton.UseVisualStyleBackColor = true;
            this.ScriptingSaveButton.Click += new System.EventHandler(this.ScriptingSaveButton_Click);
            // 
            // ScriptingWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(493, 533);
            this.Controls.Add(this.ScriptingSaveButton);
            this.Controls.Add(this.ScriptingCloseButton);
            this.Controls.Add(this.ScriptingBox);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Name = "ScriptingWindow";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Scripting";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.RichTextBox ScriptingBox;
        private System.Windows.Forms.Button ScriptingCloseButton;
        private System.Windows.Forms.Button ScriptingSaveButton;
    }
}