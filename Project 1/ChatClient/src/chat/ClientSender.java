package chat;

import message.Message;
import message.MessageTypes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * Handles the sending of messages by the Client to the Server
 *
 * @author Zachary Wilson-Long
 */
public class ClientSender extends Thread
{
    /**
     * Objects used to handle server connectivity
     */
    ServerSocket serverSocket;
    Socket serverConnection = null;
    ObjectOutputStream toServer = null;

    /**
     * Server info
     */
    String serverIP;
    int serverPort;
    String logicalName;
    boolean joining;

    /**
     * Object used for reading input
     */
    Scanner userInput;

    /**
     * Constructor that sets up sender thread
     *  @param serverIP    ip of server connecting to
     * @param serverPort  port of server connecting to
     * @param logicalName logical name of client/user
     * @param joining
     */
    public ClientSender(ServerSocket serverSocket, String serverIP, int serverPort, String logicalName, boolean joining)
    {   
        this.serverSocket = serverSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.logicalName = logicalName;
        this.joining = joining;

        // open up stdin
        userInput = new Scanner(System.in);
    }

    /**
     * closes server connection
     */
    public void closeConnection()
    {
        try
        {
            serverConnection.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot close connection", ex);
            System.exit(1);
        }
    }

    /**
     * Handles server connection for the client
     */
    public void connectToServer()
    {
        try
        {
            serverConnection = new Socket(serverIP, serverPort);
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot connect to server", ex);
            System.exit(1);
        }
    }

    /**
     * Processes user input and sends to the server.
     */
    @Override
    public void run()
    {
        Message message;
        boolean active = true;
        NodeInfo nodeInfo;

        System.out.println("\nConnection Successful...");
        System.out.printf("Alias: [%s] on Server: [%s]:[%d]\n\n", logicalName, serverIP, serverPort);


        // if the user wants to join the server send a join message
        if(joining)
        {
            connectToServer();

            nodeInfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);

            message = new Message(MessageTypes.TYPE_JOIN, nodeInfo);
            sendMessageToServer(message);

            closeConnection();
        }

        System.out.println("You may now begin sending messages.\n");

        // loop as long as sender should be open
        while (active)
        {

            String userInputString = "";

            // Loop until user closes connecting
            try
            {
                // read char from user
                userInputString = this.userInput.nextLine();
            }
            catch (Exception e)
            {
                Logger.getLogger(ClientSender.class.getName()).log(Level.SEVERE, null, e);
            }

            connectToServer();

            // Handles User Inputs
            if (userInputString.equals("LEAVE"))
            {
                // TODO: Reset property values to show client as not in a server
                nodeInfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
                message = new Message(MessageTypes.TYPE_LEAVE, nodeInfo);
                active = false;
            }
            else if (userInputString.equals("SHUTDOWN"))
            {
                nodeInfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
                message = new Message(MessageTypes.TYPE_SHUTDOWN, nodeInfo, logicalName);
                active = false;
                System.out.println("Shutting down client.\n");
                System.exit(0);
            }
            else // NOTE Message
            {
                message = new Message(MessageTypes.TYPE_NOTE, userInputString, logicalName);
            }

            // Send message to server
            sendMessageToServer(message);

            // close connection to server after sending message
            closeConnection();
        }
    }

    /**
     * Sends a given message to the connected server
     *
     * @param message server connected to
     */
    public void sendMessageToServer(Message message)
    {
        try
        {
            toServer.writeObject(message);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot send message", ex);
            System.exit(1);
        }
    }
}

