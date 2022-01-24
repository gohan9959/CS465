package server;

import message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the receiving of messages by the server
 *
 * @author Zachary Wilson-Long
 */
public class ServerSender extends Thread
{
    /**
     * Objects used to handle server connectivity
     */
    Socket client;
    ObjectOutputStream toClient;

    /**
     * Note to be sent to the client
     */
    Message note;

    /**
     * Constructor.
     * Stores given client socket.
     *
     * @param client socket containing access to the client
     */
    public ServerSender(Socket client, Message note)
    {
        this.client = client;
        this.note = note;

        try
        {
            toClient = new ObjectOutputStream(client.getOutputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot get output stream to client", ex);
            System.exit(1);
        }
    }

    /**
     * Process the sending of a message to the client
     */
    @Override
    public void run()
    {
        // attempt to send the message to client
        try
        {
            toClient.writeObject(note);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Cannot send message", ex);
            System.exit(1);
        }

        try
        {
            client.close();
        }
        catch (IOException ignored)
        {
        }
    }
}