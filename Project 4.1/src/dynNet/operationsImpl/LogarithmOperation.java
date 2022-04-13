package dynNet.operationsImpl;

import dynNet.dynCalculator.Operation;


/**
 * Class [LogarithmOperation]
 * <p>
 * This is a concrete operation class, that implements the
 * interface <code>Operation</code>.
 *
 * @author Zachary Wilson-Long
 */
public class LogarithmOperation implements Operation{
	
	public float calculate(float operand, float logBase){
            
            // get the unrounded log
            double unrounded = Math.log(operand) / Math.log(logBase);
            
            // round to 2 decimal places
            return (float) Math.round(unrounded * 100) / 100;
	}
}