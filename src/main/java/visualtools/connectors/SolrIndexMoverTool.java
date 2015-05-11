/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualtools.connectors;

import java.io.IOException;
import static visualtools.connectors.SOLRTool.SOLR;

/**
 *
 * @author kamir
 */
public class SolrIndexMoverTool {

     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // We do the core management on the cluster.
        // An SSH login to a GatewayNode is required.
        
        String collection = "FAQMails02";

        SOLRTool.init( SOLRTool.GWHOST , "mirko.kaempf" , "MUvup3uT" );
        
        SOLRTool.listCollection();
        
        SOLRTool.packageCollection(SOLRTool.ZK, collection);
        
        SOLRTool.close();
        
        System.exit( 0 );
        
    }    
    
}
