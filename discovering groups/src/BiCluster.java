/*
 * Created on 12 October, 2008
 */

import java.util.*;

/**
 * 
 * @author giscardf
 * @version 1.0
 */
class BiCluster {
    
    private BiCluster left;                                                     //point to the left node of this cluster
    private BiCluster right;                                                    //point to the right node of this cluster
    private HashMap<String,Double> data;                                        //contains the data of this cluster
    private String id;                                                          //defines an ID for this cluster
    private double distance;                                                    //defines the distance between the clusters under this one
    
    /**
     * Creates a new BiCluster object
     * @param left A <code>BiCluster</code> representing the left node
     * @param right A <code>BiCluster</code> representing the right node
     * @param data A <code>BiCluster</code> representing the data
     * @param id A <code>int</code> representing the cluster ID
     * @param distance A <code>double</code> representing the distance between the under nodes
     */
    public BiCluster(BiCluster left, BiCluster right, HashMap<String,Double> data, String id, double distance){
        this.left = left;
        this.right = right;
        this.data = data;
        this.id = id;
        this.distance = distance;
    }//End BiCluster() constructor

    /**
     * This method get the cluster id
     * @return A <code>String</code> representing the cluster id
     */
    public String getId() {
        return id;
    }//End getId() method

    /**
     * This method get the distance between two clusters
     * @return A <code>double</code> representing the distance between clusters
     */
    public double getDistance() {
        return distance;
    }//End getDistance() method

    /**
     * This method set the distance between clusters
     * @param distance A <code>double</code> representing the distance between clusters
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }//end setDistance() method
    

    /**
     * This method get the cluster data
     * @return A <code>HashMap</code> pointing to the cluster dat
     */
    public HashMap<String, Double> getData(){
        return data;
    }//End getData() method

    /**
     * This method set the cluster data
     * @param data A <code>HashMap</code> pointing the cluster data
     */
    public void setData(HashMap<String, Double> data) {
        this.data = data;
    }//End setData() method
    

    /**
     * This method get the left cluster
     * @return A <code>BiCluster</code> pointing to the left cluster
     */
    public BiCluster getLeft() {
        return left;
    }//end getLeft() method

    /**
     * This method get the right cluster
     * @return A <code>BiCluster</code> pointing to the right cluster
     */
    public BiCluster getRight() {
        return right;
    }//end getRight() method
    
    
    @Override
    /**
     * This method compare two clusters by checking their ID
     * @see java.lang.Object#equals
     */
    public boolean equals(Object obj) {
        
        /* make sure both objects are clusters */
        if(!(obj instanceof BiCluster))
            return false;
        
        /* return the comparison of the IDs */
        return (this.id.equals(((BiCluster)obj).getId()));
        
    }//End equals() method
    
}//End BiCluster class