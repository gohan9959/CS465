package account;

import java.io.IOException;
import java.util.ArrayList;

import utils.PropertyHandler;

/**
 * Manages accounts being stored on the server
 *
 * @author Conrad Murphy
 */
public class AccountManager
{
    private ArrayList<Account> accountList;

    /**
     * Constructor initiates accounts on the server by given values from server property file
     */
    public AccountManager()
    {
        // Create new account list
        accountList = new ArrayList<Account>();

        try
        {
            // Get number of accounts
            PropertyHandler properties = new PropertyHandler("config/Server.properties");
            int numAccounts = Integer.parseInt(properties.getProperty("NUM_ACCOUNTS"));
            int startBal = Integer.parseInt(properties.getProperty("STARTING_BALANCE"));

            // Fill account list
            for (int index = 0; index < numAccounts; index++)
            {
                accountList.add(new Account(startBal));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
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
