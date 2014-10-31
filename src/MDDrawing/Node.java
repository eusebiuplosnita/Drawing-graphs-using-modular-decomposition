/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import licenta_md.MDTreeLeafNode;
import licenta_md.MDTreeNode;
import licenta_md.RootedTreeNode;
import licenta_md.Vertex;

/**
 *
 * @author eusebiu.plosnita
 */
public class Node {
    
    private MDTreeNode t;
    public int level;
    private Center c;
    private Box b;
    public boolean visited;
    public Node parent;
    public ArrayList<Node> children;
    public double w;
    public double h;
    public ArrayList<Node> vecini;
    
    public Node(MDTreeNode t, Center c, Box b, int level)
    {
        this.t = t;
        this.b = b;
        this.c = c;
        this.level = level;
        this.children = new ArrayList<Node>();
        this.vecini = new ArrayList<Node>();
    }

    public void addChild(Node node)
    {
        this.children.add(node);
    }
    
    
    /**
     * @return the t
     */
    public MDTreeNode getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(MDTreeNode t) {
        this.t = t;
    }

    /**
     * @return the c
     */
    public Center getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(Center c) {
        this.c = c;
    }

    /**
     * @return the b
     */
    public Box getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(Box b) {
        this.b = b;
    }
    
    public String getLabel()
    {
        if(this.children.isEmpty())
        {
            MDTreeLeafNode leaf = (MDTreeLeafNode) this.getT();
            return leaf.vertex.label;
        }
        return null;
    }
    
}
