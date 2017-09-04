/*
 * Created on 17 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class DoCClass {

    /**
     * This method parse a document by getting all words from it
     * @param doc A <code>String</code> representing the document
     * @return A <code>String[]</code> representing all the words not repeated
     */
    public String[] getWords(String doc){
        
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
        
    }//end getWords() method
    
}//End DoCClass class
