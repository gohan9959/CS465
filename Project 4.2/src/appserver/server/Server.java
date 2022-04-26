package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server
{
    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;

    public Server(String serverPropertiesFile)
    {
        // create satellite manager and load manager
        // ...
        satelliteManager = new SatelliteManager();
        loadManager = new LoadManager();
        
        // read server properties and create server socket
        try
        {
            PropertyHandler serverProperties = new PropertyHandler(serverPropertiesFile);
            // String serverHost = serverProperties.getProperty("HOST");
            int serverPort = Integer.parseInt(serverProperties.getProperty("PORT"));
            serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException ex)
        {
            System.err.println("Properties file " + serverPropertiesFile + " not found, exiting ...");
            System.exit(1);
        }
    }

    public void run() throws IOException
    {
        Socket clientSocket = null;
        
        System.out.println("""                    
                                   Server Started
                           Accepting incoming transactions
                           ================================
                           """);
        
        // serve clients in server loop ...
        while(true)
        {
            // when a request comes in, a ServerThread object is spawned
            clientSocket = serverSocket.accept();
            new Thread(new ServerThread(clientSocket)).start();
        }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread
    {
        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        private ServerThread(Socket client)
        {
            this.client = client;
        }

        @Override
        public void run()
        {
            ConnectivityInfo satelliteInfo = null;
            
            // set up object streams and read message
            try
            {
                readFromNet = new ObjectInputStream(client.getInputStream());
                writeToNet = new ObjectOutputStream(client.getOutputStream());
                message = (Message) readFromNet.readObject();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace(System.err);
            }

            // process message
            switch (message.getType())
            {
                case REGISTER_SATELLITE:
                    // read satellite info
                    satelliteInfo = (ConnectivityInfo) message.getContent();

                    // register satellite
                    synchronized (Server.satelliteManager)
                    {
                        satelliteManager.registerSatellite(satelliteInfo);
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager)
                    {
                        loadManager.satelliteAdded(satelliteInfo.getName());
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");

                    String satelliteName = null;
                    synchronized (Server.loadManager)
                    {
                        try
                        {
                            // get next satellite from load manager
                            satelliteName = loadManager.nextSatellite();

                            // get connectivity info for next satellite from satellite manager
                            satelliteInfo = satelliteManager.getSatelliteForName(satelliteName);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace(System.err);
                        }
                    }

                    Socket satellite = null;

                    // connect to satellite
                    String host = satelliteInfo.getHost();
                    int port = satelliteInfo.getPort();
                    
                    try
                    {
                        satellite = new Socket(host, port);
                        
                        // open object streams
                        ObjectOutputStream toSatellite = new ObjectOutputStream(satellite.getOutputStream());
                        ObjectInputStream fromSatellite = new ObjectInputStream(satellite.getInputStream());
                        
                        // forward message (as is) to satellite
                        toSatellite.writeObject(message);
                        
                        // receive result from satellite
                        Message response = (Message) fromSatellite.readObject();
                        
                        // write result back to client
                        writeToNet.writeObject(response);
                    }
                    catch (IOException | ClassNotFoundException ex)
                    {
                        ex.printStackTrace(System.err);
                    }

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) throws IOException
    {
        // start the application server
        Server server = null;
        if(args.length == 1)
        {
            server = new Server(args[0]);
        }
        else
        {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
