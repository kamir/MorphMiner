/**
 *
 *
 *
 */
package de.bitocean.dspm.inspectors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author kamir
 */
public class OozieSchemaInspector {

    String schema = null;
    String fileName = null;
    String urlS = null;

    DocumentBuilderFactory builderFactory = null;
    DocumentBuilder builder = null;

    private OozieSchemaInspector() {
    }

    /**
     * Load from a File ...
     *
     * @param fileName
     * @throws XPathExpressionException
     */
    public void listAllActions() throws XPathExpressionException {

        builderFactory = DocumentBuilderFactory.newInstance();
        builder = null;

        try {

            builder = builderFactory.newDocumentBuilder();

//            Document document = getDocumentFromUrl();
            Document document = getDocumentFromFile();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/workflow-app/action";

            System.out.println(">> expression: " + expression);

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            System.out.println(nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {

                Element show = (Element) nodeList.item(i);

                System.out.print(show.getAttribute("name"));
//                System.out.print(", " + show.getAttribute("type"));
//                System.out.print(", " + show.getAttribute("required"));
//                System.out.print(", " + show.getAttribute("indexed"));
//                System.out.print(", " + show.getAttribute("stored"));
//                System.out.println(", " + show.getAttribute("multivalued"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        File f = new File("./morphline-projects/FAQMails02/mr-action.xml");
        
        OozieSchemaInspector solrInsp = new OozieSchemaInspector();
//        solrInsp.urlS = "https://github.com/rkanter/oozie-subwf-repeat-example/raw/master/src/main/subwf-repeat/mr-action.xml";
        solrInsp.fileName = f.getAbsolutePath();
        
        
//        StringBuffer sb = new StringBuffer();
//
//        BufferedReader br = new BufferedReader(new FileReader(f));
//        while (br.ready()) {
//            sb.append(br.readLine() + "");
//        }
        
        
        try {
            solrInsp.listAllActions();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OozieSchemaInspector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public OozieSchemaInspector(String s) {
        schema = s;
    }

    /**
     *
     * Use the SCHEMA variable ....
     *
     * @param m
     * @throws XPathExpressionException
     */
    public void populateTableModel(DefaultTableModel m) throws XPathExpressionException, Exception {

        try {

            Document document = getDocumentFromString();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/schema/fields/field";

            String email = xPath.compile(expression).evaluate(document);

            Node node = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element show = (Element) nodeList.item(i);

                System.out.print(show.getAttribute("name"));
                System.out.print(", " + show.getAttribute("type"));
                System.out.print(", " + show.getAttribute("required"));
                System.out.print(", " + show.getAttribute("indexed"));
                System.out.print(", " + show.getAttribute("stored"));
                System.out.println(", " + show.getAttribute("multivalued"));

                Vector row = new Vector();

                row.add(this.getAttribute(show, "name", "?"));
                row.add(this.getAttribute(show, "type", "?"));
                row.add(this.getAttribute(show, "required", "?"));
                row.add(this.getAttribute(show, "indexed", "?"));
                row.add(this.getAttribute(show, "stored", "?"));
                row.add(this.getAttribute(show, "multivalued", "?"));
                row.add(this.getAttribute(show, "name", "?"));

                m.addRow(row);

            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private Object getAttribute(Element show, String name, String string) {
        String s = show.getAttribute(name);
        if (s == null) {
            s = string;
        } else if (s.length() == 0) {
            s = string;
        }
        return s;
    }

    private Document getDocumentFromFile() throws Exception {
        builderFactory = DocumentBuilderFactory.newInstance();
        Document doc = builder.parse(new FileInputStream(fileName));
        return doc;
    }

    private Document getDocumentFromString() throws Exception {
        builder = builderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(schema.getBytes(StandardCharsets.UTF_8));
        Document document = builder.parse(stream);
        return document;
    }

    private Document getDocumentFromUrl() {

        System.out.println(">>> " + urlS);

        Document doc = null;
        URL url = null;

        try {
            
            url = new URL(urlS);

            builderFactory = DocumentBuilderFactory.newInstance();

            doc = builder.parse(url.openStream());

        } catch (MalformedURLException ex) {
            Logger.getLogger(OozieSchemaInspector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(OozieSchemaInspector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OozieSchemaInspector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }
}
