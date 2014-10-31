
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDDrawing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author eusebiu.plosnita
 */
public class PrimeNode implements InternalNodeDrawing{
    public Node node;
    public double k;
    //displacement for every t
    /*
     * @maxiter maximum number of iterations 
     */
    public int maxiter;
    public Map<Node, double[]> displacement;
    
    private ArrayList<Edge> edges; 
    private Center[] s;
    private int dim;
    //stocheaza nodurile frunze ale fiecarui nod intern, sau propriul nod in cazul in care este nod frunza
    public ArrayList<Set<Node>> leaves;
    
    public PrimeNode(Node node, double k, int dim)
    {
        this.node = node;
        this.k = k;
        this.dim = dim;
        this.s = new Center[this.node.children.size()];
        pozitioneazaRandomNodurile();
        this.displacement = new HashMap<>();
        this.edges = new ArrayList<Edge>();
        //to be set later
        this.maxiter = this.dim * (int) Math.pow(this.node.children.size(), 3);
        
        this.leaves = new ArrayList<Set<Node>>();
    }
    
    /**
     *
     * @return the medium box dimensions
     */
    public Box medie()
    {
        double medie_h=0, medie_w=0;
        for(int i =0; i < this.node.children.size(); i++)
        {
            medie_h += this.node.children.get(i).getB().getH();
            medie_w += this.node.children.get(i).getB().getH();
        }
        return new Box(medie_h/this.node.children.size(), medie_w/this.node.children.size());
    }
    
    /**
     * 
     * initialize the centers for every child of the current node
     */
    private void pozitioneazaRandomNodurile()
    {
        double maxh = 0, maxw = 0;
        for(int i = 0; i < this.node.children.size(); i++)
        {
            if(this.node.children.get(i).w > maxw)
            {
                maxw = this.node.children.get(i).w;
            }
            if(this.node.children.get(i).h > maxh)
            {
                maxh = this.node.children.get(i).h;
            }
        }
        
        Random r = new Random();
        double x;
        double y;
        Box b = medie();
        this.node.w = 10* b.getW() * this.getLeaves(this.node).size();
        this.node.h =  10*b.getH() * this.getLeaves(this.node).size();
        this.node.getB().setH(this.node.h);
        this.node.getB().setW(this.node.w);
        this.k = Math.sqrt(this.node.children.size()/Math.PI)*Math.sqrt((this.node.w * this.node.h)/this.node.children.size());
        for(int i = 0; i < this.node.children.size(); i++)
        {
            x = Math.abs(r.nextInt());
            x = x % this.node.h;
            y = Math.abs(r.nextInt());
            y = y % this.node.w;
            this.node.children.get(i).setC(new Center(x,y));
        }
        
    }
    
    /**
     *computes the coordinates of the centers for the children of the current node
     */
    @Override
    public void Draw()
    {
        int n = this.node.children.size();
        int iterations = 0;  
        boolean early_iterations = true;
        this.s = new Center[n];
        for(int i = 0; i < n; i++)
        {
            s[i] = new Center(0,0);
        }
        determinaMuchiile();
        double deltax,deltay;
        while(iterations < this.maxiter)
        {
            for(int i = 0; i < n; i++)
            {
                s[i].setX(0);
                s[i].setY(0);
                for(int j = 0; j < n; j++)
                {
                    if(j != i)
                    {
                        double fr = repulsiveForce(early_iterations, this.node.children.get(i), this.node.children.get(j));
                        deltax = this.node.children.get(i).getC().getX() - this.node.children.get(j).getC().getX();
                        deltay = this.node.children.get(i).getC().getY() - this.node.children.get(j).getC().getY();
                        s[i].setX(s[i].getX() + deltax/(Distance(this.node.children.get(i).getC(), this.node.children.get(j).getC())*fr));
                        s[i].setY(s[i].getY() + deltay/(Distance(this.node.children.get(i).getC(), this.node.children.get(j).getC())*fr));
                    }
                }
            }
            
            //attraction force
            double fa, dist;
            for(Edge e: this.edges)
            {
                fa = attractiveForce(early_iterations, this.node.children.get(e.pos_v1), this.node.children.get(e.pos_v2));
                deltax = this.node.children.get(e.pos_v1).getC().getX() - this.node.children.get(e.pos_v2).getC().getX();
                deltay = this.node.children.get(e.pos_v1).getC().getY() - this.node.children.get(e.pos_v2).getC().getY();
                dist = Distance(this.node.children.get(e.pos_v1).getC(), this.node.children.get(e.pos_v2).getC());
                s[e.pos_v1].setX(s[e.pos_v1].getX() -  deltax/dist*fa);
                s[e.pos_v1].setY(s[e.pos_v1].getY() -  deltay/dist*fa);
                s[e.pos_v2].setX(s[e.pos_v2].getX() +  deltax/dist*fa);
                s[e.pos_v2].setY(s[e.pos_v2].getY() +  deltay/dist*fa);
            }
            
            //ajusteaza centrele in functie de temperatura si deplasamentul anterior calculat
            Node n1;
            double temp = cool(iterations);;
            for(int i = 0; i < this.node.children.size(); i++)
            {
                n1 = this.node.children.get(i);
                deltax = n1.getC().getX() - s[i].getX();
                deltay = n1.getC().getY() - s[i].getY();
                n1.getC().setX(n1.getC().getX() + (s[i].getX() / Math.abs(deltax)) * getMin(Math.abs(s[i].getX()), temp));
                n1.getC().setY(n1.getC().getY() + (s[i].getY() / Math.abs(deltay)) * getMin(Math.abs(s[i].getY()), temp));
                //verificare daca nu se depaseste boxul
                if(depasesteXBox(n1))
                {
                    n1.getC().setX(getMin(this.node.h/2, getMax(-this.node.h/2, n1.getC().getX())));
                }
                if(depasesteYBox(n1))
                {
                    n1.getC().setY(getMin(this.node.w/2, getMax(-this.node.w/2, n1.getC().getY())));
                }
                
            }
            iterations++;
            //if(iterations > maxiter - maxiter/5)
              //  early_iterations = false;
        }
    }
    
