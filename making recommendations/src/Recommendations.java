/*
 * Created on 25 September, 2008
 */

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

/**
 * @author giscardf
 * @version 1.0
 */
public class Recommendations {

    /* Create a manually dataset */
    private static HashMap<String,HashMap<String,Double>> critics = new HashMap<String,HashMap<String,Double>>();
    private static HashMap<String,HashMap<String,Double>> swapCritics = new HashMap<String,HashMap<String,Double>>();
    
    static{
        critics.put("Lisa Rose", new HashMap<String,Double>());
        critics.get("Lisa Rose").put("Lady in the Water", 2.5);
        critics.get("Lisa Rose").put("Snakes on a Plane", 3.5);
        critics.get("Lisa Rose").put("Just My Luck", 3.0);
        critics.get("Lisa Rose").put("Superman Returns", 3.5);
        critics.get("Lisa Rose").put("You, Me and Dupree", 2.5);
        critics.get("Lisa Rose").put("The Night Listener", 3.0);
        
        critics.put("Gene Seymour", new HashMap<String,Double>());
        critics.get("Gene Seymour").put("Lady in the Water", 3.0);
        critics.get("Gene Seymour").put("Snakes on a Plane", 3.5);
        critics.get("Gene Seymour").put("Just My Luck", 1.5);
        critics.get("Gene Seymour").put("Superman Returns", 5.0);
        critics.get("Gene Seymour").put("The Night Listener", 3.0);
        critics.get("Gene Seymour").put("You, Me and Dupree", 3.5);
        
        critics.put("Michael Phillips", new HashMap<String,Double>());
        critics.get("Michael Phillips").put("Lady in the Water", 2.5);
        critics.get("Michael Phillips").put("Snakes on a Plane", 3.0);
        critics.get("Michael Phillips").put("Superman Returns", 3.5);
        critics.get("Michael Phillips").put("The Night Listener", 4.0);        
        
        critics.put("Claudia Puig", new HashMap<String,Double>());
        critics.get("Claudia Puig").put("Snakes on a Plane", 3.5);
        critics.get("Claudia Puig").put("Just My Luck", 3.0);
        critics.get("Claudia Puig").put("The Night Listener", 4.5);
        critics.get("Claudia Puig").put("Superman Returns", 4.0);
        critics.get("Claudia Puig").put("You, Me and Dupree", 2.5);
        
        critics.put("Mick Lasalle", new HashMap<String,Double>());
        critics.get("Mick Lasalle").put("Lady in the Water", 3.0);
        critics.get("Mick Lasalle").put("Snakes on a Plane", 4.0);
        critics.get("Mick Lasalle").put("Just My Luck", 2.0);
        critics.get("Mick Lasalle").put("Superman Returns", 3.0);
        critics.get("Mick Lasalle").put("The Night Listener", 3.0);
        critics.get("Mick Lasalle").put("You, Me and Dupree", 2.0);
        
        critics.put("Jack Matthews", new HashMap<String,Double>());
        critics.get("Jack Matthews").put("Lady in the Water", 3.0);
        critics.get("Jack Matthews").put("Snakes on a Plane", 4.0);
        critics.get("Jack Matthews").put("The Night Listener", 3.0);
        critics.get("Jack Matthews").put("Superman Returns", 5.0);
        critics.get("Jack Matthews").put("You, Me and Dupree", 3.5);
        
        critics.put("Toby", new HashMap<String,Double>());
        critics.get("Toby").put("Snakes on a Plane", 4.5);
        critics.get("Toby").put("Superman Returns", 1.0);
        critics.get("Toby").put("You, Me and Dupree", 4.0);        
        
    }//end static context
    
