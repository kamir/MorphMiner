package de.bitocean.mm.sniplibtool;

import java.io.IOException;
import java.util.Iterator;
import org.etosha.core.sc.connector.SemanticContextBridge;
import org.etosha.core.sc.connector.external.Wiki;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author kamir
 */
public class Snippet {
    
    public String namne = "???";
    
    public String snip = "cool stuff";
    public String src = "global brain";
    public String doc = "info";
    
    public static Snippet createSnipet( String name , JSONObject a, SemanticContextBridge scb ) throws IOException{

        Snippet s = new Snippet();
        s.src = "www.semanpix.de::###"; 
        s.namne = name;
        
        System.out.println("> " + a.size() );
        
        Iterator i = a.keySet().iterator();
        while( i.hasNext() ) {
            System.out.println( i.next() );
        }
        
        JSONArray array = (JSONArray)a.get("Original Dokumentation");
        System.out.println(">> " + array.size() );
        if ( array.size() > 0 ) {
            s.doc = (String)array.get( 0 );
        }
        
        JSONArray array2 = (JSONArray)a.get("Snippet");
        System.out.println(">> " + array2.size() );
        if ( array2.size() > 0 ) {
            JSONObject layerName = (JSONObject)array2.get( 0 );
            
            String sname = (String)layerName.get("fulltext");
            
            System.out.println( "Snippet.code layer pagename : " + sname );

            // pages are nodes and have layers
            //   - code
            //   - termvector
            //   - entities

            // we take the code layer of the Node, found in the snippet table.
            Wiki wiki = scb.getWiki();
            if( wiki != null ) {
                String code = wiki.getPageText( sname );
                s.snip = code;
                System.out.println( code );
            }
            
        }
        
        return s;
        
    } 

    @Override
    public String toString() {
        return namne + " (" + snip.length() + ") {DOCU: " + doc+"}";
    }
    
}
