package client;

import utils.PropertyHandler;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class that handles client interaction with the transactional server
 *
 * @author Zachary Wilson-Long
 */
public class Client
{
    /**
     * Handles randomization
     */
    private Random random;

    /**
     * Handles reading/writing to/from the property file
     */
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

        random = new Random();
    }

    /**
     * Picks a random account number from accounts on the server.
     *
     * @return random account number
     */
    private int randomAccountNumber()
    {
        int num_of_accounts = Integer.parseInt(properties.getProperty("NUM_ACCOUNTS"));
        return random.nextInt(num_of_accounts);
    }

    /**
     * Implementation of interface Runnable
     * <p>
     * Called by main() to handle client setup and then call sender and receiver threads
     */
    public void startClient()
    {
        // get number of transactions
        int numTransactions = Integer.parseInt(properties.getProperty("NUM_TRANSACTIONS"));

        for (int transIndex = 0; transIndex < numTransactions; transIndex++)
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


            System.out.printf("Attempting Transaction #%d:\n" +
                              "[Transferring $%d from account #%d to #%d]\n",
                              transIndex + 1, writeAmount, fromAccountNum, toAccountNum);


            // Keep retrying until transaction is successful
            boolean transSuccess = false;
            while(!transSuccess)
            {

                // create new proxy
                Proxy proxy = new Proxy();

                // start transaction
                proxy.openTransaction();

                // attempt read an write to account 1
                int fromAccountBal = proxy.read(fromAccountNum);
                proxy.write(fromAccountNum, fromAccountBal - writeAmount);

                // attempt read and write to account 2
                int toAccountBal = proxy.read(toAccountNum);
                proxy.write(toAccountNum, toAccountBal + writeAmount);

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
            System.out.println("Transaction successful\n");

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
            propertiesFile = "config/Client.properties";
        }

        Client client = new Client(propertiesFile);

        System.out.println("Transaction Client Started");
        System.out.println("==========================\n");

        client.startClient();
    }
}
