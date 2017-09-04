/*
 * Created on 13 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class TheData<KEY, DATATYPE> extends ArrayList<KEY> implements Comparable<TheData> {    
    
    private DATATYPE data;
    
    /**
     * This method conver the object in a string form
     * @return A <code>String</code> representing the string format
     */
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append("KEYS(");
        /* Let's run over each colum */
        for(int i = 0; i < super.size(); i++){
            output.append(this.get(i) + ",");
        }//End for
        output.replace(output.length() - 1, output.length(), ")");
        output.append("->" + this.data);
        return output.toString();
    }//End toString() method
    
    /**
     * This method set the a data
     * @param data A <code>DATATYPE</code> representing the data
     * @param keys A <code>KEY</code>representing the keys
     */
    public void set(DATATYPE data, KEY... keys){
        for(int i = 0; i < keys.length; i++)
            super.add(keys[i]);
        this.data = data;
    }//end set() method
    
    /**
     * This method retrieve the data
     * @return A <code>DATATYPE</code> representing the data
     */
    public DATATYPE getData(){
        return this.data;
    }//end getData() method

    /**
     * This method set the data (update the data for a Data)
     * @param data A <code>DATATYPE</code> representing the data to update
     */
    public void setData(DATATYPE data) {
        this.data = data;
    }//End setData() method
    
    
    /**
     * This method get a key value
     * @param index A <code>index</code> representing the key index
     * @return A <code>KEY</code> representing the key
     */
    public KEY getKey(int index){
        return (KEY)this.get(index);
    }//end getKey() method
    
    /**
     * This method get all the keys
     * @param array A <codE>KEY[]</code> representing the array to copy the value
     * @return A <code>KEY[]</code> representing the array withg copied values
     * NOTE: the incoming array must be in the same size that the data keys
     */
    public KEY[] getKeys(KEY[] array){
        for(int i = 0; i < array.length; i++)
            array[i] = super.get(i);
        return array;
    }//end getKeys() method
    
    /**
     * This method return how many keys there is on the data
     * @return A <code>int</code> representing amount of keys
     */
    public int keySize(){
        return this.size();
    }//end keySize() method

    /**
     * This method compare to TheData values, they are equals when all the
     * keys match
     * @param o A <code>Object</code> represeting the object to compare
     * @return A <code>boolean</code> telling if they are equal or not
     */
    @Override
    public boolean equals(Object o) {
        /* let's see if they are of the same type */
        if(!(o instanceof TheData))
            return false;
        TheData toCompare = (TheData)o;         //cast the object
        /* if they don't have the same number of keys, they are different */
        if(this.keySize() != toCompare.keySize())
            return false;
        /* now let's compare all keys */
        for(int i = 0; i < this.keySize(); i++){
            if(this.getKey(i) != toCompare.getKey(i))
                return false;
        }//end for
        return true;
    }//End equals() method
    
    /**
     * This method compare TheData with a set of values
     * @param keys A <code>KEY[]</code> representing a list of keys
     * @return A <code>boolean</code> telling if they are equal or not
     */
    public boolean equals(KEY... keys){
        /* if they don't have the same number of keys, they are different */
        if(this.keySize() != keys.length)
            return false;
        /* now let's compare all keys */
        for(int i = 0; i < this.keySize(); i++){
            if(this.getKey(i) != keys[i])
                return false;
        }//end for
        return true;
    }//end equals() method

    /**
     * This method compare two objects to see whom cames first
     * @param o A <code>Object</code> representing the object to be compared
     * @return A <code>int</code> telling if the object is smaller (-1), equals(0) or greater (1)
     */
    public int compareTo(TheData o) {
        DATATYPE toCompare = (DATATYPE)o.getData();
        return ((Comparable<DATATYPE>)this.data).compareTo((DATATYPE)toCompare);
    }//End compareTo() method
    
}//End TheData class
