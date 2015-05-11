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

        File fPDF = new File( PDF_BASEPATH );

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
        int returnVal = jfc.showOpenDialog( new JFrame() );
        fPDF = jfc.getSelectedFile();
        
        System.out.println( ">>> (PDF) f=" + fPDF.getAbsolutePath() );

        Metadata md = TikaGUI.getMetadataFromFile( fPDF );
        String textContent = TikaGUI.getTextContent();

        javax.swing.JFileChooser jfc2 = new javax.swing.JFileChooser();
        int returnVal2 = jfc.showOpenDialog( new JFrame() );
        File fSCHEMA = jfc.getSelectedFile();

        System.out.println( ">>> (SCHEMA) f2=" + fSCHEMA.getAbsolutePath() );

        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader( new FileReader( fSCHEMA ) );
        while( br.ready() ) {
            sb.append( br.readLine() + "\n" );
        }
        String SCHEMAXML = sb.toString();
        
        PropertyFieldMatcher pfm = new PropertyFieldMatcher();
        pfm.process( md, fPDF, SCHEMAXML, textContent );
        
    }
    
}
