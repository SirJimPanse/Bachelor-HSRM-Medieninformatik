using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using System.IO;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Media3D;
using HelixToolkit.Wpf;

namespace Client.Classes
{
    /// <summary>Falls die Plugin-Struktur fehlerhaft ist.</summary>
    public class IconSetException : Exception 
    {
        public IconSetException() : base() {}
        public IconSetException(string msg) : base(msg) { }
    }

    /// <summary>Falls choice.txt leer ist.</summary>
    public class ChoiceIsEmptyException : IconSetException
    {
        public ChoiceIsEmptyException() : base() {}
        public ChoiceIsEmptyException(string msg) : base(msg) {}
    }

    /// <summary>Falls eine benötigte Datei nicht existiert.</summary>
    public class NoSuchFileException : IconSetException
    {
        public NoSuchFileException() : base() {}
        public NoSuchFileException(string msg) : base(msg) {}
    }

    /// <summary>Falls das ColorMapping nicht korrekt angegeben wurde.</summary>
    public class ColorMappingException : IconSetException
    {
        public ColorMappingException() : base() {}
        public ColorMappingException(string msg) : base(msg) {}
    }

    /// <summary>Falls die mapping.txt Datei fehlerhaft ist.</summary>
    public class MappingException : IconSetException
    {
        public MappingException() : base() {}
        public MappingException(string msg) : base(msg) {}
    }

    /// <summary>Falls die product_mapping.txt Datei fehlerhaft ist.</summary>
    public class ProductMappingException : IconSetException
    {
        public ProductMappingException() : base() {}
        public ProductMappingException(string msg) : base(msg) {}
    }


    /// <summary>
    /// Ein IconSet ist eine Form von Plugin und bildet KlassenIDs 
    /// von Maschinenklassen (Server) auf Bilder (Client) ab.
    /// </summary>
    public class IconSet
    {
        private const string location = "..\\..\\Plugins\\iconsets\\";

        private const char SEPARATOR = ':';
        private const char COLOR_SEPARATOR = ',';

        private const string choiceFile = "choice.txt";
        private const string mapping = "mapping.txt";
        private const string productMapping = "product_mapping.txt";

        private const string DEFAULT_PRODUCT = "_.obj";

        private string folderName;
        public string FolderName { get { return folderName; } private set { folderName = value; } }

        private string actMap;

        private Dictionary<int, string> idIconMap;
        private Dictionary<int, string> idObjMap;
        private Dictionary<int, string> idTooltipMap;
        private Dictionary<string, string> productnameObjFileMap;
        private Dictionary<string, Color> productnameColorMap;

        private Color _DEFAULT_COLOR;
        public Color DEFAULT_COLOR { set{_DEFAULT_COLOR = value;} get{return _DEFAULT_COLOR;} }
        
        /// <summary>
        /// Liest alle nötigen Dateien ein und cached diese, um später 
        /// alle Maschinen und Produkte benutzerdefiniert darzustellen.
        /// </summary>
        public IconSet()
        {
            this._DEFAULT_COLOR = Colors.PeachPuff;
            this.folderName = ReadFolderName();
            this.actMap = location + folderName + "\\";

            getFileString(actMap + "product\\" + DEFAULT_PRODUCT);

            this.idIconMap      = new Dictionary<int, string>();
            this.idObjMap       = new Dictionary<int, string>();
            this.idTooltipMap   = new Dictionary<int, string>();
            this.productnameObjFileMap  = new Dictionary<string, string>();
            this.productnameColorMap    = new Dictionary<string, Color>();

            string mappingString = getFileString(actMap + mapping);
            fillMapsFromMappingString(mappingString);

            string productMappingString = getFileString(actMap + productMapping);
            fillProductMapFromProductMappingFile(productMappingString);
        }

        
        /// <summary>
        /// Liest aus der choice.txt Datei, welches PluginSet verwendet werden soll.
        /// </summary>
        /// <returns>Name des zu verwendenden PluginSets. </returns>
        /// <exception cref="ChoiceIsEmptyException">falls die Datei leer ist.</exception>
        /// <exception cref="NoSuchFileException">falls die Datei nicht existiert.</exception>
        private static string ReadFolderName()
        {
            string file = getFileString(location + choiceFile);
            string[] parts = file.Split(new string[] { "\r\n" }, StringSplitOptions.None);
            if (parts.Length > 0)
            {
                return parts[0];
            }
            throw new ChoiceIsEmptyException("In der Datei \"choice.txt\" muss der Name des PluginSets angegeben werden.");
        }

