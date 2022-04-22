package appserver.job.impl;

/**
 *
 * @author Zachary
 */
public class FibonacciAux {
    
    Integer result;
    
    public FibonacciAux(Integer input)
    {
        result = fib(input);
    }
    
    public Integer fib(Integer input)
    {
        if (input <= 1)
        {
            return input;
        }
       
        return fib(input-1) + fib(input-2);
    }
    
    public Integer getResult()
    {
        return result;
    }
    
}
