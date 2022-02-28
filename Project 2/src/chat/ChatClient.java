package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import message.MessageTypes;
import message.NodeInfo;
import utils.PropertyHandler;

public class ChatClient implements MessageTypes
{
    /**
     * List of users registered to the chat.
     */
    private static ArrayList<NodeInfo> registeredUsers;

    /**
     * Flag which determines connection status of client.
     */
    private static boolean isConnected;

    /**
     * Flag which determines whether to shut down client.
     */
    private static boolean isShutdown;

    /**
     * Client's own server socket IP
     */
    private static String selfIp;

    /**
     * Client's own server socket port
     */
    private static int selfPort;

    /**
     * Client's own logical name
     */
    private static String selfLogicalName;

    /**
     * Client's own NodeInfo object
     */
    private static NodeInfo selfNodeInfo;

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
            propertiesFile = "config/Server.properties";
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

        // Attempt to read server IP
        selfIp = serverProperties.getProperty("SERVER_IP");
        // Failed to read server IP, likely due to IP not existing
        if (selfIp == null)
        {
            // Log failure and exit
            errorLogger.log(Level.SEVERE, "Cannot read server IP.");
            System.exit(1);
        }

        // Attempt to read server socket port
        try
        {
            selfPort = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));
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
            serverSocket = new ServerSocket(selfPort);
        }
        // Failed to create server socket
        catch (IOException ex)
        {
            errorLogger.log(Level.SEVERE, "Cannot start server socket.", ex);
            System.exit(1);
        }

        // Start the server loop
        startServerLoop();
    }

    /**
     * Run main server loop.
     */
    public static void startServerLoop()
    {
        // Initialize operation flags
        Boolean firstJoinAttempt = true;
        Boolean startedReceive = false;

        // Declare user input objects
        Scanner inputReader;
        String inputString;
        String[] inputArray;

        // Initialize empty list of users
        registeredUsers = new ArrayList<NodeInfo>();

        // Create user input scanner
        inputReader = new Scanner(System.in);

        // Get logical name
        System.out.println("What is your display name?\n");
        selfLogicalName = inputReader.nextLine();

        // Create client NodeInfo
        selfNodeInfo = new NodeInfo(selfIp, selfPort, selfLogicalName);

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

                // These instructions will ALWAYS be printed on every join attempt.
                System.out.println("Use the following format:");
                System.out.println("- 'JOIN' to create a new chat room");
                System.out.println("- 'JOIN <IP Address> <Port Number>' to join an existing chat room");
                System.out.println("- 'SHUTDOWN' to exit.\n");
                

                // Read next command and split it into its arguments
                inputArray = inputReader.nextLine().split(" ");

                // Case JOIN command
                if (inputArray[0].equals("JOIN"))
                {
                    // Check for standalone JOIN command
                    if (inputArray.length == 1)
                    {
                        // Indicate creation of new chat room
                        isConnected = true;
                        System.out.println("\nCreated new chat room.");

                        // Add self to members list
                        addUser(selfNodeInfo);
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
                                System.out.println("\nSuccessfully joined to existing chat room.");
                            }
                            // Connection failed
                            else
                            {
                                System.out.println("\nCould not join existing chat room.\n");
                            }
                        }
                        // Failed to read server port
                        catch (NumberFormatException ex)
                        {
                            System.out.println("\nFailed to read port number.\n");
                        }
                        
                    }
                    // Invalid JOIN command
                    else
                    {
                        System.out.println("\nInvalid number of arguments for JOIN.\n");
                    }
                }
                // Case SHUTDOWN command
                else if (inputArray[0].equals("SHUTDOWN"))
                {
                    // Print shutdown
                    System.out.println("\nShutting down client.\n");

                    // Set shutdown flag
                    isShutdown = true;
                }
                // Invalid command input
                else
                {
                    System.out.println("\nInvalid command.\n");
                }

                // Unset startedReceive flag
                startedReceive = false;

            } // End "if not connected" block

            // Client has connected but has not yet created its receiver. This happens
            // exactly once after a join succeeds.
            else if (!startedReceive)
            {
                // Print confirmation to begin accepting user input
                System.out.println("You may now begin sending messages.\n");

                // Start receiver
                new Thread(new ClientReceiver(serverSocket, selfNodeInfo)).start();

                // Set send/receive flag
                startedReceive = true;

                // Set first attempt flag to display full instructions after leaving chat
                firstJoinAttempt = true;
            }

            // Client is connected and may begin sending input.
            else
            {
                // Block for user input
                inputString = inputReader.nextLine();

                // Parse into array form
                inputArray = inputString.split(" ");

                // Check for command LEAVE
                if (inputArray[0].equals("LEAVE"))
                {
                    leaveFromChat();
                }
                // Check for command SHUTDOWN
                else if (inputArray[0].equals("SHUTDOWN"))
                {
                    orderShutdown();
                }
                // Otherwise, send note
                else
                {
                    sendNote(inputString);
                }
            }
        }

        ////////////////////////////////
        // END MAIN SEND/RECEIVE LOOP //
        ////////////////////////////////

        // Close user input stream
        inputReader.close();

        // Close receiver connection
        System.exit(0);
    }

    /**
     * Set list of registered users.
     * 
     * @param userList ArrayList of registered users which was received by ClientReceiver.
     */
    public static void addList(ArrayList<NodeInfo> userList)
    {
        registeredUsers = userList;
    }

    /**
     * Add user to list of registered members.
     * 
     * @param newUser NodeInfo user to be added.
     */
    public static void addUser(NodeInfo newUser)
    {
        registeredUsers.add(newUser);
    }

    /**
     * Attempt to join to an existing client's chat mesh.
     * 
     * @param ip IP address of existing client's server socket.
     * @param port Port number of existing client's server socket.
     * @return True if join was successful; false otherwise.
     */
    private static boolean joinToChat(String ip, int port)
    {
        try
        {
            // Create JOIN message
            Message joinMessage = new Message(JOIN, selfNodeInfo, selfLogicalName);

            // Create ClientSender and send join message
            (new ClientSender(ip, port, selfLogicalName, joinMessage)).sendMessageToUser();

            // Return success
            return true;
        }
        catch (IOException ex)
        {
            // Return failure if error occurred
            return false;
        }
    }

    /**
     * Leave from chat.
     * 
     * Notify all other users to remove this client from their user list,
     * then set connection flag.
     */
    public static void leaveFromChat()
    {
        removeUser(selfNodeInfo);

        sendToAll(new Message(LEAVE, selfNodeInfo), false);
        isConnected = false;
    }

    /**
     * Order all users to shut down themselves down.
     */
    public static void orderShutdown()
    {
        // Create shutdown message and send to all
        sendToAll(new Message(SHUTDOWN, null), true);
    }

    /**
     * Add joining user to the mesh.
     * 
     * Send list of registered users to new user, then instruct all registered
     * users to add new user to their lists.
     * 
     * @param newUser NodeInfo user which is joining.
     */
    public static void receiveJoiningUser(NodeInfo newUser)
    {
        // Notify all users, except self, to add newly joined
        sendToAll(new Message(ADD, newUser), false);

        // Add user to own list independently of the sendToAll method, to prevent
        // concurrency issues
        addUser(newUser);

        // Create ADD LIST containing registered users
        Message arrayMessage = new Message(ADD_LIST, registeredUsers, selfLogicalName);

        // Send members list to newly joined
        new Thread(new ClientSender(newUser.getIp(), newUser.getPort(),
                selfLogicalName, arrayMessage)).start();
    }

    /**
     * Receive the command to shut down client by setting shutdown flag.
     */
    public static void receiveShutdown()
    {
        isShutdown = true;
    }

    /**
     * Remove specified user from registered user list.
     * 
     * @param leavingUser NodeInfo user to be removed.
     */
    public static void removeUser(NodeInfo leavingUser)
    {
        // Lambda function which searches for matching user in registered user list
        // and deletes accordingly
        registeredUsers.removeIf( (registeredUser) -> 
                registeredUser.isEqual(leavingUser));
    }

    /**
     * Send message of type note to all users.
     * 
     * @param note Note message to send.
     */
    public static void sendNote(String note)
    {
        // Create note message and send to all
        sendToAll(new Message(NOTE, note, selfLogicalName), true);
    }

    /**
     * Send any type of message to all registered users in the user list.
     * 
     * @param message Message to be sent to all users.
     * @param sendToSelf Flag which determines whether to send message to self as well.
     */
    private static void sendToAll(Message message, boolean sendToSelf)
    {
        // Lambda function which creates sender and sends message to each registered user
        registeredUsers.forEach( (registeredUser) ->
        {
            // Check that the user is not self, if sendToSelf flag is false
            if (sendToSelf || !(registeredUser.isEqual(selfNodeInfo)))
            {
                // Create sender and send message
                new Thread(new ClientSender(registeredUser.getIp(), registeredUser.getPort(),
                        registeredUser.getLogicalName(), message)).start();
            }
        });
    }
}
