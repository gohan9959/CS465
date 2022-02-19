package chat;

import message.Message;
import message.NodeInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import java.util.Scanner;

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
        Boolean firstJoinAttempt = true;
        Boolean startedSendAndReceive = false;

        // Declare properties file
        String propertiesFile = null;

        // Declare server socket port
        int serverPort = -1;

        // Declare server socket properties
        Properties serverProperties = null;

        // Declare user input objects
        Scanner userInput;
        String[] inputArray;

        // Declare sender and receiver
        ClientSender sender;
        ClientReceiver receiver;

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
            serverPort = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));
        }
        // Failed to read server port, likely due to port not existing or not being an integer
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

        // Create user input scanner
        userInput = new Scanner(System.in);

        //////////////////////////////////
        // BEGIN MAIN SEND/RECEIVE LOOP //
        //////////////////////////////////

        // Run client until shutdown is received
        while (!isShutdown)
        {
            // Client is not connected. Attempt to connect to a chat until connection succeeds
            if (!isConnected)
            {
                // Print join instructions, but only the first time
                if (firstJoinAttempt)
                {
                    System.out.println("\nCurrently not connected to a chat server.");
                    System.out.println("Please enter a client to which you would like to connect.");

                    // Set join attempt flag
                    firstJoinAttempt = false;
                }

                // These instructions will ALWAYS be printed on every join
                // attempt.
                System.out.println("Use the following format:");
                System.out.println("- 'JOIN' to create a new chat room");
                System.out.println("- 'JOIN <IP Address> <Port Number>' to join an existing chat room");
                System.out.println("- 'SHUTDOWN' to exit.\n");
                

                // Read next command and split it into its arguments
                inputArray = userInput.nextLine().split(" ");

                // Case JOIN command
                if (inputArray[0].equals("JOIN"))
                {
                    // Check for standalone JOIN command
                    if (inputArray.length == 1)
                    {
                        System.out.println("Created new chat room.\n");
                    }
                    // Check for three arguments--JOIN, IP, port
                    else if (inputArray.length == 3)
                    {
                        try
                        {
                            // Attempt to join: joinToChat
                            isConnected = joinToChat(inputArray[1], Integer.parseInt(inputArray[2]));
                            
                            // Connection success
                            if (isConnected)
                            {
                                System.out.println("Successfully joined to existing chat room.\n");
                            }
                            // Connection failed
                            else
                            {
                                System.out.println("Could not join existing chat room.\n");
                            }
                        }
                        // Failed to read server port
                        catch (NumberFormatException ex)
                        {
                            System.out.println("Failed to read port number.");
                        }
                        
                    }
                    // Invalid JOIN command
                    else
                    {
                        System.out.println("Invalid number of arguments for JOIN.");
                    }
                }
                // Case SHUTDOWN command
                else if (inputArray[0].equals("SHUTDOWN"))
                {
                    // Print shutdown
                    System.out.println("Shutting down client.\n");

                    // Set shutdown flag
                    isShutdown = true;
                }
                // Invalid command input
                else
                {
                    System.out.println("Invalid command.");
                }

            } // End "if not connected" block

            // Client has connected but has not yet created its sender and receiver. This happens
            // exactly once after a join succeeds.
            else if (!startedSendAndReceive)
            {
                sender = new ClientSender();
                receiver = new ClientReceiver();

                // Set send/receive flag
                startedSendAndReceive = true;

                // Set first attempt flag to display full instructions after leaving chat
                firstJoinAttempt = true;
            }

            // Client is connected. Remain on standby with sender and receiver active. Do nothing here
            // in main.
        }

        // Close user input stream
        userInput.close();
    }

    public static void addUser(NodeInfo user)
    {

    }

    private static Boolean joinToChat(String ip, int port)
    {
        return false; // TODO
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
