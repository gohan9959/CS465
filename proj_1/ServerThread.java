import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


/**
 * TODO: Document this class
 */
public class ServerThread implements Runnable {

  private Socket clientSocket;
  private Message outmessage;

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
    ChatServer chatServer = new ChatServer();
    System.out.println("ServerThread Started!");
    while(true){
    try {
      System.out.println("In Try!");
      input = new ObjectInputStream(clientSocket.getInputStream());
      message = (Message) input.readObject();
      input.close();
      System.out.println("Message Object Recieved!");

      switch (message.getMessageType()) {

        case MessageTypes.TYPE_JOIN: {
          if(message.getContent() instanceof NodeInfo){
            chatServer.joinUser((NodeInfo)message.getContent());
            outmessage = new Message(MessageTypes.TYPE_NOTE, "Joined Server Successfully!");
            new Thread(new SendNote((NodeInfo)message.getContent(), outmessage)).start();;
            //clientSocket.close();
          }
        }

        case MessageTypes.TYPE_LEAVE: {
          if(message.getContent() instanceof NodeInfo){
            chatServer.leaveUser((NodeInfo)message.getContent());
            outmessage = new Message(MessageTypes.TYPE_NOTE, "Left Server Successfully!");
            new Thread(new SendNote((NodeInfo)message.getContent(), outmessage)).start();
            //clientSocket.close();
          }
        }

        case MessageTypes.TYPE_NOTE: {
          chatServer.sendNoteToAll(message);
          //clientSocket.close();
        }

        case MessageTypes.TYPE_SHUTDOWN: {

        }

        case MessageTypes.TYPE_SHUTDOWN_ALL: {

        }

        default: {

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
  
}
