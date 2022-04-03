package account;

import java.util.ArrayList;

public class AccountManager
{
    private ArrayList<Account> accountList;

    public AccountManager()
    {
        accountList = new ArrayList<Account>();
    }

    /**
     * Read balance of account.
     * 
     * @param accountID ID of account represented by index in array list.
     * @return Account's current balance.
     */
    public int readAccountBalance(int accountID)
    {
        return accountList.get(accountID).getBalance();
    }

    /**
     * Write new balance to account.
     * 
     * @param accountID ID of account represented by index in array list.
     * @param newBalance New balance to be written.
     */
    public void writeAccountBalance(int accountID, int newBalance)
    {
        accountList.get(accountID).setBalance(newBalance);
    }
}
