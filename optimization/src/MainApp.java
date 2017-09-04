/*
 * Created on 13 November, 2008
 */

/**
 *
 * @author giscardf
 */
public class MainApp {

    /**
     * Main method to start application
     * @param args A <code>String[]</code> representing command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        Optimization optimization = new Optimization();
        optimization.parseSchedule(System.getProperty("user.dir") + "/src/schedule.txt");
//        optimization.printSchedule(new int[]{0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6});
//        double cost = optimization.scheduleCost(new int[]{0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6});
//        System.out.println("Total Cost: U$" + cost);
//        
//        /* trying random solutions */        
//        {
//        System.out.println(" ===== RANDOM SOLUTIONS ===== ");
//        int[][] domain = {{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8}};     //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = optimization.randomOptimize(1000, bestSolution, domain, "scheduleCost");
//        optimization.printSchedule(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        /* trying hill climb solution */
//        {
//        System.out.println(" ===== HILL CLIMBING ===== ");
//        int[][] domain = {{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8}};     //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = optimization.hillClimbing(bestSolution, domain, "scheduleCost");
//        optimization.printSchedule(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        /* trying annealing solution */
//        {
//        System.out.println(" ===== ANNEALING ===== ");
//        int[][] domain = {{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8}};     //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = optimization.annealingOptimize(bestSolution, domain, 10000.0, 0.95, 1, "scheduleCost", 10);
//        optimization.printSchedule(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        /* trying genetic solution */
//        {
//        System.out.println(" ===== GENETIC ===== ");
//        int[][] domain = {{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8},{0,8}};     //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = optimization.geneticOptimize(bestSolution, domain, "scheduleCost", 1000, 1, 0.2, 0.2, 100);
//        optimization.printSchedule(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        DormsOptimization dormOptimization = new DormsOptimization();
//        dormOptimization.printSolution(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
//        double cost = dormOptimization.dormCost(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
//        System.out.println("Cost is U$: " + cost);
//        
//        /* trying random solutions */        
//        {
//        System.out.println(" ===== RANDOM SOLUTIONS ===== ");
//        int[][] domain = DormsOptimization.domain;                              //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = dormOptimization.randomOptimize(1000, bestSolution, domain, "dormCost");
//        dormOptimization.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        /* trying hill climb solution */
//        {
//        System.out.println(" ===== HILL CLIMBING ===== ");
//        int[][] domain = DormsOptimization.domain;                              //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = dormOptimization.hillClimbing(bestSolution, domain, "dormCost");
//        dormOptimization.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//
//        /* trying annealing solution */
//        {
//        System.out.println(" ===== ANNEALING ===== ");
//        int[][] domain = DormsOptimization.domain;                              //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = dormOptimization.annealingOptimize(bestSolution, domain, 10000.0, 0.95, 1, "dormCost", 10);
//        dormOptimization.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//
//        /* trying genetic solution */
//        {
//        System.out.println(" ===== GENETIC ===== ");
//        int[][] domain = DormsOptimization.domain;                              //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = dormOptimization.geneticOptimize(bestSolution, domain, "dormCost", 1000, 1, 0.2, 0.2, 100);
//        dormOptimization.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//        
//        NetworkOptimize networkOptimize = new NetworkOptimize();
//        /* trying random solutions */        
//        {
//        System.out.println(" ===== RANDOM SOLUTIONS ===== ");
//        int[][] domain = NetworkOptimize.domain;                               //domain of valid values
//        int[] bestSolution = new int[domain.length];                            //points to best solution
//        double bestCost = networkOptimize.randomOptimize(1000, bestSolution, domain, "crossCost");
//        networkOptimize.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//
//        /* trying hill climb solution */
//        {
//        System.out.println(" ===== HILL CLIMBING ===== ");
//        int[][] domain = NetworkOptimize.domain;                               //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = networkOptimize.hillClimbing(bestSolution, domain, "crossCost");
//        networkOptimize.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//
//        
//        /* trying annealing solution */
//        {
//        System.out.println(" ===== ANNEALING ===== ");
//        int[][] domain = NetworkOptimize.domain;                               //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = networkOptimize.annealingOptimize(bestSolution, domain, 10000.0, 0.95, 1, "crossCost", 10);
//        networkOptimize.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }
//
//        /* trying genetic solution */
//        {
//        System.out.println(" ===== GENETIC ===== ");
//        int[][] domain = NetworkOptimize.domain;                              //domain of valid values
//        int[] bestSolution = new int[domain.length];                                                    //points to best solution
//        double bestCost = networkOptimize.geneticOptimize(bestSolution, domain, "crossCost", 1000, 1, 0.2, 0.2, 100);
//        networkOptimize.printSolution(bestSolution);
//        System.out.println("Total Cost: U$" + bestCost);
//        }

        MateOptimization mateOptimization = new MateOptimization();
//        mateOptimization.printSolution(new int[]{1, 1, 1, 4, 2, 2, 2, 0, 0, 0});
//        double cost = mateOptimization.mateCost(new int[]{1, 1, 1, 4, 2, 2, 2, 0, 0, 0});
//        System.out.println("Cost is U$: " + cost);
        
        /* trying random solutions */        
        {
        System.out.println(" ===== RANDOM SOLUTIONS ===== ");
        int[][] domain = MateOptimization.domain;                               //domain of valid values
        int[] bestSolution = new int[domain.length];                            //points to best solution
        double bestCost = mateOptimization.randomOptimize(100000, bestSolution, domain, "mateCost");
        mateOptimization.printSolution(bestSolution);
        System.out.println("Total Cost: U$" + bestCost);
        }
        
        /* trying hill climb solution */
        {
        System.out.println(" ===== HILL CLIMBING ===== ");
        int[][] domain = MateOptimization.domain;                               //domain of valid values
        int[] bestSolution = new int[domain.length];                            //points to best solution
        double bestCost = mateOptimization.hillClimbing(bestSolution, domain, "mateCost");
        mateOptimization.printSolution(bestSolution);
        System.out.println("Total Cost: U$" + bestCost);
        }

        /* trying annealing solution */
        {
        System.out.println(" ===== ANNEALING ===== ");
        int[][] domain = MateOptimization.domain;                               //domain of valid values
        int[] bestSolution = new int[domain.length];                            //points to best solution
        double bestCost = mateOptimization.annealingOptimize(bestSolution, domain, 10000.0, 0.95, 1, "mateCost", 10);
        mateOptimization.printSolution(bestSolution);
        System.out.println("Total Cost: U$" + bestCost);
        }

        /* trying genetic solution */
        {
        System.out.println(" ===== GENETIC ===== ");
        int[][] domain = MateOptimization.domain;                               //domain of valid values
        int[] bestSolution = new int[domain.length];                                                    //points to best solution
        double bestCost = mateOptimization.geneticOptimize(bestSolution, domain, "mateCost", 1000, 1, 0.2, 0.2, 100);
        mateOptimization.printSolution(bestSolution);
        System.out.println("Total Cost: U$" + bestCost);
        }
        
        
    }//end main() method
    
}//End MainApp class
