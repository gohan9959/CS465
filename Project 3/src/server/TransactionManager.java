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
        // Create field objects
        this.accountManager = new AccountManager();
        this.committedTransactions = new ArrayList<Transaction>();

        // Set values to -1--this is more convenient for other methods than 0
        this.transactionID = -1;
        this.transactionNum = -1;
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

    /**
     * Open a transaction.
     * 
     * @return Newly created transaction.
     */
    public Transaction openTransaction()
    {
        int TID = generateTID();
        int TNUM = generateTNUM();
        Transaction newTransaction = new Transaction(TID, TNUM);

        return newTransaction;
    }

    /**
     * Tell account manager to read directly from account balance.
     * 
     * @param accountID Account ID.
     * @return Balance read from account.
     */
    public int readFromAccount(int accountID)
    {
        return accountManager.readAccountBalance(accountID);
    }

    /**
     * Verify that transaction can be committed.
     * 
     * @param newTransaction Transaction to be validated.
     * @return True if committed; false if aborted.
     */
    public synchronized boolean verify(Transaction newTransaction)
    {
        int transactionIndex, readIndex, readID;
        boolean successful = true;
        Transaction oldTransaction;

        // Consider all transactions with TNUM greater than new transaction's
        // TNUM but less than most recently committed TNUM
        for (transactionIndex = newTransaction.TNUM + 1;
                transactionIndex < transactionNum; transactionIndex++)
        {
            oldTransaction = committedTransactions.get(transactionIndex);

            // Check all accounts that were read from
            for (readIndex = 0; readIndex < newTransaction.readSet.size();
                    readIndex++)
            {
                // If read set overlaps with old write set, set failure flag
                readID = newTransaction.readSet.get(readIndex);

                if (oldTransaction.checkWriteSetOverlap(readID))
                {
                    successful = false;
                }
            }
        }

        if (successful)
        {
            commitTransaction(newTransaction);
        }

        return successful;
    }

    /**
     * Commit transaction by adding to list of completed transactions
     * and instructing AccountManager to update raw data.
     * 
     * @param transaction Transaction to be committed.
     */
    private synchronized void commitTransaction(Transaction transaction)
    {
        // Add to committed
        committedTransactions.add(transaction);

        // Increment transaction number
        ++transactionNum;

        // Commit write set
        transaction.writeSet.forEach((accountID, balance) ->
        {
            accountManager.writeAccountBalance(accountID, balance);
        });
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
