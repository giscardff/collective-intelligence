package libsvm;

/*
 * Created on 17 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class NaiveBayes extends Classifier {

    /**
     * This method creates a new instance
     * @param featureMethod A <code>String</code> representing the string name of method to get the features
     * @param fileName A <code>String</code> representing the file name to do something
     */
    public NaiveBayes(String featureMethod){
        super(featureMethod);
    }//End NaiveBayes() constructor
    
    /**
     * This method calculate the probability of a document be in a specific category
     * @param item A <code>String</code> representing the item to be analized
     * @param category A <code>String</code> representing the category to check probability
     * @return A <code>double</code> representing the probability
     */
    public double docProb(String item, String category){
        
        double prob = 1.0;                                                      //keeps the final probability
        String[] features = this.getFeatures(item);                             //get all feature for this item (words for document)
        
        /* run over each feature and calculate final probability */
        for(int i = 0; i < features.length; i++){
            prob *= this.weightedProbability(features[i], category, 1.0, 0.5);
        }//end for
        
        return prob;
        
    }//end docProb() method
    
    /**
     * This method calculate the probability of a category fit in a document
     * @param item A <code>String</code> representing the item to be analized
     * @param category A <code>String</code> representing the category to check probability
     * @return A <code>double</code> representing the probability
     */
    public double prob(String item, String category){        
        double catProb = (double)this.getCategory(category) / this.totalCategories();       //get the P(cat)
        double docProb = (double)(this.docProb(item, category));                            //get the P(doc|cat)
        return (docProb * catProb);                                                         //return the P(cat|doc)
    }//end prob() method

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
        
        HashMap<String,Double> probs = new HashMap<String,Double>();            //keep probability per category
        
        /* calculate the probabliity for every category */
        for(int i = 0; i < categories.length; i++){
            probs.put(categories[i], this.prob(item, categories[i]));
            /* check if prob is greatest one */
            if(probs.get(categories[i]) > maxProb){
                maxProb = probs.get(categories[i]);
                maxCat = categories[i];
            }//end if
        }//end for
        
        /* run over all probs for all categories */
        for(Iterator<String>iterator = probs.keySet().iterator(); iterator.hasNext();){
            String cat = iterator.next();
            /* do not check for best cat */
            if(cat.equals(maxCat))
                continue;
            /* check if prob is greater than using the threshold */
            if(probs.get(cat) * this.getThreshold(maxCat) > probs.get(maxCat))
                return def;
        }//end for
        return maxCat;                                                          //return best category
        
    }//end classify() method
    
}//End NaiveBayes class
