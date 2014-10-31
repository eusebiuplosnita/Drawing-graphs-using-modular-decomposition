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
public class EdgelessNode implements InternalNodeDrawing{
    
    public int r;
    public int c;
    public Node node;
    public Node[][] F;
    public double k;//preffered edge length
    
    public EdgelessNode(Node node, double k)
    {
        this.k = k;
        this.node = node;
        double nr = Math.sqrt(node.getT().getNumChildren());
        int nr1;
        if(nr > ((int)Math.sqrt(node.getT().getNumChildren())))
            nr1 = (int)nr+1;
        else 
            nr1 = (int) nr;
        this.c = this.r = nr1;
        this.F = new Node[r][c];
        
        putChildrenOnGrid();
        
    }
    
    /**
     * positions arbitrary the children of @node to grid @F
     */
    private void putChildrenOnGrid()
    {
        int lungime = this.node.children.size();
        Node[] list = new Node[lungime];
        for(int i = 0; i < lungime; i++)
        {
            list[i] = this.node.children.get(i);
        }
        
        Node interm;
        for(int i = 0; i < lungime-1; i++)
        {
            for(int j = i + 1; j < lungime; j++)
            {
                if(list[i].w < list[j].w)
                {
                   interm = list[i];
                   list[i] = list[j];
                   list[j] = interm;
                }
            }
        }
        int aux = lungime-1;
        for(int i = 0; i < c && aux >= 0; i++)
        {
            for(int j = 0; j < r && aux >= 0; j++)
            {
                this.F[j][i] = list[aux--];
            }
        }
    
    }
    
    /**
     *computes the coordinates for the children nodes 
     */
    @Override
    public void Draw()
    {
        double[] hi = new double[c];
        double[] wi = new double[r];
        double wmax = 0;
        double sum;
        for(int i = 0; i < this.r; i++)
        {
            sum = 0;
            for(int j = 0; j < this.c; j++)
            {
                if(this.F[i][j] != null)
                {
                    sum += this.F[i][j].w /2;
                    this.F[i][j].getC().setX(0);
                    this.F[i][j].getC().setY(sum);
                    sum += this.F[i][j].w/2 + k;
                    
                    if(hi[i] < this.F[i][j].h)
                    {
                        hi[i] = this.F[i][j].h;
                    }
                }
            }
            if(sum - k > wmax)
            {
                wi[i] = wmax = sum-k;
            }
        }
       
       //setare w si h
       this.node.w = wmax;
       for(int j = 0; j < c; j++)
       {
           this.node.h += hi[j] + this.k;
       }
       this.node.h -= k;
       this.node.getB().setH(this.node.h);
       this.node.getB().setW(this.node.w);
        double ki=0;
        double kh = 0;
        for(int i = 0;i < this.r; i++)
        {
             ki += hi[i]/2;
                for(int j = 0; j < this.c; j++)
                {
                    if(this.F[i][j] != null)
                    {
                        this.F[i][j].getC().setX(this.F[i][j].getC().getX() + ki);
                    }
                }
                ki+=hi[i]/2 + k;
            
            /*for(int j = 0; j < c; j++)
            {
                if(this.F[i][j] != null)
                {
                    this.F[i][j].getC().setY(wi[i]/2 + kh);
                    
                }
            }
            kh += wi[i] + k;*/
        }
       
    }
}
