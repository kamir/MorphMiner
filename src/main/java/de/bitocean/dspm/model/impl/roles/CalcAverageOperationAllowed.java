/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.dspm.model.impl.roles;

import de.bitocean.dspm.model.InputData;
import de.bitocean.dspm.model.Operation;
import de.bitocean.dspm.model.OutputData;
import de.bitocean.dspm.model.Role;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class CalcAverageOperationAllowed implements Role {
    
    final public int ignore_operation_type = 0;
    
    final public int allow_SUM_operation = 1;
    final public int allow_DIST_operation = 2;
    final public int allow_ORDER_operation = 3;
    final public int allow_RATIO_operation = 4;
    
    public int current_mode = 0; 
    
    public boolean validateOperation(Operation op, InputData in, OutputData out) {
        
        boolean b = true;
        
    
        
        return b;
    }
    
    
}
