package libsvm;

/*
 * Created on 13 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class TheDictionary<KEY,DATATYPE> {

    private final int primes[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 45};  //represent primie numbers, used for hash calculation
    private HashMap<Long,DATATYPE> dataset;                                             //represent the dataset with all the data
    
    /**
     * Creates a new TheDictionary instance
     */
    public TheDictionary(){
        dataset = new HashMap<Long,DATATYPE>();
    }//end TheDictionary() constructor
    
    /**
     * This method add a new object to the dictionay by using its keys
     * @param data A <code>Object</code> representing the data object type
     * @param keys A <code>Object</code> representing the keys of the object
     */
    public void put(DATATYPE data, KEY... keys){        
        /* use the hash code as index */
        long hashCode = 0;
        for(int i = 0; i < keys.length; i++)
            hashCode += keys[i].hashCode() * primes[i];                         //use the hash code for all the links        
        /* let's add the data to the hash map */
        dataset.put(hashCode, data);
    }//end put() method
    
    /**
     * This method retrieve the data object based on its keys
     * @param keys A <code>Object</code> representing the keys
     * @return A <code>Object</code> representing the retrieved object
     */
    public DATATYPE get(KEY... keys){
        /* use the hash code as index */
        long hashCode = 0;
        for(int i = 0; i < keys.length; i++)
            hashCode += keys[i].hashCode() * primes[i];                         //use the hash code for all the links        
        /* let's retrieve the data */
        return dataset.get(hashCode);
    }//end get() method
    
    /**
     * This method retrieve true of false whether the dictionary contains or not
     * a key object
     * @param keys A <code>Object</code> representing the keys
     * @return A <code>boolean</code> whether contains or not the key
     */
    public boolean containsKey(KEY... keys){    
        /* use the hash code as index */
        long hashCode = 0;
        for(int i = 0; i < keys.length; i++)
            hashCode += keys[i].hashCode() * primes[i];                         //use the hash code for all the links        
            return dataset.containsKey(hashCode);
    }//end containsKey() method
    
}//End TheDictionary class


