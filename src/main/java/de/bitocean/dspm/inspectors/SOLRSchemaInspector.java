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
public class SOLRSchemaInspector {

    String schema = null;
    String fileName = null;

    DocumentBuilderFactory builderFactory = null;
    DocumentBuilder builder = null;

    private SOLRSchemaInspector() {    }

    /**
     * Load from a File ...
     *
     * @param fileName
     * @throws XPathExpressionException
     */
    public void listAllFields(String fn) throws XPathExpressionException {

        builderFactory = DocumentBuilderFactory.newInstance();

        builder = null;

        try {

            fileName = fn;
            
            Document document = getDocumentFromFile();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/schema/fields/field";

//            String email = xPath.compile(expression).evaluate(document);
//
//            Node node = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element show = (Element) nodeList.item(i);

                System.out.print(show.getAttribute("name"));
                System.out.print(", " + show.getAttribute("type"));
                System.out.print(", " + show.getAttribute("required"));
                System.out.print(", " + show.getAttribute("indexed"));
                System.out.print(", " + show.getAttribute("stored"));
                System.out.println(", " + show.getAttribute("multivalued"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        File f = new File("./morphline-projects/FAQMails02/conf/schema.xml");

        SOLRSchemaInspector solrInsp = new SOLRSchemaInspector();

//        StringBuffer sb = new StringBuffer();
//
//        BufferedReader br = new BufferedReader(new FileReader(f));
//        while (br.ready()) {
//            sb.append(br.readLine() + "");
//        }
        try {
            solrInsp.listAllFields(f.getAbsolutePath());
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SOLRSchemaInspector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Document getDocumentFromFile() throws Exception {
        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
        Document doc = builder.parse(new FileInputStream(fileName));
        return doc;
    }

    private Document getDocumentFromString() throws Exception {
        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(schema.getBytes(StandardCharsets.UTF_8));
        Document document = builder.parse(stream);
        return document;
    }

    public SOLRSchemaInspector(String s) {
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
}
