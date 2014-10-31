/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateGraph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eusebiu.plosnita
 */
public class GenerateGraph {
    
    public int n;
    public int m;
    public ArrayList<GenerateVertex> noduri;
    public int[][] matrix;
    
    public GenerateGraph(int n, int m, String fisier)
    {
        this.n = n;
        this.m = m;
        this.noduri = new ArrayList<GenerateVertex>();
        this.matrix = new int[n][n];
        int k;
        int l;
        Random r = new Random();
        for(int i =0; i < m; i++)
        {
                do{
                    k = Math.abs(r.nextInt()) % n;
                    l = Math.abs(r.nextInt()) % n;
                }while(k == l || matrix[l][k] == 1);
                matrix[k][l] = 1;
                matrix[l][k] = 1;
                i++;
        }
        
        Generate();
    }
    
    public void Generate()
    {
            String label = null;
            for(int i = 0; i < this.n; i++)
            {
                 label = Integer.toString(i);
                this.noduri.add(new GenerateVertex(label));
            }
            for(int i =0; i < n; i++)
            {
                for(int j = 0; j < n; j++)
                {
                    if(this.matrix[i][j] == 1)
                        this.noduri.get(i).vecini.add(this.noduri.get(j));
                }
            }
            PrintWriter writer;
            try {
            try {
                writer = new PrintWriter("graph.txt", "UTF-8");
                for(int i = 0; i < n; i++)
            {
                String rand = this.noduri.get(i).label + "->";
                for(int j = 0; j < this.noduri.get(i).vecini.size(); j++)
                {
                    if(j != this.noduri.get(i).vecini.size()-1)
                        rand += this.noduri.get(i).vecini.get(j).label + ",";
                    else
                        rand += this.noduri.get(i).vecini.get(j).label;
                }
                writer.println(rand);
            }
            writer.close();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GenerateGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GenerateGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    
}
