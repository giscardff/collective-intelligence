/*
 * Created on 13 November, 2008
 */

import java.util.*;

/**
 *
 * @author giscardf
 */
public class TheDataSet<KEY,DATATYPE> {

    ArrayList<TheData<KEY, DATATYPE>> dataset;
    
    /**
     * Creates a new TheDataSet instance
     */
    public TheDataSet(){
        dataset = new ArrayList<TheData<KEY, DATATYPE>>();
    }//end TheDataSet() constructor
    
    /**
     * This method add a new object to the dictionay by using its keys
     * @param data A <code>DATATYPE</code> representing the data object type
     * @param keys A <code>KEY[]</code> representing the keys of the object
     */
    public void put(DATATYPE data, KEY... keys){
        /* first let's see if the data already exist, so override it */
        /* let`s run over all rows */        
        for(int i =0; i < dataset.size(); i++){            
            TheData<KEY,DATATYPE> dataRow = dataset.get(i);
            if(dataRow.equals(keys)){
                dataRow.setData(data);
                return;
            }//end if
        }//end for        
        /* If we are here that is because the row does not exist, so let's create it */
        TheData<KEY,DATATYPE> dataRow = new TheData<KEY,DATATYPE>();        
        dataRow.set(data, keys);
        this.dataset.add(dataRow);
    }//end put() method
    
    /**
     * This method add a new data to the dataset
     * @param data A <code>TheData</code> representing the data to be added
     */
    public void add(TheData<KEY,DATATYPE> data){
        this.dataset.add(data);
    }//end add() method
    
    /**
     * This method retrieve the data object based on its keys
     * @param keys A <code>KEYS[]</code> representing the keys
     * @return A <code>DATATYPE</code> representing the retrieved object
     */
    public DATATYPE get(KEY... keys){        
        /* let`s run over all rows */
        for(int i =0; i < dataset.size(); i++){            
            TheData<KEY,DATATYPE> dataRow = dataset.get(i);
            if(dataRow.equals(keys)){                
                return dataRow.getData();
            }//end if
        }//end for
        return null;
    }//end get() method
    
    /**
     * This method get a data of a specific index
     * @param index A <code>int</code> representing the index of the data
     * @return A <code>TheData</code> representing the data
     */    
    public TheData get(int index){
        return this.dataset.get(index);
    }//end get() method
    
    /**
     * This method retrieve true of false whether the dictionary contains or not
     * a key object
     * @param keys A <code>Object</code> representing the keys
     * @return A <code>boolean</code> whether contains or not the key
     */
    public boolean containsKey(KEY... keys){    
        /* let`s run over all rows */
        for(int i =0; i < dataset.size(); i++){            
            TheData<KEY,DATATYPE> dataRow = dataset.get(i);
            if(dataRow.equals(keys)){                
                return true;
            }//end if
        }//end for
        return false;
    }//end containsKey() method

    /**
     * This method retrieve the data object randomly    
     * @return A <code>TheData</code> representing the retrieved object
     */
    public TheData getRandom(){
        int index = (int)(Math.random() * dataset.size());
        return dataset.get(index);
    }//end get() method
    
    /**
     * This method return the amount of elements into the dataset
     * @return A <code>int</code> representing the amount of elements
     */
    public int size(){
        return this.dataset.size();
    }//end size() method
    
    /**
     * This method sort the dataset
     * @return A <code>TheDataSet</code> representing this dataset after sort it
     */
    public TheDataSet sort(){
        Collections.sort(this.dataset);
        return this;
    }//end sort() method
    
    /**
     * This method conver the object in a string form
     * @return A <code>String</code> representing the string format
     */
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append("===== DATASET =====\n");
        /* Let's run over each row */
        for(int i = 0; i < dataset.size(); i++){
            TheData<KEY,DATATYPE> data = dataset.get(i);
            output.append(data + "\n");
        }//end for
        output.append("===================");
        return output.toString();
    }//End toString() method
    
}//End TheDataSet class


