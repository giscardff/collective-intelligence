/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author giscardf
 */
public class Main {

    public static FWrapper[] flist = {
            new FWrapper("add", 2),
            new FWrapper("subtract", 2),
            new FWrapper("multiply", 2),
            new FWrapper("ifFunc", 3),
            new FWrapper("isGreater", 2),
            new FWrapper("hiddenfunction", 2)
        };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        GNode node = exampletree();
        System.out.println("Evaluate(2,3) = " + node.evaluate(2, 3));
        System.out.println("Evaluate(2,3) = " + node.evaluate(5, 3));
        node.display(0);
        
        GNode random1 = makerandomtree(2, 4, 0.5, 0.5);
        System.out.println("Evaluate(7,1) random1: " + random1.evaluate(7, 1));
        System.out.println("Evaluate(2,4) random1: " + random1.evaluate(2, 4));
        random1.display(0);
        GNode random2 = makerandomtree(2, 4, 0.5, 0.5);
        System.out.println("Evaluate(5,3) random2: " + random2.evaluate(5, 3));
        System.out.println("Evaluate(5,20) random2: " + random2.evaluate(5, 20));        
        random2.display(0);
        
        int[][] hiddenset = ENode.buildhiddenset();
        System.out.println("Score random1: " + scorefunction(random1, hiddenset));
        System.out.println("Score random2: " + scorefunction(random2, hiddenset));
        
        System.out.println(" === mutate ===");
        GNode mutate = mutate(random2, 2, 0.1);
        mutate.display(0);        
        System.out.println("Score mutate: " + scorefunction(mutate, hiddenset));
        
    }//End main() method
    
    /**
     * This method create a sample tree
     * @return A <code>GNode</code> representing the sample tree
     */
    private static GNode exampletree(){        
        return new ENode(getList()[3], 
                            new ENode[]{
                                new ENode(getList()[4], new GNode[]{new ParamNode(0), new ConstNode(3)}),
                                new ENode(getList()[0], new GNode[]{new ParamNode(1), new ConstNode(5)}),
                                new ENode(getList()[1], new GNode[]{new ParamNode(1), new ConstNode(2)})
                            }
        );
        
    }//end exampletree() method
    
    /**
     * This method retrieve all existing functions
     * @return A <code>FWrapper[]</code> containing all functions
     */
    public static FWrapper[] getList(){        
        return flist;
    }//end getFWrapperList() method
    
    /**
     * This method pick a random function from the list
     * @param list A <code>FWrapper[]</code> representing the function list
     * @return A <code>FWrapper</code> representing the picked function
     */
    public static FWrapper choice(FWrapper[] list){
        return list[(int)(Math.random() * list.length)];
    }//end choice() method
    
    /**
     * This method create a random tree
     * @param pc A <code>int</code> representing the number of parameters the tree accepts
     * @param maxdepth A <code>int</code> representing the max depth of the tree
     * @param fpr A <code>double</code> representing the function node probability
     * @param ppr A <code>double</code> representing paramnode probability
     * @return A <code>GNode</code> representing the root node
     */
    public static GNode makerandomtree(int pc, int maxdepth, double fpr, double ppr){        
        if(Math.random() < fpr && maxdepth > 0){
            FWrapper f = choice(flist);
            GNode children[] = new GNode[f.getChildcount()];
            for(int i = 0; i < children.length; i++)
                children[i] = makerandomtree(pc, maxdepth - 1, fpr, ppr);
            return new ENode(f, children);
        }//end if
        else if(Math.random() < ppr){
            return new ParamNode((int)(Math.random() * (pc - 1)));
        }//end if
        else
            return new ConstNode((int)(Math.random() * 10));
    }//end makerandomtree() method
    
    /**
     * This method calculate the score of a function     * 
     * @param tree A <code>GNode</code> representing the root node of the tree
     * @param s A <code>int[][]</code> representing all the solutions
     * @return A <code>int</code> representing the cost
     */
    public static int scorefunction(GNode tree, int[][] s){
        int dif = 0;
        for(int i = 0; i < s.length; i++){
            Integer v = (Integer)tree.evaluate(s[i][0], s[i][1]);
            dif += Math.abs(v - s[i][2]);
        }//end for
        return dif;
    }//end scorefunction() method
    
    /**
     * This method mutate a tree
     * @param tree A <code>GNode</code> representing the node to mutate
     * @param pc A <code>int</code> representing the number of parameters the tree accept
     * @param probchange A <code>double</code> representing the probabliity of complete change
     * @return A <code>GNode</code> pointing the new root node
     */
    public static GNode mutate(GNode tree, int pc, double probchange){
        try{
            if(Math.random() < probchange){
                return makerandomtree(pc, 4, 0.5, 0.5);
            }//end if
            else{            
                GNode result = (GNode)tree.clone();            
                if(tree instanceof ENode){
                    GNode[] children = ((ENode)tree).getChildren();
                    for(int i = 0; i < children.length; i++)
                        children[i] = mutate(children[i], pc, probchange);
                    ((ENode)result).setChildren(children);
                }//end if                
                return result;
            }//end else        
        }//end try
        catch(CloneNotSupportedException cnse){                
            cnse.printStackTrace();
        }//end catch
        return null;
    }//end mutate() method

}//End Main class
