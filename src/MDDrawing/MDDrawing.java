/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import licenta_md.Graph;
import licenta_md.MDTree;
import licenta_md.RootedTreeNode;
import licenta_md.FactPermElement;
import licenta_md.RootedTree;
import licenta_md.RecSubProblem;
import licenta_md.MDTreeNode;
import licenta_md.MDTreeLeafNode;
import licenta_md.MDTree;
import licenta_md.MDNodeType;
import licenta_md.Vertex;

/**
 *
 * @author eusebiu.plosnita
 */
public class MDDrawing {
    
    private MDTreeNode root;
    private ArrayList<Set<Node>> levels;
    public int h;//the depth of MDT
    public ArrayList<Edge> edges;
    public ArrayList<Node> vertices;
    public ArrayList<Node> leaves;
    
   public MDDrawing(String fisier)
   {
       Graph graph = new Graph(fisier);
       System.out.println(graph.getMDTree());
       MDTree mdt = graph.getMDTree();
       RootedTreeNode rtn = mdt.getRoot().getFirstChild();
       System.out.println(rtn.toString());
       this.root = (MDTreeNode) mdt.getRoot();
       this.edges = new ArrayList<Edge>();
       this.vertices = new ArrayList<Node>();
       
       this.levels = new ArrayList<Set<Node>>();
       for(int i = 0; i < graph.getNumVertices(); i++)
       {
           levels.add(new HashSet<Node>());
       }
       
       initialize();
       
       addEdges();
       
   }
   
   /**
    * get the edges from the structure where is retained the modular decomposition tree
    */
   private void addEdges()
   {
       Node n1, n2;
       for(int i = 0; i < this.vertices.size(); i++)
       {
           n1 =this.vertices.get(i); 
           if(n1.children.isEmpty())
           {
               MDTreeLeafNode tln = (MDTreeLeafNode)n1.getT();
               System.out.println(tln.vertex.label);
               Collection<Vertex> neighbours =  tln.vertex.getNeighbours();
               for(Vertex v : neighbours)
               {
                   for(int k = 0; k < this.vertices.size(); k++)
                   {
                       if(i != k && this.vertices.get(k).children.isEmpty())
                       {
                           MDTreeLeafNode tln1 = (MDTreeLeafNode)this.vertices.get(k).getT();
                           if(tln1.vertex.label == v.label)
                           {
                               n1.vecini.add(this.vertices.get(k));
                               this.edges.add(new Edge(n1, this.vertices.get(k),30));
                           }
                       }
                   }
               }
           }
       }
   }
   
   /**
    * initialize the levels list and the leaves list
    */
   private void initialize()
   {
       int nivel = 0;
       MDTreeNode node = null;
       Queue<Node> queue = new LinkedList<Node>();
       Node rad = new Node(root, new Center(20,20), new Box(0,0), 0);
       queue.add(rad);
       this.levels.get(nivel).add(rad);
       while(!queue.isEmpty())
       {
           
           Node v = queue.poll();
           if(v.getT().getFirstChild() != null)
           {
                node = (MDTreeNode) v.getT().getFirstChild();
                Node n = new Node(node, new Center(0,0), new Box(0,0), v.level + 1);
                n.parent = v;
                v.addChild(n);
                levels.get(v.level + 1).add(n);
                this.vertices.add(n);
                queue.add(n);
           }
           while(node.getRightSibling() != null)
           {   
               node = (MDTreeNode) node.getRightSibling();
               nivel = v.level + 1;
               Node n = new Node(node, new Center(0,0), new Box(0,0), v.level + 1);
               n.parent = v;
               v.addChild(n);
               levels.get(nivel).add(n);
               this.vertices.add(n);
               queue.add(n);
           }
       }
       this.h = nivel;
       this.leaves = getLeaves(rad);
   }
   
   /**
    * draw the modules of the graph
    */
   public void DrawModule()
   {
       int dim=0;
       for(Node n : levels.get(0))
       {
           dim = getLeaves(n).size();
       }
       int k= 30;//setat cu lungimea muchiilor de la pasul curent
       Map<Node, Center> displacement = new HashMap<Node, Center>();
       
       for(int i = this.h; i >=0; i--)
       {
           for(Node n : levels.get(i))
           {
               if(!n.children.isEmpty())
               {
                   if(n.getT().getType() == MDNodeType.PARALLEL)
                   {
                       //DrawEdgeless(n)
                       EdgelessNode en = new EdgelessNode(n, k);
                       en.Draw();
                   }
                   
                   if(n.getT().getType() == MDNodeType.PRIME)
                   {
                       //DrawPrime(n);
                       PrimeNode pn = new PrimeNode(n,k, dim);
                       pn.Draw();
                   }
                   if(n.getT().getType() == MDNodeType.SERIES)
                   {
                       //DrawSeries(n);
                       CompleteNode cn = new CompleteNode(n, k);
                       cn.Draw();
                   }
                   n.getLabel();
               }
               else
               {
                   
                   n.h = 20;
                   n.w = 20;
                   n.setB(new Box(n.w, n.h));
               }
           }
           k += (h-i)*Math.log(k);
       }
       
       for(int i = 0; i < h; i++)
       {
           for(Node n : levels.get(i))
           {
               for(int j = 0; j < n.children.size();j++)
               {
                   n.children.get(j).setC(new Center(n.children.get(j).getC().getX() + n.getC().getX(),n.children.get(j).getC().getY() + n.getC().getY()));
               }
           }
       }
   }
   
    /**
     *
     * @param n
     * @return the list of the leaves from the modular decomposition tree of the node n
     */
    public ArrayList<Node> getLeaves(Node n)
    {
        ArrayList<Node> set = new ArrayList<Node>();
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while(!queue.isEmpty())
        {
            Node v = queue.poll();
            if(v.children.isEmpty())
            {
                set.add(v);
            }
            else
            {
                for(int i = 0; i < v.children.size(); i++)
                {
                    if(v.children.get(i).children.isEmpty())
                    {
                        set.add(v.children.get(i));
                    }
                    else
                    {
                        queue.add(v.children.get(i));
                    }
                }
            }
        }
        return set;
    }
   
}
