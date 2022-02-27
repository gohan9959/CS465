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
    String receiverIP;
    int receiverPort;
    String logicalName;
    Message message;

    /**
     * Object used for reading input
     */
    Scanner userInput;

    /**
     * Constructor that sets up sender thread
     *
     * @param receiverIP    ip of message receiver
     * @param receiverPort  port of message receiver
     * @param logicalName   logical name of client/user
     * @param message       object containing message to send
     */
    public ClientSender(ServerSocket hostSocket, String receiverIP, int receiverPort, String logicalName, Message message)
    {
        this.hostSocket = hostSocket;
        this.receiverIP = receiverIP;
        this.receiverPort = receiverPort;
        this.logicalName = logicalName;
        this.message = message;

        // open up stdin
        userInput = new Scanner(System.in);
    }

    /**
     * closes connection with receiver
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
    public void connectToReceiver()
    {
        try
        {
            chatConnection = new Socket(receiverIP, receiverPort);
            toChat = new ObjectOutputStream(chatConnection.getOutputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot connect to receiver", ex);
            System.exit(1);
        }
    }

    /**
     * Processes user input and sends to the server.
     */
    @Override
    public void run()
    {
        System.out.println("\nConnection Successful...");
        System.out.printf("Alias: [%s] on Server: [%s]:[%d]\n\n", logicalName, receiverIP, receiverPort);

        // Send message to user
        sendMessageToUser(message);
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
    public void sendMessageToUser(Message message)
    {
        connectToReceiver();
        try
        {
            toChat.writeObject(message);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot send message", ex);
            System.exit(1);
        }
        // close connection to server after sending message
        closeConnection();
    }
}

