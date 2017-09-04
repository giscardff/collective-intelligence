/*
 * Created on 21 October, 2008
 */

import java.lang.reflect.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.htmlcleaner.*;


/**
 *
 * @author giscardf
 */
public class NeuralNetwork {

    public static final int WORD_LAYER = 0;
    public static final int URL_LAYER = 1;
    
    /* define database connection objects */
    private Connection conn;
    private Statement stmt;
    
    /* search engine variables */
    ArrayList<Integer> wordIds;
    ArrayList<Integer> urlIds;
    ArrayList<Integer> hiddenIds;
    double[] input;
    double[] hidden;
    double[] output;
    double[][] outputWeight;
    double[][] inputWeight;
    
    public NeuralNetwork(){
        
        /* instantiate derby database and connect to it */
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");                                      //load the jdbc driver and database classes
            String dbDir = System.getProperty("user.dir") + "/src/database";                            //set the db directory            
            String dbAttr = ";user=giscardf;password=giscardf";                                         //set db properties            
            conn = DriverManager.getConnection("jdbc:derby:" + dbDir + dbAttr);                         //get the connection to db            
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //create statement            
        }//end
        catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            return;
        }//end catch
        catch(SQLException sqle){
            sqle.printStackTrace();
            return;
        }//end catch
        
    }//end NeuralNetwork() constructor
    
    /**
     * This method close all operation to the database
     */
    public void close(){
        try{
            conn.close();
        }//end try
        catch(SQLException sqle){
            sqle.printStackTrace();
        }//end catch
    }//end close() method
    
    /**
     * This method calculate the slope of tanh
     * @param y A <code>double</code> representing the tanh value to calculate the slope
     * @return A <code>double</code> representing the slope of the tanh
     */
    private double dtanh(double y){
        return 1.0 - y * y;
    }//end dtanh() method
    
    /**
     * This method get the strength of a node
     * @param fromId A <code>int</code> representing the from link
     * @param toId A <code>int</code> representing the to link
     * @param layer A <code>int</code> representing the node layer type
     * @return A <code>double</code> representing the strength of the network
     * @throws java.sql.SQLException if something goes wrong
     */
    public double getStrength(int fromId, int toId, int layer) throws SQLException {
        
        String table = null;                                                    //keep the table name
        
        /* get table based on layer id */
        switch(layer){
            case NeuralNetwork.WORD_LAYER:
                table = "WordHidden";
            break;
            default:
                table = "UrlHidden";
        }//end switch
        
        /* execute the query on the database */
        Statement stmtx = this.conn.createStatement();
        ResultSet rs = stmtx.executeQuery("SELECT strength FROM " + table + " WHERE fromId=" + fromId + " AND toId=" + toId);
        
        /* if no value is found, return the default one */
        if(rs.next() == false){
            if(layer == WORD_LAYER)
                return -0.2;
            else
                return 0;
        }//en dif
            
        return rs.getDouble(1);
        
    }//end getStrength() method
    
    /**
     * This method set the strength of a node
     * @param fromId A <code>int</code> representing the from link
     * @param toId A <code>int</code> representing the to link
     * @param layer A <code>int</code> representing the layer type
     * @param strength A <code>double</code> representing the strength value
     * @throws java.sql.SQLException
     */
    public void setStrength(int fromId, int toId, int layer, double strength) throws SQLException {
        
        String table = null;                                                    //keep the table name
        
        /* get table based on layer id */
        switch(layer){
            case NeuralNetwork.WORD_LAYER:
                table = "WordHidden";
            break;
            default:
                table = "UrlHidden";
        }//end switch
        
        /* execute the query on the database */
        Statement stmtx = this.conn.createStatement();
        ResultSet rs = stmtx.executeQuery("SELECT strength FROM " + table + " WHERE fromId=" + fromId + " AND toId=" + toId);
        
        /* if no value is found, set the value */
        if(rs.next() == false){
            stmtx.execute("INSERT INTO " + table + " (fromId,toId,strength) VALUES (" + fromId + "," + toId + "," + strength + ")");
        }//en dif
        else{
            stmtx.execute("UPDATE " + table + " SET strength=" + strength + " WHERE fromId=" + fromId + " AND toId=" + toId);
        }//end else
        
    }//end setStrength() method
    
    /**
     * This method create a hidden node based on a input/output
     * @param wordIds A <code>ArrayList</code> representing the word ids
     * @param urls A <code>ArrayList</code> representing the url ids
     * @throws java.sql.SQLException
     */
    public void generateHiddenNode(ArrayList<Integer>wordIds, ArrayList<Integer>urls) throws SQLException {
        
        /* only handle up to three words */
        if(wordIds.size() > 3)
            return;
        
        String words = "";
        for(Iterator<Integer>it = wordIds.iterator(); it.hasNext();){            
            words += "" + it.next() + ":";
        }//end for
        
        /* execute the query on the database */
        Statement stmtx = this.conn.createStatement();
        ResultSet rs = stmtx.executeQuery("SELECT rowId FROM HiddenNode WHERE rowId=" + words.hashCode());
        
        /* if we do not have it, let's create */
        if(rs.next() == false){
            stmtx.execute("INSERT INTO HiddenNode (rowId) VALUES (" + words.hashCode() + ")");            
            
            /* update the word layer */
            for(Iterator<Integer> iterator = wordIds.iterator(); iterator.hasNext();)
                this.setStrength(iterator.next(), words.hashCode(), 0, (double)(1.0 / wordIds.size()));

            /* update the url layer */
            for(Iterator<Integer> iterator = urls.iterator(); iterator.hasNext();)
                this.setStrength(words.hashCode(), iterator.next(), 1, 0.1);            
        }//end if               
        
    }//end generateHiddenNode() method
    
    /**
     * This method get all hidden nodes related to a word/url
     * @param wordIds A <code>ArrayList</code> representing all words
     * @param urlIds A <code>ArrayList</code> representing all urls
     * @return A <code>ArrayList</code> representing all hidden node ids
     * @throws java.sql.SQLException
     */
    public ArrayList<Integer> getAllHiddenIds(ArrayList<Integer>wordIds, ArrayList<Integer>urlIds) throws SQLException {
        
        ArrayList<Integer>hiddenIds = new ArrayList<Integer>();
        Statement stmtx = this.conn.createStatement();
        
        /* get all hidden node attached to the word ids */
        for(Iterator<Integer>iterator = wordIds.iterator(); iterator.hasNext();){
            ResultSet rs = stmtx.executeQuery("SELECT toId FROM WordHidden WHERE fromId=" + iterator.next());
            if(rs.next() == true)
                hiddenIds.add(rs.getInt(1));
        }//end for
        
        /* get all hidden node attached to the url ids */
        for(Iterator<Integer>iterator = urlIds.iterator(); iterator.hasNext();){
            ResultSet rs = stmtx.executeQuery("SELECT fromId FROM WordHidden WHERE toId=" + iterator.next());
            if(rs.next() == true)
                hiddenIds.add(rs.getInt(1));
        }//end for
        
        return hiddenIds;
        
    }//end getAllHiddenIds() method
    
    /**
     * This method create the neural network
     * @param wordIds A <code>ArrayList</code> representing all words
     * @param urlIds A <code>ArrayList</code> representing all urls
     * @throws java.sql.SQLException
     */
    public void setupNetwork(ArrayList<Integer>wordIds, ArrayList<Integer>urlIds) throws SQLException {
        
        /* value list */
        this.wordIds = wordIds;
        this.urlIds = urlIds;
        this.hiddenIds = this.getAllHiddenIds(wordIds, urlIds);
        
        /* node input */
        this.input = new double[wordIds.size()];
        for(int i = 0; i < input.length; i++)
            this.input[i] = 1.0;
        
        /* node output */
        this.output = new double[urlIds.size()];
        for(int i = 0; i < output.length; i++)
            this.output[i] = 1.0;
        
        /* node hidden */
        this.hidden = new double[hiddenIds.size()];
        for(int i = 0; i < hidden.length; i++)
            this.hidden[i] = 1.0;
        
        /* input weight matrix */
        this.inputWeight = new double[wordIds.size()][hiddenIds.size()];
        for(int i = 0; i < inputWeight.length; i++){
            for(int j = 0; j < inputWeight[i].length; j++){
                inputWeight[i][j] = this.getStrength(wordIds.get(i), hiddenIds.get(j), this.WORD_LAYER);
            }//end for
        }//end for
        
        /* output weight matrix */
        this.outputWeight = new double[hiddenIds.size()][urlIds.size()];
        for(int i = 0; i < outputWeight.length; i++){
            for(int j = 0; j < outputWeight[i].length; j++){
                outputWeight[i][j] = this.getStrength(hiddenIds.get(i), urlIds.get(j), this.URL_LAYER);
            }//end for
        }//end for
        
    }//end setupNetwork() method
    
    /**
     * This method feed the neural network to update the scores
     * @return A <code>double[]</code> representing the scores for url output     
     */
    public double[] feedForward(){
        
        /* use query words as input */
        for(int i = 0; i < wordIds.size(); i++)
            this.input[i] = 1.0;
        
        /* hidden activations */
        for(int j = 0; j < hiddenIds.size(); j++){
            double sum = 0.0;
            for(int i = 0; i < wordIds.size(); i++)
                sum += this.input[i] * this.inputWeight[i][j];
            this.hidden[j] = Math.tanh(sum);
        }//end for
        
        /* output activations */
        for(int k = 0; k < urlIds.size(); k++){
            double sum = 0.0;
            for(int j = 0; j < hiddenIds.size(); j++)
                sum += this.hidden[j] * this.outputWeight[j][k];
            this.output[k] = Math.tanh(sum);
        }//end for
        
        return this.output;
        
    }//end feedForward() method
    
    /**
     * This method update the neural network nodes weight by using back propagation
     * @param target A <code>double[]</code> targeting the value the nodes should have
     * @param n A <code>double</code> representing the factor to update weight values
     */
    public void backPropagation(double[] target, double n){
        
        /* calculate error for output */
        double[] outputDelta = new double[this.urlIds.size()];        
        for(int k = 0; k < outputDelta.length; k++){
            double error = target[k] - this.output[k];
            outputDelta[k] = this.dtanh(this.output[k]) * error;
        }//end for
        
        /* calculate error for hidden */
        double[] hiddenDelta = new double[this.hiddenIds.size()];        
        for(int j = 0; j < hiddenDelta.length; j++){
            double error = 0.0;
            for(int k = 0; k < urlIds.size(); k++){
                error += outputDelta[k] * this.outputWeight[j][k];                
            }//end for
            hiddenDelta[j] = this.dtanh(this.hidden[j]) * error;
        }//end for
        
        /* update output weights */
        for(int j = 0; j < this.hiddenIds.size(); j++){
            for(int k = 0; k < this.urlIds.size(); k++){
                double change = outputDelta[k] * this.hidden[j];
                this.outputWeight[j][k] = this.outputWeight[j][k] * n * change;
            }//end for
        }//end for
        
        /* update input weight */
        for(int i = 0; i < this.wordIds.size(); i++){
            for(int j = 0; j < this.hiddenIds.size(); j++){
                double change = hiddenDelta[j] * this.input[i];
                this.inputWeight[i][j] = this.inputWeight[i][j] * n * change;
            }//end for
        }//end for
        
    }//end backPropagation() method
    
    /**
     * This method train a neural network
     * @param wordIds A <code>ArrayList</code> representing the words used to perform the search
     * @param urlIds A <code>ArrayList</code> representing the urls found based on the search
     * @param selectedUrls A <code>ArrayList</code> representing the selected urls of the answer
     * @throws java.sql.SQLException
     */
    public void trainQuery(ArrayList<Integer>wordIds, ArrayList<Integer>urlIds, ArrayList<Integer>selectedUrls) throws SQLException {
        
        /* generate hidden node if necessary */
        this.generateHiddenNode(wordIds, urlIds);        
        this.setupNetwork(wordIds, urlIds);
        double[] result = this.feedForward();
        
        /* define the target value 1=selected 0=non-selected */
        double[] targets = new double[urlIds.size()];
        for(int i = 0; i < targets.length; i++)
            if(selectedUrls.contains(urlIds.get(i)))
                targets[i] = 1.0;
        
        this.backPropagation(targets, 0.5);
        this.updateDatabase();
        
    }//end trainQuery() method
    
    /**
     * This method get the result based on the neural network output
     * @param urlIds A <code>ArrayList</code> representing the output url ids
     * @return A <code>HashMap</code> containing each url id and its respective score
     */
    public HashMap<Integer,Double> getResult(ArrayList<Integer>urlIds){
        
        HashMap<Integer,Double>urlScores = new HashMap<Integer,Double>();
        for(int i = 0; i < this.output.length; i++){
            urlScores.put(urlIds.get(i), this.output[i]);
        }//end for
        
        return urlScores;
        
    }//end getResult() method
    
    /**
     * This method train a neural network considering a ranking score give by the user to the selected url
     * @param wordIds A <code>ArrayList</code> representing the words used to perform the search
     * @param urlIds A <code>ArrayList</code> representing the urls found based on the search
     * @param selectedUrls A <code>ArrayList</code> representing the selected urls of the answer
     * @throws java.sql.SQLException
     */
    public void trainQueryWithRanking(ArrayList<Integer>wordIds, ArrayList<Integer>urlIds, ArrayList<Integer>selectedUrls) throws SQLException {
        
        /* generate hidden node if necessary */
        this.generateHiddenNode(wordIds, urlIds);        
        this.setupNetwork(wordIds, urlIds);
        this.feedForward();
        
        /* define the target value 1=selected 0=non-selected */
        double[] targets = new double[urlIds.size()];
        for(int i = 0; i < targets.length; i++)
            if(selectedUrls.contains(urlIds.get(i)))
                targets[i] = ((double) ((int)(Math.random() * 4 + 1.0)) / 5.0 );
        
        this.backPropagation(targets, 0.5);
        this.updateDatabase();
        
    }//end trainQueryWithRanking() method
    
    /**
     * This method update the database with the new strength scores
     * @throws java.sql.SQLException
     */
    public void updateDatabase() throws SQLException {
        
        /* update input weight */
        for(int i = 0; i < this.wordIds.size(); i++){
            for(int j = 0; j < this.hiddenIds.size(); j++){
                this.setStrength(this.wordIds.get(i), this.hiddenIds.get(j), 0, this.inputWeight[i][j]);
            }//end for
        }//end for
        
        /* updating the strength between wordIds and hidden nodes */
        for(int j = 0; j < this.hiddenIds.size(); j++){
            for(int k = 0; k < this.urlIds.size(); k++){
                this.setStrength(this.hiddenIds.get(j), this.urlIds.get(k), 1, this.outputWeight[j][k]);
            }//end for
        }//end for
        
    }//end updateDatabase() method
    
}//End NeuralNetwork class
