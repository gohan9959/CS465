package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import message.MessageTypes;
import utils.PropertyHandler;

/**
 * Main file responsible for all server funcitons including main server loop
 *
 * @author Harshith Shakelli
 */
public class TransactionServer implements MessageTypes
{
    private static int port;

    private static TransactionManager transactionManager;

    private static ServerSocket serverSocket;

    /**
     * Constructor
     *
     * @throws IOException
     */
    public TransactionServer() throws IOException
    {
        // get port from properties file and then open a socket
        PropertyHandler properties = new PropertyHandler("config/Server.properties");
        port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        serverSocket = new ServerSocket(port);

        // create a transaction manager to be used by server
        transactionManager = new TransactionManager();
    }

    /**
     * main loop in which server manages connections
     *
     * @throws IOException
     */
    public void runServerLoop() throws IOException
    {
        Socket clientConnection;

        System.out.println("Transaction server initiated\n" +
                           "Accepting incoming transactions\n" +
                           "===============================\n");

        // accept connections and then manage incoming transactions
        while(true)
        {
            clientConnection = serverSocket.accept();
            transactionManager.startWorker(clientConnection);
        }
    }

    /**
     * Initiates constructor and attempts to start server loop
     *
     * @param args standard java arguments
     */
    public static void main(String[] args)
    {
        try
        {
            TransactionServer transactionServer = new TransactionServer();
            transactionServer.runServerLoop();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}