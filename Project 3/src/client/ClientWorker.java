package client;

import java.util.ArrayList;

import account.TransactionAction;

public class ClientWorker implements Runnable
{
    /**
     * Ordered list of actions in the transaction.
     */
    private ArrayList<TransactionAction> actionList;

    /**
     * Constructor which takes the list of actions in the transaction as
     * initial data.
     * 
     * @param actionList List of actions in the transaction.
     */
    public ClientWorker(ArrayList<TransactionAction> actionList)
    {
        this.actionList = actionList;
    }

    public void run()
    {
    }
}
