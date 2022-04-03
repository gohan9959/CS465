package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import message.Message;
import message.MessageTypes;

public class TransactionManagerWorker implements Runnable, MessageTypes
{
    private Socket clientConnection;

    private ObjectInputStream receiveAction;

    private ObjectOutputStream sendResponse;

    private TransactionManager transactionManager;

    private Transaction transaction;

    /**
     * Constructor. Requires parent transaction manager and client socket.
     * 
     * @param transactionManager Parent transaction manager.
     * @param socket Socket which is connected to client.
     */
    public TransactionManagerWorker(TransactionManager transactionManager,
            Socket socket)
    {
        try
        {
            // Initialize input and output streams
            this.clientConnection = socket;
            this.receiveAction = new ObjectInputStream(socket.getInputStream());
            this.sendResponse = new ObjectOutputStream(socket.getOutputStream());
            
            // Set other fields
            this.transactionManager = transactionManager;
            this.transaction = null;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void run()
    {
        boolean transactionClosed = false;
        Message transactionMessage, responseMessage;
        int messageType;
        int accountID;
        Integer balance;
        HashMap<Integer, Integer> writeRequestContent;

        while (!transactionClosed)
        {
            try
            {
                transactionMessage = (Message) receiveAction.readObject();
                messageType = transactionMessage.getMessageType();

                if(messageType == MessageTypes.OPEN_TRANSACTION)
                {
                    // Call for new transaction
                    transaction = transactionManager.openTransaction();

                    // Send transaction ID
                    responseMessage = new Message(OPEN_TRANSACTION, transaction.TID);
                    sendResponse.writeObject(responseMessage);
                    
                }
                else if (messageType == MessageTypes.READ_REQUEST)
                {
                    // Attempt to read from write set
                    accountID = (int) transactionMessage.getMessageContent();
                    balance = transaction.attemptToRead(accountID);

                    // Check for failed write
                    if (balance != null)
                    {
                        // Read from account
                        balance = transactionManager.readFromAccount(accountID);

                        // Update read set
                        transaction.readSet.add(accountID);
                    }

                    // Send amount read
                    responseMessage = new Message(READ_REQUEST, balance);
                    sendResponse.writeObject(responseMessage);
                }
                else if (messageType == MessageTypes.WRITE_REQUEST)
                {
                    // For each (one) write request, add values into write set
                    writeRequestContent = (HashMap<Integer, Integer>)
                            transactionMessage.getMessageContent();
                    writeRequestContent.forEach((requestID, requestBal) ->
                    {
                        transaction.writeSet.put(requestID, requestBal);
                    });
                }
                else if (messageType == MessageTypes.CLOSE_TRANSACTION)
                {
                    //TODO Call Transaction Manager Verify.
                    //TODO Send Status of the transaction.
                    //TODO Delete transaction from list.
                    transactionClosed = true;
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }

        try
        {
            clientConnection.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
