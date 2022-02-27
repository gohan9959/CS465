package chat;

import message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientReceiver implements Runnable{

    private ServerSocket serverSocket;

    private Message message;

    private NodeInfo userInfo;

    public ClientReceiver(ServerSocket serverSocket, NodeInfo userInfo){
        this.serverSocket = serverSocket;
        this.userInfo = userInfo;
    }

    public void run(){
        Socket socket;
        ObjectInputStream recieveStream;
        boolean leave = false;
        while(!leave){
            try{
                socket = serverSocket.accept(); 
                recieveStream = new ObjectInputStream(socket.getInputStream());
                message = (Message)recieveStream.readObject();
                if(message instanceof Message){
                    if(message.getMessageType() == MessageTypes.JOIN){
                        ChatClient.receiveJoiningUser((NodeInfo)message.getMessageContent());
                    }
                    else if(message.getMessageType() == MessageTypes.ADD){
                        ChatClient.addUser((NodeInfo)message.getMessageContent());
                    }
                    else if(message.getMessageType() == MessageTypes.ADD_LIST){
                        //Placeholder for client ADDLIST method.
                    }
                    else if(message.getMessageType() == MessageTypes.NOTE){
                        System.out.println(message.getSender() + ": " + (String)message.getMessageContent());
                    }
                    else if(message.getMessageType() == MessageTypes.LEAVE){
                        if(message.getSender() == userInfo.getLogicalName()){
                            leave = true;
                        }
                        else{
                            ChatClient.removeUser((NodeInfo)message.getMessageContent());
                        }
                    }
                    else if(message.getMessageType() == MessageTypes.SHUTDOWN){
                        ChatClient.receiveShutdown();
                        leave = true;
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
