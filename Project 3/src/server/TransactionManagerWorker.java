package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import message.Message;
import message.MessageTypes;

public class TransactionManagerWorker implements Runnable
{
    private Socket socket;
    private ObjectInputStream recieveAction;

    public TransactionManagerWorker(Socket socket){
        this.socket = socket;
    }

    public void run(){
        Message transactionMessage;
        try{
            recieveAction = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        while(true){
            try{
                transactionMessage = (Message) recieveAction.readObject();
                if(transactionMessage.getMessageType() == MessageTypes.OPEN_TRANSACTION){
                    //TODO Call TIDgen, TNUMgen.
                    //TODO Create Trasaction Object.
                    //TODO Store Transaction Object.
                    //TODO Return Transaction ID.
                    
                }
                else if(transactionMessage.getMessageType() == MessageTypes.READ_REQUEST){
                    //TODO Call Account Manger Function.
                    //TODO Create new transaction object, delete old one from arraylist and add the new one.
                }
                else if(transactionMessage.getMessageType() == MessageTypes.WRITE_REQUEST){
                    //TODO Call Account Manager Function.
                    //TODO Update values in accounts object in transaction object.

                }
                else if(transactionMessage.getMessageType() == MessageTypes.CLOSE_TRANSACTION){
                    //TODO Call Transaction Manager Verify.
                    //TODO Send Status of the transaction.
                    //TODO Delete transaction from list.
                    break;
                }
            }
            catch (IOException | ClassNotFoundException ignored)
            {
            }
        }
    }
}
