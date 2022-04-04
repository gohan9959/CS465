package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import message.Message;
import message.MessageTypes;
import utils.PropertyHandler;

/**
 * Proxy object which sends messages to transaction server. Requires access
 * to server properties to connect to server.
 */
public class Proxy implements MessageTypes
{
    /**
     * Sending side of connection to server
     */
    private Socket serverConnection;

    /**
     * Outbound message stream to server
     */
    private ObjectOutputStream toServer;

    /**
     * Inbound message stream to client
     */
    private ObjectInputStream fromServer;

    /**
     * Constructor.
     * 
     * Sets fields to dummy values. These fields are initialized upon calling
     * openTransaction.
     */
    public Proxy()
    {
        // Set fields to null values
        this.serverConnection = null;
        this.toServer = null;
        this.fromServer = null;
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
            PropertyHandler serverProperties = new PropertyHandler("config/Server.properties");
            String serverIP = serverProperties.getProperty("SERVER_IP");
            int serverPort = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));

            // Connect to transaction server
            serverConnection = new Socket(serverIP, serverPort);
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            fromServer = new ObjectInputStream(serverConnection.getInputStream());

            // Send message
            toServer.writeObject(new Message(OPEN_TRANSACTION, null));

            // Receive message as response
            Message response = (Message) fromServer.readObject();

            // Return transaction ID from response message content
            int transactionID = (int) response.getMessageContent();
            return transactionID;
        }
        catch (IOException | ClassNotFoundException ex)
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
            // Send message of type READ_REQUEST, contains account number from which to read
            toServer.writeObject(new Message(READ_REQUEST, accountNumber));

            // Wait for response
            Message response = (Message) fromServer.readObject();

            // Return account balance from response message content
            int accountBalance = (int) response.getMessageContent();
            return accountBalance;
        }
        catch (IOException | ClassNotFoundException ex)
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
            toServer.writeObject(new Message(WRITE_REQUEST, accountWrite));
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
            // Send message of type CLOSE_TRANSACTION, contains no content
            toServer.writeObject(new Message(CLOSE_TRANSACTION, null));

            // Wait for response
            Message response = (Message) fromServer.readObject();

            // Return transaction status from response message content
            boolean transactionResult = (boolean) response.getMessageContent();
            return transactionResult;
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
