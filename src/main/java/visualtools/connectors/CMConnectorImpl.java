/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualtools.connectors;

import visualtools.connectors.CMConnector;

/**
 *
 * @author kamir
 */
public class CMConnectorImpl implements CMConnector {

    @Override
    public String[] getFlumeHosts() {
        
        String[] s = new String[1];
        s[0] = "127.0.0.1";
        
        return s;
        
    }

    @Override
    public String[] getSolrHosts() {
        
        String[] s = new String[1];
        s[0] = "127.0.0.1";
        
        return s;
        
    }

    @Override
    public boolean submitFlumeConfiguration(String flumeCfg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean submitMorphlineConfiguration(String morphCfg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean startFlumeAgent(String agent, String host) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
