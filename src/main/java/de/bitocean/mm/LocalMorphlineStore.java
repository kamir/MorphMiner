/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.bitocean.mm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class LocalMorphlineStore {
    
    String folder = null;

    public LocalMorphlineStore( String base )  {
        folder = base;
    }
    
    public Vector<String> loadTestData( String path ) throws FileNotFoundException, IOException {
        Vector<String> m = new Vector<String>();
        
        BufferedReader br = new BufferedReader( 
               new FileReader( getTestDataFile(path) ));
        while( br.ready() ) 
            m.add( br.readLine() );
        return m;
    }

    private File getTestDataFile(String path) {
        File f = new File( folder + "/" + path + "/test.dat");
        System.out.println( "Load testdata from : " + f );
        return f;
    }
    
    
            
    public String loadMorphline( String path ) throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();
        
        BufferedReader br = new BufferedReader( 
               new FileReader( new File( folder + "/" + path + "/morphline.conf")));
        while( br.ready() ) 
            sb.append( br.readLine() + "\n" );
        return sb.toString();
    }

    public String loadFlumeCFG( String path ) throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();
        
        File flumeF = getFlumeCFGFilename( path );
        
        BufferedReader br = new BufferedReader( 
               new FileReader( flumeF ) );
        while( br.ready() ) 
            sb.append( br.readLine() + "\n" );
        return sb.toString();
    }

        
    public String loadSolrSchema( String path ) throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();
        
        BufferedReader br = new BufferedReader( 
               new FileReader( new File( folder + "/" + path + "/conf/schema.xml")));
        while( br.ready() ) 
            sb.append( br.readLine() + "\n" );
        return sb.toString();
    }
    
    public void saveSchema( String path, String text ) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter( new File( folder + "/" + path + "/conf/schema.xml") );
        fw.write(text);
        fw.close();
    }

    public void saveMorphline( String path, String text ) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter( new File( folder + "/" + path + "/morphline.conf") );
        fw.write(text);
        fw.close();
    }

    public File getFlumeCFGFilename(String path) {
        return new File( folder + "/" + path + "/flume.conf");
    }

    void saveFlumeCFG(String path, String text) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter( getFlumeCFGFilename( path ) );
        fw.write(text);
        fw.close();
    }
    
}
