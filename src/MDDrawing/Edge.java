/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

/**
 *
 * @author eusebiu.plosnita
 */
public class Edge {
    
    private double length;
    private Node v1;
    private Node v2;
    public int pos_v1;
    public int pos_v2;
    
    public Edge(Node v1, Node v2, double length)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.length = length;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @return the v1
     */
    public Node getV1() {
        return v1;
    }

    /**
     * @param v1 the v1 to set
     */
    public void setV1(Node v1) {
        this.v1 = v1;
    }

    /**
     * @return the v2
     */
    public Node getV2() {
        return v2;
    }

    /**
     * @param v2 the v2 to set
     */
    public void setV2(Node v2) {
        this.v2 = v2;
    }
}
