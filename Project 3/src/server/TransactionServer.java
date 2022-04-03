package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TransactionServer
{
    private static int port;
    private static ServerSocket serverSocket;

    public void runTransaction() throws IOException {
        port = 8080;
        serverSocket = new ServerSocket(port);
        Socket socket;
        while(true){
            socket = serverSocket.accept();
            new Thread(new TransactionManagerWorker(socket)).start();
        }
    }
    public static void main(String[] args)
    {
        TransactionServer transactionServer = new TransactionServer();
        try{
        transactionServer.runTransaction();
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}