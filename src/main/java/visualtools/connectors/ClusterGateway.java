package visualtools.connectors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import static visualtools.connectors.SOLRTool.bw;

/**
 *
 * @author kamir
 */
public class ClusterGateway {
    
    public static BufferedWriter bw = null;
    public static ClusterGateway tool = null;

    public static boolean ping(String h, String u, String p) {
        boolean isAvailable = true;
        try {
            init( h, u, p );
            
            isAvailable = tool.open();
            
            tool.close();
        
        }
        catch(Exception ex){
          isAvailable = false;
          ex.printStackTrace();
        } 
        return isAvailable;
    }
    
    SSHExec ssh = null;
    
    private String host = null;
    private String user = null;
    private String pass = null;
    
    ClusterGateway(){
    
    };
    
    private ClusterGateway(String h, String u, String p) {
        host = h;
        user = u;
        pass = p;
    }

    public static void reset()  {
        tool.close();
        tool = null;
    }
    
    public static void init(String h, String u, String p) throws IOException {

        if ( p == null ) {
            p = javax.swing.JOptionPane.showInputDialog("Password for ("+u+"): ");
        }
        
        tool = new ClusterGateway( h , u , p );
         
         String SOLRHost = h + ":8983/solr";

         SOLRTool.SOLR = SOLRHost;
         
         System.out.println( "\n### ClusterGateway : " + u + "@" + h );
         System.out.println(   "### SOLRHost       : " + SOLRTool.SOLR );
         System.out.println(   "### Zookeeper      : " + SOLRTool.ZK + "\n" );
         
         initDebugScripter();
    }
    
    /**
     * 
     * For easy debugging the remote commands are collected.
     * 
     * @throws IOException 
     */
    public static void initDebugScripter() throws IOException {
        File f = File.createTempFile("debug_solrctl_script_", ".sh", new File("./tmp") );
        
        System.out.println(   "Debug script: " + f.getAbsolutePath() + "\n" );
         
        bw = new BufferedWriter( new FileWriter( f ) );
    }
    
    
    /**
     * Copy a file to the Gateway-Node.
     *
     */
    public void scpTo(String l , String r, String cmd) {
        try {
            // https://code.google.com/p/sshxcute/

            System.out.println("\n[SCP]: " + l + "=> " + r + "\n");
       
            
            // CustomTask ct1 = new ExecShellScript("/home/tsadmin","./sshxcute_test.sh","hello world");
            ssh.connect();  // After connection
            ssh.uploadSingleDataToServer(l, r);
            executeRemoteCommand(cmd);  
            
        } 
        catch (Exception ex) {
            Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
  
    
    /**
     * Execute some remote tasks ...
     * 
     * @param cmd 
     */
    public void executeRemoteCommand(String cmd) {
        try {
            // https://code.google.com/p/sshxcute/

            System.out.println("\n[CMD]: " + cmd + "\n");
            
            CustomTask ct1 = new ExecCommand( cmd );
            ssh.exec(ct1);
            
            System.out.println("\n[DONE]\n");
            
        } 
        catch (Exception ex) {
            Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close() {
        
            if ( tool != null ) {
                if ( tool.ssh != null )
                    tool.ssh.disconnect();
            }
    }
      
    public boolean open() {
      
            // https://code.google.com/p/sshxcute/
            
            // Initialize a ConnBean object, parameter list is ip, username, password
            ConnBean cb = new ConnBean(host, user, pass );
            // Put the ConnBean instance as parameter for SSHExec static method getInstance(ConnBean) to retrieve a singleton SSHExec instance
            ssh = SSHExec.getInstance(cb);          
            // Connect to server
            return ssh.connect(); 
      
    }
    
}
