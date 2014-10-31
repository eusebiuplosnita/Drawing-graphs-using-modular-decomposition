/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

/**
 *
 * @author eusebiu.plosnita
 */
public class Box {
    
    private double w;
    private double h;
    
    public Box(double w, double h)
    {
        this.w = w;
        this.h = h;
    }

    /**
     * @return the w
     */
    public double getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(double w) {
        this.w = w;
    }

    /**
     * @return the h
     */
    public double getH() {
        return h;
    }

    /**
     * @param h the y to set
     */
    public void setH(double h) {
        this.h = h;
    }
    
}
