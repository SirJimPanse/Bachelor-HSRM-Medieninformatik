using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ObjectTest
{
    class Hobby
    {
        public Hobby() { }
        public Hobby(string hn, bool ish)
        {
            isSuperhobby = ish;
            hobbyname = hn;
        }
        public bool isSuperhobby = false;
        public string hobbyname = "";
    }
}
