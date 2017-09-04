/*
 * Created on 06 October, 2008
 */

import java.lang.reflect.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author giscardf
 * @version 1.0
 */
public class HierarchicalClustering {

    public static int BLOG_NAME = 0;                                                                    //blog name index into the data array
    
    /**
     * This method parse the blogdata.txt file and build a dataset for it
     * @param path A <code>String</code> representing the file to be parsed
     * @return A <code>HashMap</code> representing the dataset
     */
    private HashMap<String,HashMap<String,Double>> buildDataset(String path){
        
        /* Defining the variables */
        BufferedReader input = null;                                                                    //will read the blogdata.txt file
        String bufferLine = null;                                                                       //has the current line from file
        String[] words = null;                                                                          //has all words being computed
        String[] dataWords = null;                                                                      //has the number of words per blog
        HashMap<String,HashMap<String,Double>> dataset = new HashMap<String,HashMap<String,Double>>();  //contains all data
        
        try{
           /*
            * Create the I/O buffer and first read the file header
            */
            input = new BufferedReader(new FileReader(new File(path)));
            bufferLine = input.readLine();                                                              //read the first line
            /* check if first line is not null */
            if(bufferLine != null)
                words = bufferLine.split("\t");                                                         //get all words from the header
            
            /*
             * Keep reading the file data up untilk reach the end
             */
            bufferLine = input.readLine();
            while(bufferLine != null){
                dataWords = bufferLine.split("\t");                                                     //get the first row data
                dataset.put(dataWords[BLOG_NAME], new HashMap<String,Double>());                        //create the row of dataset
                /* run over each column to build the data set of the blog */
                for(int i = 1; i < dataWords.length; i++){
                    dataset.get(dataWords[BLOG_NAME]).put(words[i], Double.parseDouble(dataWords[i]));  //insert the data
                }//end for
                bufferLine = input.readLine();
            }//end while
        }//end try
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }//end catch
        catch(IOException ioe){
            ioe.printStackTrace();
        }//end catch
        
