using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ObjectTest
{
    class Schnorchel
    {
        public int alter { get; set; }
        public string name { get; set; }
        public List<Hobby> hobbies = new List<Hobby>();
        public Dictionary<string, bool> faehigkeiten = new Dictionary<string,bool>();

        public override string ToString()
        {
            return string.Format("Ich bin ein {0} Jahre alter Schnorchel und heisse {1}. Meine Hobbies sind {2}, ich kann {3}", 
                alter, name, string.Join(", ",hobbies.Select(p => p.hobbyname)),
                string.Join("; ",faehigkeiten.ToList().Select(x => x.ToString()))
                );
        }
    }
}

