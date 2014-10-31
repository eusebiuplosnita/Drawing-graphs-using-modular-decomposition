/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateGraph;

import java.util.ArrayList;

/**
 *
 * @author eusebiu.plosnita
 */
public class GenerateVertex {
    
    public String label;
    public ArrayList<GenerateVertex> vecini;
    
    public GenerateVertex(String label)
    {
        this.label = label;
        this.vecini = new ArrayList<GenerateVertex>();
    }
    
}