        public static void WriteFolderName(string s)
        {
            string filesource = location + choiceFile;
            if (!File.Exists(filesource))
            {
                throw new NoSuchFileException("Fehler. Es existiert keine Datei \"" + filesource + "\". ");
            }
            File.WriteAllText(filesource, s);
        }

        /// <summary>
        /// Gibt alle verfügbaren Plugins im Ordner "iconsets" zurück.
        /// </summary>
        /// <returns>verfügbare IconSets. </returns>
        public string[] GetAvailablePlugins()
        {
            return Directory.GetDirectories(@location).Select(t => t.Split('\\').Last()).ToArray();
        }

        /// <summary>
        /// Gibt den Inhalt einer Datei als string zurück.
        /// </summary>
        /// <param name="filesource">Pfad zur Datei. </param>
        /// <returns>Inhalt der Datei.</returns>
        /// <exception cref="NoSuchFileException">falls die Datei nicht existiert.</exception>
        private static string getFileString(string filesource)
        {
            if (!File.Exists(filesource))
            {
                throw new NoSuchFileException("Fehler. Es existiert keine Datei \""+filesource+"\". ");
            }
            System.IO.StreamReader mappingFile = new System.IO.StreamReader(filesource);
            string mappingString = mappingFile.ReadToEnd();
            mappingFile.Close();
            return mappingString;
        }

        /// <summary>
        /// Liest den Inhalt der Datei product_mapping.txt ein und speichert diesen.
        /// </summary>
        /// <param name="productMappingString">Inhalt der Datei product_mapping.txt als string</param>
        /// <exception cref="ColorMappingException">falls die Farbe nicht korrekt formatiert wurde.</exception>
        /// <exception cref="ProductMappingException">falls das Mapping nicht im korrekten Format angegeben wurde.</exception>
        private void fillProductMapFromProductMappingFile(string productMappingString)
        {
            string[] parts = productMappingString.Split(new string[] { "\r\n" }, StringSplitOptions.None);
            foreach (string part in parts)
            {
                string[] miniParts = part.Split(SEPARATOR);
                if (miniParts.Length >= 3)
                {
                    string name = miniParts[0].ToLower();
                    string fileSource = miniParts[1].ToLower();

                    productnameObjFileMap.Add(name, fileSource);

                    string[] colors = miniParts[2].Split(COLOR_SEPARATOR);

                    if (colors.Length >= 3)
                    {
                        try
                        {
                            Color color;
                            byte r = Byte.Parse(colors[0]);
                            byte g = Byte.Parse(colors[1]);
                            byte b = Byte.Parse(colors[2]);

                            if (colors.Length >= 4)
                            {
                                byte a = Byte.Parse(colors[3]);
                                color = Color.FromArgb(a, r, g, b);
                            }
                            else
                            {
                                color = Color.FromRgb(r, g, b);
                            }

                            productnameColorMap.Add(name, color);
                        }
                        catch (System.FormatException)
                        {
                            throw new ColorMappingException("Die einzelnen Farbwerte in \"product_mapping.txt\" müssen zwischen 0 und 255 liegen.");
                        }
                    }
                    else
                    {
                        throw new ColorMappingException("In \"product_mapping.txt\" muss die Farbe entweder im Format r,g,b für RGB oder im Format r,g,b,a für RGBA angegeben werden. ");
                    }
                }
                else
                {
                    throw new ProductMappingException("Die Datei \"product_mapping.txt\" muss drei ':'-separierte Argumente enthalten, den Namen des Produkts, das obj-File und den Farbwert. ");
                }
            }
        }

