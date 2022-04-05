package server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hold transactional data, such as read set, write set, and most recently
 * committed transaction number for verification purposes.
 *
 * @author Harshith Shakelli
 */
public class Transaction
{
    /**
     * Transaction ID
     */
    protected int TID;

    /**
     * Transaction number--number of most recently committed transaction
     */
    protected int TNUM;
    
    /**
     * Read set of transaction, containing IDs of all accounts read
     */
    protected ArrayList<Integer> readSet;

    /**
     * Write set of transaction, containing pairs of values as follows:
     * <ID of account written, value written into account>
     */
    protected HashMap<Integer, Integer> writeSet;

    /**
     * Constructor.
     * 
     * @param TID Transaction ID.
     * @param TNUM Transaction number--index of most recently committed
     * transaction.
     */
    public Transaction(int TID, int TNUM)
    {
        this.TID = TID;
        this.TNUM = TNUM;

        readSet = new ArrayList<Integer>();
        writeSet = new HashMap<Integer, Integer>();
    }

    /**
     * Get transaction ID.
     * 
     * @return Transaction ID.
     */
    public int getTID()
    {
        return TID;
    }

    /**
     * Get transaction number--index of the last committed transaction when
     * this one was opened.
     * 
     * @return Transaction number.
     */
    public int getTNUM()
    {
        return TNUM;
    }

    /**
     * Attempt to read account balance from write set.
     * 
     * @param accountID ID of account to be read.
     * @return Balance value from write set, or null if not found.
     */
    public Integer attemptToRead(int accountID)
    {
        if (writeSet.containsKey(accountID))
        {
            return writeSet.get(accountID);
        }

        return null;
    }

    /**
     * Check if requested account was modified in this transaction's write
     * set.
     * 
     * @param accountID Account ID.
     * @return True if specified account is in write set; false otherwise.
     */
    public boolean checkWriteSetOverlap(int accountID)
    {
        return writeSet.containsKey(accountID);
    }
}
