package server;

import chat.ServerThread;
import message.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    System.out.format("Starting socket on IP %s", serverSocket.getInetAddress());

    runServerLoop();
  }

  /**
   * Run continuous server loop.
   */
  public static void runServerLoop() {

    Socket newClient;
    Thread newThread;

    while (true) {

      try {

        newClient = serverSocket.accept();
        System.out.println("\nClient Connected!");
        newThread = new Thread(new ServerThread(newClient));
        newThread.start();

      }
      
      catch (IOException ioe) {

        ioe.printStackTrace();
      }
    }
  }

  /**
   * Add user to the list of registered users.
   * 
   * @param user NodeInfo object containing user info.
   */
  public void joinUser(NodeInfo user) {

    // TODO
    registeredUsers.add(user);
    System.out.println(user.getLogicalName() + " Added Succesfully!");
    return;
  }

  /**
   * Remove user from list of registered users.
   * 
   * @param user NodeInfo object containing user info.
   */
  public void leaveUser(NodeInfo user) {

    // TODO
    for(int i = 0; i < registeredUsers.size(); i++){
      if(user.getLogicalName() == registeredUsers.get(i).getLogicalName()){
        registeredUsers.remove(i);
        break;
      }
    }
    return;
  }

  /**
   * Send a note to all registered users.
   * 
   * @param note Message object containing the note to send out.
   */
  public void sendNoteToAll(Message note) {

    // TODO
    for(int i = 0; i < registeredUsers.size(); i++){
      new Thread(new SendNote(registeredUsers.get(i), note)).start();
    }
  }
}