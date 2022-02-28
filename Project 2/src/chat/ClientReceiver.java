package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import message.Message;
import message.MessageTypes;
import message.NodeInfo;


public class ClientReceiver implements Runnable, MessageTypes{

    private ServerSocket serverSocket; // Server socket to recieve connection.

    private Message message; // Message object to store recieved messages.

    private NodeInfo userInfo; // NodeInfo object to store user information.

    public ClientReceiver(ServerSocket serverSocket, NodeInfo userInfo){
        this.serverSocket = serverSocket; // Storing the ChatCilent created server socket in the server socket variable.
        this.userInfo = userInfo; // Soring the user info created in the ChatCilent.
    }

    public void run(){
        Socket socket; // Socket to store accepted connections.
        ObjectInputStream recieveStream; // ObjectInputStream object to recieve objects from sender.
        boolean leave = false; // Flag to check if the user has left.

        while(!leave){ // While the user is still in chat continue to loop. 
            try{ 
                socket = serverSocket.accept(); // Trying to accept connections.
                recieveStream = new ObjectInputStream(socket.getInputStream()); // Tring to recieve object from accepted socket.
                message = (Message)recieveStream.readObject(); // Trying to read the object recieved.
                if(message instanceof Message){ // Checking if the recieved object is of type Message or not.
                    if(message.getMessageType() == JOIN){ // Checking if the message type is JOIN.
                        ChatClient.receiveJoiningUser((NodeInfo)message.getMessageContent()); // If it is we are calling the recieveJoiningUser method from ChatClient.
                    }
                    else if(message.getMessageType() == ADD){ // Checking if the message type is ADD.
                        ChatClient.addUser((NodeInfo)message.getMessageContent()); // If it is we are calling the addUser method from ChatClient.
                    }
                    else if(message.getMessageType() == ADD_LIST){ // Checking if the message type is ADD_LIST.
                        ChatClient.addList((ArrayList<NodeInfo>) message.getMessageContent()); // If it is we are calling the addList method from ChatClient.
                    }
                    else if(message.getMessageType() == NOTE){ // Checking if the message type is NOTE.
                        System.out.println(message.getSender() + ": " + (String)message.getMessageContent()); // If it is we are printing the message with name of the user who sent it.
                    }
                    else if(message.getMessageType() == LEAVE){ // Checking if the message type is LEAVE.
                        if(message.getSender().equals(userInfo.getLogicalName())){ // Cheking if the leave message is ours.
                            leave = true; // If it is then we are setting the leave flag to true so that the thread ends.
                        }
                        ChatClient.removeUser((NodeInfo)message.getMessageContent()); // Calling removeUser method from ChatClient.
                    }
                    else if(message.getMessageType() == SHUTDOWN){ // Checking if the message type is SHUTDOWN.
                        ChatClient.receiveShutdown(); // If it is we are calling the recieveShutdown method from ChatClient.
                        leave = true; // Setting the leave flag to true so that the thread ends.
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
