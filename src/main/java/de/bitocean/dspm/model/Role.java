/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.dspm.model;

import java.util.Vector;

/**
 *
 * @author kamir
 */
public interface Role {
    
    public boolean validateOperation( Operation op, InputData in , OutputData out );
    
}
