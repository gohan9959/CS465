package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable<String, Object> toolsCache = null;
    
    // Properties handlers
    private PropertyHandler satelliteConfig = null;
    private PropertyHandler serverConfig = null;
    private PropertyHandler classLoaderConfig = null;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // init holder variables
        int port;
        String host;
        String name;
        String docRoot;
        
        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        
        try {
            satelliteConfig = new PropertyHandler(satellitePropertiesFile);
        } catch (IOException e) {
            // no use carrying on, so bailing out ...
            System.err.println("No config file found, bailing out ...");
            System.exit(1);
        }
        
        name = satelliteConfig.getProperty("NAME");
        satelliteInfo.setName(name);
        
        port = Integer.parseInt(satelliteConfig.getProperty("PORT"));
        satelliteInfo.setPort(port);
  
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        
        try {
            serverConfig = new PropertyHandler(serverPropertiesFile);
        } catch (IOException e) {
            // no use carrying on, so bailing out ...
            System.err.println("No config file found, bailing out ...");
            System.exit(1);
        }
        
        host = serverConfig.getProperty("HOST");
        serverInfo.setHost(host);
        
        port = Integer.parseInt(serverConfig.getProperty("PORT"));
        serverInfo.setPort(port);
        
        
        // read properties of the code server and create class loader
        // -------------------
        
        try {
            classLoaderConfig = new PropertyHandler(classLoaderPropertiesFile);
        } catch (IOException e) {
            // no use carrying on, so bailing out ...
            System.err.println("No config file found, bailing out ...");
            System.exit(1);
        }
        
        host = classLoaderConfig.getProperty("HOST");
        port = Integer.parseInt(classLoaderConfig.getProperty("PORT"));
        
        classLoader = new HTTPClassLoader(host, port);
        
        
        // create tools cache
        // -------------------
        toolsCache = new Hashtable<>();
        
    }

    @Override
    public void run() {
        
        // TODO: (IN 4.3) SatelliteManager stuff
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            // ...
            
            // reading message
            // ...
            
            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
                    break;

                default:
                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // ...
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
