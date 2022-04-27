package appserver.job.impl;

import appserver.job.Tool;

/**
 * Class [Fibonacci] Adapted from PlusOne, uses helper class to get fib of input
 * 
 * @author Zachary
 */
public class Fibonacci implements Tool {
    
    FibonacciAux helper = null;
    
    @Override
    public Object go(Object parameters) {   
        
        helper = new FibonacciAux((Integer) parameters);
        return helper.getResult();
    }
    
}