        return dataset;
        
    }//End buildDataset() method
    
    /**
     * This method gives the pearsonCorrelation between two cluster (0 means related, 1 means not related)
     * @param data1 A <code>HashMap</code> representing the data from first cluster
     * @param data2 A <code>HashMap</code> representing the data from second cluster
     * @return
     */
    public double pearsonCorrelation(HashMap<String,Double> data1, HashMap<String,Double> data2){
        
        /* defining variables to compare both person */        
        double sum1 = 0.0;                                                      //sum of rates for pearson1
        double sum2 = 0.0;                                                      //sun of rates for pearson2
        double sumOfSquares1 = 0.0;                                             //sum of squares of rates for pearson1
        double sumOfSquares2 = 0.0;                                             //sum of squares of rates for pearson2
        double sumOfProducts = 0.0;                                             //sum of products of rates for both pearson1 and pearson2        
        
        /* run over pearson1 items to find similar items with pearson2 */        
        for(Iterator<String> itemIterator = data1.keySet().iterator(); itemIterator.hasNext();){
            String word = itemIterator.next();
            if(data2.containsKey(word)){                
                sum1 += data1.get(word);                                  //sum all pearson1 rate
                sumOfSquares1 += Math.pow(data1.get(word), 2);            //sum of sequare for all pearson1 rate
                sum2 += data2.get(word);                                  //sum all pearson2 rate                
                sumOfSquares2 += Math.pow(data2.get(word), 2);            //sum of sequare for all pearson2 rate
                sumOfProducts += data1.get(word) * data2.get(word); //sum of products for both pearson1 and pearson2                
            }//end for
        }//end for
        
        double numerator = sumOfProducts - (sum1 * sum2 / data1.size());
        double denominator = Math.sqrt((sumOfSquares1 - Math.pow(sum1, 2) / data1.size()) * (sumOfSquares2 - Math.pow(sum2, 2) / data2.size()));
        
        /* check if denominator is different from 0 */
        if(denominator == 0)
            return 1.0;
        
        return (1.0 - (numerator / denominator));                               //0 means related, 1 means not related
        
    }//end pearsonCorrelation() method
    
    /**
     * This method gives the Tanimo Score for two group of data
     * @param data1 A <code>HashMap</code> containing the first group of data
     * @param data2 A <code>HashMap</code> containing the second group of data
     * @return A <code>double</code> defining the similarity between both groups (1 means none, 0 means similar)
     */
    public double tanimotoScore(HashMap<String,Double> data1, HashMap<String,Double> data2){
        
        int items1 = 0;                                                         //items on data 1
        int items2 = 0;                                                         //items on data 2
        double share = 0.0;                                                     //items on both data1 and data2
        
        /* run over all data */
        for(Iterator<String>itemIterator = data1.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            /* check if data1 has the item */
            if(data1.get(item) != 0)
                items1++;
            /* check if data2 has the item */
            if(data2.get(item) != 0)
                items2++;
            /* check if both data1 and data2 has the item */
            if(data1.get(item) != 0 && data2.get(item) != 0)
                share++;
        }//end for
        
        return 1.0 - (share / (items1 + items2 - share));                       //return the tanimoto score
        
    }//end tanimotoScore() method
    
    /**
     * Give the Euclidian Distance between two given items     
     * @param position1 A <code>HashMap</code> representing the first item to compare
     * @param position2 A <code>HashMap</code> representing the second item to compare
     * @return A <code>double</code> representing how far is one person from another
     */
    public double euclidianDistance(HashMap<String,Double> position1, HashMap<String,Double> position2){
        
        double sumOfSquare = 0.0;                                               //sum of square of the differences
        
        /* run over each item */
        for(Iterator<String>iterator = position1.keySet().iterator(); iterator.hasNext();){
            String item = iterator.next();
            sumOfSquare += Math.pow(position1.get(item) - position2.get(item), 2);
        }//end for
        
        return Math.sqrt(sumOfSquare);                                          //return the distance
        
    }//end euclidianDistance() method
    
    /**
     * Give the Manhattan Distance between two given items
     * @param position1 A <code>HashMap</code> representing the first item to compare
     * @param position2 A <code>HashMap</code> representing the second item to compare
     * @return A <code>double</code> representing how far is one person from another
     */
    public double manhattanDistance(HashMap<String,Double> position1, HashMap<String,Double> position2){
        
        double sumOfDistances = 0.0;
        
        /* run over all words and get the difference */
        for(Iterator<String>iterator = position1.keySet().iterator(); iterator.hasNext();){
            String item = iterator.next();            
            sumOfDistances += Math.abs(position1.get(item) - position2.get(item));
        }//end for
        
        return sumOfDistances;
        
    }//end manhattanDistance() method
    
    
    /**
     * This method invokes dinamically one type of similarity metnod based on methodName param
     * @param A <code>HashMap</code> containing the first group of data
     * @param A <code>HashMap</code> containgin the second group of data
     * @return A <code>double</code> representing how far is one person from another
     */
    public double similarityMethod(HashMap<String,Double> data1, HashMap<String,Double> data2, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {HashMap.class, HashMap.class};                    //An array for all attributes to invoke the method dynamically
        Method compareMethod = null;                                            //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);                  //get the method name using reflection
            Double comparation = (Double)compareMethod.invoke(this, data1, data2);              //invoke the method to compare
            return comparation;                                                                 //return the comparison result
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
    
    private HashMap<String,Double> dataAverage(HashMap<String,Double> data1, HashMap<String,Double> data2){
        
        /* defining the variables */
        HashMap<String,Double> average = new HashMap<String,Double>();          //store the average
        
        /* run over all elements and calculate the average */
        for(Iterator<String>iterator = data1.keySet().iterator(); iterator.hasNext();){
            String word = iterator.next();
            average.put(word, (data1.get(word) + data2.get(word) / 2.0));
        }//end for
        
        return average;
        
    }//end dataAverage() method
    
    /**
     * This method swap the items from a dataset
     * @param dataset A <code>HashMap</code> all blogs and its 
     * @return A <code>HashMap</code> representing the swapped items dataset
     */
    private HashMap<String,HashMap<String,Double>> swapDataset(HashMap<String,HashMap<String,Double>> dataset){
        
        HashMap<String,HashMap<String,Double>> swapScore = new HashMap<String,HashMap<String,Double>>();
        
        for(Iterator<String>personIterator = dataset.keySet().iterator(); personIterator.hasNext();){
            String person = personIterator.next();
            for(Iterator<String>itemIterator = dataset.get(person).keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                if(swapScore.containsKey(item)){
                    if(swapScore.get(item).containsKey(person)){
                        continue;
                    }//end if
                    else{
                        swapScore.get(item).put(person, dataset.get(person).get(item));
                    }//end else
                }//end if
                else{
                    swapScore.put(item, new HashMap<String,Double>());
                    swapScore.get(item).put(person, dataset.get(person).get(item));
                }//end else               
            }//end for
        }//end for
        
        return swapScore;
        
    }//end swapDataset() method
    
    /**
     * This method compare two group of k-cluster to see if they are the same
     * @param group1 A <code>HashMap</code> representing the first group to compare
     * @param group2 A <code>HashMap</code> representing the second group to compare
     * @return A <code>boolean</code> defining if both group are equals or not
     */
    private boolean compareKGroupCluster(HashMap<BiCluster,ArrayList<BiCluster>> group1, HashMap<BiCluster,ArrayList<BiCluster>> group2){
        
        /* compare both group list */
        for(Iterator<BiCluster>clusterIterator = group1.keySet().iterator(); clusterIterator.hasNext();){
            BiCluster kCluster = clusterIterator.next();                        //get the k-cluster
            ArrayList<BiCluster> clusterListOfGroup1 = group1.get(kCluster);    //get the list of cluster for group 1
            ArrayList<BiCluster> clusterListOfGroup2 = group2.get(kCluster);    //get the list of cluster for group 2
            
            /* compare list of clusters */
            if(clusterListOfGroup1.equals(clusterListOfGroup2) == false)
                return false;
            
        }//end for
        
        /* if we are here, that is because all the lists are the same, so let`s return true */
        return true;
        
    }//end compareKGroupCluster() method
    
    /**
     * This method creates a Hierarchical Cluster for a given dataset
     * @param dataset A <code>HashMap</code> containing the dataset to clustering
     * @param methodName A <code>String</code> defining the method name for correlation     
     * @return A <code>BiCluster</code> representing the root node of the cluster
     */
    public BiCluster hCluster(HashMap<String,HashMap<String,Double>> dataset, String methodName){
        
        /* defining the variables */
        int mergedClusterId = 1;                                                                                    //run all merged cluster
        BiCluster rootCluster = null;                                                                               //point to the root cluster
        BiCluster currentCluster = null;                                                                            //point to current cluster
        BiCluster closestCluster = null;                                                                            //point to the closest cluster
        double currentDistance = 100;                                                                               //point the current distance
        double closestDistance = Double.MAX_VALUE;                                                                  //point the closest distance
        ArrayList<BiCluster>clusters = new ArrayList<BiCluster>();                                                  //list of all clusters
        HashMap<String,HashMap<String,Double>> distanceCache = new HashMap<String,HashMap<String,Double>>();        //keep all distance caches
        
        /* populate the initial cluster set */
        for(Iterator<String>iterator = dataset.keySet().iterator(); iterator.hasNext();){
            String blog = iterator.next();
            clusters.add(new BiCluster(null, null, dataset.get(blog), blog, 0));                                    //create and add the cluster
        }//end for
        
        /* keep clustering up until one cluster remains */
        while(clusters.size() > 1){            
            currentCluster = clusters.get(0);                                                                       //always get the first cluster            
            /* run over all remaining clusters to compare */
            for(Iterator<BiCluster>iterator = clusters.iterator(); iterator.hasNext();){                
                BiCluster compareCluster = iterator.next();                                                         //get the cluster to compare                
                /* do not compare the same cluster */
                if(compareCluster.equals(currentCluster))
                        continue;
                /* At this point the cluster is different, let's check if the distance was calculated previously */
                if(distanceCache.get(currentCluster.getId()) == null){
                    distanceCache.put(currentCluster.getId(), new HashMap<String,Double>());
                    distanceCache.get(currentCluster.getId()).put(compareCluster.getId(), this.similarityMethod(currentCluster.getData(), compareCluster.getData(), methodName));
                }//end if
                else if(distanceCache.get(currentCluster.getId()).get(compareCluster.getId()) == null){
                    distanceCache.get(currentCluster.getId()).put(compareCluster.getId(), this.similarityMethod(currentCluster.getData(), compareCluster.getData(), methodName));
                }//end if
                /* At this point we have the distance and the cluster let's get the minimal distance */
                if(closestCluster == null){    
                    closestCluster = compareCluster;
                    closestDistance = distanceCache.get(currentCluster.getId()).get(closestCluster.getId());
                }//end if
                else{
                    closestDistance = distanceCache.get(currentCluster.getId()).get(closestCluster.getId());    //get the closest distance at the moment
                    currentDistance = distanceCache.get(currentCluster.getId()).get(compareCluster.getId());    //get the current distance between the clusters
                    /* if the current distance is less than the closest one */
                    if(currentDistance < closestDistance){
                        closestCluster = compareCluster;                                                        //update the closest cluster
                        closestDistance = currentDistance;
                    }//end if
                }//end else                
            }//end for
            
            /* now that we have finished the cluster comparison, let's merge the closest one */
            BiCluster mergeCluster = new BiCluster(currentCluster, closestCluster, this.dataAverage(currentCluster.getData(), closestCluster.getData()), "" + mergedClusterId++, closestDistance);
            clusters.remove(currentCluster);                                    //merge the compared cluster
            clusters.remove(closestCluster);                                    //merge the compared cluster
            clusters.add(mergeCluster);                                         //merge the compared cluster
            rootCluster = mergeCluster;                                         //point to the last merge
            closestCluster = null;                                              //reset it for next iteration
            closestDistance = Double.MAX_VALUE;                                 //reset it for next iteration            
            
        }//end while
        
        return rootCluster;
        
    }//end hCluster() method
    
    /**
     * This method creates a Hierarchical Cluster for a given dataset
     * @param dataset A <code>HashMap</code> containing the dataset to clustering
     * @param k A <code>int</code> defininf how many k-cluster will be created
     * @param methodName A <code>String</code> defining the method name for correlation     
     * @return A <code>BiCluster</code> representing the root node of the cluster
     */
    public HashMap<BiCluster,ArrayList<BiCluster>> kCluster(HashMap<String,HashMap<String,Double>> dataset, int k, String methodName){
        
        /* defining the variables */
        int clusterId = 1;                                                                                      //index to put blog data into matrix
        HashMap<String,HashMap<String,Double>> swapDataset = this.swapDataset(dataset);                         //get the dataset swapped
        HashMap<String,Double> minRanges = new HashMap<String,Double>();                                        //keep min amount of words for all blogs
        HashMap<String,Double> maxRanges = new HashMap<String,Double>();                                        //keep max amount of words for all blogs
        ArrayList<BiCluster>clusters = new ArrayList<BiCluster>();                                              //list of all clusters
        ArrayList<BiCluster>kClusters = new ArrayList<BiCluster>();                                             //list of all k-clusters
        BiCluster closestCluster = null;                                                                        //best matches for a cluster
        double closestDistance = Double.MAX_VALUE;                                                              //best distance for a cluster
        HashMap<BiCluster,ArrayList<BiCluster>> kGroupCluster = new HashMap<BiCluster,ArrayList<BiCluster>>();  //keep the clusters grouped by the k-cluster       
        HashMap<BiCluster,ArrayList<BiCluster>> lastKGroup = new HashMap<BiCluster, ArrayList<BiCluster>>();    //keep trace of the last group
        
        /* populate the initial cluster set */
        for(Iterator<String>iterator = dataset.keySet().iterator(); iterator.hasNext();){
            String blog = iterator.next();
            clusters.add(new BiCluster(null, null, dataset.get(blog), blog, 0));                                //create and add the cluster
        }//end for
        
        /* 
         * get the minimum and maximum amount of words for all blogs
         */
        
        /* run over each word */
        for(Iterator<String>wordIterator = swapDataset.keySet().iterator(); wordIterator.hasNext();){
            String word = wordIterator.next();
            /* run over each blog */
            for(Iterator<String>blogIterator = swapDataset.get(word).keySet().iterator(); blogIterator.hasNext();){
                String blog = blogIterator.next();
                
                /* get min value */
                if(minRanges.containsKey(word) == false)
                    minRanges.put(word, swapDataset.get(word).get(blog));
                else if(swapDataset.get(word).get(blog) < minRanges.get(word))
                    minRanges.put(word, swapDataset.get(word).get(blog));
                
                /* get max values */
                if(maxRanges.containsKey(word) == false)
                    maxRanges.put(word, swapDataset.get(word).get(blog));
                else if(swapDataset.get(word).get(blog) > maxRanges.get(word))
                    maxRanges.put(word, swapDataset.get(word).get(blog));
                
            }//end for
        }//end for
        
        /*
         * generate the random clusters
         */
        
        /* run over the amounf of k clusters */
        for(int i = 0; i < k; i++){
            HashMap<String,Double> randomData = new HashMap<String,Double>();
            /* for each word generate a random value to be used in the k-cluster data */
            for(Iterator<String>wordIterator = minRanges.keySet().iterator(); wordIterator.hasNext();){
                String word = wordIterator.next();
                randomData.put(word, (Math.random() * (maxRanges.get(word) - minRanges.get(word)) + minRanges.get(word)));  //generate a random value
            }//end for
            BiCluster kCluster = new BiCluster(null, null, randomData, "" + clusterId++, 0);                                //create the new k-cluster
            kClusters.add(kCluster);                                                                                        //add k-cluster to the list
        }//end for
        
        /*
         * Find the cluster group for each k-cluster
         */
        
        /* keep running up until the k-cluster stop to move */        
        while(true){
            /* for each cluster */
            for(int i = 0; i < clusters.size(); i++){            
                BiCluster cluster = clusters.get(i);                                                            //get the cluster
                /* find each k-cluster */
                for(int j = 0; j < kClusters.size(); j++){
                    BiCluster kCluster = kClusters.get(j);
                    double distance = this.similarityMethod(cluster.getData(), kCluster.getData(), methodName); //get the distance between the clusters
                    /* check if the distance is lower than previously found */
                    if(distance < closestDistance){
                        closestCluster = kCluster;                                                              //keep updated the closest one
                        closestDistance = distance;
                    }//end if
                }//end for
                
                /* put the cluster into the k-cluster group */
                if(kGroupCluster.containsKey(closestCluster) == false){
                    kGroupCluster.put(closestCluster, new ArrayList<BiCluster>());
                    cluster.setDistance(closestDistance);
                    kGroupCluster.get(closestCluster).add(cluster);
                }//end if                    
                else{
                    cluster.setDistance(closestDistance);
                    kGroupCluster.get(closestCluster).add(cluster);
                }//end else
                
                closestDistance = Double.MAX_VALUE;                             //reset distance for next iteration
                closestCluster = null;                                          //reset custer for next iteration
                
            }//end for
            
            /* if the last and the current gruop list is the same, there is nothing else to compute */
            if(compareKGroupCluster(lastKGroup, kGroupCluster) == true)
                break;
            lastKGroup = kGroupCluster;                                         //update the last list with the current one
        }//end while
        
        /* repositioning all k-Cluster by calculating data average */
        for(int i = 0; i < kClusters.size(); i++){
            BiCluster kCluster = kClusters.get(i);
            HashMap<String,Double> average = new HashMap<String,Double>();
            ArrayList<BiCluster> clusterList = kGroupCluster.get(kCluster);
            
            /* do not calculate average for empty list */
            if(clusterList == null)
                continue;
            
            /* get the sun of words for all clusters */
            for(int j = 0; j < clusterList.size(); j++){
                BiCluster cluster = clusterList.get(j);
                for(Iterator<String>wordIterator = cluster.getData().keySet().iterator(); wordIterator.hasNext();){
                    String word = wordIterator.next();
                    if(average.containsKey(word) == false)
                        average.put(word, cluster.getData().get(word));
                    else
                        average.put(word, cluster.getData().get(word) + average.get(word));
                }//end for
            }//end for
            
            /* get the average for all cluster */
            for(Iterator<String>wordIterator = average.keySet().iterator(); wordIterator.hasNext();){
                String word = wordIterator.next();
                average.put(word, average.get(word) / clusterList.size());
            }//end for
            
            kCluster.setData(average);                                          //update the kCluster data
            
        }//end for
        
        return kGroupCluster;
        
    }//end kCluster() method
    
    
    /**
     * This method get a dataset and renderer it as a one-dimensional data showing the difference between the items
     * @param dataset A <code>HashMap</code> containing the data
     * @param rate A <code>double</code> defininf the rate to move data in pixels
     * @param methodName A <copde>String</code> defining the method to use in order to calculate the distance between items
     * @return A <code>HashMap</code> containg each item and its position in a two-dimensional plane
     */
    public HashMap<String,HashMap<String,Double>> scaleDownOneDimension(HashMap<String,HashMap<String,Double>> dataset, double rate, String methodName){
        
        /* Defining the variables */
        HashMap<String,HashMap<String,Double>> realDistance = new HashMap<String,HashMap<String,Double>>(); //real distance between two items
        HashMap<String,HashMap<String,Double>> fakeDistance = new HashMap<String,HashMap<String,Double>>(); //current distance between two items
        HashMap<String,HashMap<String,Double>> location = new HashMap<String,HashMap<String,Double>>();     //item location in the one-dimensional plane
        HashMap<String,HashMap<String,Double>> gradient = new HashMap<String,HashMap<String,Double>>();     //gradient between each item
        double lastError = Double.MAX_VALUE;                                                                //error of last iteration
        double totalError = 0.0;                                                                            //error of current iteration
        
        /*
         * First, let's calculate the distance between each item
         */
        for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                String compare = compareIterator.next();
                /* do not compare the same item */
                if(item.equals(compare))
                    continue;
                double distance = this.similarityMethod(dataset.get(item), dataset.get(compare), methodName);   //get the distance betweent the items
                /* store the distance into the array */
                if(realDistance.containsKey(item) == false){
                    realDistance.put(item, new HashMap<String,Double>());
                    realDistance.get(item).put(compare, distance);                    
                }//end if
                else{
                    realDistance.get(item).put(compare, distance);                    
                }//end else
            }//end for
        }//end for
        
        /*
         * Now, let's put each item in a random location
         */
        for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            location.put(item, new HashMap<String,Double>());                   //create the location for the item
            location.get(item).put("XPos", Math.random() * dataset.size());     //find a random x position            
        }//end for
        
        /*
         * Now, let's keep runing up until find the final position
         */
        while(true){
            
            /* let's calculate the current distance between each item */
            for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                    String compare = compareIterator.next();
                    /* do not compare the same item */
                    if(item.equals(compare))
                        continue;
                    /* store the current distance into the array */
                    if(fakeDistance.containsKey(item) == false){
                        fakeDistance.put(item, new HashMap<String,Double>());
                        fakeDistance.get(item).put(compare, this.manhattanDistance(location.get(item), location.get(compare)));                        
                    }//end if
                    else{
                        fakeDistance.get(item).put(compare, this.manhattanDistance(location.get(item), location.get(compare)));                        
                    }//end else
                }//end for
            }//end for
            
            /* let's calculate the gradient between the real and current distance */                        
            gradient.clear();
            totalError = 0;
            for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                    String compare = compareIterator.next();
                    /* do not compare the same item */
                    if(item.equals(compare))
                        continue;
                    /* do not compare already compared items */
                    if(gradient.get(compare) != null && gradient.get(compare).get(item) != null)
                        continue;
                    
                    double current = fakeDistance.get(item).get(compare);
                    double real = realDistance.get(item).get(compare);
                    double error = (current - real) / real;
                    totalError += Math.abs(error);
                    
                    if(gradient.containsKey(item) == false){
                        gradient.put(item, new HashMap<String,Double>());
                        gradient.get(item).put("XPos", ((location.get(item).get("XPos") - location.get(compare).get("XPos")) / fakeDistance.get(item).get(compare)) * error);                        
                    }//end if
                    else{
                        gradient.get(item).put("XPos", gradient.get(item).get("XPos") + ((location.get(item).get("XPos") - location.get(compare).get("XPos")) / fakeDistance.get(item).get(compare)) * error);                        
                    }//end else
                    
                }//end for
            }//end for
            System.out.println("=========== totalError: " + totalError);
            
            if(lastError < totalError)break;
            lastError = totalError;
            
            /* move the points based on its current location and the gradient */
            for(Iterator<String>itemIterator = location.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();                
                location.get(item).put("XPos", location.get(item).get("XPos") - rate * gradient.get(item).get("XPos"));                
            }//end for
            
        }//end while
        
        return location;
        
    }//end scaleDownOneDimension() method
    
    /**
     * This method get a dataset and renderer it as a two-dimensional data showing the difference between the items
     * @param dataset A <code>HashMap</code> containing the data
     * @param rate A <code>double</code> defininf the rate to move data in pixels
     * @param methodName A <copde>String</code> defining the method to use in order to calculate the distance between items
     * @return A <code>HashMap</code> containg each item and its position in a two-dimensional plane
     */
    public HashMap<String,HashMap<String,Double>> scaleDownTwoDimension(HashMap<String,HashMap<String,Double>> dataset, double rate, String methodName){
        
        /* Defining the variables */
        HashMap<String,HashMap<String,Double>> realDistance = new HashMap<String,HashMap<String,Double>>(); //real distance between two items
        HashMap<String,HashMap<String,Double>> fakeDistance = new HashMap<String,HashMap<String,Double>>(); //current distance between two items
        HashMap<String,HashMap<String,Double>> location = new HashMap<String,HashMap<String,Double>>();     //item location in the two-dimensional plane
        HashMap<String,HashMap<String,Double>> gradient = new HashMap<String,HashMap<String,Double>>();     //gradient between each item
        double lastError = Double.MAX_VALUE;                                                                //error of last iteration
        double totalError = 0.0;                                                                            //error of current iteration
        
        /*
         * First, let's calculate the distance between each item
         */
        for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                String compare = compareIterator.next();
                /* do not compare the same item */
                if(item.equals(compare))
                    continue;
                double distance = this.similarityMethod(dataset.get(item), dataset.get(compare), methodName);   //get the distance betweent the items
                /* store the distance into the array */
                if(realDistance.containsKey(item) == false){
                    realDistance.put(item, new HashMap<String,Double>());
                    realDistance.get(item).put(compare, distance);                    
                }//end if
                else{
                    realDistance.get(item).put(compare, distance);                    
                }//end else
            }//end for
        }//end for
        
        /*
         * Now, let's put each item in a random location
         */
        for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            location.put(item, new HashMap<String,Double>());                   //create the location for the item
            location.get(item).put("XPos", Math.random() * dataset.size());     //find a random x position
            location.get(item).put("YPos", Math.random() * dataset.size());     //find a random y position
        }//end for
        
        /*
         * Now, let's keep runing up until find the final position
         */
        while(true){
            
            /* let's calculate the current distance between each item */
            for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                    String compare = compareIterator.next();
                    /* do not compare the same item */
                    if(item.equals(compare))
                        continue;
                    /* store the current distance into the array */
                    if(fakeDistance.containsKey(item) == false){
                        fakeDistance.put(item, new HashMap<String,Double>());
                        fakeDistance.get(item).put(compare, this.euclidianDistance(location.get(item), location.get(compare)));                        
                    }//end if
                    else{
                        fakeDistance.get(item).put(compare, this.euclidianDistance(location.get(item), location.get(compare)));                        
                    }//end else
                }//end for
            }//end for
            
            /* let's calculate the gradient between the real and current distance */                        
            gradient.clear();
            totalError = 0;
            for(Iterator<String>itemIterator = dataset.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                for(Iterator<String>compareIterator = dataset.keySet().iterator(); compareIterator.hasNext();){
                    String compare = compareIterator.next();
                    /* do not compare the same item */
                    if(item.equals(compare))
                        continue;
                    /* do not compare already compared items */
                    if(gradient.get(compare) != null && gradient.get(compare).get(item) != null)
                        continue;
                    
                    double current = fakeDistance.get(item).get(compare);
                    double real = realDistance.get(item).get(compare);
                    double error = (current - real) / real;
                    totalError += Math.abs(error);
                    
                    if(gradient.containsKey(item) == false){
                        gradient.put(item, new HashMap<String,Double>());
                        gradient.get(item).put("XPos", ((location.get(item).get("XPos") - location.get(compare).get("XPos")) / fakeDistance.get(item).get(compare)) * error);
                        gradient.get(item).put("YPos", ((location.get(item).get("YPos") - location.get(compare).get("YPos")) / fakeDistance.get(item).get(compare)) * error);
                    }//end if
                    else{
                        gradient.get(item).put("XPos", gradient.get(item).get("XPos") + ((location.get(item).get("XPos") - location.get(compare).get("XPos")) / fakeDistance.get(item).get(compare)) * error);
                        gradient.get(item).put("YPos", gradient.get(item).get("YPos") + ((location.get(item).get("YPos") - location.get(compare).get("YPos")) / fakeDistance.get(item).get(compare)) * error);
                    }//end else
                    
                }//end for
            }//end for
            System.out.println("=========== totalError: " + totalError);
            
            if(lastError < totalError)break;
            lastError = totalError;
            
            /* move the points based on its current location and the gradient */
            for(Iterator<String>itemIterator = location.keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();                
                location.get(item).put("XPos", location.get(item).get("XPos") - rate * gradient.get(item).get("XPos"));
                location.get(item).put("YPos", location.get(item).get("YPos") - rate * gradient.get(item).get("YPos"));
            }//end for
            
        }//end while
        
        return location;
        
    }//End scaleDownTwpDimension() method
    
    public static void main(String args[]){
        Recommendations recom = new Recommendations();
        HierarchicalClustering app = new HierarchicalClustering();        
        
        HashMap<String,HashMap<String,Double>> datasetBlog = app.buildDataset(System.getProperty("user.dir") + "/src/blogdata.txt");
        HashMap<String,HashMap<String,Double>> swapDataset = app.swapDataset(datasetBlog);
        HashMap<String,HashMap<String,Double>> datasetZebo = app.buildDataset(System.getProperty("user.dir") + "/src/zebo.txt");
        HashMap<String,HashMap<String,Double>> datasetMovie = recom.loadMovieDataset(System.getProperty("user.dir") + "/src/u.item", System.getProperty("user.dir") + "/src/u.data");
        
//        /* find common blogs */
//        BiCluster blogRoot = app.hCluster(datasetBlog, "pearsonCorrelation");        
//        ClusterDisplay datasetWindow = new ClusterDisplay(blogRoot);
//        datasetWindow.setVisible(true);
//        datasetWindow.setSize(800, 600);        
//        
//        /* find common words */
//        BiCluster swapRoot = app.hCluster(swapDataset, "pearsonCorrelation");
//        ClusterDisplay swapDatasetWindow = new ClusterDisplay(swapRoot);
//        swapDatasetWindow.setVisible(true);
//        swapDatasetWindow.setSize(800, 600);
//        
//        /* find common blogs - k-cluster */
//        for(int k = 1; k < 50; k++){
//            HashMap<BiCluster,ArrayList<BiCluster>> kGroupCluster = app.kCluster(datasetBlog, k, "pearsonCorrelation");
//            for(Iterator<BiCluster>clusterIterator = kGroupCluster.keySet().iterator(); clusterIterator.hasNext();){
//                BiCluster kCluster = clusterIterator.next();
//                ArrayList<BiCluster> clusters = kGroupCluster.get(kCluster);
//                //System.out.println("===== Group K-" + kCluster.getId() + " =====");
//                for(int i = 0; i < clusters.size(); i++)
//                    if(clusters.get(i).getId().startsWith("Wired"))
//                        System.out.println("\t" + clusters.get(i).getId() + ": " + clusters.get(i).getDistance());
//            }//end for
//            System.out.println("============ K = " + k + " ===============");
//        }//end for
//        
//        /* find common desires */
//        BiCluster zeboRoot = app.hCluster(datasetZebo, "tanimotoScore");
//        ClusterDisplay zeboDisplay = new ClusterDisplay(zeboRoot);
//        zeboDisplay.setVisible(true);
//        zeboDisplay.setSize(800, 600);
//        
//        /* putting blogs in a two-dimensional place */
//        HashMap<String,HashMap<String,Double>> location = app.scaleDownTwoDimension(datasetBlog, 0.01, "pearsonCorrelation");
//        TwoDimensionalDisplay locationDisplay = new TwoDimensionalDisplay(location);
//        locationDisplay.setVisible(true);
//        locationDisplay.setSize(800, 600);
//        
//        /* find common users (for movies) */
//        HashMap<BiCluster,ArrayList<BiCluster>> kGroupCluster = app.kCluster(datasetMovie, 5, "pearsonCorrelation");
//        for(Iterator<BiCluster>clusterIterator = kGroupCluster.keySet().iterator(); clusterIterator.hasNext();){
//            BiCluster kCluster = clusterIterator.next();
//            ArrayList<BiCluster> clusters = kGroupCluster.get(kCluster);
//            System.out.println("===== Group K-" + kCluster.getId() + " =====");
//            for(int i = 0; i < clusters.size(); i++)
//                System.out.println("\t" + clusters.get(i).getId());
//        }//end for
//        
//        /* find common blogs using pythagorean distance */
//        BiCluster blogRoot = app.hCluster(datasetBlog, "euclidianDistance");        
//        ClusterDisplay datasetWindowForPythagorean = new ClusterDisplay(blogRoot);
//        datasetWindowForPythagorean.setVisible(true);
//        datasetWindowForPythagorean.setSize(800, 600);  
//        
//        /* find common blogs using pythagorean distance */
//        BiCluster zeboRoot = app.hCluster(datasetZebo, "manhattanDistance");        
//        ClusterDisplay datasetWindowForManhattan = new ClusterDisplay(zeboRoot);
//        datasetWindowForManhattan.setVisible(true);
//        datasetWindowForManhattan.setSize(800, 600);  
        
        /* putting blogs in a two-dimensional place */
        HashMap<String,HashMap<String,Double>> location = app.scaleDownOneDimension(datasetBlog, 0.01, "pearsonCorrelation");
        OneDimensionDisplay locationDisplayOneDimension = new OneDimensionDisplay(location);
        locationDisplayOneDimension.setVisible(true);
        locationDisplayOneDimension.setSize(800, 600);

        
    }//end main() method
    
}//End HierarchicalClustering class

