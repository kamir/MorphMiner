package de.bitocean.mm.importer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFrame;
import org.semanpix.parser.TikaGUI;
import org.apache.tika.metadata.*;

/**
 *
 * @author kamir
 */
public class PlainTextFromPDFExtractor {

    public static String PDF_BASEPATH = "/Users/" + System.getProperty("user.name") + "/Desktop/exp4/pdf";
    public static String TXT_BASEPATH = "/Users/" + System.getProperty("user.name") + "/Desktop/exp4/txt";

    public static void main(String[] args) throws Exception {

        File fPDF = new File(PDF_BASEPATH);

        System.out.println(">>> (PDF) f=" + fPDF.getAbsolutePath());

        File[] pdfs = fPDF.listFiles();

        System.out.println(">>> number of files: " + pdfs.length);

        File fSCHEMA = new File("./morphline-projects/corpus1/conf/schema.xml");
        System.out.println(">>> (SCHEMA) exists:" + fSCHEMA.exists() + ": " + fSCHEMA.getAbsolutePath());
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(fSCHEMA));
        while (br.ready()) {
            sb.append(br.readLine() + "\n");
        }
        String SCHEMAXML = sb.toString();

        int z = 1;
        for (File f : pdfs) {
            if (f.getName().endsWith(".pdf")) {
//                System.out.println(" * " + f.getAbsolutePath());
                String fn = f.getName();
                int i = fn.length() - 4;

                String fnSUB = fn.substring(0, i);
                
                fnSUB = fnSUB.replaceAll("/", "_");
                fnSUB = fnSUB.replaceAll(":", "_");
                
                
                String txtFN = TXT_BASEPATH + "/" + fnSUB + ".txt";

                
                Metadata md = TikaGUI.getMetadataFromFile(f);
                String textContent = TikaGUI.getTextContent();

                System.out.println(z + ") >" + textContent.split(" ").length + " # " + txtFN);

                BufferedWriter brOut = new BufferedWriter(new FileWriter(new File(txtFN)));
                brOut.write(textContent);
                brOut.flush();
                brOut.close();
                
                z++;
            }
        }

//        
//        
//        PropertyFieldMatcher pfm = new PropertyFieldMatcher();
//        pfm.process( md, fPDF, SCHEMAXML, textContent );
//        
        System.exit( 0 );
    }

}
