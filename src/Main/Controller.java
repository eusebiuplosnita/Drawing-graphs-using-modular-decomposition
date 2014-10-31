/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import MDDrawing.Box;
import MDDrawing.Center;
import MDDrawing.MDDrawing;
import MDDrawing.Node;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 *
 * @author Albert
 */
public class Controller {
    
    private String file;
    private  JGraphModelAdapter m_jgAdapter;
    
    private  final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
       private  final Dimension DEFAULT_SIZE = new Dimension( 530, 320 );
    
    public Controller(String file)
    {
        this.file = file;
    }
    
    public void Draw()
    {
        MDDrawing mdd = new MDDrawing(file);
                mdd.DrawModule();
                
                
                //MDDrawing d = new MDDrawing(args[0]);
                
                // create a JGraphT graph
                ListenableGraph g = new ListenableUndirectedGraph( DefaultEdge.class );
                
                // create a visualization using JGraph, via an adapter
                m_jgAdapter = new JGraphModelAdapter( g );
                
                JGraph jgraph = new JGraph( m_jgAdapter );
                //jgraph.setPreferredSize(new Dimension(2000,2000));
                jgraph.setSize(new Dimension(5000,5000));
                adjustDisplaySettings( jgraph );
                
                
                //  add( jgraph );
                //resize( DEFAULT_SIZE );
                
                // add some sample data (graph manipulated via JGraphT)
                double maxy=-1, maxx=-1;
                for(Node n : mdd.leaves)
                {
                    if(n.getC().getX() > maxx)
                    {
                        maxx = n.getC().getX();
                    }
                    if(n.getC().getY() > maxy)
                    {
                    maxy = n.getC().getY();
                    }
                }
        
        double coef_x, coef_y;
         if(maxx > 1080 || maxy > 1920)
        {
            coef_x = maxx / 1000;
            coef_y = maxy / 1000;
        
                scalate(mdd,coef_x, coef_y);
                rescalate(mdd);
                maxx = 0;
                maxy = 0;
            for(Node n : mdd.leaves)
                {
                    if(n.getC().getX() > maxx)
                    {
                        maxx = n.getC().getX();
                    }
                    if(n.getC().getY() > maxy)
                    {
                    maxy = n.getC().getY();
                    }
                }
            if(maxx > 1000 || maxy > 1000)
            {
                coef_x = maxx / 1000;
                coef_y = maxy / 1000;
        
                scalate(mdd,coef_x, coef_y);
            }
        }
                for(Node n : mdd.leaves)
                {
                    g.addVertex( n.getLabel() );
                    positionVertexAt( n.getLabel(), (int)n.getC().getX(), (int)n.getC().getY() , n.getB());
                }
                
                for(int i = 0; i < mdd.edges.size(); i++)
                {
                    g.addEdge( mdd.edges.get(i).getV1().getLabel(), mdd.edges.get(i).getV2().getLabel());
                }
                
                // position vertices nicely within JGraph component
                
                //jgraph.getGraphLayoutCache().insert(g);
                JFrame frame = new JFrame();
                               
               // get the screen size as a java dimension
               Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                System.out.println(screenSize.height +" "+ screenSize.width);
                int height = screenSize.height +2000;
                int width = screenSize.width + 2000;
                
                 //set the jframe height and width
                frame.setPreferredSize(new Dimension(width, height));
                frame.setResizable(true);
                
                frame.getContentPane().add(new JScrollPane(jgraph,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
                
                //frame.getContentPane().add(new JPanel((LayoutManager) jgraph));
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                
                OutputStream out;
        try {
            out = new FileOutputStream("graph.jpg");
                BufferedImage img = jgraph.getImage(DEFAULT_BG_COLOR, 100);
        
            ImageIO.write(img, "jpg", out);
       
            out.flush();
       
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
     private  void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        c = Color.white;

        jg.setBackground( c );
    }


    private  void positionVertexAt( Object vertex, int x, int y , Box box) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map attr = cell.getAttributes(  );
        
        Rectangle2D b    =  GraphConstants.getBounds( attr );

        GraphConstants.setBounds( attr, new Rectangle( x, y, (int)box.getH()+1,(int)box.getW()+1) );

        Map cellAttr = new HashMap();
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null);
    }

    private void scalate(MDDrawing mdd, double coef_x, double coef_y)
    {
        
        for(Node n: mdd.leaves)
        {
            n.setC(new Center(n.getC().getX()/coef_x, n.getC().getY()/coef_y));
            n.setB(new Box(n.getB().getH()/coef_x, n.getB().getW()/coef_y));
        }
    }

    private int distinct_values(boolean x, ArrayList<Node> nodes)
    {
        int k =1;
        if(x)
        {   
            for(int i = 0; i < nodes.size(); i++)
            {
                if(i!=0 && nodes.get(i-1).getC().getX() < nodes.get(i).getC().getX())
                {
                    k++;
                }
            }
        }else
        {
            for(int i = 0; i < nodes.size(); i++)
            {
                if(i!=0 && nodes.get(i-1).getC().getY() < nodes.get(i).getC().getY())
                {
                    k++;
                }
            }
        }
        return k;
        
    }
    
    private void rescalate(MDDrawing mdd) {
        sort(true,mdd);
        double ratia = 1000/distinct_values(true, mdd.leaves);
        double displacement =0;
        double curent_x = mdd.leaves.get(0).getC().getX();
        for(int i = 0; i < mdd.leaves.size(); i++)
        {
            displacement += ratia;
            curent_x = mdd.leaves.get(i).getC().getX();
            while( i< mdd.leaves.size() && mdd.leaves.get(i).getC().getX() == curent_x)
            {
                mdd.leaves.get(i).getC().setX( displacement);
                mdd.leaves.get(i).getB().setH( ratia/2);
                i++;
            }
            i--;
        }
        
        sort(false,mdd);
        ratia = 1000/distinct_values(false, mdd.leaves);
        displacement =0;
        for(int i = 0; i < mdd.leaves.size(); i++)
        {
            //displacement +=  0.3*displacement + mdd.leaves.get(i).getB().getW()/2;
           displacement += ratia;
            curent_x = mdd.leaves.get(i).getC().getY();
            while( i< mdd.leaves.size() && mdd.leaves.get(i).getC().getY() == curent_x )
            {
                mdd.leaves.get(i).getC().setY( displacement);
                mdd.leaves.get(i).getB().setW( ratia/2);
                i++;
            }
            i--;
        }
    }
    
    private void sort(boolean x, MDDrawing mdd)
    {
        if(x)
        {
            Node interm;
            for(int i = 0; i < mdd.leaves.size(); i++)
            {
                for(int j = 0; j < mdd.leaves.size(); j++)
                {
                    if(mdd.leaves.get(i).getC().getX() < mdd.leaves.get(j).getC().getX() && i != j)
                    {
                        interm = mdd.leaves.get(i);
                        mdd.leaves.set(i,mdd.leaves.get(j));
                        mdd.leaves.set(j,interm);
                    }
                }
            }
        }
        else
        {
            Node interm;
            for(int i = 0; i < mdd.leaves.size(); i++)
            {
                for(int j = 0; j < mdd.leaves.size(); j++)
                {
                    if(mdd.leaves.get(i).getC().getY() < mdd.leaves.get(j).getC().getY() && i != j)
                    {
                        interm = mdd.leaves.get(i);
                        mdd.leaves.set(i,mdd.leaves.get(j));
                        mdd.leaves.set(j,interm);
                    }
                }
            }
        }
    }
}
