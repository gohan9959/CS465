package appserver.server;

import appserver.comm.ConnectivityInfo;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class SatelliteManager
{
    // (the one) hash table that contains the connectivity information of all satellite servers
    static private Hashtable<String, ConnectivityInfo> satellites = null;

    public SatelliteManager()
    {
        // initialize satellites table
        satellites = new Hashtable<>();
    }

    public void registerSatellite(ConnectivityInfo satelliteInfo)
    {
        // get satellite name
        String satelliteName = satelliteInfo.getName();
        
        // put <name, info> key pair into satellites
        satellites.put(satelliteName, satelliteInfo);
    }

    public ConnectivityInfo getSatelliteForName(String satelliteName)
    {
        // get and return info corresponding to name
        return satellites.get(satelliteName);
    }
}
