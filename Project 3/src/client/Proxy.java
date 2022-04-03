package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import message.Message;
import message.MessageTypes;
import utils.PropertyHandler;

/**
 * Proxy object which sends messages to transaction server. Requires access
 * to client properties to start server socket, and server properties to
 * connect to server.
 */
public class Proxy implements MessageTypes
{
    /**
     * Most recent message saved from ProxyReceiver
     */
    private Message recentMessage;

    /**
     * Receiving side of connection to server
     */
    private ServerSocket receiver;

    /**
     * Sending side of connection to server
     */
    private Socket serverConnection;

    /**
     * Constructor. Requires access to client properties.
     */
    public Proxy()
    {
        try
        {
            // Configure node info and server socket
            PropertyHandler properties = new PropertyHandler("Client.properties");
            int port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            this.receiver = new ServerSocket(port);

            // Set other fields to dummy values
            this.recentMessage = null;
            this.serverConnection = null;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Open a transaction.
     * 
     * Requires access to server properties in order to connect.
     * 
     * @return Transaction ID of newly opened transaction.
     */
    public int openTransaction()
    {
        try
        {
            // Get transaction server properties
            PropertyHandler serverProperties = new PropertyHandler("Server.properties");
            String serverIP = serverProperties.getProperty("SERVER_IP");
            int serverPort = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));

            // Start receiver
            Thread receiverThread = new Thread(new ProxyReceiver(this, receiver));
            receiverThread.start();

            // Connect to transaction server
            serverConnection = new Socket(serverIP, serverPort);

            // Send message of type OPEN_TRANSACTION, contains no content
            ObjectOutputStream toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            toServer.writeObject(new Message(OPEN_TRANSACTION, null));
            toServer.close();
            
            // Wait for response
            receiverThread.join();

            // Return transaction ID from response message content
            int transactionID = (int) recentMessage.getMessageContent();
            return transactionID;
        }
        catch (IOException | InterruptedException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Read balance from account specified by its ID.
     * 
     * Must call openTransaction before using this method.
     * 
     * @param accountNumber Integer ID of account.
     * @return Balance contained within account.
     */
    public int read(int accountNumber)
    {
        try
        {
            // Start receiver
            Thread receiverThread = new Thread(new ProxyReceiver(this, receiver));
            receiverThread.start();

            // Send message of type READ_REQUEST, contains account number from which to read
            ObjectOutputStream toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            toServer.writeObject(new Message(READ_REQUEST, accountNumber));
            toServer.close();

            // Wait for response
            receiverThread.join();

            // Return account balance from response message content
            int accountBalance = (int) recentMessage.getMessageContent();
            return accountBalance;
        }
        catch (IOException | InterruptedException ex)
        {
            ex.printStackTrace();
            return -999999;
        }
    }

    /**
     * Write balance to account specified by its ID.
     * 
     * Must call openTransaction before using this method.
     * 
     * @param accountNumber Integer ID of account.
     * @param balance Balance to write into account.
     */
    public void write(int accountNumber, int balance)
    {
        try
        {
            // Create accountID / balance hash map
            HashMap<Integer, Integer> accountWrite = new HashMap<Integer, Integer>();
            accountWrite.put(accountNumber, balance);

            // Send message of type WRITE_REQUEST, contains hash map of account number and
            // balance to be written into it
            ObjectOutputStream toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            toServer.writeObject(new Message(WRITE_REQUEST, accountWrite));
            toServer.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Close transaction and receive commit/abort result.
     * 
     * Must call openTransaction before using this method.
     * 
     * @return True if transaction was committed, false if aborted.
     */
    public boolean closeTransaction()
    {
        try
        {
            // Start receiver
            Thread receiverThread = new Thread(new ProxyReceiver(this, receiver));
            receiverThread.start();

            // Send message of type CLOSE_TRANSACTION, contains no content
            ObjectOutputStream toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            toServer.writeObject(new Message(CLOSE_TRANSACTION, null));
            toServer.close();

            // Wait for response
            receiverThread.join();

            // Close connection
            serverConnection.close();

            // Return transaction status from response message content
            boolean transactionResult = (boolean) recentMessage.getMessageContent();
            return transactionResult;
        }
        catch (IOException | InterruptedException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Used exclusively by ProxyReceiver to receive incoming message.
     * 
     * @param message Message to be received and saved.
     */
    protected void receiveMessage(Message message)
    {
        recentMessage = message;
    }
}
