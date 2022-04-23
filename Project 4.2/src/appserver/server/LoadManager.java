package appserver.server;

import java.util.ArrayList;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager
{
    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;

    public LoadManager()
    {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName)
    {
        // add satellite
        satellites.add(satelliteName);
    }

    public String nextSatellite() throws Exception
    {
        synchronized (satellites)
        {
            // implement policy that returns the satellite name according to a round robin methodology
            lastSatelliteIndex++;
            if (lastSatelliteIndex >= satellites.size())
            {
                lastSatelliteIndex = 0;
            }
        }

         // name of satellite who is supposed to take job
        return (String) satellites.get(lastSatelliteIndex);
    }
}
