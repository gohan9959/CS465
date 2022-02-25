package chat;

import message.Message;
import message.MessageTypes;

import java.io.*;
import java.net.ServerSocket;
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
    ServerSocket serverConnection = null;
    Socket connection;
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
     * @param serverConnection the server socket
     */
    public ClientReceiver(ServerSocket serverConnection)
    {
        this.serverConnection = serverConnection;
    }

    /**
     * Handles server connection for the client
     */
    public void connectToServer() throws InterruptedException
    {
        try
        {
            connection = serverConnection.accept();
            fromServer = new ObjectInputStream(connection.getInputStream());
        }
        catch (SocketException connectionClosed)
        {
            throw (new InterruptedException());
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
     * Close server connection and stop the thread.
     *
     * @throws IOException
     */
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

        try
        {
            // loop as long as receiver should be open
            while (active)
            {
                connectToServer();

                try
                {
                    message = (Message) fromServer.readObject();
                }
                catch (IOException | ClassNotFoundException ex)
                {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "No input message found", ex);
                    System.exit(1);
                }

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
        catch (InterruptedException interrupt)
        {
            System.exit(0);
            // Close thread
        }
    }
}