    /**
     * This method swap the items from a dataset
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @return A <code>HashMap</code> representing the swapped items dataset
     */
    private HashMap<String,HashMap<String,Double>> swapDataset(HashMap<String,HashMap<String,Double>> score){
        
        HashMap<String,HashMap<String,Double>> swapScore = new HashMap<String,HashMap<String,Double>>();
        
        for(Iterator<String>personIterator = score.keySet().iterator(); personIterator.hasNext();){
            String person = personIterator.next();
            for(Iterator<String>itemIterator = score.get(person).keySet().iterator(); itemIterator.hasNext();){
                String item = itemIterator.next();
                if(swapScore.containsKey(item)){
                    if(swapScore.get(item).containsKey(person)){
                        continue;
                    }//end if
                    else{
                        swapScore.get(item).put(person, score.get(person).get(item));
                    }//end else
                }//end if
                else{
                    swapScore.put(item, new HashMap<String,Double>());
                    swapScore.get(item).put(person, score.get(person).get(item));
                }//end else               
            }//end for
        }//end for
        
        return swapScore;
        
    }//end swapDataset() method
    
    /**
     * 
     * @param scores A <code>HashMap</code> containg the person scores per item
     * @param methodName A <code>String</code> the method to use in order to find all closest person
     * @return A <code>HashMap</code> containing the item correlation
     */
    private HashMap<String,HashMap<String,Double>> buildItemCorrelation(HashMap<String,HashMap<String,Double>> swapScores, String methodName){
        
        /* Defining the variable */
        HashMap<String,HashMap<String,Double>> itemCorrelation = new HashMap<String,HashMap<String,Double>>();
        int counterItem = 0;
        int counterItem2Compare = 0;
        
        /* run over each item */
        for(Iterator<String> itemIterator = swapScores.keySet().iterator(); itemIterator.hasNext();){                
            String item = itemIterator.next();            
            counterItem++;
            /* run over each item to compare */
            for(Iterator<String> item2CompareIterator = swapScores.keySet().iterator(); item2CompareIterator.hasNext();){
                String item2Compare = item2CompareIterator.next();
                counterItem2Compare++;
                /* do not compare the same items */
                if(item.equals(item2Compare))
                    continue;
                
                double comparison = this.similarityMethod(swapScores, item, item2Compare, methodName);  //get the comparison result for both items
                
                /* store the comparison result */
                if(itemCorrelation.containsKey(item)){
                    itemCorrelation.get(item).put(item2Compare, comparison);
                }//end if
                else{
                    itemCorrelation.put(item, new HashMap<String,Double>());
                    itemCorrelation.get(item).put(item2Compare, comparison);
                }//end else
            }//end for            
        }//end for            
            
        return itemCorrelation;
        
    }//end buildItemCorrelation() method
    
    /**
     * This method parse a database from http://www.grouplens.org in order to build a dataset
     * @param filePath A <code>String</code> representing the database file path
     * @return A <code>HashMap</code> representing the dataset
     */
    public HashMap<String,HashMap<String,Double>> loadMovieDataset(String moviePath, String scorePath){
    
        /* Defining the variables */
        int MOVIE_MOVIE_ID = 0;                                                                         //index for get movie id from movie tokens
        int MOVIE_MOVIE_NAME = 1;                                                                       //index for get movie name from movie tokens
        int SCORE_USER_ID = 0;                                                                          //index for get user id from score tokens
        int SCORE_MOVIE_ID = 1;                                                                         //index for get movie id from score tokens
        int SCORE_USER_RATE = 2;                                                                        //index for get user rate from score tokens
        HashMap<Integer,String> movies = new HashMap<Integer,String>();                                 //keep the database for movie_id/movie_name
        HashMap<String,HashMap<String,Double>> dataset = new HashMap<String,HashMap<String,Double>>();  //keep the database data
        BufferedReader input = null;                                                                    //input stream to read u.data file        
        String[] tokens = null;                                                                         //tokens for each line of file 
        
        /*
         * First let's load all movies
         */
        try{
            input = new BufferedReader(new FileReader(new File(moviePath)));
            /* run over the file collecting data */
            String movieLine = input.readLine();
            while(movieLine != null){
                tokens = movieLine.split("\\|");                                                        //split line into tokens movie_id|movie_name
                movies.put(new Integer(tokens[MOVIE_MOVIE_ID]), tokens[MOVIE_MOVIE_NAME]);              //put movie into the collection
                movieLine = input.readLine();
            }//end while
        }//end try
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            return dataset;
        }//end catch
        catch(IOException ioe){
            ioe.printStackTrace();
            return dataset;
        }//end ioe
        