    /**
     * @param n
     * @return true if the coordinate x of the center is too big or too low for the established dimension
     */
    private boolean depasesteXBox(Node n)
    {
        if(n.getC().getX() + n.h/2 > this.node.h || n.getC().getX() - n.h/2 < 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * 
     * @param n
     * @return true if the coordinate y is too big or too low for the established dimension
     */
    private boolean depasesteYBox(Node n)
    {
        if(n.getC().getY() + n.w/2 > this.node.w || n.getC().getY() - n.w/2 < 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private double getMax(double d1, double d2)
    {
        if(d1 > d2)
        {
            return d1;
        }
        else 
        {
            return d2;
        }
    }
    
    public double getMin(double d1, double d2)
    {
        if(d1 > d2)
        {
            return d2;
        }
        else 
        {
            return d1;
        }
    }
    
    /**
     * 
     * @param i
     * @return the temperature for the current iteration
     */
    private double cool(int i)
    {
        //return Math.sqrt(Math.PI/this.node.children.size())/(1 + Math.PI/this.node.children.size() * Math.sqrt(i * i * i));
        double area = this.node.w * this.node.h;
        return Math.sqrt(area/this.node.children.size())/(1 + area/this.node.children.size() * Math.sqrt(i * i * i));
    }
    
    /**
     * @param early_iterations
     * @param n1
     * @param n2
     * @return the attractive force between two nodes computed considering the current iteration
     */
    private double attractiveForce(boolean early_iterations, Node n1, Node n2)
    {
        double fa =0;
        double dist;
        if(early_iterations)
        {
            dist = Distance(n1.getC(),n2.getC());
            //if(!isDense())
            //{
                fa = (0.31 * dist * dist)/k;
           // }
            //else
            //{
              //  fa = (1.0/0.31 *dist)/k;
            //}
        }
        else
        {
            dist = getMinDistance(n1,n2);
            fa = (0.31*dist * dist)/k;
        }
        return fa;
    }
    
    /**
     *
     * @return true if the graph is dense or false if the graph is not dense
     */
    public boolean isDense()
    {
        Node n1, n2;
        double diag_n1, diag_n2;
        for(int i = 0; i < this.node.children.size(); i++)
        {
            n1 = this.node.children.get(i);
            diag_n1 = Math.sqrt(n1.h*n1.h + n1.w * n1.w);
            for(int j =0; j < this.node.children.size() && i != j; j++)
            {
                n2 = this.node.children.get(j);
                diag_n2 = Math.sqrt(n2.h*n2.h + n2.w * n2.w);
                if(Distance(n1.getC(), n2.getC()) <= diag_n1 || Distance(n1.getC(), n2.getC()) <= diag_n2)
                {
                    return true;
                }
            }
        }
        return true;
    }
    
    /**
     *determine the edges between the nodes of the current subtree
     */
    public void determinaMuchiile()
    {
        int k,l;
        for(int i = 0; i < this.node.children.size(); i++)
        {
            this.leaves.add(getLeaves(this.node.children.get(i)));
        }
        int n = this.node.children.size();
        boolean val = false;
        
        int[][] mat = new int[n][n];
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n && i != j; j++)
            {
                for(Node n1 : getLeaves(this.node.children.get(j)))
                {
                    if((n1.vecini.contains(this.node.children.get(i)) || this.node.children.get(i).vecini.contains(this.node.children.get(j))) && mat[i][j]==0 && mat[j][i]==0)
                    {
                        Edge e = new Edge(this.node.children.get(i), this.node.children.get(j),30);
                        e.pos_v1 = i;
                        e.pos_v2 = j;
                        this.edges.add(e);
                        mat[i][j] = 1;
                        mat[j][i] = 1; 
                    }
                }
            }
        }
    }
    
    /**
     *
     * @param n
     * @return the leaves for the node n. If n is a leaf in the modular decomposition tree then returns the node, else it computes
     * the leaves of the node with a BFS algorithm 
     */
    public Set<Node> getLeaves(Node n)
    {
        Set<Node> set = new HashSet<Node>();
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
    
    /**
     *
     * @param early_iterations
     * @param ti
     * @param tj
     * @return the value for the repulsive force for those two nodes offered as input
     */
    public double repulsiveForce(boolean early_iterations, Node ti, Node tj)
    {
        double fr;
        if(early_iterations)
        {
            fr = (this.k * this.k)/Distance(ti.getC(), tj.getC());
        }
        else
        {
            //get the shortest distance between the boundaries
            fr = (this.k * this.k)/getMinDistance(ti,tj);
        }
        return fr;
    }
    
    /**
     *
     * @param c1
     * @param c2
     * @return the distance between two nodes (Euclidean distance)
     */
    public double Distance(Center c1, Center c2)
    {
        double result = Math.sqrt((c2.getX()-c1.getX())*(c2.getX()-c1.getX()) + (c2.getY()-c1.getY())*(c2.getY()-c1.getY()));
        return result;
    }
    
    /**
     *
     * @param t1
     * @param t2
     * @return the minimum distance between two nodes, comparing the position of the boxes of to nodes
     */
    public double getMinDistance(Node t1, Node t2)
    {
        double result = 0;
        ArrayList<Double> lista_distante = new ArrayList<Double>();
        Center[] colturi_t1 = new Center[4];
        Center[] colturi_t2 = new Center[4];
        colturi_t1[0] = new Center(t1.getC().getX() - t1.h/2, t1.getC().getY() - t1.w/2);//stanga sus
        colturi_t1[1] = new Center(t1.getC().getX() + t1.h/2, t1.getC().getY() - t1.w/2);//stanga jos
        colturi_t1[2] = new Center(t1.getC().getX() + t1.h/2, t1.getC().getY() + t1.w/2);//dreapta jos
        colturi_t1[3] = new Center(t1.getC().getX() - t1.h/2, t1.getC().getY() + t1.w/2);//dreapta sus
        
        colturi_t2[0] = new Center(t2.getC().getX() - t2.h/2, t2.getC().getY() - t2.w/2);//stanga sus
        colturi_t2[1] = new Center(t2.getC().getX() + t2.h/2, t2.getC().getY() - t2.w/2);//stanga jos
        colturi_t2[2] = new Center(t2.getC().getX() + t2.h/2, t2.getC().getY() + t2.w/2);//dreapta jos
        colturi_t2[3] = new Center(t2.getC().getX() - t2.h/2, t2.getC().getY() + t2.w/2);//dreapta sus
        
        if(t1.getC().getX() >t2.getC().getX() && t1.getC().getY() > t2.getC().getY())
        {
            if(t1.getC().getX() - t1.h/2 > t2.getC().getX() + t2.h/2)
            {
                result = Distance(colturi_t1[0], colturi_t2[2]);
            }
            else
            {
                result = Math.abs(colturi_t1[0].getY() - colturi_t2[2].getY());
            }
        }
        
        if(t1.getC().getX() >t2.getC().getX() && t1.getC().getY() < t2.getC().getY())
        {
            if(t1.getC().getX() - t1.h/2 > t2.getC().getX() + t2.h/2)
            {
                result = Distance(colturi_t1[3], colturi_t2[1]);
            }
            else
            {
                result = Math.abs(colturi_t1[3].getY() - colturi_t2[1].getY());
            }
        }
        
        if(t1.getC().getX() < t2.getC().getX() && t1.getC().getY() < t2.getC().getY())
        {
            if(t1.getC().getY() + t1.w/2 > t2.getC().getY() - t2.w/2)
            {
                result = Distance(colturi_t1[2], colturi_t2[0]);
            }
            else
            {
                result = Math.abs(colturi_t1[2].getX() - colturi_t2[0].getX());
            }
        }
        
        if(t1.getC().getX() < t2.getC().getX() && t1.getC().getY() > t2.getC().getY())
        {
            if(t1.getC().getY() - t1.w/2 > t2.getC().getY() + t2.w/2)
            {
                result = Distance(colturi_t1[1], colturi_t2[3]);
            }
            else
            {
                result = Math.abs(colturi_t1[1].getX() - colturi_t2[3].getX());
            }
        }
        return result;
        
    }
    
}
