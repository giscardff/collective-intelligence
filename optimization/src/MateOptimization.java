/*
 * Created on 15 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class MateOptimization extends Optimization {

    private String students[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private String prefs[][] = {
                                {"A","B"},
                                {"B","C"},
                                {"C","D"},
                                {"D","E"},
                                {"E","F"},
                                {"F","G"},
                                {"G","H"},
                                {"H","I"},
                                {"I","J"},
                                {"J","A"}
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
        
        ArrayList<Integer>mates = new ArrayList<Integer>();        
        
        /* create the mate list based on students */
        for(int i = 0; i < students.length; i++){
            mates.add(i);                                                       //one mate per student
        }//end for
        
        /* run over the solution to print it */
        for(int i = 0; i < vec.length; i++){
            int x = vec[i];                                                     //get student dormitory
            String mateName = students[mates.get(x)];                           //get the mate name
            System.out.println(prefs[i][0] + " = " + mateName);
            mates.remove(x);                                                    //remove the slot
        }//end for
        
    }//end printSolution() method
    
    /**
     * This method calculate the cost for a specfici problem
     * @param vec A <code>int[]</code> representing the solution
     * @return A <code>double</code> representing the cost
     */
    public double mateCost(int[]vec){
        
        ArrayList<Integer>mates = new ArrayList<Integer>();    
        double cost = 0.0;                                                      //final cost
        
        /* create the mate list based on students */
        for(int i = 0; i < students.length; i++){
            mates.add(i);                                                       //one mate per student
        }//end for
        
        /* run over the solution */
        for(int i = 0; i < vec.length; i++){            
            int x = vec[i];
            String mate = students[mates.get(x)];            
            /* is the preferred dorm */
            if(mate.equals(prefs[i][1]))
                cost += 0;            
            else
                cost += 5;            
            mates.remove(x);                                                    //remove the slot            
        }//end for
        
        return cost;                                                            //return the cost
        
    }//end mateCost() method
    
}//End MateOptimization class
