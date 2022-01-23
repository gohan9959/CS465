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

/**
 * TODO: Document this class
 */
public class Client {
    
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
      if(userinput.split(" ")[0].equals("JOIN")){
        clientSocket = new Socket(userinput.split(" ")[1], Integer.parseInt(userinput.split(" ")[2]));
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_JOIN, nodeinfo);
        new Thread(new Sender(clientSocket, message)).start();
        //Start reciever thread.
        clientSocket.close();
      }
      else if(userinput.equals("LEAVE")){
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_LEAVE, nodeinfo);
        new Thread(new Sender(clientSocket, message)).start();
        //Close reciever thread.
        clientSocket.close();
        serverSocket.close();
      }
      else if(userinput.equals("SHUTDOWN")){
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        NodeInfo nodeinfo = new NodeInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), logicalName);
        message = new Message(MessageTypes.TYPE_SHUTDOWN, nodeinfo);
        new Thread(new Sender(clientSocket, message)).start();
        //Close reciever thread.
        clientSocket.close();
        serverSocket.close();
        System.exit(0);
      }
      else{
        clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_DEFAULT_PORT);
        message = new Message(MessageTypes.TYPE_NOTE, userinput);
        new Thread(new Sender(clientSocket, message)).start();
        clientSocket.close();
      }
    }
  }

  public Socket acceptConnection() throws IOException {

    return serverSocket.accept();
  }

}