/*
 * Created on 13 December 2008
 */

import java.lang.reflect.*;
import org.jfree.data.xy.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;

/**
 *
 * @author giscardf
 */
public class NumberPredict {

    /**
     * This method invokes a inverse fuction based on method name
     * @param param1
     * @param param2
     * @param param3
     * @param methodName A <code>String</code> representing the inverse function method
     * @return A <code>double</code> representing the inverse value
     */
    public double inverseMethod(double param1, double param2, double param3, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = null;                                              //An array for all attributes to invoke the method dynamically
        if(methodName.equals("inverseweight")){
            parameters = new Class[]{Double.class, Double.class, Double.class};
        }//end if
        else if(methodName.equals("subtractweight")){
            parameters = new Class[]{Double.class, Double.class};
        }//end if
        else if(methodName.equals("gaussian")){
            parameters = new Class[]{Double.class, Double.class};
        }//end method
        
        Method inverseMethod = null;                                    //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            inverseMethod = this.getClass().getMethod(methodName, parameters);  //get the method name using reflection
            if(methodName.equals("inverseweight")){
                return (Double)inverseMethod.invoke(this, param1, param2, param3); //invoke the method to compare
            }//end if
            else if(methodName.equals("subtractweight")){
                return (Double)inverseMethod.invoke(this, param1, param2); //invoke the method to compare
            }//end if
            else if(methodName.equals("gaussian")){
                return (Double)inverseMethod.invoke(this, param1, param2); //invoke the method to compare
            }//end method                        
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
    
    
    public double estimativeMethod(TheData param1, TheDataSet param2, int param3, String param4, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {TheData.class, TheDataSet.class, int.class, String.class};
        Method estimativeMethod = null;                                                     //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            estimativeMethod = this.getClass().getMethod(methodName, parameters);           //get the method name using reflection
            return (Double)estimativeMethod.invoke(this, param1, param2, param3, param4);   //invoke the method to compare            
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
        
    }//end estimativeMethod() method
    
    /**
     * This method calculate the wine price based on its rating and age
     * @param rating A <code>double</code> representing the wine rating
     * @param age A <code>double</code> representing the wine age
     * @return  A <code>double</code> representing the wine price
     */
    public double wineprice(double rating, double age){        
         double peak_age = rating - 50.0;         
         /* calculate price based on rate */
         double price = rating / 2.0;
         if(age > peak_age){
             /* Past its peak, goes bad in 5 years */
             price = price * (5 - (age - peak_age));
         }//end if
         else{
             /* Increase to 5x its original value as it approaces its peak */
             price = price * (5 * ((age + 1)/peak_age));
         }//end else         
         return (price < 0.0 ? 0.0 : price);        
    }//end wineprice() method
    
    /**
     * This method create a dataset of wines
     * @return A <code>TheDataSet</code> representing the set of wines
     */
    public TheDataSet wineset1(){
        TheDataSet<Double,Double> rows = new TheDataSet<Double,Double>();
        for(int i = 0; i < 300; i++){
            /* create random age and rating */
            double rating = Math.floor(Math.random() * 50 + 50);
            double age = Math.floor(Math.random() * 50);
            /* get the reference price */
            double price = (int)this.wineprice(rating, age);
            /* put some noise on the data */
            price *= Math.random() * 0.4 + 0.8;
            /* add to the dataset */
            rows.put(price, rating, age);
        }//end for
        return rows;
    }//end wineset1() method
    
    /**
     * This method create a dataset of wines
     * @return A <code>TheDataSet</code> representing the set of wines
     */
    public TheDataSet wineset2(){
        TheDataSet<Double,Double> rows = new TheDataSet<Double,Double>();
        for(int i = 0; i < 300; i++){
            /* create random age and rating */
            double rating = Math.floor(Math.random() * 50 + 50);
            double age = Math.floor(Math.random() * 50);
            double aisle = Math.floor(Math.random() * 20);
            double bottlesizes[] = {375.0, 750.0, 1500.0, 3000.0};
            double bottlesize = bottlesizes[(int)Math.floor(Math.random() * 3)];
            /* get the reference price */
            double price = (int)this.wineprice(rating, age);
            price *= bottlesize / 750.0;
            /* put some noise on the data */
            price *= Math.random() * 0.9 + 0.2;
            /* add to the dataset */
            rows.put(price, rating, age, aisle, bottlesize);
        }//end for
        return rows;
    }//end wineset2() method
    
    /**
     * This method create a dataset of wines
     * @return A <code>TheDataSet</code> representing the set of wines
     */
    public TheDataSet wineset3(){
        TheDataSet<Double,Double> rows = this.wineset1();
        for(int i = 0; i < rows.size(); i++){
            if(Math.random() < 0.5){
                /* put some discount on the wine */
                TheData<Double,Double> data = rows.get(i);
                rows.put(data.getData() * 0.6, data.getKeys(new Double[data.keySize()]));
            }//end if
        }//end for
        return rows;
    }//end wineset3() method
    
    /**
     * This method calculate the distance between two objects
     * @param v1 A <code>double</code> representing the first object
     * @param v2 A <code>double</code> representing the second object
     * @return A <code>double</code> representing the distance
     */
    public double euclidean(Double[] v1, Double[] v2){
        double dist = 0.0;
        for(int i = 0; i < v1.length; i++){            
            dist += Math.pow((v1[i] - v2[i]), 2);
        }//end for
        return Math.sqrt(dist);
    }//end euclidean() method
    
    /**
     * This method get all the distances from a specific data and all the other ones in the dataset
     * @param data A <code>TheData</code> representing the data to be compared
     * @param wineSet A <code>TheDataSet</code> representing all the other datas
     * @return A <code>TheDataSet</code> sorted by distance
     */
    public TheDataSet<TheData,Double> getdistances(TheData<Double,Double> data, TheDataSet dataset){
        TheDataSet<TheData,Double> distSet = new TheDataSet<TheData,Double>();
        for(int i = 0; i < dataset.size(); i++){            
            TheData<Double,Double> toCompare = dataset.get(i);
            /* do not compare the same data */
            if(data.equals(toCompare))
                continue;            
            double dist = this.euclidean(data.getKeys(new Double[data.keySize()]), toCompare.getKeys(new Double[toCompare.keySize()]));
            distSet.put(dist, data, toCompare);
        }//end for        
        return distSet.sort();
    }//end getdistance() method
    
    /**
     * This method calculate the price based on knn estimate
     * @param data A <code>TheData</code> representing the item to get the price average
     * @param dataset A <code>TheDataSet</code> representing all other data to take as sample
     * @param k A <code>int</code> representing how much neighbors will be used as sample
     * @return A <code>double</code> representing the price average
     */
    public double knnestimate(TheData<Double,Double> data, TheDataSet<TheData,Double> dataset, int k, String method){
        /* get sorted distance */
        TheDataSet<TheData,Double> distSet = this.getdistances(data, dataset);
        double avg = 0.0;
        /* get the average of top k results */
        for(int i = 0; i < k; i++){
            TheData<TheData,Double> distData = distSet.get(i);            
            TheData<Double,Double> datas[] = distData.getKeys(new TheData[distData.keySize()]);
            for(int j = 0; j < datas.length; j++){
                if(!datas[j].equals(data))
                    avg += datas[j].getData();
            }//end for
        }//end for
        return (double)(avg / k);
    }//end knnestimate() method
    
    /**
     * This method calculate the inverse of the distance
     * @param distance A <code>double</code> representing the distance
     * @param num A <code>double</code> representing the numerator value (usually 1.0)
     * @param factor A <code>double</code> representing the adding factor to now allow divide by zero (usually 0.1)
     * @return A <code>double</code> representing the inverse function
     */
    public double inverseweight(Double distance, Double num, Double factor){
        return num / (distance + factor);
    }//end inverseweight() method
    
    /**
     * This method calculate the subract of the distance
     * @param distance A <code>double</code> representing the distance     
     * @param factor A <code>double</code> representing the adding factor to now allow divide by zero (usually 0.1)
     * @return A <code>double</code> representing the inverse function
     */
    public double subtractweight(Double distance, Double factor){
        if(distance > factor)
            return 0;
        else
            return factor - distance;
    }//end substractweight() method
    
    /**
     * This method calculate the inverse of distance by using gaussian curve
     * @param distance A <code>double</code> representing the distance     
     * @param sigma A <code>double</code> representing row tough the curve will be (usually 10.0)
     * @return A <code>double</code> representing the inverse function
     */
    public double gaussian(Double distance, Double sigma){
        double num = -1 * Math.pow(distance,2);
        double den = 2 * Math.pow(sigma,2);        
        return Math.pow(Math.E , num / den);
    }//end gaussian() method
    
    /**
     * This method calcule the price based on weighter knn estimate
     * This method calculate the price based on knn estimate
     * @param data A <code>TheData</code> representing the item to get the price average
     * @param dataset A <code>TheDataSet</code> representing all other data to take as sample
     * @param k A <code>int</code> representing how much neighbors will be used as sample
     * @param method A <code>String</code> representing the method to calcule the weight
     * @return A <code>double</code> representing the final price
     */
    public double weightedknn(TheData<Double,Double> data, TheDataSet<Double,Double> dataset, int k, String method){
        /* get the distances */
        TheDataSet<TheData,Double> distance = this.getdistances(data, dataset);
        double avg = 0.0;
        double totalweight = 0.0;
        /* calculate weight average */
        for(int i = 0; i < k; i++){
            TheData<TheData,Double> distData = distance.get(i);
            double weight = this.inverseMethod(distData.getData(), 10.0, 0.1, "gaussian");
            TheData<Double,Double> datas[] = distData.getKeys(new TheData[distData.keySize()]);
            for(int j = 0; j < datas.length; j++){
                if(!datas[j].equals(data))
                    avg += datas[j].getData() * weight;
            }//end for            
            totalweight += weight;
        }//end for
        return avg / totalweight;
    }//end weightedknn() method
    
    /**
     * This method divide a dataset into two new groups
     * @param dataset A <code>TheDataSet</code> representing the dataset
     * @param test A <code>double</code> representing the percentage of data to be considered test
     * @return A <code>TheDataSet[]</code> representing the two new created dataset
     */
    public TheDataSet[] dividedata(TheDataSet<Double,Double> dataset, double test){        
        TheDataSet<Double,Double> trainset = new TheDataSet<Double,Double>();
        TheDataSet<Double,Double> testset = new TheDataSet<Double,Double>();
        for(int i = 0; i < dataset.size(); i++){
            if(Math.random() < test)
                testset.add(dataset.get(i));
            else
                trainset.add(dataset.get(i));
        }//end for
        return new TheDataSet[]{trainset, testset};
    }//end dividedata() method
    
    /**
     * This method divide a dataset into two new groups
     * @param dataset A <code>TheDataSet</code> representing the dataset
     * @param test A <code>int</code> representing the index of the dataset to be tested
     * @return A <code>TheDataSet[]</code> representing the two new created dataset
     */
    public TheDataSet[] dividedata(TheDataSet<Double,Double> dataset, int test){        
        TheDataSet<Double,Double> trainset = new TheDataSet<Double,Double>();
        TheDataSet<Double,Double> testset = new TheDataSet<Double,Double>();
        for(int i = 0; i < dataset.size(); i++){
            if(i != test)
                trainset.add(dataset.get(i));
            else
                testset.add(dataset.get(i));
        }//end for
        return new TheDataSet[]{trainset, testset};
    }//end dividedata() method
    
    /**
     * This method test the knn weight estimate algorithm
     * @param trainset A <code>TheDataSet</code> representing the dataset to use as traine
     * @param testset A <code>TheDataSet</code> representing the dataset to use as test
     * @param method A <code>String</code> representing the metnod of estimative
     * @return A <code>double</code> representing how big is the error
     */
    public double testalgorithm(TheDataSet<Double,Double> trainset, TheDataSet<Double,Double> testset, String method){        
        double error = 0.0;
        for(int i = 0; i < testset.size(); i++){
            TheData<Double,Double> testdata = testset.get(i);
            double guess = this.estimativeMethod(testdata, trainset, 5, "gaussian", method);
            error += Math.abs(guess - testdata.getData());
            //error += Math.pow(guess - testdata.getData(), 2);
        }//end for
        return (double)(error / testset.size());
    }//end testalgorithm() method
    
    /**
     * This method test the knn weight estimate algorithm
     * @param trainset A <code>TheDataSet</code> representing the dataset to use as traine
     * @param testset A <code>TheDataSet</code> representing the dataset to use as test
     * @param method A <code>String</code> representing the metnod of estimative
     * @param k A <code>int</code> representing the k neighbor
     * @return A <code>double</code> representing how big is the error
     */
    public double testalgorithm(TheDataSet<Double,Double> trainset, TheDataSet<Double,Double> testset, int k, String method){        
        double error = 0.0;
        for(int i = 0; i < testset.size(); i++){
            TheData<Double,Double> testdata = testset.get(i);
            double guess = this.estimativeMethod(testdata, trainset, k, "gaussian", method);
            error += Math.abs(guess - testdata.getData());
            //error += Math.pow(guess - testdata.getData(), 2);
        }//end for
        return (double)(error / testset.size());
    }//end testalgorithm() method
       
    /**
     * This method test the knn trial times in order to get algorithm accuracy
     * @param dataset A <code>TheDataSet</code> representing the whole dataset
     * @param trials A <code>double</code> representing amount of attempts
     * @param test A <code>double</code> representing the percentage of test sample
     * @param method A <code>String</code> representing the metnod of estimative
     * @return A <code>double</code> representing the final error
     */
    public double crossvalidate(TheDataSet<Double,Double>dataset, double trials, double test, String method){
        double error = 0.0;
        for(int i = 0; i < trials; i++){
            TheDataSet<Double,Double>sets[] = this.dividedata(dataset, test);
            error += this.testalgorithm(sets[0], sets[1], method);
        }//end for
        return (double)(error / trials);
    }//end crossvalidate() method
    
    /**
     * This method test the knn size times in order to get algorithm accuracy
     * @param dataset A <code>TheDataSet</code> representing the whole dataset     
     * @param method A <code>String</code> representing the metnod of estimative
     * @return A <code>double</code> representing the final error
     */
    public double leaveoneoutcrossvalidation(TheDataSet<Double,Double>dataset, String method){
        double error = 0.0;
        for(int i = 0; i < dataset.size(); i++){
            TheDataSet<Double,Double>sets[] = this.dividedata(dataset, i);
            error += this.testalgorithm(sets[0], sets[1], method);
        }//end for
        return (double)(error / dataset.size());
    }//end leaveoneoutcrossvalidation() method
    
    /**
     * This method test the knn trial times in order to get algorithm accuracy
     * @param dataset A <code>TheDataSet</code> representing the whole dataset
     * @param trials A <code>double</code> representing amount of attempts
     * @param test A <code>double</code> representing the percentage of test sample
     * @param k A <code>int</code> representing the k neighbor
     * @param method A <code>String</code> representing the metnod of estimative
     * @return A <code>double</code> representing the final error
     */
    public double crossvalidate(TheDataSet<Double,Double>dataset, double trials, double test, int k, String method){
        double error = 0.0;
        for(int i = 0; i < trials; i++){
            TheDataSet<Double,Double>sets[] = this.dividedata(dataset, test);
            error += this.testalgorithm(sets[0], sets[1], method);
        }//end for
        return (double)(error / trials);
    }//end crossvalidate() method
    
    /**
     * This method rescale a dataset key values
     * @param dataset A <code>TheDataSet</code> representing the dataset to scale
     * @param scale A <code>double[]</code> representing all the scale values
     * @return A <code>TheDataSet</code> representing the scaled dataset
     */
    public TheDataSet<Double,Double> rescale(TheDataSet<Double,Double>dataset, int[] scale){        
        TheDataSet<Double,Double> scaledataset = new TheDataSet<Double,Double>();
        for(int i = 0; i < dataset.size(); i++){
            TheData<Double,Double>data = dataset.get(i);
            Double[] keys = data.getKeys(new Double[data.keySize()]);
            for(int j = 0; j < keys.length; j++){
                keys[j] *= scale[j];
            }//end for
            scaledataset.put(data.getData(), keys);
        }//end for
        return scaledataset;
    }//end rescale() method
    
    /**
     * This method defines the best cost solution by using annealing
     * @param bestSolution A <code>int[]</code> pointing the best solution
     * @param domain A <code>int[][]</code> representing the domain for valid solutions
     * @param T A <code>double</code> representing how hot is jump to worse solutions
     * @param cool A <code>double</code> representing how to cool jump to worse solutions
     * @param step A <code>int</code> represening how much the next solutions will vary     
     * @param dataset A <code>TheDataSet</code> representing the dataset to analyze
     * @param estMethod A <code>String</code> representing the estimative model to use
     * @return A <code>double</code> representing the best cost
     */
    public double annealingOptimize(int[] bestSolution, int[][]domain, double T, double cool, int step, TheDataSet<Double,Double> dataset, String estMethod){        
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[] vec = new int[domain.length];                                     //contains a random solution        
        int[] vecn = new int[domain.length];                                    //contains next moved solution
        
        /* create a random solution */
        for(int j = 0; j < domain.length; j++){
            vec[j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
        }//end for
        
        /* run until temperature becomes cool */
        while(T > 0.1){
            int index = (int)(Math.random() * domain.length);                           //select the variable to change
            int direction = (int)Math.floor((Math.random() * (step + step)) - step);    //select direction to move -1 or +1
            /* creates new solution and change it */
            System.arraycopy(vec, 0, vecn, 0, vec.length);                  //copy the best solution into array
            vecn[index] += direction;                                       //change the solution
            /* do not allow to pass array domain limits */
            if(vecn[index] < domain[index][0])
                vecn[index] = domain[index][0];
            else if(vecn[index] > domain[index][1])
                vecn[index] = domain[index][1];
            /* calculate both costs */
            TheDataSet<Double,Double> datasetc = this.rescale(dataset, vec);
            TheDataSet<Double,Double> datasetn = this.rescale(dataset, vecn);
            double curCost = this.crossvalidate(datasetc, 100, 0.05, estMethod);
            double nextCost = this.crossvalidate(datasetn, 100, 0.05, estMethod);
            /* let's calculate the weallingness to get the best or worst case */
            double prob = Math.pow(Math.E, ((-1 * nextCost) - curCost)/T);            
            if((nextCost < curCost) || (Math.random() < prob)){
                System.arraycopy(vecn, 0, vec, 0, vecn.length);             //copy the best solution into array
                System.arraycopy(vec, 0, bestSolution, 0, vec.length);      //copy the best solution
                bestCost = nextCost;
            }//end if
            T = T * cool;                                                   //decrease the temperature
            System.out.println("T: " + T);
        }//end while
        return bestCost;                                                    //return the best cost        
    }//end annealingOptimize() method
    
    /**
     * This method defines the best cost solution by using annealing
     * @param bestSolution A <code>int[]</code> pointing the best solution
     * @param domain A <code>int[][]</code> representing the domain for valid solutions
     * @param T A <code>double</code> representing how hot is jump to worse solutions
     * @param cool A <code>double</code> representing how to cool jump to worse solutions
     * @param step A <code>int</code> represening how much the next solutions will vary     
     * @param dataset A <code>TheDataSet</code> representing the dataset to analyze
     * @param estMethod A <code>String</code> representing the estimative model to use
     * @return A <code>double</code> representing the best cost
     */
    public double annealingOptimizeForK(int[] bestSolution, int[][]domain, double T, double cool, int step, TheDataSet<Double,Double> dataset, String estMethod){        
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        int[] vec = new int[domain.length];                                     //contains a random solution        
        int[] vecn = new int[domain.length];                                    //contains next moved solution
        
        /* create a random solution */
        for(int j = 0; j < domain.length; j++){
            vec[j] = domain[j][0] + (int)(Math.random() * (domain[j][1] - domain[j][0]));
        }//end for
        
        /* run until temperature becomes cool */
        while(T > 0.1){
            int index = (int)(Math.random() * domain.length);                           //select the variable to change
            int direction = (int)Math.floor((Math.random() * (step + step)) - step);    //select direction to move -1 or +1
            /* creates new solution and change it */
            System.arraycopy(vec, 0, vecn, 0, vec.length);                  //copy the best solution into array
            vecn[index] += direction;                                       //change the solution
            /* do not allow to pass array domain limits */
            if(vecn[index] < domain[index][0])
                vecn[index] = domain[index][0];
            else if(vecn[index] > domain[index][1])
                vecn[index] = domain[index][1];
            /* calculate both costs */
            //TheDataSet<Double,Double> datasetc = this.rescale(dataset, vec);
            //TheDataSet<Double,Double> datasetn = this.rescale(dataset, vecn);
            double curCost = this.crossvalidate(dataset, 10, 0.05, vec[0], estMethod);
            double nextCost = this.crossvalidate(dataset, 10, 0.05, vecn[0], estMethod);
            /* let's calculate the weallingness to get the best or worst case */
            double prob = Math.pow(Math.E, ((-1 * nextCost) - curCost)/T);            
            if((nextCost < curCost) || (Math.random() < prob)){
                System.arraycopy(vecn, 0, vec, 0, vecn.length);             //copy the best solution into array
                System.arraycopy(vec, 0, bestSolution, 0, vec.length);      //copy the best solution
                bestCost = nextCost;
            }//end if
            T = T * cool;                                                   //decrease the temperature
            System.out.println("T: " + T);
        }//end while
        return bestCost;                                                    //return the best cost        
    }//end annealingOptimizeForK() method
    
    /**
     * This method check all useless variables
     * @param bestSolution A <code>int[]</code> representing the best solution
     * @param dataset A <code>TheDataSet</code> representing the dataset
     * @param estMethod A <code>String</code> representing the estimative method
     */
    public void optimizeZero(int[] bestSolution, TheDataSet<Double,Double> dataset, String estMethod){        
        
        for(int i = 0; i < bestSolution.length; i++){
            bestSolution[i] = 1;
            /* restart all scale */
            int[] scale = new int[bestSolution.length];
            for(int j = 0; j < scale.length; j++)
                scale[j] = (i == j ? 0 : 1);
            /* calculate both values */
            double value = this.crossvalidate(dataset, 100, 0.05, estMethod);
            double valuescaled = this.crossvalidate(this.rescale(dataset, scale), 100, 0.05, estMethod);
            /* check if the values did not changed */
            if(value > valuescaled)
                bestSolution[i] = 0;
        }//end for
        
    }//end optimizeZero() method
    
    /**
     * This method calculate the cost of the gauss function based on ss value
     * @param dataset A <code>TheDataSet</code> representing dataset
     * @param data A <code>TheData</code> representing the data
     * @param low A <code>double</code> representing the low limit
     * @param high A <code>double</code> representing the high limit
     * @param k A <code>int</code> representing the number of neighbor
     * @param method A <code>String</code> representing the method of estimative     
     * @return A <code>double</code> representing the cost
     */
    public double gaussCost(TheDataSet<Double,Double> dataset, TheData<Double,Double>data, double high, int k, double ss, String method){        
        double series[][] = new double[2][101];
        double smoothie[][] = new double[2][101];        
        for(int i = 0; i <= high; i++){
            smoothie[0][i] = series[0][i] = i;
            series[1][i] = this.probguess(dataset, data, ((i - 1) < 0 ? 0:(i - 1)), i, k, method);
        }//end for
        
        for(int i = 0; i <= high; i++){
            double sv = 0.0;
            for(int j = 0; j <= high; j++){
                double dist = Math.abs(i - j) * 1;
                double weight = this.gaussian(dist, ss);
                sv += weight * series[1][j];
            }//end for
            smoothie[1][i] = sv;
        }//end for
        
        boolean flipUp = false;
        double costFlip = 0.0;
        double costPeak = 0.0;
        double peak = 0.0;        
        for(int i = 1; i <= high; i++){
            if(flipUp == false && smoothie[1][i - 1] < smoothie[1][i]){
                costFlip += smoothie[1][i - 1];
                flipUp = true;
            }//end if
            /* getting down the graph */
            if(smoothie[1][i - 1] > smoothie[1][i]){
                flipUp = false;
                /* check if peaks are closer */
                costPeak += 1 / ((smoothie[0][i - 1] - peak) * high);
                peak = smoothie[0][i - 1];
                
            }//end if
        }//end for
        
        return (costFlip + costPeak);        
    }//end gaussCost() method
    
    /**
     * This method get the best cost for a gauss function
     * @param dataset A <code>TheDataSet</code> representing dataset
     * @param data A <code>TheData</code> representing the data
     * @param low A <code>double</code> representing the low limit
     * @param high A <code>double</code> representing the high limit
     * @param k A <code>int</code> representing the number of neighbor
     * @param ssMin A <code>double</code> representing the start ss value
     * @param ssMax A <code>double</code> representing the end ss value
     * @param ssStep A <code>double</code> representing the step value
     * @param print A <code>boolean</code> defining if the graph will be printed or not
     * @param method A <code>String</code> representing the method of estimative     
     * @return A <code>double</code> representing the best SS
     */
    public double optimizeForGauss(TheDataSet<Double,Double> dataset, TheData<Double,Double>data, double high, int k, double ssMin, double ssMax, double ssStep, boolean print, String method){
        
        double bestSS = ssMin;
        double bestCost = Double.MAX_VALUE;
        for(double i = ssMin; i <= ssMax; i += ssStep){
            double cost = this.gaussCost(dataset, data, high, k, i, method);
            if(print)
                this.probabilitygraph(dataset, data, high, k, i, method);
            if(cost < bestCost){
                bestCost = cost;
                bestSS = i;
            }//end if
        }//end for
        return bestSS;
        
    }//end optimizeGorGauss() method
    
    /**
     * This method give the probability of a item be in a specific range of value
     * @param dataset A <code>TheDataSet</code> representing dataset
     * @param data A <code>TheData</code> representing the data
     * @param low A <code>double</code> representing the low limit
     * @param high A <code>double</code> representing the high limit
     * @param k A <code>int</code> representing the number of neighbor
     * @param method A <code>String</code> representing the method of estimative
     * @return A <code>double</code> representing the final probability
     */
    public double probguess(TheDataSet<Double,Double>dataset, TheData<Double,Double>data, double low, double high, int k, String method){
        TheDataSet<TheData,Double>distDataset = this.getdistances(data, dataset);
        double nweight = 0.0;
        double tweight = 0.0;
        for(int i = 0; i < k; i++){
            /* get the distance and its inverse value */
            TheData<TheData,Double> distdata = distDataset.get(i);
            double dist = distdata.getData();
            double weight = this.inverseMethod(dist, 10.0, 0.0, "gaussian");
            /* get the price of the similar item */
            double price = 0.0;
            TheData<Double,Double> datas[] = distdata.getKeys(new TheData[distdata.keySize()]);
            for(int j = 0; j < datas.length; j++){
                if(!datas[j].equals(data))
                    price = datas[j].getData();            }//end for                        
            /* check if price is in the range [low..min] */
            if(price >= low && price <= high){
                nweight += weight;
            }//end if
            tweight += weight;
        }//end for
        if(tweight == 0.0)
            return 0.0;
        /* the probability is the weight in the range divided by all the weights */
        return nweight / tweight;
    }//end probguess() method
    
    /**
     * This method print the data in a cumulative graph
     * @param dataset A <code>TheDataSet</code> representing the dataset
     * @param data A <code>data</code> representing the data tio be analyzed
     * @param high A <code>double</code> representing the domain
     * @param k A <code>int</code> representing the k neighbors
     * @param method A <code>String</code> representing the estimative method
     */
    public void cumulativegraph(TheDataSet<Double,Double> dataset, TheData<Double,Double>data, double high, int k, String method){
        
        double series[][] = new double[2][101];
        DefaultXYDataset graphdataset = new DefaultXYDataset();
        /* create the series */
        for(int i = 0; i <= high; i++){
            series[0][i] = i;
            series[1][i] = this.probguess(dataset, data, 0, i, k, method);
        }//end for
        graphdataset.addSeries(high, series);
        JFreeChart chart = ChartFactory.createXYLineChart("Cumulative Graph", "Price(R$)", "Wines(%)", graphdataset, PlotOrientation.VERTICAL, false, true, false);
        ChartFrame frame = new ChartFrame("Cumulative Graph", chart);
        frame.pack();
        frame.setVisible(true);
        
    }//end cumulativegraph() method
    
    /**
     * This method print the data in a probability graph
     * @param dataset A <code>TheDataSet</code> representing the dataset
     * @param data A <code>data</code> representing the data tio be analyzed
     * @param high A <code>double</code> representing the domain
     * @param k A <code>int</code> representing the k neighbors
     * @param method A <code>String</code> representing the estimative method
     */
    public void probabilitygraph(TheDataSet<Double,Double> dataset, TheData<Double,Double>data, double high, int k, double ss, String method){
        
        double series[][] = new double[2][101];
        double smoothie[][] = new double[2][101];
        DefaultXYDataset graphdataset = new DefaultXYDataset();
        for(int i = 0; i <= high; i++){
            smoothie[0][i] = series[0][i] = i;
            series[1][i] = this.probguess(dataset, data, ((i - 1) < 0 ? 0:(i - 1)), i, k, method);
        }//end for
        
        for(int i = 0; i <= high; i++){
            double sv = 0.0;
            for(int j = 0; j <= high; j++){
                double dist = Math.abs(i - j) * 1;
                double weight = this.gaussian(dist, ss);
                sv += weight * series[1][j];
            }//end for
            smoothie[1][i] = sv;
        }//end for
        graphdataset.addSeries(high, smoothie);
        JFreeChart chart = ChartFactory.createXYLineChart("Probability Graph", "Price(R$)", "Wines(%)", graphdataset, PlotOrientation.VERTICAL, false, true, false);
        ChartFrame frame = new ChartFrame("Prabability Graph", chart);
        frame.pack();
        frame.setVisible(true);
        
        
    }//end probabilitygraph() method
    
}//End NumberPredict class
