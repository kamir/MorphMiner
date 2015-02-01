package de.bitocean.mm.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFrame;
import org.semanpix.parser.TikaGUI;
import org.apache.tika.metadata.*;

/**
 *
 * @author kamir
 */
public class PDFImporter {

    public static String PDF_BASEPATH = "/Users/" + System.getProperty("user.name") + "/Desktop";  
    
    public static void main(String[] args) throws Exception {

        File f = new File( PDF_BASEPATH );

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
        int returnVal = jfc.showOpenDialog( new JFrame() );
        f = jfc.getSelectedFile();

        Metadata md = TikaGUI.getMetadataFromFile( f );
        String textContent = TikaGUI.getTextContent();

        javax.swing.JFileChooser jfc2 = new javax.swing.JFileChooser();
        int returnVal2 = jfc.showOpenDialog( new JFrame() );
        File f2 = jfc.getSelectedFile();
        
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader( new FileReader( f2 ) );
        while( br.ready() ) {
            sb.append( br.readLine() + "\n" );
        }
        
        PropertyFieldMatcher pfm = new PropertyFieldMatcher();
        pfm.process( md, f, sb.toString(), textContent );
        
    }
    
}
