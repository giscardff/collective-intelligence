/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author giscardf
 */
public class FWrapper implements Cloneable {

    private int childcount;
    private String function;        
    
    /**
     * Creates a new object
     * @param function A <code>String</code> representing the method name
     * @param childcount A <code>int</code> representing the maximum amount of children     
     */
    public FWrapper(String function, int childcount){
        this.function = function;
        this.childcount = childcount;        
    }//end FWrapper() constructor

    /**
     * Retrieve the function name
     * @return A <code>String</code> representing the function name
     */
    public String getFunction() {
        return function;
    }//End getFunction() method

    /**
     * Retrieve the number of children
     * @return A <code>int</code> representing the number of children
     */
    public int getChildcount() {
        return childcount;
    }//End getChildCount() method

    @Override
    protected Object clone() throws CloneNotSupportedException {
        FWrapper clone = (FWrapper)super.clone();
        return clone;
    }//end clone() method
    
    
    
}//End FWrapper class
