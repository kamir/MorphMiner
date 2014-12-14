package de.bitocean.mm.importer;

import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONException;

public class ResultLineImporter {

    public static int main( String[] args ) throws IOException, Exception {
    
        
      
        String p = "/Volumes/MyExternalDrive/CALCULATIONS/CC/CN_1_en___Econophysics_BIN=24_dissertation_DEMO_2011_merged";
        String fn = "FINAL.CC.INFOFLOW.false.10-001.0.en___Econophysics_BIN=24.json";
        File f = new File(p+"/"+fn);
        
//        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
//        int returnVal = jfc.showOpenDialog( new JFrame() );
//        f = jfc.getSelectedFile();
//        
        int i = 0;
        BufferedReader br2 = new BufferedReader( new FileReader( f ) );
        while( br2.ready() && i < 10 ) {
            
            String lineA = br2.readLine();
            System.out.println( lineA);
            
            importRecord( lineA );
            
//            lineA = "{ \"id\":\"CN_" + i + "_en___Econophysics_BIN\" }";
//            String[] cmd = {"curl",
//                            "http://172.16.14.225:8983/solr/tscorrelationCollection_shard1_replica1/update", 
//                            "-H",
//                            "'Content-type:application/json'",
//                            "-d",
//                            "'[" + lineA + "]'"};
//
//        ProcessBuilder probuilder = new ProcessBuilder( cmd );
//        //You can set up your work directory
//        probuilder.directory(new File("/Users/kamir"));
//        
//        Process process = probuilder.start();
//        
//        //Read out dir output
//        InputStream is = process.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String line;
//        System.out.printf("Output of running %s is:\n",
//                Arrays.toString(cmd));
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        
//        //Wait to get exit value
//        try {
//            int exitValue = process.waitFor();
//            System.out.println("\n\nExit Value is " + exitValue);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
            
            i++;
        }
        
        // ResultLineImporter.commit();

        return 0;
    }
    
    static String zkHostString = null;
    static SolrServer solr = null;
    
    static String collection = "tscorrelationCollection";

 
    public static void init() throws Exception {
        // zkHostString = "training01.sjc.cloudera.com:2181,training03.sjc.cloudera.com:2181,training06.sjc.cloudera.com:2181/solr";
        zkHostString = "172.16.14.228:2181/solr";
        solr = new CloudSolrServer( zkHostString );
        
//        System.out.println( solr.ping() );
//        System.out.println( "[PING] DONE." );
        
        System.out.println( ">>> init() ..." );
        
    }



    private static void importRecord(String rec) throws SolrServerException, IOException, JSONException {

        System.out.println("**** Import a record " );

        // JSON record erzeugen ...
        JSONObject o = parseJSON( rec );
        
        SolrInputDocument document = new SolrInputDocument();
        
        Iterator k = o.keys();
        while( k.hasNext() ) {
            String key = (String)k.next();
//                System.out.println( key + "  " + o.get(key).toString() );
            document.addField( key , o.get(key).toString() );
        }

        
        

        UpdateRequest add = new UpdateRequest();
        add.add(document);
        add.setParam("collection", collection);
        add.process(solr);
    }
        
//    static private void commit() throws SolrServerException, IOException {
//        UpdateRequest commit = new UpdateRequest();
//        commit.setAction(UpdateRequest.ACTION.COMMIT, true, true);
//        commit.setParam("collection", collection );
//        commit.process(solr);
//    }

    private static JSONObject parseJSON(String rec) {
        try {
            JSONObject jo = new JSONObject(rec);
//            Iterator k = jo.keys();
//            while( k.hasNext() ) {
//                String key = (String)k.next();
//                System.out.println( key + "  " + jo.get(key).toString() );
//            }
            
            return jo;
        } 
        catch (JSONException ex) {
            Logger.getLogger(ResultLineImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    static private Object getIdFromRecord() {
//       return null; 
//    }

//    private static void importRecordJSON(String line) {
//
//        try {
//            
//            String IP = "172.16.14.225";
//            String collection = "tscorrelationCollection_shard1_replica1";
//
//            String URL = "http://" + IP + ":8983/solr/" + collection + "/update/json?wt=json&commit=true";
//            
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost post = new HttpPost( URL );
//            
//            // StringEntity entity  = new StringEntity("{\"add\": { \"doc\": " + line + "}}", "UTF-8");
//            StringEntity entity  = new StringEntity( line, "UTF-8");
//            
//            System.out.println( URL );
//
//            entity.setContentType("application/json");
// 
//            post.setEntity(entity);                
// 
//            HttpResponse response = httpClient.execute(post);
//            HttpEntity httpEntity = response.getEntity();
//            InputStream in = httpEntity.getContent();
// 
//            String encoding = httpEntity.getContentEncoding() == null ? "UTF-8" : httpEntity.getContentEncoding().getName();
//            encoding = encoding == null ? "UTF-8" : encoding;
//            String responseText = IOUtils.toString(in, encoding);
//            System.out.println("response Text is " + responseText);
// 
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
    
}
