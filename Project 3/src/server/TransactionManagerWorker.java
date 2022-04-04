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
    /**
     * Socket connection to client
     */
    private Socket clientConnection;

    /**
     * Stream for receiving messages from client
     */
    private ObjectInputStream receiveAction;

    /**
     * Stream for sending response messages
     */
    private ObjectOutputStream sendResponse;

    /**
     * Transaction manager
     */
    private TransactionManager transactionManager;

    /**
     * Transaction which this thread is working with
     */
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

    /**
     * Listen for transaction actions requested by client. Runtime ends when
     * client decides to close the transaction.
     */
    public void run()
    {
        boolean transactionClosed = false;
        Message transactionMessage, responseMessage;
        int messageType;
        int accountID;
        Integer balance;
        HashMap<?, ?> writeRequestContent;
        boolean commitResult;

        // Loop through transaction actions
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

                    System.out.printf("[TransactionManagerWorker] Transaction #%d - OPEN_TRANSACTION\n",
                                      transaction.TID);
                    
                }
                else if (messageType == MessageTypes.READ_REQUEST)
                {
                    // Attempt to read from write set
                    accountID = (int) transactionMessage.getMessageContent();
                    balance = transaction.attemptToRead(accountID);

                    // Check for failed write
                    if (balance == null)
                    {
                        // Read from account
                        balance = transactionManager.readFromAccount(accountID);

                        // Update read set
                        transaction.readSet.add(accountID);
                    }

                    // Send amount read
                    responseMessage = new Message(READ_REQUEST, balance);
                    sendResponse.writeObject(responseMessage);

                    System.out.printf("[Transaction Worker] Transaction #%d - READ_REQUEST > Account #%d\n",
                                      transaction.TID, accountID);

                }
                else if (messageType == MessageTypes.WRITE_REQUEST)
                {
                    // For each (one) write request, add values into write set
                    writeRequestContent = (HashMap<?, ?>)
                            transactionMessage.getMessageContent();
                    writeRequestContent.forEach((requestID, requestBal) ->
                    {
                        transaction.writeSet.put((int) requestID, (int) requestBal);
                    });

                    System.out.printf("[TransactionManagerWorker] Transaction #%d - WRITE_REQUEST > Account #%d, " +
                                      "balance $%d\n", transaction.TID, 1, 1);// requestID, requestBal);
                }
                else if (messageType == MessageTypes.CLOSE_TRANSACTION)
                {
                    // Verify transaction
                    commitResult = transactionManager.verify(transaction);

                    System.out.printf("[TransactionManagerWorker] Transaction #%d - CLOSED_TRANSACTION - ",
                                      transaction.TID);

                    // Send commit result
                    if (commitResult)
                    {
                        messageType = TRANSACTION_COMMITTED;

                        System.out.println("COMMITTED\n");
                    }
                    else
                    {
                        messageType = TRANSACTION_ABORTED;

                        System.out.println("ABORTED\n");
                    }
                    responseMessage = new Message(messageType, commitResult);
                    sendResponse.writeObject(responseMessage);

                    // Set closed transaction flag
                    transactionClosed = true;
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }

        // Close connection
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
