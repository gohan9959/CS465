package chat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;

/**
 * This Sender class, given a Socket and Message, will run a thread which
 * sends a message to the socket and then immediately closes the connection.
 * Can send from server to client or from client to server.
 * 
 * @author Conrad Murphy
 */
public class Sender implements Runnable {
  
  /**
   * Socket object to which the message will be sent.
   */
  private Socket socket;

  /**
   * Message object to send to the receiving socket.
   */
  private Message message;

  public Sender(Socket socket, Message message) {
    
    // Initialize object variables
    this.socket = socket;
    this.message = message;
  }

  @Override
  public void run() {

    try {

      // Open stream
      ObjectOutputStream output = new ObjectOutputStream(
          socket.getOutputStream());
      output.writeObject(message);

      // Close stream
      output.flush();
      output.close();

      // Close connection
      socket.close();
    }

    catch (IOException ioe) {

      ioe.printStackTrace();
    }
  }
}
