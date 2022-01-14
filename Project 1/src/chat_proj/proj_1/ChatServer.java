package proj_1;

import java.io.IOException;
import java.net.ServerSocket;

@SuppressWarnings("unused")
public class ChatServer {

  private static final int DEFAULT_PORT = 8881;

  private int port;

  private ServerSocket serverSocket;

  /**
   * 
   * 
   * @param port
   */
  public ChatServer(int port) {

    this.port = port;
  }

  /**
   * 
   */
  public ChatServer() {

    this.port = DEFAULT_PORT;
  }

  /**
   * 
   * 
   * @param args
   */

  public static void main(String[] args) {
    // TODO: Complete main method

  }

  /**
   * 
   * 
   * @throws IOException
   */
  public void runServerLoop() throws IOException {
    // TODO: Complete server loop
  }
}