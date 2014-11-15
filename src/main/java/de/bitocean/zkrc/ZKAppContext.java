/**
 * 
 * This is a sample for the usage of the Zookeeper, 
 * to start remote tools.
 * 
 **/
package de.bitocean.zkrc;

import de.bitocean.zkrc.Executor;

/**
 *
 * @author root
 */
public class ZKAppContext {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // this is a creat time to start the Executor ...
        String[] paraNames = { "zkServer", "zNode", "filename", "clientID", "program",  
                               "program.arg1", "para1", "para2", "para3", "para4" };
        
        String[] para  = { "cdh42", "/zooview/cfg", "log/autorun.log", "DEMO",
                           "ant", "-buildfile", "autoruntool.xml", "status", "-Dcmd=status" };
        
        System.out.println( "ZKAppContext (Semantic Context Tools for Hadoop)");
        System.out.println( "================================================");
        System.out.println( "> init parameter set : ");
        
        args = new String[ para.length ];
        for( int i = 0; i < para.length; i++ ) {
            args[i] = para[i];
            System.out.println( paraNames[i]+ " : " + para[i] );
        }  

        System.out.println( "> start the Executor ... ");
        
        Executor exec = Executor.getExecutor( args );
        exec.run();
        
        
    }
}
