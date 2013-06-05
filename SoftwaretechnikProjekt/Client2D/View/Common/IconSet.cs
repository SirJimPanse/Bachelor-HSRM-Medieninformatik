using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace Client.Classes
{

    /** Ein IconSet ist eine Form von Plugin und bildet KlassenIDs von Maschinenklassen (Server) auf Bilder (Client) ab. **/
    class IconSet
    {
        private const string location = "..\\..\\Plugins\\iconsets\\";
        private const string mapping  = "img_mapping.txt";
        private string actMap;
        public Dictionary<int,string> idIconMap;

        public IconSet(string folderName)
        {
            this.idIconMap = new Dictionary<int, string>();
            this.actMap = location + folderName + "\\";
            System.IO.StreamReader myFile = new System.IO.StreamReader(actMap + mapping);
            string myString = myFile.ReadToEnd();
            myFile.Close();
            splitFileString(myString); // idIconMap mit Werten aus der eingelesenen Datei füllen
        }

        private void splitFileString(string files)
        {
            string[] parts = files.Split(new string[]{"\r\n"}, StringSplitOptions.None);
            foreach (string part in parts)
            {
                string[] miniParts = part.Split(':');
                int id = int.Parse(miniParts[0]);
                string fileSource = miniParts[1];
                idIconMap.Add(id, fileSource);
            }
        }

        public Image getImage(int id) {
            Image img = new Image();
            BitmapImage logo = new BitmapImage();
            logo.BeginInit();
            logo.UriSource = new Uri("pack://application:,,,/Client;component/" + actMap + "img\\" + idIconMap[id]);
            logo.EndInit();
            img.Source = logo;
            return img;
        }
    }
}
