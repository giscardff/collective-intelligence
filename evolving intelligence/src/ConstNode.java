/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author giscardf
 */
public class ConstNode extends GNode implements Cloneable {

    private Object value;
    
    /**
     * Creates a new object
     * @param value A <code>Object</code> representing the node value
     */
    public ConstNode(Object value){
        this.value = value;
    }//end ConstNode() constructor
    
    
    public Object evaluate(Object... input){
        return value;
    }//end evaluate() method
    
    /**
     * This method print this node
     * @param indent A <code>int</code> representing how much to indent
     */
    @Override
    public void display(int indent) {
        for(int i = 0; i < indent; i++)
            System.out.print(" ");
        System.out.println(value);
    }//End display() method

    @Override
    protected Object clone() throws CloneNotSupportedException {        
        ConstNode clone = (ConstNode)super.clone();        
        return clone;
    }//End clone() method
    
}//End ConstNode class
