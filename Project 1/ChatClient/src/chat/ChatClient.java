package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO: Document this class
 */
public class ChatClient {

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {

        serverSocket = new ServerSocket(8881);

        while (true) {
            // TODO: Implement listening and receiving threads

        }
    }

    public Socket acceptConnection() throws IOException {

        return serverSocket.accept();
    }

}