/*
 * Created on 13 November, 2008
 */

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/**
 *
 * @author giscardf
 */
public class Optimization {

    public static String destination = "LGA";
    public static String[][] people = {{"Seymour", "BOS"},
                                       {"Franny","DAL"},
                                       {"Zooey","CAK"},
                                       {"Walt","MIA"},
                                       {"Buddy","ORD"},
                                       {"Les","OMA"}};
    
    TheDictionary<String,ArrayList<FlightData>> flights = new TheDictionary<String,ArrayList<FlightData>>();    //dictionary containing the flights
    
    /**
     * This method invokes using reflection a cost function and return it results
     * @param solution A <code>int[]</code> representing the solution
     * @param methodName A <code>String</code> representing the method name
     * @return A <code>double</code> representing the cost
     */
    public double costFunction(int[] solution, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {int[].class};                                    //An array for all attributes to invoke the method dynamically
        Method compareMethod = null;                                            //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);  //get the method name using reflection
            Double comparation = (Double)compareMethod.invoke(this, solution);  //invoke the method to compare
            return comparation;                                                 //return the comparison result
        }//end catch
        catch(NoSuchMethodException nsme){
            nsme.printStackTrace();            
        }//end catch
        catch(IllegalAccessException iae){
            iae.printStackTrace();            
        }//end catch
        catch(InvocationTargetException ite){
            ite.printStackTrace();
        }//end catch
        
