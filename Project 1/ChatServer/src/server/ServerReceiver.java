package server;

import java.io.IOException;
import java.net.Socket;

import chat.ReceiverWorker;

public class ServerReceiver implements Runnable {

    @Override
    public void run() {

        while (true) {

            try {

                Socket client = ChatServer.acceptConnection();
                (new Thread(new ReceiverWorker(client))).start();
            } catch (IOException ioe) {

                ioe.printStackTrace();
            }
        }
    }
}