package server;

import chat.ChatClient;
import chat.NodeInfo;
import message.Message;
import message.MessageTypes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the receiving of messages by the server
 *
 * @author Zachary Wilson-Long
 */
public class ServerReceiver extends Thread
{
    /**
     * Objects used to handle server connectivity
     */
    Socket client;
    ObjectInputStream fromClient;

    /**
     * Constructor.
     * Stores given client socket.
     *
     * @param client socket containing access to the client
     */
    public ServerReceiver(Socket client)
    {
        this.client = client;

//        try
//        {
//            System.out.println("TRYING");
//
//            System.out.println("TRIED");
//        }
//        catch (IOException ex)
//        {
//            System.out.println("FAILED");
//            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Cannot get input stream from client", ex);
//            System.exit(1);
//        }
    }

    /**
     * Process receiving messages from client
     */
    @Override
    public void run()
    {
        Message message = null;

        try
        {
            fromClient = new ObjectInputStream(client.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        // now talk to the client


            // read message from user
        while(true){

            try {
                message = (Message) fromClient.readObject();
            } catch (IOException | ClassNotFoundException ignored) {
            }

            if(message instanceof Message){
                if(message.getMessageType() == MessageTypes.TYPE_JOIN)
                {
                    ChatServer.joinUser((NodeInfo) message.getMessageContent());
                }
                else if(message.getMessageType() == MessageTypes.TYPE_LEAVE
                        || message.getMessageType() == MessageTypes.TYPE_SHUTDOWN)
                {
                    ChatServer.leaveUser((NodeInfo) message.getMessageContent());
                    break;
                }
                else if(message.getMessageType() == MessageTypes.TYPE_NOTE) // type Note
                {
                    System.out.printf("Received Note: %s\n\n", message.getMessageContent());
                    ChatServer.sendNoteToAll(message);
                }
                message = null;
            }
        }
    }
}