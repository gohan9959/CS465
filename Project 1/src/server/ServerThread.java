package server;

import message.Message;
import message.MessageTypes;
import message.NodeInfo;

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
    System.out.println("ServerThread Started!");
    try {
      input = new ObjectInputStream(clientSocket.getInputStream());

      message = (Message) input.readObject();

      input.close();

      switch (message.getMessageType()) {

        case MessageTypes.TYPE_JOIN: {
          ChatServer.joinUser((NodeInfo)message.getMessageContent());
        }

        case MessageTypes.TYPE_LEAVE: {
          ChatServer.leaveUser((NodeInfo)message.getMessageContent());
        }

        case MessageTypes.TYPE_NOTE: {
          ChatServer.sendNoteToAll((Message)message);

        }

        case MessageTypes.TYPE_SHUTDOWN: {
          ChatServer.shutDown();
        }

        default: {
          System.out.println("Unknown Message Type");
        }
      }
    }

    catch (IOException ioe) {

      ioe.printStackTrace();
    }

    catch (ClassNotFoundException cnfe) {

      cnfe.printStackTrace();
    }

    try {
      clientSocket.close();
    }

    catch (IOException ioe) {

      ioe.printStackTrace();
    }
  }
  
}
