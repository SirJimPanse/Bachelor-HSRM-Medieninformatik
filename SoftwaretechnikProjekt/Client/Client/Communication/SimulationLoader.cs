using System;
using Client.SimulationServiceReference;

namespace Client.Communication
{
    public delegate void SimulationDownloadHandler(object sender, SimulationLoaderEventArgs e);

    public class SimulationLoaderEventArgs : EventArgs
    {
        public readonly int width;
        public readonly int height;
        public readonly machineMap factory;
        public readonly bool success;

        public SimulationLoaderEventArgs(bool success, int width, int height, machineMap factory)
        {
            this.success = success;
            this.height = height;
            this.width = width;
            this.factory = factory;
        }
    }


    public class SimulationLoader
    {
        private int id;

        public event SimulationDownloadHandler SimulationDownload;

        public int height { get; set; }
        public int width { get; set; }
        public machineMap simulation { get; set; }
        public bool success { get; set; }
        private IMqObserver obs;

        public SimulationLoader(int id, IMqObserver obs)
        {
            this.obs = obs;
            this.id = id;
        }

        public void load()
        {
            ClientServerUtil csu = ClientServerUtil.getInstance();

            csu.RegisterFactoryMQListener(id, obs);
            simulation = csu.GetSimulation(id);
            if (simulation != null)
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

            SimulationDownload(this, new SimulationLoaderEventArgs(success, width, height, simulation));
        }
       
    }
}