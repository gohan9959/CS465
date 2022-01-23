package chat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;

public class Sender extends Thread {

    private Socket socket;
    private Message message;

    public Sender(Socket socket, Message message){
        this.socket = socket;
        this.message = message; 
    }

    public void run(){
        try{
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
        oos.close();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

    }
}
