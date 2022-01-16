import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
/**
 * TODO: Document this class
 * */
public class ChatClient {
  // TODO: Implement this class
  private static Socket socket;

  public static void main(String[] args) throws IOException{
    socket = new Socket(InetAddress.getLocalHost(), 8881);
    System.out.println("Connected To The Server!");
    ObjectInputStream ois;
    ObjectOutputStream oos;
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    Message message;
    String str;
    NodeInfo NI = new NodeInfo(socket.getLocalAddress().getHostAddress(), socket.getPort(), "Harhon");
    System.out.println("NodeInfo Object Created!");
    message = new Message(MessageTypes.TYPE_JOIN, NI);
    oos = new ObjectOutputStream(socket.getOutputStream());
    oos.writeObject(message);
    oos.flush();
    oos.close();
    System.out.println("NodeInfo Object Sent!");
    try{
      ois = new ObjectInputStream(socket.getInputStream());
      message = (Message) ois.readObject();
      ois.close();
      str = (String) message.getContent();
      System.out.println(str);
    } catch (Exception e){
      System.out.println(e);
    }
    while(true){
      //socket = new Socket(InetAddress.getLocalHost(), 8881);
      System.out.println("Enter Message to send: ");
      str = bf.readLine();
      message = new Message(MessageTypes.TYPE_NOTE, str);
      oos.writeObject(message);
      try{
        ois = new ObjectInputStream(socket.getInputStream());
        message = (Message) ois.readObject();
        ois.close();
        str = (String) message.getContent();
        System.out.println(str);
      } catch (Exception e){
        System.out.println(e);
      }
    }
  }
}