/*
 * SnipLibConnector connects to background knowledge, stored in SOLR 
 * and in WIKIs.
 */
package de.bitocean.mm.sniplibtool;

import java.awt.Container;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author kamir
 */
public class SnipLibConnector {
    
    SnippetConsumer sc = null;
    BackgroundKnowledgeFrame frame = null;
    static SnipLibConnector con;
    
    private SnipLibConnector () {};
    
    public static SnipLibConnector getSnipLibConnector() {
        if ( con == null ) {
            con = new SnipLibConnector();
            con.init();
        }
        return con;
    }
    
    public void init(){
        try {
            frame = new BackgroundKnowledgeFrame();
        } 
        catch (IOException ex) {
            Logger.getLogger(SnipLibConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.con = this;
    }
    
    public JFrame getView(){
        return frame;
    }
    
    public void show(String title, SnippetConsumer sC) {
        
        System.out.println( "> open the snippet selector ...");
        sc = sC;

        frame.setTitle(title);
        
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize( 800,800);
        frame.setVisible(true);
    }
    
    
}
