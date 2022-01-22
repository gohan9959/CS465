package chat;

import message.Message;
import message.MessageTypes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * TODO: Document this class
 */
public class ServerThread implements Runnable {

    private Socket clientSocket;

    /**
     * TODO: Document
     *
     * @param clientSocket
     */
    public ServerThread(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    /**
     * TODO
     */
    @Override
    public void run() {

        ObjectInputStream input;
        Message message;

        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            // clientSocket.getOutputStream();

            message = (Message) input.readObject();

            input.close();

            switch (message.getMessageType()) {

                case MessageTypes.TYPE_JOIN: {

                }

                case MessageTypes.TYPE_LEAVE: {

                }

                case MessageTypes.TYPE_NOTE: {

                }

                case MessageTypes.TYPE_SHUTDOWN: {

                }

                default: {

                }
            }
        } catch (IOException ioe) {

            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {

            cnfe.printStackTrace();
        }

        try {
            clientSocket.close();
        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }

}
