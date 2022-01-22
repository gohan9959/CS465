package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import message.Message;

/**
 * TODO: Document this class
 */
public class ReceiverWorker implements Runnable {

    /**
     * Socket object from which the message will be received.
     */
    private Socket socket;

    /**
     * Constructor.
     *
     * @param socket Socket object from which the message will be rceived.
     */
    public ReceiverWorker(Socket socket) {

        // Initialize object fields
        this.socket = socket;
    }

    /**
     * Run ReceiverWorker thread.
     */
    @Override
    public void run() {

        try {

            // Open socket input stream
            ObjectInputStream input = new ObjectInputStream(
                    socket.getInputStream());

            // Receive message object
            Message message = (Message) input.readObject();

            // Attempt to parse message
            processMessage(message);
        }

        catch (IOException ioe) {

            ioe.printStackTrace();
        }

        catch (ClassNotFoundException cfne) {

            cfne.printStackTrace();
        }
    }

    /**
     * // TODO: Document this method
     *
     * @param message
     */
    public void processMessage(Message message) {

        // TODO: Figure out what to do here
    }
}