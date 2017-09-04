/*
 * Created on 17 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class FisherClassifier extends Classifier {

    private double cprob = 0.0;
    private HashMap<String,Double>minimus = new HashMap<String,Double>();
    
    /**
     * This method creates a new instance
     * @param featureMethod A <code>String</code> representing the string name of method to get the features
     * @param fileName A <code>String</code> representing the file name to do something
     */
    public FisherClassifier(String featureMethod, String fileName){
        super(featureMethod, fileName);
    }//End NaiveBayes() constructor
    
    /**
     * This method retrieve the minimum value for a category
     * @param category A <code>String</code> representing the category
     * @return A <code>double</code> representing the value
     */
    public double getMinimus(String category){
        if(this.minimus.containsKey(category) == false)
            return 0;
        return this.minimus.get(category);
    }//end getMinimus() method
    
    /**
     * This method set a minimum value for a category
     * @param category A <code>String</code> representing the category
     * @param value A <code>double</code> representing the value
     */
    public void setMinimus(String category, double value){
        this.minimus.put(category, value);
    }//end setMinimus() method
    
    /**
     * This method calculate the fisher probability of a document being part of a category
     * @param feature A <code>String</code> representing the feature
     * @param category A <code>String</code> representing the category
     * @return A <code>double</code> representing the probability
     */
    public double cprob(String feature, String category){
        
        /* get frequency of this feature in this category */
        double fprob = this.featureProbability(feature, category);
        if(fprob == 0.0)
            return 0;
        
        /* get frequency is this feature for all categories */
        double ffreq = 0.0;
        String[] categories = this.getCategories();
        for(int i = 0; i < categories.length; i++){
            ffreq += this.featureProbability(feature, categories[i]);
        }//end for
        
        this.cprob = fprob / ffreq;
        
        /* fisher prob is the frequency for a cat divided by all frequencies */
        return fprob / ffreq;
        
    }//End cprob() method
    
    /**
     * This method calculate the inverse of a chi-square function
     * @param chi A <code>double</code> representing the chi-square
     * @param size A <code>int</code> representing the size of the sample
     * @return A <code>double</code> representing the inverse
     */
    public double invchi2(double chi, double size){
        
        double m = chi / 2.0;
        double term = Math.exp(-m);
        double sum = term;
        
        for(double i = 1; i < Math.floor((double)size / 2); i++){
            term *=  m / i;
            sum += term;
        }//end for
        
        return Math.min(sum, 1.0);
        
    }//end invchi2() method
    
    /**
     * This method calculate the fisher probability of an item be in a specific category
     * @param item A <code>String</code> representing the item
     * @param category A <code>String</code> representing the category
     * @return A <code>double</code> representing the probability
     */
    public double fisherProb(String item, String category){
        
        /* multiply all probabilities together */
        double prob = 1.0;
        String[] features = this.getFeatures(item);
        for(int i = 0; i < features.length; i++)
            prob *= this.weightedProbability(features[i], category, this.cprob, 0.5);
        
        /* calcule fisher combined probability */
        double fscore = -2 * Math.log(prob);
        
        /* return the inverse chi2 function to get probability */
        return this.invchi2(fscore, features.length * 2);
        
    }//end fisherProb() method
    
    /**
     * This method classify a given item by retrieving its category
     * @param item A <code>String</code> representing the item
     * @param def A <code>String</code> representing a default category if none is found
     * @return A <code>String</code> representing the best-fit category
     */
    public String classify(String item, String def){
        
        double maxProb = 0.0;
        String maxCat = null;
        String[] categories = this.getCategories();
        
        /* calculate the probabliity for every category */
        for(int i = 0; i < categories.length; i++){
            double prob = this.fisherProb(item, categories[i]);
            if( prob > this.getMinimus(categories[i]) && prob > maxProb){
                maxCat = categories[i];
                maxProb = prob;
            }//end if
        }//end for
        
        return maxCat;
        
    }//end classify() method
    
}//End FisherClassifier class
