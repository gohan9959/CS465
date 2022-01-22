package chat;

import java.io.IOException;
import java.net.Socket;

import server.ChatServer;

public class ServerReceiver implements Runnable {

  @Override
  public void run() {
    
    while (true) {

      try {

        Socket client = ChatServer.acceptConnection();
        (new Thread(new ReceiverWorker(client))).start();
      }

      catch (IOException ioe) {

        ioe.printStackTrace();
      }
    }
  }
}
