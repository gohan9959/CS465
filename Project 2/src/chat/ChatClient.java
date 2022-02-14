package chat;

import message.Message;
import message.NodeInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;

import utils.PropertyHandler;

public class ChatClient
{
    /**
     * List of users registered to the chat.
     */
    private static ArrayList registeredUsers;

    /**
     * Flag which determines connection status of client.
     */
    private static boolean isConnected;

    /**
     * Flag which determines whether to shut down client.
     */
    private static boolean isShutdown;

    /**
     * Client's server socket with which to accept messages.
     */
    private static ServerSocket serverSocket;

    public static void main(String[] args)
    {
        // Initialize logger for fatal errors
        Logger errorLogger = Logger.getLogger(ChatClient.class.getName());

        // Initialize operation flags
        isConnected = false;
        isShutdown = false;

        // Declare properties file
        String propertiesFile = null;

        // Declare server socket port
        int serverPort = -1;

        // Declare server socket properties
        Properties serverProperties = null;

        // Attempt to fetch properties file
        try
        {
            propertiesFile = args[0];
        }
        // Use default properties file location, if none given
        catch (ArrayIndexOutOfBoundsException ex)
        {
            propertiesFile = "ChatClient/config/Server.properties";
        }

        // Attempt to read properties file
        try
        {
            serverProperties = new PropertyHandler(propertiesFile);
        }
        // Failed to read properties file
        catch (IOException ex)
        {
            // Log failure and exit
            errorLogger.log(Level.SEVERE, "Cannot read properties file.", ex);
            System.exit(1);
        }

        // Attempt to read server socket port
        try
        {
            serverPort = Integer.parseInt(
                    serverProperties.getProperty("SERVER_PORT"));
        }
        // Failed to read server port, likely due to port not existing
        // or not being an integer
        catch (NumberFormatException ex)
        {
            // Log failure and exit
            errorLogger.log(Level.SEVERE, "Cannot read server port.", ex);
            System.exit(1);
        }

        // Attempt to create server socket
        try
        {
            serverSocket = new ServerSocket(serverPort);
        }
        // Failed to create server socket
        catch (IOException ex)
        {
            errorLogger.log(Level.SEVERE, "Cannot start server socket.", ex);
            System.exit(1);
        }

        //////////////////////////////////
        // BEGIN MAIN SEND/RECEIVE LOOP //
        //////////////////////////////////

        // Run client until shutdown is received
        while (!isShutdown)
        {
            // If client is not connected, attempt to connect to a chat
            // until connection succeeds
            if (!isConnected)
            {
                
            }

            // If client is connected, remain on standby with sender and
            // receiver active. Do nothing here in main.
        }
    }

    public static void addUser(NodeInfo user)
    {

    }

    public static void joinToChat(String ip, int port)
    {

    }

    public static void leaveFromChat()
    {

    }

    public static void orderShutdown()
    {

    }

    public static void receiveJoiningUser(NodeInfo user)
    {

    }

    public static void receiveShutdown()
    {

    }

    public static void removeUser(NodeInfo user)
    {

    }

    public static void sendNote(String note)
    {

    }

    private static void sendToAll(Message message)
    {

    }
}
