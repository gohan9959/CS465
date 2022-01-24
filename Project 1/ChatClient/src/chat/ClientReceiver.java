package chat;

import message.Message;
import message.MessageTypes;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the sending of messages by the Client to the Server
 *
 * @author Zachary Wilson-Long
 */
public class ClientReceiver extends Thread
{

    /**
     * Objects used to handle server connectivity
     */
    Socket serverConnection = null;
    ObjectInputStream fromServer = null;

    /**
     * Server info
     */
    String serverIP;
    int serverPort;
    String logicalName;

    /**
     * Constructor that sets up sender thread
     *
     * @param serverIP    ip of server connecting to
     * @param serverPort  port of server connecting to
     * @param logicalName logical name of client/user
     */
    public ClientReceiver(String serverIP, int serverPort, String logicalName)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.logicalName = logicalName;
    }

    /**
     * Handles server connection for the client
     */
    public void connectToServer() throws InterruptedException
    {
        try
        {
            serverConnection = new Socket(serverIP, serverPort);
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
        }

        catch (SocketException connectionClosed) {

            throw new InterruptedException();
        }

        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot connect to server", ex);
            System.exit(1);
        }
    }

    public void closeConnection() throws IOException
    {
        serverConnection.close();
    }

    /**
     * Process message received from server
     */
    @Override
    public void run()
    {
        Message message = null;
        boolean active = true;

        try {

            // loop as long as receiver should be open
            while (active)
            {

                connectToServer();

                message = (Message) fromServer.readObject();

                // Handles Received Message Types
                if (message.getMessageType() == MessageTypes.TYPE_LEAVE ||
                    message.getMessageType() == MessageTypes.TYPE_SHUTDOWN)
                {
                    active = false;
                }
                else // NOTE Message
                {
                    System.out.printf("[%s]: %s\n", message.getSender(), message.getMessageContent());
                }
            }
        }

        // Catches if the receiver is stopped by killing the connection
        catch (InterruptedException interrupt) {

            // Left the chat server.
        }

        catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "No input message found", ex);
            System.exit(1);
        }
    }
}