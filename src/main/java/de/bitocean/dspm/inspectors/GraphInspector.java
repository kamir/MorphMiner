
package de.bitocean.dspm.inspectors;

import de.bitocean.dspm.model.Operation;
import de.bitocean.dspm.model.impl.ops.CalcAverage;
import de.bitocean.dspm.model.impl.ops.CalcPearsonCorrelation;
import de.bitocean.dspm.model.impl.ops.EndOperation;
import de.bitocean.dspm.model.impl.ops.FilterOperation;
import de.bitocean.dspm.model.impl.ops.StartOperation;

/**
 *
 * @author kamir
 */
public class GraphInspector {
    
    public static void main(String[] args) {
        
        /**
         * 
         * Load / Create a DSPM Graph ...
         * 
         */
        StartOperation so = new StartOperation();

        CalcAverage op1 = new CalcAverage();
        CalcPearsonCorrelation op2 = new CalcPearsonCorrelation();
        FilterOperation op3 = new FilterOperation();
        
        EndOperation se = new EndOperation();
        
        so.addNextOperation(op1);
        op1.addNextOperation(op2);
        op2.addNextOperation(op3);
        op3.addNextOperation(se);
        
        /**
         * 
         * Do a logical Validation ...
         *
         */ 
        
        // Traverse all operations ...
        Operation current = so;
        while( !(current instanceof EndOperation) ) {

            System.out.println( current.getClass() );
            System.out.println( "Roles: " + current.getNumberOfRules() );
            
            // Load required evaluation roles from DESCRIPTOR
            // Lookup additional evaluation roles from METASTORE
            // 
            // Load DATASET Profile From METASTORE
            // Load DATASET Schema From METASTORE
            //
            // Compare Input conditions
            // Compare Output conditions

            current = current.next;
        }
    
    
    }
    
}