        return 0.0;
        
    }//end similarityMethod() method
    
    /**
     * This method get a time in format HH:MM and return the amount of minutes
     * @param time A <code>String</code> representing the time in the format HH:MM
     * @return A <code>long</code> representing how much minutes it is starting from 00:00
     */
    private long getTime(String time){
        
        long minutes = 0;
        
        String[] tokens = time.split(":");              //parse the timer
        minutes += Integer.valueOf(tokens[0]) * 60;     //each hour has 60 minutes
        minutes += Integer.valueOf(tokens[1]) * 1;      //eahc minutes has 1 minute :-)
        
        return minutes;                                 //return the amount of minutes
        
    }//end getTime() method
    
    /**
     * This method get a population and mutate it
     * @param vec A <code>int[]</code> representing the population
     * @param domain A <code>int[][]</code> representing the domain of population
     * @param step A <code>int</code> representing how much to change
     * @return A <code>int[]</code> representing the mutated population
     */
    protected int[] mutate(int[] vec, int[][]domain, int step){
        
        int index = (int)Math.random() * vec.length;                            //get the index to change
        int mutate[] = new int[vec.length];                                     //mutated population        
        System.arraycopy(vec, 0, mutate, 0, vec.length);
        
        /* change population by incrementing it */
        if(Math.random() < 0.5 && ((vec[index] - step) > domain[index][0]))
            mutate[index] = vec[index] - step;
        else if(((vec[index] + step) < domain[index][1]))
            mutate[index] = vec[index] + step;
            
        return mutate;
        
    }//end mutate() method
    
    /**
     * This method get two population and cross it
     * @param vec1 A <code>int[]</code> representing a population
     * @param vec2 A <code>int[]</code> representing another population
     * @param domain A <code>int[][]</code> representing the domain of population
     * @return A <code>int[]</code> representing the corssed population
     */
    protected int[] crossover(int[] vec1, int[]vec2, int[][]domain){
        
        int index = (int)Math.random() * vec1.length;                           //get the index to Cross
        int crossed[] = new int[vec1.length];                                   //crossed population        
        
        System.arraycopy(vec1, 0, crossed, 0, index);                           //cross population
        System.arraycopy(vec2, index, crossed, index, vec2.length);             //cross population
        
        return crossed;
        
    }//end crossover() method
    
    /**
     * This method parse a schedule file and populate the dictionary of flights
     * @param fileName A <code>String</code> representing the file path
     * @return A <code>TheDictionary</code> representing the dictionary of flights
     */
    public TheDictionary<String,ArrayList<FlightData>> parseSchedule(String fileName) throws FileNotFoundException, IOException {    
        
        File file = new File(fileName);                                                                             //file to be read
        BufferedReader input =  new BufferedReader(new FileReader(file));                                           //IO interface to read file
        String line;                                                                                                //line read from the file
        
        /* keep reading the file line by line */
        line = input.readLine();
        while(line != null){            
            
            /* parse the line by getting the values */
            String[] tokens = line.split(",");
            String departure = tokens[0];
            String arrive = tokens[1];
            String departureTime = tokens[2];
            String arriveTime = tokens[3];
            String cost = tokens[4];            
            
            /* create the object and put into the map */
            FlightData flightData = new FlightData(departureTime, arriveTime, Double.valueOf(cost));
            if(flights.containsKey(departure, arrive) == false)                
                flights.put(new ArrayList<FlightData>(), departure, arrive);    //create the list of flight
            flights.get(departure, arrive).add(flightData);                     //add the new flight to the list
            
            /* read next line */
            line = input.readLine();
        }//end while        
        return flights;        //return all the flights        
        
    }//End parseSchedule() method
    
    /**
     * This method get a solution for the a problem (in this case the travel)
     * and print it in a Human Readable Format
     * @param solution A <code>int[]</code> representing the solution
     */
    public void printSchedule(int[] solution){
        
        /* 
         * run over the solution
         *     - event index is departure flight
         *     - odd index is arrive flight
         */
        for(int i = 0; i < solution.length; i += 2){
            String name = people[i / 2][0];                                                 //get person name
            String origin = people[i / 2][1];                                               //get person origin city
            FlightData out = flights.get(origin, destination).get(solution[i / 2]);         //get outgoing flights
            FlightData ret = flights.get(destination, origin).get(solution[i / 2 + 1]);     //get return flights
            System.out.printf("%s\t%s\t%s-t%s\t%4.2f / %s-t%s\t%4.2f\n", name, origin, out.getDeparture(), out.getArrive(), out.getCost(), ret.getDeparture(), ret.getArrive(), ret.getCost());
        }//end for
        
    }//end printSchedule() method
    
    /**
     * This method calculate the cost of a specific solution
     * @param solution A <code>int[]</code> representing the solution
     * @return A <code>double</code> representing the solution cost
     */
    public double scheduleCost(int[] solution){
        
        double totalPrice = 0.0;                    //total of money spend with air tickets
        long latestArrival = 0;                     //last person to arrive
        long earliestDeparture = 24 * 60;           //first person to departure
        long totalWait = 0;                         //total of time spend waiting
        
        /* 
         * run over the solution
         *     - event index is departure flight
         *     - odd index is arrive flight
         */
        for(int i = 0; i < solution.length; i += 2){            
            String origin = people[i / 2][1];                                               //get person origin city
            FlightData out = flights.get(origin, destination).get(solution[i / 2]);         //get outgoing flights
            FlightData ret = flights.get(destination, origin).get(solution[i / 2 + 1]);     //get return flights            
            /* calculate the total prices */
            totalPrice += out.getCost();
            totalPrice += ret.getCost(); 
            /* each minute at the airplane cost time, and time cost money */
            totalPrice += getTime(out.getArrive()) - getTime(out.getDeparture()) * 0.50;
            totalPrice += getTime(ret.getArrive()) - getTime(ret.getDeparture()) * 0.50;            
            /* penalize too earlier flights */
            if( getTime(out.getDeparture()) < getTime("08:00") )
                totalPrice += 20;
            if( getTime(ret.getDeparture()) < getTime("08:00") )
                totalPrice += 20;
            /* track the last arrival and first departure */
            latestArrival = (latestArrival < getTime(out.getArrive()) ? getTime(out.getArrive()) : latestArrival);
            earliestDeparture = (earliestDeparture > getTime(ret.getDeparture()) ? getTime(ret.getDeparture()) : earliestDeparture);            
        }//end for
        
        /* 
         * run over the solution
         *     - event index is departure flight
         *     - odd index is arrive flight
         */
        for(int i = 0; i < solution.length; i += 2){            
            String origin = people[i / 2][1];                                               //get person origin city
            FlightData out = flights.get(origin, destination).get(solution[i / 2]);         //get outgoing flights
            FlightData ret = flights.get(destination, origin).get(solution[i / 2 + 1]);     //get return flights            
            /* calculate the total wait */
            totalWait += latestArrival - getTime(out.getArrive());
            totalWait += getTime(ret.getDeparture()) - earliestDeparture;            
        }//end for
        
        /* check for extra day car rental */
        if(latestArrival > earliestDeparture)
            totalPrice += 50;
        
        return totalPrice + totalWait;
        
    }//end scheduleCost() method
    
    /**
     * This method define the best cost solution by inputing random values
     * @param nRand A <code>int</code> representing how many random solutions shall be tried
     * @param domain A <code>int[]</code> representing the domain of possible solutions
     * @param costFunction A <code>String</code> representing the cost function name
     */
    public double randomOptimize(int nRand, int[] bestSolution, int[][] domain, String costFunction){
        
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[] randSolution = new int[domain.length];                            //contains a random solution        
        
        /* run n random solutions */
        for(int i = 0; i < nRand; i++){            
            /* create a random solution */
            for(int j = 0; j < domain.length; j++){
                randSolution[j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
            }//end for
            /* get the cost */
            double cost = this.costFunction(randSolution, costFunction);
            
            /* if cost is better, update best cost */
            if(bestCost > cost){
                bestCost = cost;
                System.arraycopy(randSolution, 0, bestSolution, 0, randSolution.length);
            }//end if
        }//end for
        
        return bestCost;                                                        //return the best cost
        
    }//end randomOptimize() method
    
    /**
     * This method define the best cost solution by using hill climbind idea
     * @param bestSolution A <code>int[]</code> representing the best find solution
     * @param domain A <code>int[][]</code> representing the domain of possible solutions
     * @param costFunction A <code>String</code> representing the cost function name
     * @return A <code>double</code> representing the cost
     */
    public double hillClimbing(int[] bestSolution, int[][]domain, String costFunction){
        
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[] solution = new int[domain.length];                            //contains a random solution        
        
        /* create a random solution */
        for(int j = 0; j < domain.length; j++){
            solution[j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
        }//end for
        
        /* run up until stop to improve costs */
        while(true){
            
            int[][]neighbor = new int[2][solution.length];
            
            /* run over the solution to get the beighboord */
            for(int i = 0; i < solution.length; i++){
                /* get next neighboor at the right */
                if(solution[i] < domain[i][1])
                    neighbor[1][i] = solution[i] + 1;
                /* get next neighboor at the left */                
                if(solution[i] > domain[i][0])
                    neighbor[0][i] = solution[i] - 1;
            }//end for
            
            /* get the cost for current */
            double currentCost = costFunction(solution, costFunction);
            bestCost = currentCost;
            
            /* get the cost for the neighbors */
            for(int i = 0; i < neighbor.length; i++){
                double cost = costFunction(neighbor[i], costFunction);
                /* check if cost was improved */
                if(bestCost > cost){
                    bestCost = cost;
                    System.arraycopy(neighbor[i], 0, solution, 0, solution.length);
                }//end if
            }//end for
            
            /* if cost was not improved in both neighbors */
            if(currentCost == bestCost)
                break;
            
        }//end while
        
        System.arraycopy(solution, 0, bestSolution, 0, solution.length);        //copy the best solution into array
        return bestCost;                                                        //return the best cost
        
    }//end hillClimbing
    
    /**
     * This method defines the best cost solution by using annealing
     * @param bestSolution A <code>int[]</code> pointing the best solution
     * @param domain A <code>int[][]</code> representing the domain for valid solutions
     * @param T A <code>double</code> representing how hot is jump to worse solutions
     * @param cool A <code>double</code> representing how to cool jump to worse solutions
     * @param step A <code>int</code> represening how much the next solutions will vary
     * @param costFunction A <code>String</code> representing the cost function name
     * @return A <code>double</code> representing the cost
     */
    public double annealingOptimize(int[] bestSolution, int[][]domain, double T, double cool, int step, String costFunction, int maxIter){
        
        double saveT = T;                                                       //save temperature over iteration
        double bestBestCost = Double.MAX_VALUE;                                 //best cost for all iterations
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[] vec = new int[domain.length];                                     //contains a random solution        
        int[] vecn = new int[domain.length];                                    //contains next moved solution
        
        /* iterates n times using random numbers */
        for(int i = 0; i < maxIter; i++){
        
            T = saveT;                                                          //restore temperature value
            
            /* create a random solution */
            for(int j = 0; j < domain.length; j++){
                vec[j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
            }//end for

            /* run until temperature becomes cool */
            while(T > 0.1){

                int index = (int)(Math.random() * domain.length);               //select the variable to change
                int direction = (int)(Math.random() * (step + step)) - step;    //select direction to move -1 or +1

                /* creates new solution and change it */
                System.arraycopy(vec, 0, vecn, 0, vec.length);                  //copy the best solution into array
                vecn[index] += direction;                                       //change the solution
                /* do not allow to pass array domain limits */
                if(vecn[index] < domain[index][0])
                    vecn[index] = domain[index][0];
                else if(vecn[index] > domain[index][1])
                    vecn[index] = domain[index][1];

                /* calculate both costs */
                double curCost = costFunction(vec, costFunction);
                double nextCost = costFunction(vecn, costFunction);

                /* let's calculate the weallingness to get the best or worst case */
                double prob = Math.pow(Math.E, ((-1 * nextCost) - curCost)/T);            
                if((nextCost < curCost) || (Math.random() < prob)){
                    System.arraycopy(vecn, 0, vec, 0, vecn.length);             //copy the best solution into array
                    bestCost = nextCost;
                }//end if

                T = T * cool;                                                   //decrease the temperature

            }//end while
            
            /* check if this iteration got best result */
            if(bestBestCost > bestCost){
                bestBestCost = bestCost;
                System.arraycopy(vec, 0, bestSolution, 0, vec.length);          //copy the best solution into array
            }//end if
            
        }//end for
        
        return bestBestCost;                                                    //return the best cost
        
    }//end annealingOptimize() method
    
    /**
     * This method get the best solution by using natual selection
     * @param bestSolution A <code>int[]</code> pointing to the best solution
     * @param domain A <code>int[][]</code> representing the domain of solutions
     * @param costFunction A <code>String</code> representing the cost function
     * @param popSize A <code>int</code> representing the maximum sized of population
     * @param step A <code>int</code> representing how to change the population
     * @param mutProb A <code>double</code> representing the probablity of a population suffer mutation
     * @param elite A <code>double</code> representing the percentage of population to be selected
     * @param maxIter A <code>int</code> representing how many interaction (selections) to perform
     * @return A <code>double</code> representing the best solution cost
     */
    public double geneticOptimize(int[]bestSolution, int[][]domain, String costFunction, int popSize, int step, double mutProb, double elite, int maxIter){
        
        int noImprovement = 0;                                                  //count iterations that did not improve result
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[][] pop = new int[popSize][domain.length];                          //contains a random solution                
        int topElite = (int)(popSize * elite);                                  //calculate the toppest pop whom will be selected                
        TreeMap<Double,Integer>scores = new TreeMap<Double,Integer>();          //contains the score for each population        
        
        /* create a random solution */
        for(int i = 0; i < pop.length; i++){
            for(int j = 0; j < domain.length; j++){
                pop[i][j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
            }//end for
        }//end for
        
        /* keep doiing natural selection n times */
        //for(int i = 0; i < maxIter; i++){
        while(true){
            
            /* get the cost for each population */
            for(int j = 0; j < popSize; j++)
                scores.put(costFunction(pop[j], costFunction), j);
            
            /* run over top population and separate them */            
            int[][] ranked = new int[topElite][domain.length];
            int k = 0;
            for(Iterator<Double>iterator = scores.keySet().iterator(); iterator.hasNext();){
                double key = iterator.next();
                ranked[k++] = pop[scores.get(key)];
                /* stop to fileter when reach topest */
                if(k >= topElite)
                    break;
            }//end for
            
            /* update population with toppest winners */
            for(int j = 0; j < topElite; j++){
                System.arraycopy(ranked[j], 0, pop[j], 0, ranked[j].length);
            }//end for
            
            /* apply mutation on the rest of population */
            for(int j = topElite; j < popSize; j++){                
                /* if suffer mutation */
                if(Math.random() < mutProb){
                    int index = (int)(Math.random() * topElite);                //get a index to mutate
                    int[] mutate = this.mutate(ranked[index], domain, step);    //generate mutation
                    pop[j] = mutate;                                            //update population
                }//end if
                /* otherwise cross over */
                else{
                    int indexA = (int)(Math.random() * topElite);                //get a index to mutate
                    int indexB = (int)(Math.random() * topElite);                //get a index to mutate
                    int crossed[] = this.crossover(ranked[indexA], ranked[indexB], domain);
                    pop[j] = crossed;
                }//end else                
            }//end for
            
            /* check if result did not reduce cost */
            if(bestCost <= scores.firstEntry().getKey()){
                noImprovement++;                                                //increment no improvement
            }//end if
            else{
                noImprovement = 0;                                              //reset counter
                /* get the final best result */
                Map.Entry<Double,Integer> toppest = scores.firstEntry();        
                bestCost = toppest.getKey();                                                        //get the final best cost        
                System.arraycopy(ranked[0], 0, bestSolution, 0, bestSolution.length); //get the final best population
            }//end else
            
            /* if iterates n times with no improvement, let's get out */
            if(noImprovement == maxIter)
                break;
            
        }//end while
        
        return bestCost;                                                        //return the best cost
        
    }//end geneticOptimize() method
    
}//End Optimization class

class FlightData {
    
    private String departure;       //define the departure date of a flight
    private String arrive;          //define the arrive date of a flight
    private double cost;            //define the cost of a flight
    
    /**
     * Creates a new FlighData instance
     * @param departure A <code>String</code> representing the date of departure
     * @param arrive A <code>String</code> representing the date of arrive
     * @param cost A <code>double</code> representing the cost of flight
     */
    public FlightData(String departure, String arrive, double cost){
        this.departure = departure;
        this.arrive = arrive;
        this.cost = cost;
    }//end FlighData() constructor

    /**
     * This method get the departure date of the flight
     * @return A <code>String</code> representing the date of departure
     */
    public String getDeparture() {
        return departure;
    }//end getDeparture() method
    
    /**
     * This method get the arrive date of the flight
     * @return A <code>String</code> representing the date of arrive
     */
    public String getArrive() {
        return arrive;
    }//end getArrive() method

    /**
     * This method get the cost of the flight
     * @return A <code>double</code> representing the cost of flight
     */
    public double getCost() {
        return cost;
    }//end getCost() method
    
    
    
}//End FlightData class
