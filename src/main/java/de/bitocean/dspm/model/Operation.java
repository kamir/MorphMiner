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
public class Operation {
    
    public Operation next = null;
    public Operation prev = null;
    
    Vector<Role> roles = new Vector<Role>();
    
    public void addNextOperation( Operation op ) {
        next = op;
        op.addPreviousOperation( this );
    }
    
    public void addPreviousOperation( Operation op ) {
        prev = op;
        if( op.next == null) op.addNextOperation(op);
    }
    
    public Vector<Role> getAllRoles() {
        return null;
    };

    public void rolesAdd(Role r) {
        roles.add(r);
    }

    public String getNumberOfRules() {
        if ( roles == null ) return "<Node has no roles>";
        else return roles.size() + " roles available.";
    }
    
}
