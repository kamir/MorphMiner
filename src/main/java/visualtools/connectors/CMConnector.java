/**
 * 
 *  In the future, we interact with CM via the REST-API.
 *
 **/
package visualtools.connectors;

/**
 *
 * @author kamir
 *
 **/
public interface CMConnector {
    
    public String[] getFlumeHosts();
    
    public String[] getSolrHosts();
        
    public boolean submitFlumeConfiguration(String flumeCfg );
    
    public boolean submitMorphlineConfiguration(String morphCfg );
    
    public boolean startFlumeAgent( String agent, String host );
    
}
