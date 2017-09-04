/*
 * Created on 17 November, 2008
 */

import java.util.*;
import java.lang.reflect.*;

/**
 *
 * @author giscardf
 */
public class Classifier {

    protected TheDictionary<String,Integer> fc;                                 //dictionary with feature counted
    protected HashMap<String,Integer> cc;                                       //dictionary with category counter
    protected HashMap<String,Integer>threshold;                                 //dictionary with category threshold
    protected String featureMethod;                                             //function name to get the features
    protected String fileName;                                                  //file path name
    
    /**
     * This method creates a new instance
     * @param featureMethod A <code>String</code> representing the string name of method to get the features
     * @param fileName A <code>String</code> representing the file name to do something
     */
    public Classifier(String featureMethod, String fileName){
        this.featureMethod = featureMethod;
        this.fileName = fileName;
        this.fc = new TheDictionary<String,Integer>();
        this.cc = new HashMap<String,Integer>();
        this.threshold = new HashMap<String,Integer>();
    }//End Classifier() clonstructor
    
    /**
     * This method invokes dinamically a specific feature method
     * @param doc A <code>String</code> representing the document to get the features
     * @param methodName A <code>String</code> representing the feature method
     * @return A <code>String[]</code> representing all the features
     */
    protected String[] featureMethod(String doc, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {String.class};                                    //A String
        Method compareMethod = null;                                            //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);  //get the method name using reflection
            String[] features = (String[])compareMethod.invoke(this, doc);      //invoke the method to get features
            return features;                                                    //return the features
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
        
        return null;
        
    }//end featureMethod() method
    
    /**
     * This method parse a document by getting all words from it
     * @param doc A <code>String</code> representing the document
     * @return A <code>String[]</code> representing all the words not repeated
     */
    public String[] getFeatures(String doc){
        
        ArrayList<String>uniqueWords = new ArrayList<String>();                 //contains all words not repeated
        String[] words = doc.split("\\W+");                                      //split the document into words
        
        /* for each word in the document */
        for(String word : words){
            /* thw word must have at least 2 letters and no more than 20 */
            if(word.length() <= 2 || word.length() >= 20)
                continue;                                                       //skip this word
            /* check if word was already saved */
            if(uniqueWords.contains(word.toLowerCase()) == false)
                uniqueWords.add(word.toLowerCase());                            //save the word
        }//end for
        
        return uniqueWords.toArray(new String[0]);                              //return a array with not repeated words
        
    }//end getFeatures() method
    
    /**
     * This method increment the amount of features in a category
     * @param feature A <code>String</code> representing the feature
     * @param category A <code>String</code> representing the category
     */
    public void incrementFeature(String feature, String category){
        
        /* if is the first time */
        if(fc.containsKey(feature, category) == false)
            fc.put(1, feature, category);                                       //set value to one
        /* otherwise */
        else
            fc.put(fc.get(feature, category) + 1, feature, category);           //increment the value
        
    }//end incrementFeature() method
    
    /**
     * This method increment the amount of category found
     * @param category A <code>String</code> representing the category
     */
    public void incrementCategory(String category){
        
        /* if is the first time */
        if(cc.containsKey(category) == false)
            cc.put(category, 1);                                                //set the value to one
        /* otherwise */
        else
            cc.put(category, cc.get(category) + 1);                             //increment the value
        
    }//end incrementCategory() method
    
    /**
     * This method return the frequency of a feature into a category
     * @param feature A <code>String</code> representing the feature
     * @param category A <code>String</code> representing the category
     * @return A <code>int</code> representing the frequency
     */
    public int getFeature(String feature, String category){
        
        /* if there is no feature into the category */
        if(fc.containsKey(feature, category) == false)
            return 0;                                                           //return 0
        /* otherwise */
        else
            return fc.get(feature, category);                                   //return the frequency
        
    }//end getFeature() method
    
