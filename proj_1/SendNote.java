import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SendNote implements Runnable{
    
    private ObjectOutputStream output;
    private Socket socket;
    private Message message;
    private NodeInfo nodeinfo;

    public SendNote(NodeInfo nodeinfo, Message message){
        this.nodeinfo = nodeinfo;
        this.message = message;
    }

    public void run(){
        try{
            socket = new Socket(nodeinfo.getIp(), nodeinfo.getPort());
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(message);
            output.flush();
            output.close();
            //socket.close();
        } catch(IOException e){
            System.out.println("Error Sending Message to " + nodeinfo.getLogicalName());
        }
    }
    
}
