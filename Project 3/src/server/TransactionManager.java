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
    private ArrayList<Transaction> committedTransactions;

    /**
     * Transaction ID, or TID for short. Holds latest assigned transaction ID.
     */
    private int transactionID;

    /**
     * Transaction number, or TNUM for short. Holds index of latest committed
     * transaction.
     */
    private int transactionNum;

    /**
     * Constructor.
     */
    public TransactionManager()
    {
        // Set values to -1--this is more convenient for other methods than 0
        transactionID = -1;
        transactionNum = -1;
    }

    /**
     * Start transaction by forwarding client connection to worker thread.
     * 
     * @param clientConnection
     */
    public void startWorker(Socket clientConnection)
    {
        new Thread(new TransactionManagerWorker(this, clientConnection)).start();
    }

    public Transaction openTransaction()
    {
        int TID = generateTID();
        int TNUM = generateTNUM();
        Transaction newTransaction = new Transaction(TID, TNUM);

        return newTransaction;
    }

    public int readFromAccount(int accountID)
    {
        return accountManager.readAccountBalance(accountID);
    }

    public void verify()
    {
        
    }

    /**
     * Generate transaction ID, or TID for short.
     * 
     * @return New transaction ID.
     */
    private int generateTID()
    {
        return ++transactionID;
    }

    /**
     * Generate transaction number, or TNUM for short. TNUM is the index of the
     * most recently committed transaction.
     * 
     * @return New transaction number.
     */
    private int generateTNUM()
    {
        return transactionNum;
    }

}
