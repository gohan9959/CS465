package appserver.client;

/**
 *
 * @author Zachary
 */
public class FibonacciClient extends Thread{
    
    private int parameter;
    
    private int fibNumber;
    
    public FibonacciClient(String config, int index)
    {
        // read config file
        
        parameter = index;
    }
    
    @Override
    public void run()
    {
        // do fib stuff
        fibNumber = 0;
        
        System.out.printf("Fibonacci of %d: %d\n", parameter, fibNumber);
    }
    
    public static void main(String[] args)
    {
        int index;
        for (index=46; index>0; index--)
        {
            new Thread(new FibonacciClient("../../config/Server.properties", index)).start();
        }
    }
}
