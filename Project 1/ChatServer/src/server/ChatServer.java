package server;

import chat.Sender;
import message.Message;
import message.MessageTypes;
import chat.NodeInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.PropertyHandler;


/**
 * TODO: Document this class
 */
public class ChatServer
{

    /**
     * Port number on which the server socket is initialized.
     */
    private static int port;

    /**
     * Array list containing NodeInfo objects representing users
     * registered into the chat.
     */
    private static ArrayList<NodeInfo> registeredUsers;

    /**
     * ServerSocket object accepting connections to the server.
     */
    private static ServerSocket serverSocket;

    /**
     * Constructor
     *
     * @param propertiesFile String of a file on relative path containing properties
     */
    public ChatServer(String propertiesFile) {

        Properties properties = null;

        // open properties
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot open properties file", ex);
            System.exit(1);
        }

        // get server port number
        try
        {
            //port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            port = 8881;
        }
        catch (NumberFormatException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot read server port", ex);
            System.exit(1);
        }

        // open server socket
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch(IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot start server socket", ex);
        }

        // Initialize list of registered users with max capacity 25.
        registeredUsers = new ArrayList<NodeInfo>(25);
    }

    /**
     * Run continuous server loop.
     *
     * @throws IOException in case of failure
     */
    public void runServerLoop() throws IOException
    {
        System.out.printf("Chat Server Started on port: [%d]\n", port);
        System.out.println("===================\n");

        Socket newClient;
        Thread newThread;

        while (true)
        {
            System.out.printf("Waiting for connections on port: [%d]\n", port);

            // open up port to listen for messages
            newClient = serverSocket.accept();
            newThread = new Thread(new ServerReceiver(newClient));
            newThread.start();
        }
    }

    /**
     * Add user to the list of registered users.
     *
     * @param user NodeInfo object containing user info.
     */
    public static void joinUser(NodeInfo user)
    {

        // Add NodeInfo object to array
        registeredUsers.add(user);

        System.out.println(user.getLogicalName() + " Added Successfully!");
        System.out.println("List of Users is: ");

        registeredUsers.forEach((u) ->
                                {
                                    System.out.println(u.getLogicalName());
                                });
    }

    /**
     * Remove user from list of registered users.
     *
     * @param user NodeInfo object containing user info.
     */
    public static void leaveUser(NodeInfo user)
    {

        // Check each item in the list and remove if if matches up with
        // the requested user
        registeredUsers.removeIf((registeredUser) ->
                                         registeredUser.isEqual(user));

        System.out.println(user.getLogicalName() + " Removed Successfully!");
        System.out.println("List of Users is: ");

        registeredUsers.forEach((u) ->
                                {
                                    System.out.println(u.getLogicalName());
                                });
        System.out.println();
    }

    /**
     * Send a note to all registered users.
     *
     * @param note Message object containing the note to send out.
     */
    public static void sendNoteToAll(Message note)
    {

        // Lambda function which performs the specified action for each user
        registeredUsers.forEach((user) ->
                                {
                                    try
                                    {
                                        // Establish socket connection to client
                                        Socket userSocket = new Socket(user.getIp(), user.getPort());

                                        // Start Sender thread which sends the message
                                        new Thread(new ServerSender(userSocket, note)).start();
                                    }
                                    catch (IOException ioe)
                                    {
                                        ioe.printStackTrace();
                                    }
                                });

        System.out.println("Message Sent to All Users Successfully!\n");
    }

    public static void shutDown()
    {
        System.out.println("Server Shutdown Successfully!");
        System.exit(0);
    }

    /**
     * Main method.
     *
     * @param args Command line arguments.
     * @throws IOException I/O exception from ServerSocket initialization.
     */
    public static void main(String[] args) throws Exception {
        // create instance of echo server
        // note that hardcoding the port is bad, here we do it just for simplicity reasons
        ChatServer chatServer = new ChatServer("ChatServer/config/Server.properties");

        // fire up server loop
        chatServer.runServerLoop();
    }
}