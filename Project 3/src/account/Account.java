package account;

public class Account
{
    /**
     * Account balance, in dollars. Can be negative.
     */
    private int balance;

    /**
     * Constructor.
     * 
     * @param balance Starting balance.
     */
    public Account(int balance)
    {
        this.balance = balance;
    }

    /**
     * Get balance.
     * 
     * @return Current balance.
     */
    public int getBalance()
    {
        return balance;
    }

    /**
     * Set balance.
     * 
     * @param newBalance New balance to be set.
     */
    public void setBalance(int newBalance)
    {
        balance = newBalance;
    }
}
