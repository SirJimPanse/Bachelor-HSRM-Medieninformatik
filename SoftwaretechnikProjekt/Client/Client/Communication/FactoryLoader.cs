using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Client.FactoryServiceReference;

namespace Client.Communication
{
    public delegate void FactoryDownloadHandler(object sender, FactoryLoaderEventArgs e);

    public class FactoryLoaderEventArgs : EventArgs
    {
        public readonly int width;
        public readonly int height;
        public readonly machineMap factory;
        public readonly bool success;

        public FactoryLoaderEventArgs(bool success, int width, int height, machineMap factory)
        {
            this.success = success;
            this.height = height;
            this.width = width;
            this.factory = factory;
        }
    }


    public class FactoryLoader
    {
        private int id;
        
        public event FactoryDownloadHandler FactoryDownload;

        public int height { get; set; }
        public int width { get; set; }
        public machineMap factory { get; set; }
        public bool success { get; set; }
        private IMqObserver obs;

        public FactoryLoader(int id, IMqObserver obs)
        {
            this.obs = obs;
            this.id = id;
        }

        public void load()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();
            if (csu.ConnectToFactory(id))
            {
                csu.RegisterFactoryMQListener(id, obs);
                factory = csu.GetFactory();
                if (factory != null)
                {
                    width = csu.GetAreaWidth();
                    height = csu.GetAreaHeight();
                    if (width != -1 && height != -1)
                    {
                        success = true;
                    }
                }
                else
                {
                    success = false;
                }

                FactoryDownload(this, new FactoryLoaderEventArgs(success, width, height, factory));
            }
        }
    }
}