        /// <summary>
        /// Liest den Inhalt der Datei mapping.txt ein und speichert diesen.
        /// </summary>
        /// <param name="fileMapping">Inhalt der Datei mapping.txt als string</param>
        /// <exception cref="MappingException">falls das Mapping nicht im korrekten Format angegeben wurde.</exception>
        private void fillMapsFromMappingString(string fileMapping)
        {
            string[] parts = fileMapping.Split(new string[]{"\r\n"}, StringSplitOptions.None);
            foreach (string part in parts)
            {
                string[] miniParts = part.Split(SEPARATOR);
                if (miniParts.Length >= 3)
                {
                    int id = int.Parse(miniParts[0]);
                    string fileSource = miniParts[1];
                    string objSource = miniParts[2];
                    idIconMap.Add(id, fileSource);
                    idObjMap.Add(id, objSource);

                    if (miniParts.Length >= 4)
                    {
                        string tooltip = miniParts[3];
                        idTooltipMap.Add(id, tooltip);
                    }
                }
                else
                {
                    throw new MappingException("Das Mapping muss mindestens drei ':'-separierte Argumente für die Maschinenklassen-ID, die 2D-Darstellung und für das 3D-obj enthalten. ");
                }
            }
        }


        /// <summary>
        /// Gibt ein 2D Image Objekt zur class-id einer Maschine zurück.
        /// </summary>
        /// <param name="id">class-id einer Maschine</param>
        /// <returns>2D Image Objekt zur class-id einer Maschine</returns>
        public Image getImage(int id) 
        {
            Image img = new Image();
            BitmapImage logo = new BitmapImage();
            logo.BeginInit();
            logo.UriSource = new Uri("pack://application:,,,/Client;component/" + actMap + "img\\" + idIconMap[id]);
            logo.EndInit();
            img.Source = logo;
            return img;
        }

        /// <summary>
        /// Gibt eine Model3DGroup aus dem obj-File zur class-id einer Maschine zurück
        /// </summary>
        /// <param name="id">class-id einer Maschine</param>
        /// <returns>Model3DGroup aus dem obj-File zur class-id</returns>
        public Model3DGroup getObj(int id)
        {
            string filePath = actMap + "obj\\" + idObjMap[id];
            return ModelImporter.Load(filePath);
        }

        /// <summary>
        /// Gibt den Tooltip Text für eine MaschinenklassenID zurück.
        /// </summary>
        /// <param name="id">KlassenID einer Maschine</param>
        /// <returns>Info-Text</returns>
        public string getTooltip(int id)
        {
            return idTooltipMap[id];
        }

        /// <summary>
        /// Gibt eine Model3DGroup aus dem obj-File für den Namen eines Produktes zurück. 
        /// Wenn der Name nicht existiert wird das Default-Model (_.obj) zurückgegeben.
        /// Groß- und Kleinschreibung müssen nicht beachtet werden.
        /// </summary>
        /// <param name="productname">Name eines Produktes</param>
        /// <returns>Model3DGroup zum obj-File des angegebenen Namens oder aus _.obj, falls der Name nicht existiert.</returns>
        public Model3DGroup getProductModel(string productname)
        {
            string filename;

            productname = productname.ToLower();
            if (productnameObjFileMap.ContainsKey(productname))
            {
                filename = productnameObjFileMap[productname];
            }
            else
            {
                filename = DEFAULT_PRODUCT;
            }
            Uri uri = new Uri("pack://application:,,,/Client;component/" + actMap + "product\\" + filename);
            return ModelImporter.Load("..\\.." + uri.AbsolutePath); //Wenn das Helixtoolkit verschoben wird muss der Pfad hier möglicherweise angepasst werden!
        }

        /// <summary>
        /// Gibt ein Material aus dem obj-File für den Namen eines Produktes zurück. 
        /// Groß- und Kleinschreibung müssen nicht beachtet werden.
        /// Wenn der Name nicht existiert, wird die DEFAULT_COLOR zurückgegeben.
        /// </summary>
        /// <param name="productname">Name eines Produktes</param>
        /// <returns>Material zur Farbe des Produktes oder DEFAULT_COLOR, falls der Name nicht existiert.</returns>
        public Material getProductColor(string productname)
        {
            Color color;
            productname = productname.ToLower();

            if (productnameColorMap.ContainsKey(productname))
            {
                color = productnameColorMap[productname];
            }
            else
            {
                color = _DEFAULT_COLOR;
            }
            return MaterialHelper.CreateMaterial(color);
        }
    }
}
