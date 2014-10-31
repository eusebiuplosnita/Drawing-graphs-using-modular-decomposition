/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

import java.util.ArrayList;

/**
 *
 * @author eusebiu.plosnita
 */
public class CompleteNode implements InternalNodeDrawing{
    
    public double k;
    public Node node;
    public double f[];
    public double r[];
    public ArrayList<Node> Circle;
    public double teta;
    
    public CompleteNode(Node node, double k)
    {
        this.node = node;
        this.k = k;
        this.f = new double[this.node.children.size()];
        this.r = new double[this.node.children.size()];
        for(int i = 0; i < this.node.children.size(); i++)
        {
            f[i] = computeDiagonal(node.children.get(i).getB());
        }
        this.teta = (2*Math.PI)/(node.children.size());
        this.Circle = new ArrayList();
        
        for(int i = 0; i < this.node.children.size(); i++)
        {
            this.Circle.add(this.node.children.get(i));
        }
    }
    
    /**
     *
     * @param b
     * @return value for the diagonal of the Box b
     */
    public double computeDiagonal(Box b)
    {
        double result = 0;
        result = Math.sqrt(b.getW()*b.getW() + b.getH()+b.getH());
        return result;
    }
    
    /**
     *computes the coordinates of the children nodes for the current node so that the drawing will be circular 
     */
    @Override
    public void Draw()
    {
        double r1, r2;
        int indice1, indice2;
        for(int i = 0; i < this.node.children.size(); i++)
        {
            if(i == 0)
            {
                r1 = (f[i] + k + f[this.node.children.size()-1])/(2*Math.sin(teta/2));
                indice1 = this.node.children.size()-1;
            }
            else
            {
                r1 = (f[i] + k + f[i-1])/(2*Math.sin(teta/2));
                indice1 = i - 1; 
            }
            
            if(i == this.node.children.size()-1)
            {
                r2 =  (f[i] + k + f[0])/(2*Math.sin(teta/2));
                indice2 = 0;
            }
            else
            {
                r2 = (f[i] + k + f[i+1])/(2*Math.sin(teta/2));
                indice2 = i+1;
            }
            if(this.node.children.get(i).getB().getH() == this.node.children.get(indice1).getB().getH() &&
                    this.node.children.get(i).getB().getW() == this.node.children.get(indice1).getB().getW())
            {
                r[i] = r1;
            }
            else
            {
                if(this.node.children.get(i).getB().getH() == this.node.children.get(indice2).getB().getH() &&
                    this.node.children.get(i).getB().getW() == this.node.children.get(indice2).getB().getW())
                {
                    r[i] = r2;
                }
                else
                {
                    if(r1 > r2)
                    {
                        r[i] = r1;
                    }
                    else
                    {
                        r[i] = r2;
                    }
                }
            }
        }
        
        setDimensions();//calculeaza dimensiunile w si h pentru nodul curent in functie de raza maxima si lungimea si inaltimea maxima a unui nod copil

        double angle = 0;
        double xi;
        double yi;
        for(int i = 0; i < this.node.children.size(); i++)
        {
            xi = r[i] * Math.cos(angle) + (this.node.h/2);
            yi = r[i] * Math.sin(angle) + (this.node.w/2);
            this.node.children.get(i).setC(new Center(xi, yi));
            angle += this.teta;
        }
    }
    
    /**
     * set the dimensions of the current node 
     */
    private void setDimensions()
    {
        double rmax= 0;
        for(int i = 0; i < r.length; i++)
        {
            if(r[i] > rmax)
            {
                rmax = r[i];
            }
        }
        double hmax = 0;
        double wmax = 0;
        for(Node n : this.node.children)
        {
            if(hmax < n.h)
            {
                hmax = n.h;
            }
            if(wmax < n.w)
            {
                wmax = n.w;
            }
        }
        
        this.node.w = 2*(rmax + wmax);
        this.node.h = 2*(rmax + hmax);
    }
    
}
