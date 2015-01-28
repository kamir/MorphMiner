/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.dspm.model;

import java.util.Date;

/**
 *
 * @author kamir
 */
public class Aspect {
    
    public boolean validateReadAccess(String username) {
        /**
         * AspectPhysicalRepresentation is used to do the validation
         * on a real system. 
         */
        return true;
    };
    
    public boolean validateAvailability(String username) {
        /**
         * AspectPhysicalRepresentation is used to do the validation
         * on a real system. 
         */
        return true;
    };
    
    public boolean validateAge_isOlderThan(Date olderThan) {
        /**
         * AspectPhysicalRepresentation is used to do the validation
         * on a real system. 
         */
        return true;
    };
    
    /**
     *  min
     *  max
     *  avg
     *  isGaussianDistribution
     */
    public boolean getColumnProperty(String columnNam, String property) {
        /**
         * AspectPhysicalRepresentation is used to do the validation
         * on a real system. 
         */
        return true;
    };
    
    public static final int scaleType_nominal = 0;
    public static final int scaleType_ordinal = 1;
    public static final int scaleType_interval = 2;
    public static final int scaleType_ratio = 3;

    public boolean validateColumnScaleType(int[] must, int[] mustNot) {
 
        return true;
    };



}
