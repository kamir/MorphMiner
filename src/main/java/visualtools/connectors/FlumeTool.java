package visualtools.connectors;



import visualtools.connectors.ClusterGateway;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

/**
 *
 * @author kamir
 */
public class FlumeTool extends ClusterGateway {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // We do the core management on the cluster.
        // An SSH login to a GatewayNode is required.
        
        
        FlumeTool.init( "master2" , "root" , "cloudera" );

        // is Flume CFG reloaded???
        FlumeTool.updateConfiguration( "/Users/kamir/etosha/p1/flume.conf", "/home/etosha/p1/flume.conf" );

        FlumeTool.runAgentWithConfiguration( "/home/etosha/p1/flume.conf", "agent1" );
        
        FlumeTool.close();
        
        System.exit( 0 );

    }    

    public static void updateConfiguration(String userskamiretoshap1fljumeconf, String homeetoshap1fljumeconf) {
        
    }

    public static void runAgentWithConfiguration(String userskamiretoshap1fljumeconf, String homeetoshap1fljumeconf) {
    
    }

    
   
  

   

    
  

    
    
    
}
