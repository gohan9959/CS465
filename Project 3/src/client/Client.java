package client;

import utils.PropertyHandler;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client
{

    private Proxy proxy;

    private Properties properties;

    /**
     * Constructor.
     * Handles property file to set up field variables
     */
    public Client(String propertiesFile)
    {
        properties = null;

        // open properties
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Cannot open properties file", ex);
            System.exit(1);
        }

    }

    private int randomAccountNumber()
    {
        int[] accounts = {123, 231, 20};
        int num_of_accounts = 3;

        Random random = new Random();
        return accounts[random.nextInt(num_of_accounts)];
    }

    /**
     * Implementation of interface Runnable
     * <p>
     * Called by main() to handle client setup and then call sender and receiver threads
     */
    public void startClient() throws IOException
    {
        // get number of transactions
        int numTransactions = Integer.parseInt(properties.getProperty("NUM_TRANSACTIONS"));

        // init random variable
        Random random = new Random();

        int transIndex;
        for (transIndex = 0; transIndex < numTransactions; transIndex++)
        {

            // get two random account numbers
            int fromAccountNum = randomAccountNumber();
            int toAccountNum;
            do
            {
                toAccountNum = randomAccountNumber();
            }
            while (fromAccountNum == toAccountNum);

            // random amount between 1 to 100
            int writeAmount = random.nextInt(100) + 1;


            System.out.printf("Attempting Transaction:\n" +
                              "[Moving $%d from account #%d to #%d]\n\n",
                              writeAmount, fromAccountNum, toAccountNum);


            // Keep retrying until transaction is successful
            boolean transSuccess = false;
            while(!transSuccess)
            {
                proxy = new Proxy();

                // start transaction
                proxy.openTransaction();

                // attempt read an write to account 1
                proxy.read(fromAccountNum);
                proxy.write(fromAccountNum, -writeAmount);

                // attempt read and write to account 2
                proxy.read(toAccountNum);
                proxy.write(toAccountNum, writeAmount);

                // determine if transaction succeeded otherwise repeat attempt
                if (proxy.closeTransaction())
                {
                    transSuccess = true;
                }
                else
                {
                    System.out.println("Transaction failed, restarting...\n");
                }

            }

            // if loop exits than transaction must have been completed
            System.out.println("Transaction successful");

        }
    }

    /**
     * Load in property file info and then run the constructor
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        String propertiesFile;

        try
        {
            propertiesFile = args[0];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            propertiesFile = "ChatClient/config/Server.properties";
        }

        Client client = new Client(propertiesFile);

        System.out.println("Chat Client Started");
        System.out.println("===================\n");

        client.startClient();
    }


}
