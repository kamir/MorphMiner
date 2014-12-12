

package de.bitocean.mm.importer;
 
import java.io.IOException;
import javax.security.auth.login.LoginException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kamir
 */
public class EmailImport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        GMailLoader.init();
        
        String cat = javax.swing.JOptionPane.showInputDialog("List of categories to import:", "");
        String[] topics = cat.split(",");
        
        GMailLoader.importEmailsForLabels( topics );
        
        javax.swing.JOptionPane.showMessageDialog( null, "Import finished." );
        
    }
    
}
