package client;

public class ProxyServer
{
    // TODO: Decide whether this class should have a main() or implement runnable.

    public boolean closeTransaction()
    {
        return false;
    }

    public int openTransaction()
    {
        return -1;
    }

    public int readBalance(int accountNumber)
    {
        return -1;
    }

    public void writeBalance(int accountNumber, int amountWritten)
    {

    }
}
