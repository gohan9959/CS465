package dynNet.operationsImpl;

import dynNet.dynCalculator.Operation;


/**
 * Class [RootOperation]
 * <p>
 * This is a concrete operation class, that implements the
 * interface <code>Operation</code>.
 *
 * @author Zachary Wilson-Long
 */
public class RootOperation implements Operation{
	
	public float calculate(float operand, float rootBase){
            
            // get the root but unrounded
            double unrounded = Math.pow(Math.E, Math.log(operand)/rootBase);
            
            // round to 2 decimal places
            return (float) Math.round(unrounded * 100) / 100;
	}
}