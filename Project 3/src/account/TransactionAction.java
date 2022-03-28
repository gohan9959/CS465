package account;

public class TransactionAction
{
    /**
     * Symbolic constants for read mode and write mode.
     */
    public static final boolean READ = true;
    public static final boolean WRITE = false;
    
    /**
     * Type of action, either read mode or write mode.
     */
    private boolean actionType;

    /**
     * Amount to be added to the specified account, if in write mode. This
     * value is ignored in read mode.
     */
    private int balanceAdded;

    /**
     * Account on which the action will be performed.
     */
    private Account targetedAccount;

    /**
     * Read mode constructor. Set action type to read mode and fill in a dummy
     * value for balance added.
     * 
     * @param targetedAccount Account from which balance will be read.
     */
    public TransactionAction(Account targetedAccount)
    {
        this.actionType = READ;
        this.targetedAccount = targetedAccount;
        this.balanceAdded = 0;
    }

    /**
     * Write mode constructor. Set action type to read mode and fill in
     * given values for account and amount written to balance.
     * 
     * @param targetedAccount Account to which balance will be written.
     * @param balanceAdded Amount of money to be added to the account. Can be
     * negative.
     */
    public TransactionAction(Account targetedAccount, int balanceAdded)
    {
        this.actionType = WRITE;
        this.targetedAccount = targetedAccount;
        this.balanceAdded = balanceAdded;
    }

    /**
     * @return Targeted account
     */
    public Account getAccount()
    {
        return targetedAccount;
    }

    /**
     * @return Action type symbolic constant
     */
    public boolean getActionType()
    {
        return actionType;
    }

    /**
     * @return Balance added
     */
    public int getBalanceAdded()
    {
        return balanceAdded;
    }
}
