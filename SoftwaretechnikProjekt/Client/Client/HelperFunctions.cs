using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Client.FactoryServiceReference;

namespace Client
{
    public enum DirectionEnum { NORTH, EAST, SOUTH, WEST };

    class HelperFunctions
    {
        

        public static string SPLIT_FACTORY_NAME = " - ";

        public static int GetClassIdFromItemId(string instanceID)
        {
            int classID = int.Parse(instanceID.Split('-')[0]);
            return classID;
        }

        public static string ToString(int key, string value)
        {
            return key + SPLIT_FACTORY_NAME + value;
        }

        public static int GetIdFromFactoryString(string factoryString)
        {
            if (factoryString == null)
            {
                return -1;
            }
            string[] parts = factoryString.Split(new string[] { SPLIT_FACTORY_NAME }, StringSplitOptions.None);
            return int.Parse(parts[0]);
        }


        public static string GetNameFromFactoryString(string factoryString)
        {
            if (factoryString == null)
            {
                return null;
            }
            string[] parts = factoryString.Split(new string[] { SPLIT_FACTORY_NAME }, StringSplitOptions.None);
            return parts[1];
        }

        /* ------------------------------ Direction - Functions ------------------------------------- */

        public static int GetAngleFromDirection(DirectionEnum dir)
        {
            switch (dir)
            {
                case DirectionEnum.NORTH:
                    return 0;
                case DirectionEnum.WEST:
                    return 90;
                case DirectionEnum.SOUTH:
                    return 180;
                case DirectionEnum.EAST:
                    return 270;
                default:
                    return -1;
            }
        }

    }
}
