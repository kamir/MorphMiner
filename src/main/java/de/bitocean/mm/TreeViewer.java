package de.bitocean.mm;

import de.bitocean.mm.Agent;
import de.bitocean.mm.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * Demonsrates TreeLayout and RadialTreeLayout.
 * @author Tom Nelson
 * 
 */
@SuppressWarnings("serial")
public class TreeViewer extends JApplet {

    /**
     * the graph
     */
    Forest<String,Integer> graph;
    
    Factory<DirectedGraph<String,Integer>> graphFactory = 
    	new Factory<DirectedGraph<String,Integer>>() {

			public DirectedGraph<String, Integer> create() {
				return new DirectedSparseMultigraph<String,Integer>();
			}
		};
			
	Factory<Tree<String,Integer>> treeFactory =
		new Factory<Tree<String,Integer>> () {

		public Tree<String, Integer> create() {
			return new DelegateTree<String,Integer>(graphFactory);
		}
	};
	
	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i=0;
		public Integer create() {
			return i++;
		}};
    
    Factory<String> vertexFactory = new Factory<String>() {
    	int i=0;
		public String create() {
			return "V"+i++;
		}};

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String,Integer> vv;
    
    VisualizationServer.Paintable rings;
    
    String root;
    
    TreeLayout<String,Integer> treeLayout;
    
    RadialTreeLayout<String,Integer> radialLayout;

    public TreeViewer() {
        
        // create a simple graph for the demo
        graph = new DelegateForest<String,Integer>();
    }
    
    public void finishTree() {
        
        treeLayout = new TreeLayout<String,Integer>(graph);
        radialLayout = new RadialTreeLayout<String,Integer>(graph);
        radialLayout.setSize(new Dimension(600,600));
        vv =  new VisualizationViewer<String,Integer>(treeLayout, new Dimension(600,600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        rings = new Rings();

        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        
        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });
        
        JToggleButton radial = new JToggleButton("Radial");
        radial.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					
					LayoutTransition<String,Integer> lt =
						new LayoutTransition<String,Integer>(vv, treeLayout, radialLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.addPreRenderPaintable(rings);
				} else {
					LayoutTransition<String,Integer> lt =
						new LayoutTransition<String,Integer>(vv, radialLayout, treeLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.removePreRenderPaintable(rings);
				}
				vv.repaint();
			}});

        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(radial);
        controls.add(scaleGrid);
        controls.add(modeBox);

        content.add(controls, BorderLayout.SOUTH);
    }
    
    class Rings implements VisualizationServer.Paintable {
    	
    	Collection<Double> depths;
    	
    	public Rings() {
    		depths = getDepths();
    	}
    	
    	private Collection<Double> getDepths() {
    		Set<Double> depths = new HashSet<Double>();
    		Map<String,PolarPoint> polarLocations = radialLayout.getPolarLocations();
    		for(String v : graph.getVertices()) {
    			PolarPoint pp = polarLocations.get(v);
    			depths.add(pp.getRadius());
    		}
    		return depths;
    	}

		public void paint(Graphics g) {
			g.setColor(Color.lightGray);
		
			Graphics2D g2d = (Graphics2D)g;
			Point2D center = radialLayout.getCenter();

			Ellipse2D ellipse = new Ellipse2D.Double();
			for(double d : depths) {
				ellipse.setFrameFromDiagonal(center.getX()-d, center.getY()-d, 
						center.getX()+d, center.getY()+d);
				Shape shape = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).transform(ellipse);
				g2d.draw(shape);
			}
		}

		public boolean useTransform() {
			return true;
		}
    }
    
    
    /**
     * 
     */
    public void createTree( Hashtable<String, Agent> agents, File f ) {
    	
    	graph.addVertex("f:" + f.getName() );
    	
    	for( String n : agents.keySet() ) {
    		Agent a = agents.get(n);
    		graph.addEdge(edgeFactory.create(), "f:" + f.getName(), "a:" + a.an );
    		
    		// process agents data 
    		Hashtable<String, Component> sources = a.sources;
    		Hashtable<String, Component> channels = a.channels;
    		
    		for( String sn : sources.keySet() ) {
        		graph.addEdge(edgeFactory.create(), "a:" + a.an, "src:"+sn );
        		
        		Enumeration<Component> en = channels.elements();
        		while( en.hasMoreElements() ) {
        			Component c = en.nextElement();
        			graph.addEdge( edgeFactory.create(), "src:"+sn, "c:" + c.cn );
          		} 
    		}
    		
    		Hashtable<String, Component> sinks = a.sinks;
    		for( String sn : sinks.keySet() ) {
    			Component c = sinks.get(sn);
    			//System.out.println( "Component:" + c.cn + " <" + c.props.size() + ">");
    			String channelName = c.props.get("channel");
    			
    			graph.addEdge(edgeFactory.create(), "c:" + channelName, "sk:"+sn );
        		
    		}

    	}

    	
       	   	
    	finishTree();
    }
     


    /**
     * a driver for this demo
     */
    public void show(String title) {
    	
        JFrame frame = new JFrame();
        frame.setTitle(title);
        
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        content.add( this );
        frame.pack();
        frame.setVisible(true);
    }
}
