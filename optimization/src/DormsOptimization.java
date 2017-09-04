/*
 * Created on 15 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class DormsOptimization extends Optimization {

    public static String[] dorms = {"Zeus", "Athena", "Hercules", "Bacchus",  "Pluto"};
    public static String[][]prefs = {
                                     {"Toby"  , "Bacchus" , "Hercules"},
                                     {"Steve" , "Zeus"    , "Pluto"},
                                     {"Andrea", "Athena"  , "Zeus"},
                                     {"Sarah" , "Zeus"    , "Pluto"},
                                     {"Dave"  , "Athena"  , "Bacchus"},
                                     {"Jeff"  , "Hercules", "Pluto"},
                                     {"Fred"  , "Pluto"   , "Athena"},
                                     {"Suzie" , "Bacchus" , "Hercules"},
                                     {"Laura" , "Bacchus" , "Hercules"},
                                     {"Neil"  , "Hercules", "Athena"},
                                    };
    
    public static int[][]domain = {
                                   {0,9},
                                   {0,8},
                                   {0,7},
                                   {0,6},
                                   {0,5},
                                   {0,4},
                                   {0,3},
                                   {0,2},
                                   {0,1},
                                   {0,0},
                                  };
    
    
    /**
     * Thist method print the solution for the problem
     * @param vec A <code>int[]</code> representing the solution
     */
    public void printSolution(int[] vec){
        
        ArrayList<Integer>slots = new ArrayList<Integer>();        
        
        /* create the slot list based on dorms */
        for(int i = 0; i < dorms.length; i++){
            slots.add(i);                                   //two slots per domitory
            slots.add(i);
        }//end for
        
        /* run over the solution to print it */
        for(int i = 0; i < vec.length; i++){
            int dormIndex = vec[i];                         //get student dormitory
            String dormName = dorms[slots.get(dormIndex)];  //get dormitory name
            System.out.println(prefs[i][0] + " = " + dormName);
            slots.remove(dormIndex);                        //remove the slot
        }//end for
        
    }//end printSolution() method
    
    /**
     * This method calculate the cost for a specfici problem
     * @param vec A <code>int[]</code> representing the solution
     * @return A <code>double</code> representing the cost
     */
    public double dormCost(int[]vec){
        
        ArrayList<Integer>slots = new ArrayList<Integer>();    
        double cost = 0.0;                                                      //final cost
        
        /* create the slot list based on dorms */
        for(int i = 0; i < dorms.length; i++){
            slots.add(i);                                                       //two slots per domitory
            slots.add(i);
        }//end for
        
        /* run over the solution */
        for(int i = 0; i < vec.length; i++){            
            int x = vec[i];
            String dorm = dorms[slots.get(x)];            
            /* is the preferred dorm */
            if(dorm.equals(prefs[i][1]))
                cost += 0;
            /* is the second choice room */
            else if(dorm.equals(prefs[i][2]))
                cost += 3;
            /* is not a preferred room */
            else
                cost += 5;            
            slots.remove(x);                                                    //remove the slot            
        }//end for
        
        return cost;                                                            //return the cost
        
    }//end dormCost() method
    
    
}//End DormsOptimization class

