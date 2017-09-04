/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author giscardf
 */
public class ParamNode extends GNode implements Cloneable {

    private int index;
    
    /**
     * Creates a new object
     * @param index A <code>int</code> representing the node index
     */
    public ParamNode(int index){
        this.index = index;
    }//end ParamNode() constructor
    
    public Object evaluate(Object... input){        
        return input[index];
    }//end input() method
    
    /**
     * This method print this node
     * @param indent A <code>int</code> representing how much to indent
     */
    @Override
    public void display(int indent) {
        for(int i = 0; i < indent; i++)
            System.out.print(" ");
        System.out.println(index);
    }//End display() method

    @Override
    protected Object clone() throws CloneNotSupportedException {        
        ParamNode clone = (ParamNode)super.clone();        
        return clone;
    }//End clone() method
    
}//End ParamNode class
