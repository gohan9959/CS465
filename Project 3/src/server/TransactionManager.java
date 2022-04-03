package server;

import java.net.Socket;
import java.util.ArrayList;

import account.AccountManager;

public class TransactionManager
{
    /**
     * Account manager
     */
    private AccountManager accountManager;

    /**
     * List of completed transactions
     */
    private ArrayList<Transaction> transactions;

    /**
     * Transaction ID, or TID for short. Holds latest assigned transaction ID.
     */
    private int transactionID;

    /**
     * Transaction number, or TNUM for short. Holds index of latest committed
     * transaction.
     */
    private int transactionNum;

    public TransactionManager()
    {
        transactionID = -1;
        transactionNum = -1;
    }

    /**
     * Start transaction by forwarding client connection to worker thread.
     * 
     * @param clientConnection
     */
    public void startTransaction(Socket clientConnection)
    {
        new Thread(new TransactionManagerWorker(clientConnection)).start();
    }

    /**
     * Generate transaction ID, or TID for short.
     * 
     * @return New transaction ID.
     */
    public int generateTID()
    {
        return ++transactionID;
    }

    /**
     * Generate transaction number, or TNUM for short. TNUM is the index of the
     * most recently committed transaction.
     * 
     * @return New transaction number.
     */
    public int generateTNUM()
    {
        return transactionNum;
    }

    public void verify()
    {
        
    }

}
