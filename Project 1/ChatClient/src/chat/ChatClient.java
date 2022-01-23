package chat;

import message.Message;
import message.MessageTypes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * TODO: Document this class
 */
public class ChatClient
{

    private static final int USER_DEFAULT_PORT = 8882;
    private static final int SERVER_DEFAULT_PORT = 8881;

    public static void main(String[] args) throws IOException
    {

        // Local variables
        String logicalName;
        ServerSocket serverSocket = new ServerSocket(USER_DEFAULT_PORT);
        Scanner read = new Scanner(System.in);

        System.out.print("Enter Name: ");
        logicalName = read.nextLine();

        // main chat loop
        boolean active = true;
        while (active)
        {
            // TODO: Implement listening and receiving threads

            // Local variables
            Socket clientSocket;
            String userInput;
            Message message;

            System.out.print("Enter Message: ");
            userInput = read.nextLine();


            // Handles JOIN Message
            if (userInput.split(" ")[0].equals("JOIN"))
            {

                clientSocket = new Socket(userInput.split(" ")[1], Integer.parseInt(userInput.split(" ")[2]));
                NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(),
                                                 serverSocket.getLocalPort(), logicalName);
                message = new Message(MessageTypes.TYPE_JOIN, nodeinfo);

                //Start sender thread.
                //Start receiver thread.
            }

            // Handles LEAVE Message
            else if (userInput.equals("LEAVE"))
            {
                clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
                NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(),
                                                 serverSocket.getLocalPort(), logicalName);
                message = new Message(MessageTypes.TYPE_LEAVE, nodeinfo);

                //Start sender thread.
                //Close receiver thread.

                serverSocket.close();
            }

            // Handles SHUTDOWN Message
            else if (userInput.equals("SHUTDOWN"))
            {
                clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
                NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(),
                                                 serverSocket.getLocalPort(), logicalName);
                message = new Message(MessageTypes.TYPE_SHUTDOWN, nodeinfo);

                //Start sender thread.
                //Close receiver thread.

                serverSocket.close();
                active = false;
            }

            // Handles NOTE Message
            else
            {
                clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
                message = new Message(MessageTypes.TYPE_NOTE, userInput);

                //Start sender thread.

            }
            clientSocket.close();

        }
    }

}