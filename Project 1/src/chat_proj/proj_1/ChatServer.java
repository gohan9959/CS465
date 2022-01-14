package proj_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * TODO: Document this class
 */
@SuppressWarnings("unused")
public class ChatServer {

  private static final int DEFAULT_PORT = 8881;

  private static int port;

  private static ArrayList<NodeInfo> registeredUsers;

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

    runServerLoop();
  }

  /**
   * Run continuous server loop.
   */
  public static void runServerLoop() {

    // TODO: Complete server loop
  }

  /**
   * Add user to the list of registered users.
   * 
   * @param user NodeInfo object containing user info.
   */
  public void registerUser(NodeInfo user) {

    // TODO
  }

  /**
   * Send a note to all registered users.
   * 
   * @param note Message object containing the note to send out.
   */
  public void sendNoteToAll(Message note) {

    // TODO
  }
}