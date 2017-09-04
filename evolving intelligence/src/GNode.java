/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author giscardf
 */
public abstract class GNode implements Cloneable {

    /** this method evaluate a node */
    public abstract Object evaluate(Object... input);
    
    /**
     * This method display a node and its children recursevely
     * @param indent A <code>int</code> representing the indent level     
     */
    public abstract void display(int indent);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        GNode clone = (GNode)super.clone();
        return clone;
    }//end clone() method
    
}//End GNode class
