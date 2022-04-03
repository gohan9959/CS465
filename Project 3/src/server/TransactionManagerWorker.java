package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;
import message.MessageTypes;

public class TransactionManagerWorker implements Runnable
{
    private Socket clientConnection;

    private ObjectInputStream receiveAction;

    private ObjectOutputStream sendResponse;

    public TransactionManagerWorker(Socket socket)
    {
        try
        {
            // Initialize input and output streams
            this.clientConnection = socket;
            this.receiveAction = new ObjectInputStream(socket.getInputStream());
            this.sendResponse = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void run()
    {
        boolean transactionClosed = false;
        Message transactionMessage;
        int messageType;

        while (!transactionClosed)
        {
            try
            {
                transactionMessage = (Message) receiveAction.readObject();
                messageType = transactionMessage.getMessageType();

                if(messageType == MessageTypes.OPEN_TRANSACTION)
                {
                    //TODO Call TIDgen, TNUMgen.
                    //TODO Create Trasaction Object.
                    //TODO Store Transaction Object.
                    //TODO Return Transaction ID.
                    
                }
                else if (messageType == MessageTypes.READ_REQUEST)
                {
                    //TODO Call Account Manger Function.
                    //TODO Create new transaction object, delete old one from arraylist and add the new one.
                }
                else if (messageType == MessageTypes.WRITE_REQUEST)
                {
                    //TODO Call Account Manager Function.
                    //TODO Update values in accounts object in transaction object.

                }
                else if (messageType == MessageTypes.CLOSE_TRANSACTION)
                {
                    //TODO Call Transaction Manager Verify.
                    //TODO Send Status of the transaction.
                    //TODO Delete transaction from list.
                    break;
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
