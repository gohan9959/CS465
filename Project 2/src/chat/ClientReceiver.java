package chat;

import message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientReceiver implements Runnable{

    private ServerSocket serverSocket;

    private Message message;

    public ClientReceiver(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        Socket socket;
        ObjectInputStream recieveStream;
        while(true){
            try{
                socket = serverSocket.accept(); 
                recieveStream = new ObjectInputStream(socket.getInputStream());
                message = (Message)recieveStream.readObject();
                if(message instanceof Message){
                    if(message.getType() == MessageTypes.JOIN){
                        ChatClient.receiveJoiningUser((NodeInfo)message.getMessageContent());
                    }
                    else if(message.getType() == MessageTypes.ADD){
                        ChatClient.addUser((NodeInfo)message.getMessageContent());
                    }
                    else if(message.getType() == MessageTypes.ADD_LIST){
                        //Placeholder for client ADDLIST method.
                    }
                    else if(message.getType() == MessageTypes.NOTE){
                        System.out.println(message.getName() + ": " + (String)message.getMessageContent());
                    }
                    else if(message.getType() == MessageTypes.LEAVE){
                        ChatClient.removeUser((NodeInfo)message.getMessageContent());
                    }
                    else if(message.getType() == MessageTypes.SHUTDOWN){
                        ChatClient.receiveShutdown();
                    }
                }
            } 
            catch (IOException e){
                e.printStackTrace();
            }
            catch(ClassNotFoundException cfe){
                cfe.printStackTrace();
            }
            
        }

    }
}
