package server;

import chat.Sender;
import chat.ServerThread;
import message.Message;
import message.MessageTypes;
import message.NodeInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * TODO: Document this class
 */
@SuppressWarnings("unused")
public class ChatServer {

  private static final int DEFAULT_PORT = 8881;

  /**
   * Port number on which the server socket is initialized.
   */
  private static int port;

  /**
   * Array list containing NodeInfo objects representing users
   * registered into the chat.
   */
  private static ArrayList<NodeInfo> registeredUsers;

  /**
   * ServerSocket object accepting connections to the server.
   */
  private static ServerSocket serverSocket;

  /**
   * Main method.
   * 
   * @param args Command line arguments.
   * @throws IOException I/O exception from ServerSocket initialization.
   */
  public static void main(String[] args) throws IOException {

    // Determine the server port based on the config file.
      // TODO: Write config parsing, initialize port.
    port = DEFAULT_PORT;

    // Initialize list of registered users with max capacity 25.
    registeredUsers = new ArrayList<NodeInfo>(25);

    serverSocket = new ServerSocket(port);

    System.out.format("Starting socket on IP %s", serverSocket.getInetAddress());

    runServerLoop();
  }

  /**
   * Run continuous server loop.
   */
  public static void runServerLoop() {

    while (true) {
      
      
    }
  }

  public static Socket acceptConnection() throws IOException {

    return serverSocket.accept();
  }

  /**
   * Add user to the list of registered users.
   * 
   * @param user NodeInfo object containing user info.
   */
  public static void joinUser(NodeInfo user) {

    // Add NodeInfo object to array
    registeredUsers.add(user);
  }

  /**
   * Remove user from list of registered users.
   * 
   * @param user NodeInfo object containing user info.
   */
  public static void leaveUser(NodeInfo user) {

    // Check each item in the list and remove if if matches up with
    // the requested user
    registeredUsers.removeIf((registeredUser) -> 
        registeredUser.isEqual(user));
  }

  /**
   * Send a note to all registered users.
   * 
   * @param note Message object containing the note to send out.
   */
  public static void sendNoteToAll(Message note) {

    // Lambda function which performs the specified action for each user
    registeredUsers.forEach((user) -> {

      try {

        // Establish socket connection to client
        Socket userSocket = new Socket(user.getIp(), user.getPort());

        // Alter note message to include the logical name of the user who
        // sent it
        // - Format: "<user>: <note>"
        String userNoteMessage = user.getLogicalName() + ": "
            + (String) note.getMessageContent();
        
        // Create altered note message to be sent
        Message userNote = new Message(
            MessageTypes.TYPE_NOTE, userNoteMessage);

        // Start Sender thread which sends the message
        new Thread(new Sender(userSocket, userNote)).start();
      }
      
      catch (IOException ioe) {

        ioe.printStackTrace();
      }
    });
  }
}