/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eusebiu.plosnita         
 */
public class PNode {
    
    public Node node;
    public Map<Node, Double> s;
    
    public PNode(Node node, ArrayList<Node> vecini)
    {
        this.node = node;
        s = new HashMap<Node, Double>();
        for(int i = 0; i < vecini.size(); i++)
        {
                s.put(node, 0.0);
        }
    }
    
   public void computeDisplacement(Node tj)
   {
       if(tj == this.node)
       {
            s.remove(tj);
            s.put(tj, 0.0);
       }
       else
       {
            s.remove(tj);
            s.put(tj, s.get(tj) + getDistance(tj));//forta de repulsie
       }
   }
    
    public double getDistance(Node tj)
    {
        double distance = Math.sqrt((tj.getC().getX()-this.node.getC().getX())*(tj.getC().getX()-this.node.getC().getX()))
                + ((tj.getC().getY()-this.node.getC().getY())*(tj.getC().getY()-this.node.getC().getY()));
        return distance;
    }
    
}