        /*
         * Second let's load all scores
         */
        try{
            input = new BufferedReader(new FileReader(new File(scorePath)));            
            /* run over the file collecting the data */
            String scoreLine = input.readLine();                                //read the first line
            while(scoreLine != null){
                tokens = scoreLine.split("\t");                                 //split line into tokens
                /* check if dataset contains user */
                if(dataset.containsKey("User " + tokens[SCORE_USER_ID])){
                    dataset.get("User " + tokens[SCORE_USER_ID]).put(movies.get(new Integer(tokens[SCORE_MOVIE_ID])), new Double(tokens[SCORE_USER_RATE]));
                }//end if
                else{
                    dataset.put("User " + tokens[SCORE_USER_ID], new HashMap<String,Double>());
                    dataset.get("User " + tokens[SCORE_USER_ID]).put(movies.get(new Integer(tokens[SCORE_MOVIE_ID])), new Double(tokens[SCORE_USER_RATE]));
                }//end else
                scoreLine = input.readLine();
            }//end while
        }//end try
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            return dataset;
        }//end catch
        catch(IOException ioe){
            ioe.printStackTrace();
            return dataset;
        }//end ioe
        
        return dataset;
        
    }//End loadMovieDataset() method
    
    /**
     * Give the Euclidian Distance between two given person
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @param person1 A <code>String</code> representing the first person to compare
     * @param person2 A <code>String</code> representing the second person to compare
     * @return A <code>double</code> representing how far is one person from another
     */
    public double euclidianDistance(HashMap<String,HashMap<String,Double>> scores, String person1, String person2){
        
        /* defining variables to compare both person */
        HashMap<String,Double> person1Item = scores.get(person1);               //all items from first person
        HashMap<String,Double> person2Item = scores.get(person2);               //all items from second person        
        double sumOfSquares = 0.0;                                              //sum of squares for all similar items
        int itemsInCommon = 0;
        
        /* run over person1 items to find similar items with person2 */        
        for(Iterator<String> itemIterator = person1Item.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            if(person2Item.containsKey(item)){                
                sumOfSquares += Math.pow(person1Item.get(item.toString()) - person2Item.get(item.toString()), 2);   //calculate the sum of squares for items rate
                itemsInCommon++;
            }//end for
        }//end for
        
        /* if there is no item in common, the distance is 0 */
        if(itemsInCommon == 0)
            return 0.0;
        
        /* force euclidian distance to fit a interval between 0..1 */
        return (1 / (1 + Math.sqrt(sumOfSquares)));
        
    }//end euclidianDistance() method
    
    /**
     * Give the Person Correlation between two given person
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @param person1 A <code>String</code> representing the first person to compare
     * @param person2 A <code>String</code> representing the second person to compare
     * @return A <code>double</code> representing how far is one person from another
     */
    public double personCorrelation(HashMap<String,HashMap<String,Double>> scores, String person1, String person2){
        
        /* defining variables to compare both person */
        HashMap<String,Double> person1Item = scores.get(person1);               //all items from first person
        HashMap<String,Double> person2Item = scores.get(person2);               //all items from second person        
        HashMap<String,Double> similar1Item = new HashMap<String,Double>();
        HashMap<String,Double> similar2Item = new HashMap<String,Double>();
        double sum1 = 0.0;                                                      //sum of rates for person1
        double sum2 = 0.0;                                                      //sun of rates for person2
        double sumOfSquares1 = 0.0;                                             //sum of squares of rates for person1
        double sumOfSquares2 = 0.0;                                             //sum of squares of rates for person2
        double sumOfProducts = 0.0;                                             //sum of products of rates for both person1 and person2        
        
        /* getting similar items */
        for(Iterator<String>itemIterator = person1Item.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            if(person2Item.containsKey(item)){
                similar1Item.put(item, person1Item.get(item));
                similar2Item.put(item, person2Item.get(item));
            }//end if
        }//end for
        
        /* check if there is similar items */
        if(similar1Item.size() == similar2Item.size() && (similar1Item.size() == 0))
            return 0.0;
        
        /* run over person1 items to find similar items with person2 */        
        for(Iterator<String> itemIterator = similar1Item.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            if(person2Item.containsKey(item)){                
                sum1 += person1Item.get(item);                                  //sum all person1 rate
                sumOfSquares1 += Math.pow(person1Item.get(item), 2);            //sum of sequare for all person1 rate
                sum2 += person2Item.get(item);                                  //sum all person2 rate                
                sumOfSquares2 += Math.pow(person2Item.get(item), 2);            //sum of sequare for all person2 rate
                sumOfProducts += person1Item.get(item) * person2Item.get(item); //sum of products for both person1 and person2                
            }//end for
        }//end for
        
        double numerator = sumOfProducts - (sum1 * sum2 / similar1Item.size());
        double denominator = Math.sqrt((sumOfSquares1 - Math.pow(sum1, 2) / similar1Item.size()) * (sumOfSquares2 - Math.pow(sum2, 2) / similar2Item.size()));
        
        /* check if denominator is different from 0 */
        if(denominator == 0)
            return 0.0;
        
        return (numerator / denominator);
        
    }//end personCorrelation() method
    
    /**
     * Give the Tanimoto Correlation between two given person
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @param person1 A <code>String</code> representing the first person to compare
     * @param person2 A <code>String</code> representing the second person to compare
     * @return A <code>double</code> representing how far is one person from another
     */
    public double tanimotoCorrelation(HashMap<String,HashMap<String,Double>> scores, String person1, String person2){
        
        /* defining variables to compare both person */
        HashMap<String,Double> person1Item = scores.get(person1);               //all items from first person
        HashMap<String,Double> person2Item = scores.get(person2);               //all items from second person        
        HashMap<String,Double> similar1Item = new HashMap<String,Double>();     //common items between two persons
        HashMap<String,Double> similar2Item = new HashMap<String,Double>();     //common items between two persons
        
        /* getting similar items */
        for(Iterator<String>itemIterator = person1Item.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            if(person2Item.containsKey(item)){
                similar1Item.put(item, person1Item.get(item));
                similar2Item.put(item, person2Item.get(item));
            }//end if
        }//end for
        
        /* check if there is similar items */
        if(similar1Item.size() == similar2Item.size() && (similar1Item.size() == 0))
            return 0.0;
        
        double numerator = similar1Item.size();
        double denominator = person1Item.size() + person2Item.size() - similar1Item.size();
        
        /* check if denominator is different from 0 */
        if(denominator == 0)
            return 0.0;
        
        return (numerator / denominator);
        
    }//end tanimotoCorrelation() method
    
    /**
     * This method invokes dinamically one type of similarity metnod based on methodName param
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @param person1 A <code>String</code> representing the first person to compare
     * @param person2 A <code>String</code> representing the second person to compare
     * @param methodName A <code>String</code> representing the method name
     * @return A <code>double</code> representing how far is one person from another
     */
    public double similarityMethod(HashMap<String,HashMap<String,Double>> scores, String person1, String person2, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {HashMap.class, String.class, String.class};       //An array for all attributes to invoke the method dynamically
        Method compareMethod = null;                                            //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);                  //get the method name using reflection
            Double comparation = (Double)compareMethod.invoke(this, scores, person1, person2);  //invoke the method to compare
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
    
    /**
     * This method give the closest person in taste for a specific person
     * @param scores A <code>HashMap</code> containg the scores rated by the person
     * @param person A <code>String</code> representing the person to find the closest ones
     * @param topMatches A <code>int</code> the N topest person to find
     * @param methodName A <code>String</code> the method to use in order to find all closest person
     * @return A <code>TreeMap</code> display the topest related person
     */
    public SortedMap<Double,String> topMatches(HashMap<String,HashMap<String,Double>> scores, String person, int topMatches, String methodName){
        
        /* defining the variable to store similar-taste persons */                
        TreeMap<Double,String> comparison = new TreeMap<Double,String>();       //A Map containing the comparison rate for each person        
        
        /* run over each person in the scores dataset */
        for(Iterator<String> personIterator = scores.keySet().iterator(); personIterator.hasNext();){
            String person2Compare = personIterator.next();                                              //get the person to compare
                
            /* Don't compare the same person */
            if(person2Compare.equals(person))
                continue;
                
            double comparation = similarityMethod(scores, person, person2Compare, methodName);          //invoke the method to compare
            
            /* check if comparison is  of 0, objects are not correlated */
            if(comparation == 0.0)
                continue;
            
            comparison.put(comparation, person2Compare);                                                //put the result into the comparison map
            
        }//end for
        
        SortedMap<Double,String> topest = comparison.descendingMap();                                   //reverse the comparison result        
        TreeMap<Double,String> nTopest = new TreeMap<Double,String>();                                  
        
        /* Cut the result by only getting the N topest matches */
        for(Iterator<Double>scoreIterator = topest.keySet().iterator(); scoreIterator.hasNext();){
            Double score = scoreIterator.next();
            if(topMatches <= 0){                
                break;
            }//end if
            nTopest.put(score, topest.get(score));
            topMatches--;
        }//end for
        
        return nTopest.descendingMap();
        
    }//end topMatches() method
    
    /**
     * This method return the wighted score between items
     * @param scores A <code>HashMap</code> containing the score given for each person to the item being weighted
     * @param rates A <code>HashMap</code> containing the similarity rate of each person with the person whom is receive the recommendation
     * @return A <code>double</code> wighted average for the item
     */
    public double weightedMean(HashMap<String,Double> scores, HashMap<String,Double> rates){
        
        double sumOfProducts = 0.0;     //will keep the value of the weighted score
        double sumOfRates = 0.0;        //will keep the sum of all rates
        
        /* run over all scores and rates */
        for(Iterator<String>iterator = scores.keySet().iterator(); iterator.hasNext();){
            String it = iterator.next();
            
            /* check if compared objects exist in both dataset */
            if(!(scores.containsKey(it) && rates.containsKey(it)))
                continue;
            
            sumOfProducts += scores.get(it) * rates.get(it);                    //calculate the wighted score
            sumOfRates += rates.get(it);                                        //calcula the sum of rates
        }//end for
        
        /* check if denominator is 0 */
        if(sumOfRates == 0.0)
            return 0.0;
        
        return (sumOfProducts / sumOfRates);                                    //return the weighted average
        
    }//end weightedMean() method
    
    /**
     * This method gives the best recommendation for a specific person based on person similarities
     * @param scores A <code>HashMap</code> containg the scores by person
     * @param sourcePerson A <code>String</code> representing the person to get recommendations   
     * @param methodName A <code>String</code> the method of correlation to use
     * @return A <code>SortedMap</code> displaying the best recommendations
     */
    public SortedMap<Double,String> getRecommendations(HashMap<String,HashMap<String,Double>> scores, String sourcePerson, String methodName){
        
        /* defining the variables */
        HashMap<String,HashMap<String,Double>> swapScores = this.swapDataset(scores);   //get the scores by item        
        HashMap<String,Double> similarity = new HashMap<String,Double>();               //keep all person similarity to main person
        TreeMap<Double,String> itemScores = new TreeMap<Double,String>();               //keep all person scores for a specific item
        
        /* run over each person to get similarity */
        for(Iterator<String>personIterator = scores.keySet().iterator(); personIterator.hasNext();){
            String person = personIterator.next();            
            /* do not get similarity for the same person */
            if(person.equals(sourcePerson))
                continue;                    
            /* put the similarity between the two person */            
            similarity.put(person, similarityMethod(scores, sourcePerson, person, methodName));            
        }//end for
        
        /* run over each source person item and get the recommendation */
        for(Iterator<String>itemIterator = swapScores.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            /* do not get recommendation for previous socred items */
            if(scores.get(sourcePerson).containsKey(item))
                continue;
            double weightScore = this.weightedMean(swapScores.get(item), similarity);   //get the weighted socre
            /* zero menas not correlated */
            if(weightScore == 0.0)
                continue;
            /* Do not override items, in such cases let's add a small factor to the item and insert into the result */
            while(itemScores.containsKey(weightScore))
                weightScore += 1.0 / 100;                                       //adding 0.0001 just to prevent key overriding
            itemScores.put(weightScore, item);
        }//end for
        
        return  itemScores.descendingMap();                                     //return the map in high-to-low recommendation order
        
    }//end getRecommendations() method
    
    /**
     * This method gives the best recommendation for a specific person based on item similarities
     * @param scores A <code>HashMap</code> containg the scores by person
     * @param sourcePerson A <code>String</code> representing the person to get recommendations   
     * @param methodName A <code>String</code> the method of correlation to use
     * @return A <code>SortedMap</code> displaying the best recommendations
     */
    public SortedMap<Double,String> getRecommendationsItem(HashMap<String,HashMap<String,Double>> scores, String sourcePerson, String methodName){
        
        /* defining the variables */
        HashMap<String,HashMap<String,Double>> swapScores = this.swapDataset(scores);                       //get the scores by item                
        HashMap<String,HashMap<String,Double>> similarity = new HashMap<String,HashMap<String,Double>>();   //keep all item similarity
        TreeMap<Double,String> itemScores = new TreeMap<Double,String>();                                   //keep all person scores for a specific item        
        
        /* run over each item to get item similarity */
        for(Iterator<String>itemIterator = swapScores.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next(); 
            for(Iterator<String>personalItemIterator = scores.get(sourcePerson).keySet().iterator(); personalItemIterator.hasNext();){                
                String personalItem = personalItemIterator.next(); 
                /* only compares not scored vs scored items */
                if(scores.get(sourcePerson).containsKey(item))
                    continue;
                /* put the similarity into the dataset */
                if(similarity.containsKey(item)){
                    similarity.get(item).put(personalItem, this.similarityMethod(swapScores, item, personalItem, methodName));
                }//end if
                else{
                    similarity.put(item, new HashMap<String,Double>());
                    similarity.get(item).put(personalItem, this.similarityMethod(swapScores, item, personalItem, methodName));
                }//end else
            }//end for
        }//end for
        
        /* run over each source person item and get the recommendation */
        for(Iterator<String>itemIterator = similarity.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            double weightScore = this.weightedMean(scores.get(sourcePerson), similarity.get(item)); //get the weighted socre
            /* zero menas not correlated */
            if(weightScore == 0.0)
                continue;
            /* Do not override items, in such cases let's add a small factor to the item and insert into the result */
            while(itemScores.containsKey(weightScore))
                weightScore += 1.0 / 10000;                                       //adding 0.0001 just to prevent key overriding
            itemScores.put(weightScore, item);
        }//end for
        
        return  itemScores.descendingMap();                                     //return the map in high-to-low recommendation order
        
    }//Ennd getRecommendationsItem() method
    
    /**
     * This method gives the best recommendation for a specific person based on item similarities using a pre-build correlation dataset
     * @param scores A <code>HashMap</code> containg the scores by person
     * @param similarity A <code>HashMap</code> containing the pre build similarity dataset between all items
     * @param sourcePerson A <code>String</code> representing the person to get recommendations   
     * @param methodName A <code>String</code> the method of correlation to use
     * @return A <code>SortedMap</code> displaying the best recommendations
     */
    public SortedMap<Double,String> getRecommendationsItem(HashMap<String,HashMap<String,Double>> scores, HashMap<String,HashMap<String,Double>> similarity, String sourcePerson, String methodName){
        
        /* defining the variables */
        HashMap<String,HashMap<String,Double>> swapScores = this.swapDataset(scores);                       //get the scores by item                        
        TreeMap<Double,String> itemScores = new TreeMap<Double,String>();                                   //keep all person scores for a specific item        
        
        /* run over each source person item and get the recommendation */
        for(Iterator<String>itemIterator = swapScores.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            /* do not compare previous scored item */            
            if(scores.get(sourcePerson).containsKey(item))
                continue;
            double weightScore = this.weightedMean(scores.get(sourcePerson), similarity.get(item)); //get the weighted socre            
            /* zero menas not correlated */
            if(weightScore == 0.0)
                continue;
            /* Do not override items, in such cases let's add a small factor to the item and insert into the result */
            while(itemScores.containsKey(weightScore))
                weightScore += 1.0 / 10000;                                       //adding 0.0001 just to prevent key overriding            
            itemScores.put(weightScore, item);            
        }//end for
        
        return  itemScores.descendingMap();                                     //return the map in high-to-low recommendation order
        
    }//Ennd getRecommendationsItem() method
    
    /**
     * Main method to execute the application
     * @param args
     */
    public static void main(String args[]){
                
        long startTime = 0;
        long endTime = 0;        
        String basePath = "D:/Data/Learning/Collective Intelligence/MakingRecommendations/src/";        
        Recommendations app = new Recommendations();                        
        HashMap<String,HashMap<String,Double>> dataset = app.loadMovieDataset(basePath + "u.item", basePath + "u.data");
        HashMap<String,HashMap<String,Double>> swapDataset = app.swapDataset(dataset);        
        HashMap<String,HashMap<String,Double>> similarityDataset = app.buildItemCorrelation(swapDataset, "personCorrelation");
        String[] methods = {"euclidianDistance", "personCorrelation", "tanimotoCorrelation"};        
        

        
        for(Iterator<String>person1Iterator = dataset.keySet().iterator(); person1Iterator.hasNext();){            
            String person1 = person1Iterator.next();
            System.out.println("xxx " + person1 + " xxx");
            
            startTime = System.currentTimeMillis();
            System.out.println("Recommendation: " + app.getRecommendations(dataset, person1, "personCorrelation"));
            endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) + "ms");
            
            startTime = System.currentTimeMillis();
            System.out.println("Recommendation: " + app.getRecommendationsItem(dataset, person1, "personCorrelation"));
            endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) + "ms");
            
            startTime = System.currentTimeMillis();
            System.out.println("Recommendation: " + app.getRecommendationsItem(dataset, similarityDataset, person1, "personCorrelation"));
            endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime) + "ms");
//            for(Iterator<String>person2Iterator = critics.keySet().iterator(); person2Iterator.hasNext();){
//                String person2 = person2Iterator.next();
//                System.out.println("\t=== " + person1 + " vs " + person2 + " ===");
//                System.out.println("\tEuclidian Score: " + app.similarityMethod(critics, person1, person2, "euclidianDistance"));
//                System.out.println("\tPerson    Score: " + app.similarityMethod(critics, person1, person2, "personCorrelation"));
//                System.out.println("\tTanimoto  Score: " + app.similarityMethod(critics, person1, person2, "tanimotoCorrelation"));                
//            }//end for            
        }//end for
            
        
    }//end main() method
    
}//End Recommendations class