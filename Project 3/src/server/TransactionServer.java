package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import message.MessageTypes;
import utils.PropertyHandler;

public class TransactionServer implements MessageTypes
{
    private static int port;

    private static TransactionManager transactionManager;

    private static ServerSocket serverSocket;

    public TransactionServer() throws IOException
    {
        PropertyHandler properties = new PropertyHandler("Server.properties");
        port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        serverSocket = new ServerSocket(port);

        transactionManager = new TransactionManager();
    }

    public void runServerLoop() throws IOException
    {
        Socket clientConnection;

        while(true)
        {
            clientConnection = serverSocket.accept();
            transactionManager.startWorker(clientConnection);
        }
    }
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