    /**
     * This method return the frequency a category was found
     * @param category A <code>String</code> representing the category
     * @return A <code>int</code> representing the frequency
     */
    public int getCategory(String category){
        
        /* if there is no category */
        if(cc.containsKey(category) == false)
            return 0;                                                           //return 0
        /* otherwise */
        else
            return cc.get(category);                                            //return the frequency
        
    }//end getCategory() method
    
    /**
     * This method return the total amount of categories
     * @return A <code>int</code> representing the total type of categories
     */
    public int totalCategories(){
        
        int total = 0;
        
        /* get total amount of documents */
        for(Iterator<Integer>iterator = cc.values().iterator(); iterator.hasNext();){
            total += iterator.next();   
        }//end for
        
        return total;
        
    }//end totalCategories() method
    
    /**
     * This method return all the values of a category dictionary
     * @return A <code>String[]</code> representing all categories
     */
    public String[] getCategories(){
        return cc.keySet().toArray(new String[0]);
    }//end getCategories() method
    
    /**
     * This method set a threshold for a category
     * @param category A <code>String</code> representing the category
     * @param threshold A <code>int</code> representing the threshold
     */
    public void setThreshold(String category, int threshold){
        this.threshold.put(category, threshold);
    }//end setThreshold() method
    
    /**
     * This method retrieve the threshold of a specific category
     * @param category A <code>String</code> representing the category
     * @return A <code>int</code> representing the threshold
     */
    public int getThreshold(String category){
        return (this.threshold.containsKey(category) == true  ? this.threshold.get(category) : 1);
    }//end getThreshold() method
    
    /**
     * This method train a given classifier
     * @param doc A <code>String</code> representing the document to classify
     * @param category A <code>String</code> representing the category of the document     
     */
    public void train(String doc, String category){
        String[] features = this.featureMethod(doc, this.featureMethod);
        /* increment counter for every feature with this category */
        for(int i = 0; i < features.length; i++){
            this.incrementFeature(features[i], category);
        }//end for
        /* increment counter for this category */
        this.incrementCategory(category);
    }//end train() method
    
    /**
     * This method just put some sample data into the spam training
     */
    public void sampleTrain(){
        
        this.train("Nobody owns the water", "good");
        this.train("the quick rabbit jumps fence", "good");
        this.train("buy pharmaceuticals now", "bad");
        this.train("make quick money at the online casino", "bad");
        this.train("the quick brown fox jumpss", "good");        
        
    }//end sampleTrain() method
    
    /**
     * This method caculate the probability of a feature being part of one category
     * @param feature A <code>String</code> representing the feature
     * @param category A <code>String</code> representing the category
     * @return A <code>double</code> representing the probability
     */
    public double featureProbability(String feature, String category){
        
        /* if there is no item with this category */
        if(this.getCategory(category) == 0)
            return 0;                                                           //return 0
        
        return ((double)this.getFeature(feature, category) / this.getCategory(category));
        
    }//end featureProbability() method
    
    /**
     * This method calculate the prability of a feature being part of one category
     * @param feature A <code>String</code> representing the feature
     * @param category A <code>String</code> representing the category
     * @param weight A <code>double</code> representing the weight for start probability
     * @param start A <code>double</code> representing the initial value for each feature
     * @return A <code>double</code> representing the probability
     */
    public double weightedProbability(String feature, String category, double weight, double start){
        
        /* defining some variables */
        double prob = 0.0;                                                      //keep the basic probablility
        double wprob = 0.0;                                                     //keep the weighter probability
        String[] categories = this.getCategories();                             //keep all categories
        double total = 0.0;                                                     //keep total times the feature appear in all categories
        
        /* get the current probability */
        prob = this.featureProbability(feature, category);
        
        /* run over each category counting how many times the feature appeared */
        for(int i = 0; i < categories.length; i++){
            total += this.getFeature(feature, categories[i]);
        }//end for
        
        /* calculate the weighter average */
        wprob = ((weight * start) + (total * prob)) / (weight + total);
        return wprob;
        
    }//end weightedProbability() method
    
    
    
}//End Classifier class
