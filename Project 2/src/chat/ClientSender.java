package chat;

import message.Message;
import message.MessageTypes;
import message.NodeInfo;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Enumeration;
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
    ServerSocket hostSocket;
    Socket chatConnection = null;
    ObjectOutputStream toChat = null;

    /**
     * Server info
     */
    String hostIP;
    int hostPort;
    String logicalName;
    boolean joining;

    /**
     * Object used for reading input
     */
    Scanner userInput;

    /**
     * Constructor that sets up sender thread
     *
     * @param hostIP    ip of server connecting to
     * @param hostPort  port of server connecting to
     * @param logicalName logical name of client/user
     * @param joining indicates whether or not the client
     */
    public ClientSender(ServerSocket hostSocket, String hostIP, int hostPort, String logicalName, boolean joining)
    {
        this.hostSocket = this.hostSocket;
        this.hostIP = this.hostIP;
        this.hostPort = this.hostPort;
        this.logicalName = logicalName;
        this.joining = joining;

        // open up stdin
        userInput = new Scanner(System.in);
    }

    // Just to supress errors for now
    public ClientSender()
    {

    }

    /**
     * closes server connection
     */
    public void closeConnection()
    {
        try
        {
            chatConnection.close();
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
            chatConnection = new Socket(hostIP, hostPort);
            toChat = new ObjectOutputStream(chatConnection.getOutputStream());
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
        System.out.printf("Alias: [%s] on Server: [%s]:[%d]\n\n", logicalName, hostIP, hostPort);


        // if the user wants to join the server send a join message
        if (joining)
        {
            connectToServer();

            nodeInfo = new NodeInfo(hostSocket.getInetAddress().getHostAddress(), hostSocket.getLocalPort(),
                                    logicalName);

            message = new Message(MessageTypes.JOIN, nodeInfo);
            sendMessageToChat(message);

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
                nodeInfo = new NodeInfo(hostSocket.getInetAddress().getHostAddress(), hostSocket.getLocalPort(),
                                        logicalName);
                message = new Message(MessageTypes.LEAVE, nodeInfo);
                active = false;
            }
            else if (userInputString.equals("SHUTDOWN"))
            {
                nodeInfo = new NodeInfo(hostSocket.getInetAddress().getHostAddress(), hostSocket.getLocalPort(),
                                        logicalName);
                message = new Message(MessageTypes.SHUTDOWN, nodeInfo, logicalName);
                active = false;
                System.out.println("Shutting down client.\n");
                System.exit(0);
            }
            else // NOTE Message
            {
                message = new Message(MessageTypes.NOTE, userInputString, logicalName);
            }

            // Send message to server
            sendMessageToChat(message);

            // close connection to server after sending message
            closeConnection();
        }
    }

    /**
     * Method from online which gets the system's IP address
     *
     * @return IP address, or null if an error occurred
     */
    public String getMyIP()
    {
        try
        {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()
                    || networkInterface.isVirtual() || networkInterface.isPointToPoint())
                {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    InetAddress address = addresses.nextElement();

                    final String myIP = address.getHostAddress();
                    if (Inet4Address.class == address.getClass())
                    {
                        return myIP;
                    }
                }
            }
        }
        catch (SocketException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Sends a given message to the connected chat users
     *
     * @param message server connected to
     */
    public void sendMessageToChat(Message message)
    {
        try
        {
            toChat.writeObject(message);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot send message", ex);
            System.exit(1);
        }
    }
}

