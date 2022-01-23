package chat;

import java.io.*;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

import utils.PropertyHandler;


/**
 * Primary class to handle the chat client.
 * Initializes client and call helper threads.
 *
 * @author Conrad Murphy, Harshith Shakelli, and Zachary Wilson-Long
 */
public class ChatClient
{

    /**
     * server connectivity information
     */
    private static String serverIP = null;
    private static int serverPort = 0;
    private boolean joinedStatus;
    public static ServerSocket serverSocket;

    /**
     * alias of user to be recognized by
     */
    String logicalName;

    /**
     * Used to access property file to see if client has any stored information
     */
    private Properties properties;

    /**
     * Constructor.
     * Handles property file to set up field variables
     *
     * @param propertiesFile
     */
    public ChatClient(String propertiesFile)
    {
        properties = null;

        // open properties
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot open properties file", ex);
            System.exit(1);
        }

        // get joined status
        try
        {
            if (Integer.parseInt(properties.getProperty("JOINED")) == 1)
            {
                joinedStatus = true;
            }
            else
            {
                joinedStatus = false;
            }
        }
        catch (NumberFormatException ex)
        {
            joinedStatus = false;
        }

        // get logical name
        try
        {
            logicalName = properties.getProperty("NAME");
        }
        catch (NumberFormatException ignored)
        {
        }

        // get server IP
        try
        {
            serverIP = properties.getProperty("SERVER_IP");
        }
        catch (Exception ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot read server IP", ex);
            System.exit(1);
        }

        // get server port
        try
        {
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot read server port", ex);
            System.exit(1);
        }
    }

    //@Override
    /**
     * Implementation of interface Runnable
     *
     * Called by main() to handle client setup and then call sender and receiver threads
     */
    public void Client() throws IOException
    {

        System.out.println("Chat Client Started");
        System.out.println("===================\n");

        // init whether the user is attempting to join a server
        serverSocket = new ServerSocket(8882);
        boolean joining = false;

        // If user not recognized as Joined to a server
        if (!joinedStatus)
        {
            System.out.println("Currently not connected to a chat server.\n");
            System.out.println("Please enter which server you would like to join");
            System.out.println("Use the following format: 'JOIN <IP Address> <Port Number>'\n");

            // since user not already joined then they are attempting to join a server now
            joining = true;

            // handle user join
            Scanner read = new Scanner(System.in);
            while (true)
            {
                String userInput;

                System.out.print("Command: ");
                userInput = read.nextLine();
                String[] inputArr = userInput.split(" ");

                if (inputArr[0].equals("JOIN"))
                {
                    // handle invalid use of JOIN
                    if (inputArr.length < 3)
                    {
                        System.out.println("Invalid number of arguments for join");
                        System.out.println("Use the following format: 'JOIN <IP Address> <Port Number>'\n");
                        continue;
                    }

                    // update IP and port to user inputs
                    //serverIP = inputArr[1];
                    //serverPort = Integer.parseInt(inputArr[2]);

                    // TODO: Fix this, not updating file
                    // update property file
                    properties.setProperty("JOINED", "1");
                    properties.setProperty("SERVER_IP", serverIP);
                    properties.setProperty("SERVER_PORT", Integer.toString(serverPort));

                    System.out.print("Please enter an alias for server to recognize you as: ");
                    logicalName = read.nextLine();
                    properties.setProperty("NAME", logicalName);

                    // since entered valid JOIN statement we close the loop and start connecting
                    break;
                }
                else if (userInput.equals("SHUTDOWN"))
                {
                    System.out.println("Shutting down client.");
                    System.exit(1);
                }
                else // Invalid input
                {
                    System.out.println("Invalid argument at this time please enter a valid argument");
                    System.out.println("Use the following format: 'JOIN <IP Address> <Port Number>'");
                    System.out.println("Or to close the program: 'SHUTDOWN'\n");
                }

            }
        }

        // start the sender/receiver threads
        //(new ClientReceiver(serverSocket)).start();
        (new ClientSender(serverSocket, serverIP, serverPort, logicalName, joining)).start();
    }

    /**
     * Load in property file info and then run the constructor
     * @throws IOException
     *
     */
    public static void main(String[] args) throws IOException
    {

        String propertiesFile;

        try
        {
            propertiesFile = args[0];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            propertiesFile = "ChatClient/config/Server.properties";
        }

        ChatClient chatClient = new ChatClient(propertiesFile);
        chatClient.Client();
    }

}