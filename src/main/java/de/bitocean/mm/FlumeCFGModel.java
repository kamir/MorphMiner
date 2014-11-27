package de.bitocean.mm;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;

import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 * 
 * Depends on Jung2
 * 
 * http://sourceforge.net/projects/jung/files/latest/download?source=files
 * 
 * 
 
    <dependency>
        <groupId>net.sf.jung</groupId>
        <artifactId>jung2</artifactId>
        <version>2.0.1</version>
        <type>pom</type>
    </dependency>
    * 
    <dependency>
        <groupId>net.sf.jung</groupId>
        <artifactId>jung-graph-impl</artifactId>
        <version>2.0.1</version>
        <type>jar</type>
        <scope>compile</scope>
    </dependency>
    
 * 
 * @author training
 *
 */
public class FlumeCFGModel {
	
	Hashtable<String, Agent> agents = new Hashtable<String, Agent>();

	public static void main(String[] args) throws IOException {

		File flumeCFG = new File( "flumecfg/spooldir.conf" );
		File flumeCFG2 = new File( "flumecfg/spooldir2.conf" );
		File flumeCFG3 = new File( "flumecfg/spooldir3.conf" );
		
		FlumeCFGModel fp1 = new FlumeCFGModel( flumeCFG );
		FlumeCFGModel fp2 = new FlumeCFGModel( flumeCFG2 );
		FlumeCFGModel fp3 = new FlumeCFGModel( flumeCFG3 );
	
		fp1.show( flumeCFG.getAbsolutePath() );
		fp2.show( flumeCFG2.getAbsolutePath() );
		fp3.show( flumeCFG3.getAbsolutePath() );
		
	}
	
	public void show(String title) {
		tv.show( title );
	}
	
	TreeViewer tv = null;
	
	public FlumeCFGModel( File f ) throws IOException{
				
		BufferedReader br = new BufferedReader(new FileReader( f ) );

		while (br.ready()) {

			String line = br.readLine();
					
			if (line.length() > 2 && !line.startsWith("#")) {

				String pair[] = line.split("=");

				String prop = pair[0].trim();
				String value = pair[1].trim();

				System.out.println("[" + prop + "]{" + value + "}");

				Agent a = getAgent(prop, value);
			}
		}

		br.close();

		for (String name : agents.keySet()) {
			Agent a = agents.get(name);
			a.describe();
		}
 
		if ( tv == null ) tv = new TreeViewer();

		tv.createTree(agents, f );
		
	}

	private Agent getAgent(String prop, String value) {
		// System.out.println( "  ??? " + prop );
		String[] agentNameA = prop.split("\\.");
		// System.out.println( "  ==> " + agentNameA.length );

		String agentName = agentNameA[0];
		Agent a = agents.get(agentName);
		if (a == null)
			a = new Agent(agentName);
		agents.put(agentName, a);

		System.out.println( prop + "  ==> " + value );
		a.addComponent(prop, value);

		return a;
	}

}

class Agent {

	String an = null;

	public Agent(String agentName) {
		an = agentName;
		
		all.put("sources", sources);
		all.put("sinks", sinks);
		all.put("channels", channels);
		all.put("interceptors", interceptors);
		
	}

	public void addComponent(String prop, String value) {
		
		String[] compgroupNamesA = prop.split("\\.");

		String group = compgroupNamesA[1];

		if ( compgroupNamesA.length == 2 ) {
		
		if ( group.equals("sources") ) {

			String[] cns = value.split(" ");
			for (String cn : cns) {

				Component c = sources.get(cn);

				if (c == null) {
					c = new Component(cn);
					sources.put(cn, c);
				}

			}
		}

		if (group.equals("interceptors")) {

			String[] cns = value.split(" ");
			for (String cn : cns) {

				Component c = interceptors.get(cn);
				if (c == null) {
					c = new Component(cn);
					interceptors.put(cn, c);
				}
			}
		}

		if (group.equals("channels")) {

			String[] cns = value.split(" ");
			for (String cn : cns) {

				Component c = channels.get(cn);
				if (c == null) {
					c = new Component(cn);
					channels.put(cn, c);
				}
			}

		}

		if (group.equals("sinks")) {

			String[] cns = value.split(" ");
			for (String cn : cns) {

				Component c = sinks.get(cn);
				if (c == null) {
					c = new Component(cn);
					sinks.put(cn, c);
				}
			}

		}
		
		}
		else {
			
			String k = compgroupNamesA[2];
			
			String pn = compgroupNamesA[3];
			System.out.println( compgroupNamesA.length + "(" + k +"#"+ pn + "=" + value + ")" );
			
			Hashtable<String, Component> comps = all.get( group ); 
			
			Component c = comps.get(k);
			c.props.put(pn, value);		
			
		}

	}

	public void describe() {
		System.out.println("[Agent:" + an + "]");
		System.out.println("    [Sources      :" + sources.size() + "]");
		System.out.println("    [Interceptors :" + interceptors.size() + "]");
		System.out.println("    [Channels     :" + channels.size() + "]");
		System.out.println("    [Sinks        :" + sinks.size() + "]");
	}

	Hashtable<String,Hashtable<String, Component>> all = new Hashtable<String,Hashtable<String, Component>>(); 
	
	Hashtable<String, Component> sources = new Hashtable<String, Component>();
	Hashtable<String, Component> sinks = new Hashtable<String, Component>();
	Hashtable<String, Component> channels = new Hashtable<String, Component>();
	Hashtable<String, Component> interceptors = new Hashtable<String, Component>();

}

class Component {
	String cn = null;

	public Component(String compname) {
		cn = compname;
	}

	Hashtable<String, String> props = new Hashtable<String, String>();
}
