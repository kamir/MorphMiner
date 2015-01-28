/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.dspm.examples;
 
import edu.uci.ics.jung.io.*;
import edu.uci.ics.jung.io.graphml.*;
import java.io.*;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.graph.*;


/**
 * This example shows how to read and map a graphml file into JUNG graph objects 
 * @see http://ansaurus.com/question/2662046-how-do-i-use-graphmlreader2-in-jung
 *  
 * @author actran
 *
 */


class Vertex{
	String id;
	String name;
	String expression;
	
	public String toString() {
		return "<node id=\"" + id + "\">\n\t<data key=\"name\">" + name 
				+ "</data>\n\t<data key=\"expression\">" + expression + "</data>\n</node>";
	}
}

class Edge{
	String source;
	String target;
	double weight;
	
	public String toString() {
		return "<edge source=\"" + source + "\" target=\"" + 
				target + "\">\n\t<data key=\"weight\">" + weight + "</data>\n</edge>";
	}
}

public class JUNGExample {
    
	//public static final String GRAPHML_ACTIVITY_DEPENDENCY_FILE = "./data/DSPM_Demo/DSPM_01.graphml";
	public static final String GRAPHML_ACTIVITY_DEPENDENCY_FILE = "./data/activity-dependency-graphml.xml";
	

	public static void  main( String[] args) throws GraphIOException, FileNotFoundException, IOException{
            
		
                Reader reader = new FileReader(GRAPHML_ACTIVITY_DEPENDENCY_FILE);
	
                BufferedReader br = new BufferedReader( reader );
                while( br.ready() ) {
                    System.out.println( br.readLine() );
                }
                
		Transformer<NodeMetadata, Vertex> vtrans = new Transformer<NodeMetadata,Vertex>(){
			public Vertex transform(NodeMetadata nmd){
				Vertex v = new Vertex();				
				v.id = nmd.getId();
				v.name = nmd.getProperty("name");
				v.expression = nmd.getProperty("expression");
				return v;
			}
		};
		
		Transformer<EdgeMetadata, Edge> etrans = new Transformer<EdgeMetadata,Edge>(){
			public Edge transform(EdgeMetadata emd){
                                System.out.println( emd.toString() );
				Edge e = new Edge() ;
                                try{
        				e.source = emd.getSource();
                                }
                                catch(Exception ex) {
                                
                                }
				e.target = emd.getTarget();
				e.weight = Double.parseDouble(emd.getProperty("weight"));
				return e;
			}
		};

		Transformer<HyperEdgeMetadata, Edge> hetrans = new Transformer<HyperEdgeMetadata,Edge>(){ 
			public Edge transform(HyperEdgeMetadata emd){				
				Edge e = new Edge();
                                try {
                                    e.source = emd.getProperty("source");
                                }
                                catch( Exception ex ) {
                                
                                }
    				e.target = emd.getProperty("target");
				e.weight = Double.parseDouble(emd.getProperty("weight"));
				return e;
			}
		};
		Transformer< GraphMetadata , DirectedSparseGraph<Vertex,Edge>> gtrans = new Transformer<GraphMetadata,DirectedSparseGraph<Vertex,Edge>>(){
			public DirectedSparseGraph<Vertex,Edge> transform( GraphMetadata gmd ){
				return new DirectedSparseGraph<Vertex,Edge>();
			}
		};

		GraphMLReader2<DirectedSparseGraph<Vertex,Edge> , Vertex , Edge> gmlr =
			//new GraphMLReader2<UndirectedSparseGraph<Vertex,Edge>, Vertex, Edge>(fileReader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer)
			new GraphMLReader2<DirectedSparseGraph<Vertex,Edge> ,Vertex, Edge>(
					reader,
					gtrans,
					vtrans,
					etrans,
					hetrans);

		DirectedSparseGraph<Vertex,Edge> g = gmlr.readGraph();
		
		System.out.println("Number of vetex: " + g.getVertexCount());
		System.out.println("Number of edges: " + g.getEdgeCount());
		
		System.out.println("==========================");
		System.out.println("Vertices");
		System.out.println("==========================");
		for (Vertex v : g.getVertices()) {
			System.out.println(v);
		}

		System.out.println("==========================");
		System.out.println("Edges");
		System.out.println("==========================");
		for (Edge e : g.getEdges()) {
			System.out.println(e);
		}

	}
}