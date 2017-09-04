/*
 * Created on 30 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class DecisionNode {

    public int col = -1;
    public Object value = "None";
    public HashMap<String,Double> results = null;
    public DecisionNode tb = null;
    public DecisionNode fb = null;
    
    /**
     * Creates a new instance of object
     * @param col A <code>int</code> representing the column of the table this node represents
     * @param value A <code>value</code> representing the value of the column this node represents
     * @param results A <code>HashMap</code> representing the final result this node contains (only final nodes has this different of null)
     * @param tb A <code>DecisionNode</code> representing the child node for true decisions
     * @param fb A <code>DecisionNode</code> representing the child node for false decisions
     */
    public DecisionNode(int col, Object value, HashMap<String,Double> results, DecisionNode tb, DecisionNode fb){
        this.col = col;
        this.value = value;
        this.results = results;
        this.tb = tb;
        this.fb = fb;
    }//End DecisionNode() constructor
    
}//End DecisionNode class
