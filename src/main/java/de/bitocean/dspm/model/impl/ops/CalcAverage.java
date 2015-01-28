/**
 * 
 * CalcAverage Opration:
 * 
 * Only useful if the data is of type 
 *     INTERVAL-Scale or
 *     RATIO-Scale
 * 
 */
package de.bitocean.dspm.model.impl.ops;

import de.bitocean.dspm.model.Operation;
import de.bitocean.dspm.model.impl.roles.CalcAverageOperationAllowed;

/**
 *
 * @author kamir
 */
public class CalcAverage extends Operation {
    
    
    public CalcAverage() {
        rolesAdd( new CalcAverageOperationAllowed() );
    }

    
}
