/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.mm;

import java.io.File;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.cmd.EtoshaContextLogger;
import org.etosha.core.sc.connector.SemanticContextBridge;

/**
 *
 * @author kamir
 */
public class MMAppRunner extends Configured implements Tool {
    
    public static MMApp app = null;
    public static SemanticContextBridge scb = null;
    
    public static void main(String[] args) throws Exception {
        
        Configuration cfg = new Configuration();

        File cfgFile = EtoshaContextLogger.getCFGFile();
        
        System.out.println( ">>> CFG:   " + cfgFile.getAbsolutePath() );
        System.out.println( ">>> exists : " + cfgFile.exists() );

        
        /**
         * according to:
         *
         * http://stackoverflow.com/questions/11478036/hadoop-configuration-property-returns-null
         *
         * we add the resource as a URI
         */
        cfg.addResource(cfgFile.getAbsoluteFile().toURI().toURL());
        cfg.reloadConfiguration();
        System.out.println(cfg);

        System.out.println( cfg.getRaw("smw.url") );
        System.out.println( cfg.get("smw.pw") );
        System.out.println( cfg.get("smw.user") );   // for SMW account
        System.out.println( cfg.get("smw.env") );
        
        SemanticContextBridge.overWriteEnvForLocaltest = false;
      
        int exitCode = ToolRunner.run(cfg, new MMAppRunner(), args);

         
    }

    public int run(String[] strings) throws Exception {
        scb = new SemanticContextBridge( new Configuration() );
        scb.login();
        System.out.println("*** SemanticContextBridge.init() # done! ***\n");
        app = new MMApp();
        return app.run(strings);    
    }


}
