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
    
    String host = null;
    String user = null;
    String pass = null;
    
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
         //if ( tool == null )
         tool = new ClusterGateway( h , u , p );
         initDebugScripter();
    }
    
    public static void initDebugScripter() throws IOException {
        File f = File.createTempFile("debug_solrctl_script_", ".sh", new File(".") );
        bw = new BufferedWriter( new FileWriter( f ) );
    }
    
    public void scpTo(String l , String r, String cmd) {
        try {
            // https://code.google.com/p/sshxcute/

            // CustomTask ct1 = new ExecShellScript("/home/tsadmin","./sshxcute_test.sh","hello world");
            ssh.connect();  // After connection
            ssh.uploadSingleDataToServer(l, r);
            executeRemoteCommand(cmd);  
            
        } 
        catch (Exception ex) {
            Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public void executeRemoteCommand(String cmd) {
        try {
            // https://code.google.com/p/sshxcute/

            CustomTask ct1 = new ExecCommand( cmd );
            ssh.exec(ct1);
                    
        } 
        catch (Exception ex) {
            Logger.getLogger(SOLRTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close() {
        
            tool.ssh.disconnect();
    
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
