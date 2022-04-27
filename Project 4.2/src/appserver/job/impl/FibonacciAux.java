package appserver.job.impl;

/**
 * Class [FibonacciAux] Helper class for Fibonacci
 * 
 * @author Zachary
 */
public class FibonacciAux {
    
    Integer result;
    
    // take in number to fib in constructor
    public FibonacciAux(Integer input)
    {
        result = fib(input);
    }
    
    
    // recursively fib our input
    public Integer fib(Integer input)
    {
        // once we are at 1 then we are done so stop recursing
        if (input <= 1)
        {
            return input;
        }
       
        // if not 1 yet then keep on doing recursion
        return fib(input-1) + fib(input-2);
    }
    
    // getter for Fibonacci class to use
    public Integer getResult()
    {
        return result;
    }
    
}
