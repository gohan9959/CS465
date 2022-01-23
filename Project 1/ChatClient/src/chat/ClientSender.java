package chat;

import message.Message;
import message.MessageTypes;

import java.io.*;
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
    Socket serverConnection = null;
    ObjectOutputStream toServer = null;

    /**
     * Server info
     */
    String serverIP;
    int serverPort;
    String logicalName;

    /**
     * Object used for reading input
     */
    Scanner userInput;

    /**
     * Constructor that sets up sender thread
     *
     * @param serverIP    ip of server connecting to
     * @param serverPort  port of server connecting to
     * @param logicalName logical name of client/user
     */
    public ClientSender(String serverIP, int serverPort, String logicalName)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.logicalName = logicalName;

        // open up stdin
        userInput = new Scanner(System.in);
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

        System.out.println("\nConnection Successful...");
        System.out.printf("Alias: [%s] on Server: [%s]:[%d]\n\n", logicalName, serverIP, serverPort);
    }

    /**
     * Processes user input and sends to the server.
     */
    @Override
    public void run()
    {
        Message message;
        boolean active = true;

        // loop as long as sender should be open
        while (active)
        {
            connectToServer();

            String userInputString = "";

            // Loop until user closes connecting
            System.out.print("Enter Message: ");

            try
            {
                // read char from user
                userInputString = this.userInput.nextLine();
            }
            catch (Exception e)
            {
                Logger.getLogger(ClientSender.class.getName()).log(Level.SEVERE, null, e);
            }
            System.out.println("Input Received: " + userInput);

            NodeInfo nodeinfo = new NodeInfo(serverConnection.getInetAddress().getHostAddress(),
                                             serverConnection.getLocalPort(), logicalName);


            // Handles User Inputs
            if (userInputString.equals("LEAVE"))
            {
                // TODO: Reset property values to show client as not in a server
                message = new Message(MessageTypes.TYPE_LEAVE, nodeinfo);
                active = false;
            }
            else if (userInputString.equals("SHUTDOWN"))
            {
                message = new Message(MessageTypes.TYPE_SHUTDOWN, nodeinfo, logicalName);
                active = false;
            }
            else // NOTE Message
            {
                message = new Message(MessageTypes.TYPE_NOTE, userInputString, logicalName);
            }

            // Send message to server
            try
            {
                toServer.writeObject(message);
            }
            catch (IOException ex)
            {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot send message", ex);
                System.exit(1);
            }

            // close connection to server after sending message
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
    }
}
