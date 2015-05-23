package visualtools.connectors;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * The SOLR tool is the component to talk to zookeeper and to use the 
 * solrctl command on a cluster Gateway-Node.
 * 
 * @author kamir
 */
public class SOLRTool extends ClusterGateway {

    
    static String GWHOST = "training09.mtv.cloudera.com";
    
    //static String ZK = "172.16.14.228:2181/solr";
    static String ZK = "training01.sjc.cloudera.com:2181,training03.sjc.cloudera.com:2181,training06.sjc.cloudera.com:2181/solr";
    
    static String SOLR = "127.0.0.1:8983/solr";
    
    // path on Gateway server in which all files are stored before ZOOKEEPER upload.
    static String HOME = "/home/mirko.kaempf";

    // path of Collections in HDFS
    static String DATAHOME = "/solr";

    /**
     * The default number of shards for collection creation.
     */ 
    static int SHARDS = 1;    
    
    
    public static String projectContext = null;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // We do the core management on the cluster.
        // An SSH login to a GatewayNode is required.
        
        String collection = "FAQMails02";

        SOLRTool.init( GWHOST , "mirko.kaempf" , null );
//        SOLRTool.init( "127.0.0.1" , "cloudera" , "cloudera" );
        
        // SOLRTool.prepareCollection( "127.0.0.1:2181/solr", collection );
        
        SOLRTool.close();
        
        bw.flush();
        
        System.exit( 0 );
        
    }    

    public static void listCollection() {
        listCollection( true );   
    }

    public static void listCollection(boolean closeAtEnd) {
         
//        String cmd1 = "solrctl --solr " + SOLR + " --zk " + ZK + " instancedir --list";
        String cmd1 = "solrctl --zk " + ZK + " instancedir --list";

        if ( bw != null ) {
            try {
                bw.write(cmd1 + "\n");
            } 
            catch (IOException ex) {
                Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        tool.open();
         
        tool.executeRemoteCommand( cmd1 );
       
        if( closeAtEnd ) tool.close();
       
    }
    
    /**
     * The current collection has to be optimized and copied to local disc for 
     * DOWNLOAD and of data and configuration. 
     * 
     * @param zk
     * @param coll 
     */
    public static void packageCollection(String zk, String coll) {

        tool.open();
        
        String locationOfCFG =  HOME + "/" + coll + "SearchConfig";
        
        String locationOfDATA = DATAHOME + "/" + coll + "Collection";

        System.out.println( ">> Instancedir: " + locationOfCFG );
        System.out.println(  ">> Datadir:    " + locationOfDATA );
 
        String tempDirName = System.currentTimeMillis() + ".stage";

        tool.executeRemoteCommand( "mkdir " + "/data/2/tmp/" + tempDirName );
        tool.executeRemoteCommand( "mkdir " + "/data/2/tmp/" + tempDirName + "/data" );
        
        tool.executeRemoteCommand( "cp -r " + locationOfCFG + " /data/2/tmp/" + tempDirName + "/cfg/" );
        tool.executeRemoteCommand( "hadoop fs -get " + locationOfDATA + " /data/2/tmp/" + tempDirName + "/data/" );

        tool.executeRemoteCommand( "tar cfzv /data/2/tmp/TTFAQ_package_" + tempDirName + ".tar.gz /data/2/tmp/" + tempDirName );
        
        tool.executeRemoteCommand( "ls -sh /data/2/tmp/");
        
        tool.scpTo(zk, zk, coll);
        
        
        
    }

    public static void prepareCollection(String zk, String coll) {
         
//        String cmd1 = "solrctl --solr " + SOLR + " --zk " + ZK + " instancedir --generate " + HOME + "/" + coll + "SearchConfig";
//       String cmd2 = "solrctl --solr " + SOLR + " --zk " + ZK + " instancedir --create " + coll + "Collection " + HOME + "/" + coll + "SearchConfig";
//        String cmd3 = "solrctl --solr " + SOLR + " --zk " + ZK + " collection --create " + coll + "Collection -s " + SHARDS; 
        String cmd1 = "solrctl --zk " + ZK + " instancedir --generate " + HOME + "/" + coll + "SearchConfig";
        String cmd2 = "solrctl --zk " + ZK + " instancedir --create " + coll + "Collection " + HOME + "/" + coll + "SearchConfig";
        String cmd3 = "solrctl --zk " + ZK + " collection --create " + coll + "Collection -s " + SHARDS; 

        if ( bw != null ) {
            try {
                bw.write(cmd1 + "\n");
                bw.write(cmd2 + "\n");
                bw.write(cmd3 + "\n");
            } 
            catch (IOException ex) {
                Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        tool.open();
        
        tool.executeRemoteCommand( cmd1 );
        
        tool.executeRemoteCommand( cmd2 );

        tool.executeRemoteCommand( cmd3 );

        tool.executeRemoteCommand( "ls " + HOME );
        
        tool.executeRemoteCommand( "solrctl collection --list");
       
        tool.close();
        
        downloadSchema( HOME + "/" + coll + "SearchConfig" );      
       
    }

    public static void uploadAndPublish(String coll) {

        // copy the localy changed shema file 
        String fnRemote = HOME + "/" + coll + "SearchConfig/conf/schema.xml";
        String fnLocal = projectContext + "/conf/schema.xml";

        System.out.println("Project context : " + projectContext);
        System.out.println("local file      : " + fnLocal);
        System.out.println("remote file     : " + fnRemote);
        
//        String cmd3 = "solrctl --solr " + SOLR + " --zk " + ZK + " instancedir --update " + coll + "Collection " + HOME + "/" + coll + "SearchConfig"; // -s " + SHARDS; 
//        String cmd4 = "solrctl --solr " + SOLR + " --zk " + ZK + " collection --reload " + coll + "Collection"; // -s " + SHARDS; 
        String cmd3 = "solrctl --zk " + ZK + " instancedir --update " + coll + "Collection " + HOME + "/" + coll + "SearchConfig"; // -s " + SHARDS; 
        String cmd4 = "solrctl --zk " + ZK + " collection --reload " + coll + "Collection"; // -s " + SHARDS; 

        tool.open();
        tool.scpTo(fnLocal, fnRemote, cmd3);
        tool.executeRemoteCommand(cmd4);
        
        if ( bw != null ) {
            try {
                bw.write(cmd3 + "\n");
                bw.write(cmd4 + "\n");
            } 
            catch (IOException ex) {
                Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }

    public static void downloadSchema(String path) {
        
        // remote file 
        String fnRemote = path + "/conf/schema.xml";
        String fnLocal = projectContext + "/conf/schema.xml";
        
        javax.swing.JOptionPane.showMessageDialog(
                null, "If not done locally already, than please copy from (remote): \n\t" + fnRemote + "\nto (local):\n\t" + fnLocal);
        
    }
    
    public static void setProjectContext(File selectedFile) {
        projectContext = selectedFile.getAbsolutePath();
    }

    /**
     * 
     * TODO: 
     * 
     * All available Collection should be listed, followed by the number
     * of documents in it.
     */
    public static void openCollectionListFrame() {
        
    }
    
    public static void flushDebugScript() throws IOException {
        bw.flush();
        bw.close();
        initDebugScripter();
    }

    public static void closeDebugScript() throws IOException {
        bw.flush();
        bw.close();
    }
    
}
