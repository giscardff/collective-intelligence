/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author giscardf
 */
public class ENode extends GNode implements Cloneable {

    private FWrapper fwrapper;    
    private GNode[] children;
    
    /**
     * Creates a new object
     * @param fwrapper A <code>FWrapper</code> representing the function wrapper
     * @param children A <code>ArrayList</code> representing all childrens
     */
    public ENode(FWrapper fwrapper, GNode[] children){
        this.fwrapper = fwrapper;
        this.children = children;
    }//End ENode() constructor
    
    public Object evaluate(Object... input){
        Object[] results = new Object[children.length];
        for(int i = 0; i < results.length; i++)
            results[i] = children[i].evaluate(input);
        try{
            Method method = this.getClass().getMethod(fwrapper.getFunction(), new Class[]{Object[].class});
            Object res = method.invoke(this, (Object)results);
            return res;
        }//end try
        catch(NoSuchMethodException nsme){
            nsme.printStackTrace();
        }//end catch
        catch(IllegalAccessException iae){
            iae.printStackTrace();
        }//end catch
        catch(InvocationTargetException ite){
            ite.printStackTrace();
        }//end catch        
        return null;        
    }//end evaluate() method

    /**
     * This method retrieve the children of a node
     * @return A <code>GNode[]</code> representing all node children
     */
    public GNode[] getChildren() {
        return children;
    }//End getChildren() method
    
    
    /**
     * This method point the node children to some place else
     * @param children A <code>GNode[]</code> representing the new children
     */
    public void setChildren(GNode[] children) {
        this.children = children;
    }//end setChildren() method
    
    
    /***************************************************************************
     * Define generic functions
     ***************************************************************************/
    /**
     * This method add two numbers and give its result
     * @param input A <code>Object</code> representing the incoming numbers
     * @return A <code>Object</code> representing the sum result
     */ 
    public Object add(Object... input){
         int a = (Integer)input[0];
         int b = (Integer)input[1];
         return (new Integer(a + b));
     }//end add() method
    
    /**
     * This method subtract two numbers and give its result
     * @param input A <code>Object</code> representing the incoming numbers
     * @return A <code>Object</code> representing the subtract result
     */ 
    public Object subtract(Object... input){
         int a = (Integer)input[0];
         int b = (Integer)input[1];
         return (new Integer(a - b));
     }//end add() method
    
    /**
     * This method multiply two numbers and give its result
     * @param input A <code>Object</code> representing the incoming numbers
     * @return A <code>Object</code> representing the multiply result
     */ 
    public Object multiply(Object... input){
         int a = (Integer)input[0];
         int b = (Integer)input[1];
         return (new Integer(a * b));
     }//end add() method
    
    /**
     * This method check a condition and return one of two values
     * @param input A <code>Object</code> representing the condition and both values
     * @return A <code>Object</code> representing the result
     */
    public Object ifFunc(Object ...input){
        Integer cond = (Integer)input[0];
        Integer trueResult = (Integer)input[1];
        Integer falseResult = (Integer)input[2];
        if(cond > 0)
            return trueResult;
        else
            return falseResult;        
    }//end IfFunc() method
    
    /**
     * This method check if a value is greater than other one
     * @param input A <code>Object</code> representing the parameters
     * @return A <code>Object</code> representing the result
     */
    public Object isGreater(Object... input){
        Integer a = (Integer)input[0];
        Integer b = (Integer)input[1];
        if(a > b)
            return new Integer(1);
        else
            return new Integer(0);
    }//end IsGreater() method
    
    /**
     * This method calculate a specific f(z) = x + y value
     * @param input A <code>Object</code> representing the parameters
     * @return A <code>Object</code> representing the result
     */
    public static int hiddenfunction(Object... input){        
        Integer x = (Integer)input[0];
        Integer y = (Integer)input[1];
        return (x * x + 2 * y + 3 * x + 5);
    }//end hiddenfunction() method
    
    /**
     * This method generate a set of x/y values and calculate the hidden function for them
     * @return A <code>int[][]</code> represneting all x, y and f(z) values
     */
    public static int[][] buildhiddenset(){
        int rows[][] = new int[200][3];
        for(int i = 0; i < rows.length; i++){
            rows[i][0] = (int)(Math.random() * 40);
            rows[i][1] = (int)(Math.random() * 40);
            rows[i][2] = hiddenfunction(rows[i][0], rows[i][1]);
        }//end for
        return rows;
    }//end buildhiddenset() method

    /**
     * This method print this node
     * @param indent A <code>int</code> representing how much to indent
     */
    @Override
    public void display(int indent) {
        for(int i = 0; i < indent; i++)
            System.out.print(" ");
        System.out.println(fwrapper.getFunction());
        for(int i = 0; i < children.length; i++)
            children[i].display(indent + 2);
    }//End display() method
    
    @Override
    protected Object clone() throws CloneNotSupportedException {        
        ENode clone = (ENode)super.clone();
        clone.fwrapper = (FWrapper)fwrapper.clone();
        clone.children = children.clone();
        return clone;
    }//End clone() method   
    
}//End ENode class
