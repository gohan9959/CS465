package chat;

import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;
import message.MessageTypes;
import message.NodeInfo;

/**
 * TODO: Document this class
 */
public class ChatClient {
    
  private static ServerSocket serverSocket;
  private static Socket clientSocket;
  private static final int USER_DEFAULT_PORT = 8882;
  private static final int SERVER_DEFAULT_PORT = 8881;

  public static void main(String[] args) throws IOException {
    Scanner read = new Scanner(System.in);
    Message message;
    String userinput;
    String logicalName;
    serverSocket = new ServerSocket(USER_DEFAULT_PORT);
    System.out.print("Enter Name: ");
    logicalName = read.nextLine();
    while (true) {
      // TODO: Implement listening and receiving threads
      System.out.print("Enter Message: ");
      userinput = read.nextLine();
      if(userinput == "JOIN"){
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_JOIN, nodeinfo);
        //Start sender thread.
        //Start reciever thread.
        clientSocket.close();
      }
      else if(userinput == "LEAVE"){
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_LEAVE, nodeinfo);
        //Start sender thread.
        //Close reciever thread.
        clientSocket.close();
        serverSocket.close();
      }
      else if(userinput == "SHUTDOWN"){
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_SHUTDOWN, nodeinfo);
        //Start sender thread.
        //Close reciever thread.
        clientSocket.close();
        serverSocket.close();
      }
      else{
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        message = new Message(MessageTypes.TYPE_NOTE, userinput);
        //Start sender thread.
        clientSocket.close();
      }
    }
  }

  public Socket acceptConnection() throws IOException {

    return serverSocket.accept();
  }

}