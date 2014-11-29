/*
 * SnipLibConnector connects to background knowledge, stored in SOLR 
 * and in WIKIs.
 */
package de.bitocean.mm.sniplibtool;

import java.awt.Container;
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
        frame = new BackgroundKnowledgeFrame();
        frame.con = this;
    }
    
    public JFrame getView(){
        return frame;
    }
    
    public void show(String title, SnippetConsumer sC) {
        
        sc = sC;

        frame.setTitle(title);
        
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize( 800,800);
        frame.setVisible(true);
    }
    
    
}
