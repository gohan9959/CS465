package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;

public class ProxyReceiver implements Runnable
{
    /**
     * Proxy which created this receiver
     */
    private Proxy parentProxy;

    /**
     * Server socket
     */
    private ServerSocket serverSocket;

    /**
     * Constructor. Takes calling proxy object ("this") and its server socket
     * as parameters.
     * 
     * @param parentProxy Proxy which created this receiver
     * @param serverSocket Server socket used by proxy
     */
    public ProxyReceiver(Proxy parentProxy, ServerSocket serverSocket)
    {
        this.parentProxy = parentProxy;
        this.serverSocket = serverSocket;
    }

    /**
     * Run thread. Actions consist of connecting to transaction server,
     * receiving message object, and saving the message to the proxy which
     * called the receiver.
     */
    public void run()
    {
        try
        {
            // Connect
            Socket serverConnection = serverSocket.accept();

            // Receive object input
            ObjectInputStream fromServer = new ObjectInputStream(serverConnection.getInputStream());
            Message receivedMessage = (Message) fromServer.readObject();
            fromServer.close();

            // Receive message in proxy
            parentProxy.receiveMessage(receivedMessage);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }
}
