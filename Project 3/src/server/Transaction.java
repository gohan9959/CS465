package server;

import java.util.ArrayList;
import java.util.HashMap;

public class Transaction {
    
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

    public int getTID()
    {
        return TID;
    }

    public int getTNUM()
    {
        return TNUM;
    }

    /**
     * Attempt to read account balance from write set.
     * 
     * @param accountID ID of account to be read.
     * @return Account ID / balance values, or null if not found.
     */
    public int attemptToRead(int accountID)
    {
        return writeSet.get(accountID);
    }
}
