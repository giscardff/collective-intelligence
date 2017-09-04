/*
 * Created on 30 November, 2008
 */

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.table.*;

/**
 *
 * @author giscardf
 */
public class MainApp {

    /**
     * This method execute the main application
     * @param args A <code>String[]</code> representing the command line paramter args
     */
    public static void main(String[] args) throws Exception {
        
        Class[] types = {String.class, String.class, String.class, String.class, String.class};
        MainApp app = new MainApp();
        String filename = System.getProperty("user.dir") + "/src/decision_tree_example.txt";
//        String filename4 = System.getProperty("user.dir") + "/src/decision_tree_exercise_4.txt";
        TheTable data = app.parseData(filename);
        data.setColumnTypes(types);
        
        TheTable lower = new TheTable();
        TheTable higher = new TheTable();
        app.divideset(data, 2, "yes", lower, higher);
        System.out.println("Lower: " + lower.toString());
        System.out.println("Higher: " + higher.toString());
        
        HashMap<String,Double> result = app.uniquecounts(data, String.class);
        System.out.println("Result Unique: " + result);
        
        System.out.println("Gini Impurity: " + app.giniimpurity(data));
        System.out.println("Entropy      : " + app.entropy(data));
        
        DecisionNode root = app.buildtree(data, "entropy", 0.1);
        app.printtree(root, " ");
        
        DecisionTreeDisplay display = new DecisionTreeDisplay(root);
        
        Vector observ = new Vector();
        observ.add("(direct)");
        observ.add("USA");
        observ.add("yes");
        observ.add("5");
        System.out.println("Classify: " + app.classify(observ, root));
        
        app.prune(root, 0.1);
        DecisionTreeDisplay pruneDisplay = new DecisionTreeDisplay(root);
        
        Vector misobserv = new Vector();
        misobserv.add("google");
        misobserv.add(null);
        misobserv.add("yes");
        misobserv.add(null);
        System.out.println("MD Classify: " + app.mdclassify(misobserv, root));
//        
//        Vector misobserv2 = new Vector();
//        misobserv2.add("google");
//        misobserv2.add("France");
//        misobserv2.add(null);
//        misobserv2.add(null);
//        System.out.println("MD Classify: " + app.mdclassify(misobserv2, root));
//        
//        /* Exercise 2 */
//        TheTable data2 = app.parseDataNumeric(filename);
//        Class[] types2 = new Class[data2.getColumnCount()];
//        for(int i = 0; i < types2.length; i++)
//            if(data2.getValueAt(0, i) instanceof Double)
//                types2[i] = Double.class;
//            else
//                types2[i] = String.class;
//        data2.setColumnTypes(types2);
//        
//        DecisionNode root2 = app.buildtree(data2, "entropy", 0.1);
//        Vector misobserv22 = new Vector();
//        misobserv22.add("google");
//        misobserv22.add("Brazil");
//        misobserv22.add("yes");
//        Vector tuple2 = new Vector();
//        tuple2.add(new Double(22.0));
//        tuple2.add(new Double(25.0));
//        misobserv22.add(tuple2);
//        System.out.println("MD Classify: " + app.mdclassify(misobserv22, root2));
        
        /* Exercise 4 */
//        TheTable data4 = app.parseData(filename4);
//        data4.setColumnTypes(types);        
//        DecisionNode tree = app.buildtree(data4, "entropy", 0.0);
//        DecisionTreeDisplay display4 = new DecisionTreeDisplay(tree);
//        Vector observation4 = new Vector();
//        observation4.add("google");
//        observation4.add("Brazil");
//        observation4.add("no");
//        observation4.add("27");
//        System.out.println("Classify: " + app.classify(observation4, tree));
//        System.out.println("MD Classify: " + app.mdclassify(observation4, tree));
                        
        
    }//End main() method
    
    /**
     * This method sum all values from a collection
     * @param c A <code>Collection</code> representing the collection
     * @return A <code>int</code> representing the sum
     */
    private double sum(Collection c){
        double sum = 0;
        for(Iterator<Double>iterator = c.iterator(); iterator.hasNext();)
            sum += iterator.next();
        return sum;
    }//end sum() method
    
    /**
     * This method parse the data file and retrieve a table representation of it
     * @param filename A <code>String</code> representing the file path
     * @return A <code>TheTable</code> representing the table representation
     */
    private TheTable parseData(String filename) throws FileNotFoundException, IOException {
        
        TheTable data = new TheTable();                                                 //final table        
        String line = null;                                                             //keep current line
        BufferedReader input = new BufferedReader(new FileReader(new File(filename)));  //open the file 
        
        /* parse the file */
        line = input.readLine();
        while(line != null){
            String[] tokens = line.split("\\t");                                        //data are \t separated
            data.setColumnCount(tokens.length);            
            Object[] rowData = new Object[tokens.length];
            for(int i = 0; i < tokens.length; i++){
                if(tokens[i].equals("null"))
                    rowData[i] = "null";
                else
                    rowData[i] = tokens[i];                
            }//end for
            data.addRow(rowData);
            line = input.readLine();                                                    //keep reading
            data.getValueAt(0, 0);
        }//end while
        input.close();                                                                  //close the file
        
        return data;                                                                    //return the table
        
    }//End parseData() method
    
    /**
     * This method parse the data file and retrieve a table representation of it
     * @param filename A <code>String</code> representing the file path
     * @return A <code>TheTable</code> representing the table representation
     * NOTE: Every data that can be converted will be converted to double
     */
    private TheTable parseDataNumeric(String filename) throws FileNotFoundException, IOException {
        
        TheTable data = new TheTable();                                                 //final table        
        String line = null;                                                             //keep current line
        BufferedReader input = new BufferedReader(new FileReader(new File(filename)));  //open the file 
        
        /* parse the file */
        line = input.readLine();
        while(line != null){
            String[] tokens = line.split("\\t");                                        //data are \t separated
            data.setColumnCount(tokens.length);
            Object[] rowData = new Object[tokens.length];
            for(int i = 0; i < tokens.length; i++){
                try{
                    double numToken = Double.parseDouble(tokens[i]);
                    rowData[i] = new Double(numToken);
                }//end try
                catch(Exception ex){
                    if(tokens[i].equals("null"))
                        rowData[i] = "null";
                    else
                        rowData[i] = tokens[i];
                }//end catch
            }//end for
            data.addRow(rowData);
            line = input.readLine();                                                    //keep reading
            data.getValueAt(0, 0);
        }//end while
        input.close();                                                                  //close the file
        
        return data;                                                                    //return the table
        
    }//end parseDataNumeric() method
    
    /**
     * This method split a set of data in two other sets
     * @param data A <code>DefaultTableModel</code> representing all the data
     * @param column A <code>
     * @param value
     * @param lowerSet
     * @param higherSet
     */
    public void divideset(TheTable data, int column, Object value, TheTable lowerSet, TheTable higherSet){
        
        /* first check if data is numeric */
        if(data.getColumnType(column) == Double.class){            
            this.splitNumeric(data, column, (Double)value, lowerSet, higherSet);
        }//end if
        /* otherwise check if data is text */
        else if(data.getColumnType(column) == String.class){            
            this.splitText(data, column, (String)value, lowerSet, higherSet);
        }//end else
        else {
            //should not be here
        }//end else
        
    }//end divideset() method
    
    /**
     * This method split a set of numeric values in two sets
     * @param columnData A <code>Vector</code> representing all numeric values
     * @param cmpValue A <code>double</code> representing the value to compare wth
     * @param lowerSet A <code>Vector</code> representing the lower set values
     * @param higherSet A <code>Vector</code> representing the higher set values
     * NOTE: Both lowerSet and higherSet does not need to be created before be passed as paramter
     */
    public void splitNumeric(TheTable data, int column, double cmpValue, TheTable lowerSet, TheTable higherSet){        
        lowerSet.setColumnCount(data.getColumnCount());
        higherSet.setColumnCount(data.getColumnCount());
        /* run over all data */
        for(int i = 0; i < data.getRowCount(); i++){            
            if(data.getValueAt(i, column) == null){
                lowerSet.addRow(data.getRow(i));
                continue;
            }//end if
            double value = (Double)data.getValueAt(i, column);                           //get current data
            /* compare data values */
            if(value >= cmpValue)
                higherSet.addRow(data.getRow(i));
            else
                lowerSet.addRow(data.getRow(i));
        }//end for
    }//end splitNumeric() method
    
    /**
     * This method split a set of text values in two sets
     * @param columnData A <code>Vector</code> representing all numeric values
     * @param cmpValue A <code>String</code> representing the value to compare wth
     * @param lowerSet A <code>Vector</code> representing the not match set values
     * @param higherSet A <code>Vector</code> representing the match set values
     * NOTE: Both lowerSet and higherSet does not need to be created before be passed as paramter
     */
    public void splitText(TheTable data, int column, String cmpValue, TheTable lowerSet, TheTable higherSet){
        lowerSet.setColumnCount(data.getColumnCount());
        higherSet.setColumnCount(data.getColumnCount());
        /* run over all data */
        for(int i = 0; i < data.getRowCount(); i++){
            String value = (String)data.getValueAt(i, column);                           //get current data
            /* compare data values */            
            if(value != null && value.equals(cmpValue))
                higherSet.addRow(data.getRow(i));
            else
                lowerSet.addRow(data.getRow(i));
        }//end for
    }//end splitText() method
    
    /**
     * This method count the amount of each data in a specific column
     * @param columnValues A <code>TheTable</code> containing all data
     * @param type A <code>Class</code> representing the datatype
     * @return A <code>HashMap</code> representing the result
     */
    public HashMap<String,Double> uniquecounts(TheTable data, Class type){
        HashMap<String,Double> result = new HashMap<String,Double>();                 //keep all results
        Vector columnValues = data.getColumnRows(data.getColumnCount() - 1);            //last column hold the results
        /* run over all column values */
        for(int i = 0; i < columnValues.size(); i++){
            if(type == Double.class){                
                Double value = (Double)columnValues.get(i);
                if(result.containsKey(value))result.put(value.toString(), result.get(value) + 1); //increment
                else result.put(value.toString(), 1.0);                                             //initialize
            }//end if
            /* if data is of text type */
            else if(type == String.class){
                String value = (String)columnValues.get(i);
                if(result.containsKey(value))result.put(value, result.get(value) + 1); //increment
                else result.put(value, 1.0);                                             //initialize
            }//end if
            else{
                
            }//end else
        }//end for
        return result;                                                                  //return the result
    }//end uniqueCounts() method
    
    /**
     * This method return the impurity of a set of data using gini method
     * @param columnValues A <code>TheTable</code> containing the data
     * @return A <code>double</code> representing the impurity (0 means pure)
     */
    public double giniimpurity(TheTable data){
       double total = data.getRowCount();
       HashMap<String,Double>result = this.uniquecounts(data, String.class);
       double imp = 0.0;
       for(Iterator<String>iterator1 = result.keySet().iterator(); iterator1.hasNext();){
           String value1 = iterator1.next();
           double prob1 = (double)result.get(value1) / total;
           for(Iterator<String>iterator2 = result.keySet().iterator(); iterator2.hasNext();){
               String value2 = iterator2.next();
               if(value1.equals(value2))continue;
               double prob2 = (double)result.get(value2) / total;
               imp += prob1 * prob2;
           }//end for
       }//end for
       return imp;
    }//end giniimpurity() method
    
    /**
     * This method return the impurity of a set of data using entropy method
     * @param columnValues A <code>TheTabler</code> containing the data
     * @return A <code>double</code> representing the impurity (0 means pure)
     */
    public double entropy(TheTable data){
        HashMap<String,Double>result = this.uniquecounts(data, String.class);
        double ent = 0.0;
        for(Iterator<String>iterator = result.keySet().iterator(); iterator.hasNext();){
            double prob = (double)result.get(iterator.next()) / data.getRowCount();
            ent = ent - prob * Math.log(prob) / Math.log(2.0);
        }//end for
        return ent;
    }//end entropy() method
    
    /**
     * This method calculate the variance of numbers
     * @param rows A <code>TheTable</code> containing all the data
     * @return A <code>double</code> representing the variance (0 means pure)
     */
    public double variance(TheTable rows){
        if(rows.getRowCount() == 0)return 0.0;
        ArrayList<Double>data = new ArrayList<Double>();
        for(int row = 0; row < rows.getRowCount(); row++)
            data.add((Double)rows.getValueAt(row, rows.getColumnCount() - 1));
        double mean = this.sum(data) / data.size();
        double sum = 0.0;
        for(Iterator<Double>iterator = data.iterator(); iterator.hasNext();){
            sum += Math.pow((iterator.next() - mean),2);
        }//end for
        double variance = sum / (double)data.size();
        return variance;
    }//end variance() method
    
    /**
     * This method invokes dinamically one type of impurity metnod based on methodName param
     * @param columnValues A <code>Vector</code> representing all the columns
     * @return A <code>double</code> representing how far is one person from another
     */
    public double impurityMethod(TheTable data, String methodName){
        /* defining the variables to invoke similarity method */
        Class[] parameters = {TheTable.class};  //An array for all attributes to invoke the method dynamically
        Method compareMethod = null;            //The method to be invoked dynamically
        
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);  //get the method name using reflection
            Double comparation = (Double)compareMethod.invoke(this, data);      //invoke the method to compare
            return comparation;                                                 //return the comparison result
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
     * This method create a decision tree based on the table data
     * @param data A <code>TheTable</code> representing the table data
     * @param impurityMethod A <code>String</code> representing the impurity method to use
     * @return A <code>DecisionNode</code> pointing to the root node
     */
    public DecisionNode buildtree(TheTable data, String impurityMethod, double min_gain){
        
        if(data.getRowCount() == 0)return new DecisionNode(-1, null, null, null, null);
        double current_score = this.impurityMethod(data, impurityMethod);
        
        /* set some variable to track best criteria */
        double best_gain = 0.0;
        int best_column = -1;
        Object best_criteria = null;
        TheTable[] best_sets = {null, null};
        
        for(int col = 0; col < data.getColumnCount() - 1; col++){            
            Vector columnValues = data.getColumnRows(col);
            /* try to divide the the rows up for each value in this column */
            for(int i = 0; i < columnValues.size(); i++){
                TheTable set1 = new TheTable();
                set1.setColumnTypes(data.getColumnTypes());
                TheTable set2 = new TheTable();
                set2.setColumnTypes(data.getColumnTypes());
                
                if(columnValues.get(i) instanceof Double)
                    this.divideset(data, col, (Double)columnValues.get(i), set1, set2);
                else if(columnValues.get(i) instanceof String)
                    this.divideset(data, col, (String)columnValues.get(i), set1, set2);
                
                /* information gain */
                double prob = (double)set1.getRowCount() / data.getRowCount();
                double gain = (double)(current_score - prob * this.impurityMethod(set1, impurityMethod) - (1 - prob) * this.impurityMethod(set2, impurityMethod));                
                
                if((gain - best_gain) < min_gain)
                    break;
                
                if(gain > best_gain && set1.getRowCount() > 0 && set2.getRowCount() > 0){
                    best_gain = gain;
                    best_column = col;
                    if(columnValues.get(i) instanceof Double)
                        best_criteria = (Double)columnValues.get(i);
                    else
                        best_criteria = (String)columnValues.get(i);
                    best_sets[0] = set1;
                    best_sets[1] = set2;
                }//end if
                
            }//end for
        }//end for
        
        /* create the subranches */
        if(best_gain > 0){
            DecisionNode falseBranch = buildtree(best_sets[0], impurityMethod, min_gain);
            DecisionNode trueBranch = buildtree(best_sets[1], impurityMethod, min_gain);
            return new DecisionNode(best_column, best_criteria, null, trueBranch, falseBranch);
        }//end if
        else
            return new DecisionNode(-1, null, this.uniquecounts(data, String.class), null, null);
        
    }//end buildtree() method
    
    /**
     * This method print the decision tree
     * @param node A <code>DecisionNode</code> representing the node to print
     * @param indent A <code>String</code> representing how much to indent each level
     */
    public void printtree(DecisionNode node, String indent){
        /* check if is a leaf node */
        if(node.results != null){
            System.out.print(node.results);
        }//end if
        else{
            System.out.println("" + node.col + ": " + node.value + "?");
            
            System.out.print(indent + "T->");
            printtree(node.tb, indent + " ");
            System.out.println();
            
            System.out.print(indent + "F->");
            printtree(node.fb, indent + " ");
            System.out.println();
        }//end else
    }//end printtree() method
    
    /**
     * This method classify a new entry based on the decision tree
     * @param observation A <code>Vector</code> containing the set of data
     * @param tree A <code>DecisionNode</code> representing the tree
     * @return A <code>HashMap</code> containing the result
     */
    public HashMap<String,Double> classify(Vector observation, DecisionNode tree){        
        DecisionNode branch = null;
        if(tree.results != null){
            for(Iterator<String>iterator = tree.results.keySet().iterator(); iterator.hasNext();){
                String key = iterator.next();
                tree.results.put(key, tree.results.get(key) / this.sum(tree.results.values()));
            }//end for
            return tree.results;
        }//end if
        else{  
            Object v = observation.get(tree.col);                        
            if(tree.value instanceof String && tree.value.equals("null")){
                HashMap<String,Double>tr = this.classify(observation, tree.tb);
                HashMap<String,Double>fr = this.classify(observation, tree.fb);                
                double tcount = this.sum(tr.values());
                double fcount = this.sum(fr.values());
                double tw = (double)tcount/(tcount + fcount);
                double fw = (double)fcount/(tcount + fcount);
                
                HashMap<String,Double>result = new HashMap<String,Double>();                
                for(Iterator<String>iterator = tr.keySet().iterator(); iterator.hasNext();){
                    String key = iterator.next();
                    Double value = tr.get(key);
                    if(result.containsKey(key) == false)
                        result.put(key, value * tw);                    
                }//end for                
                for(Iterator<String>iterator = fr.keySet().iterator(); iterator.hasNext();){
                    String key = iterator.next();
                    Double value = fr.get(key);
                    if(result.containsKey(key) == false)
                        result.put(key, value * fw);                    
                }//end for
                return result;
            }//end if
            else if(v instanceof Double){                
                double val = ((Double)v);
                double value = (Double)tree.value;
                if(val >= value) branch = tree.tb;
                else branch = tree.fb;                        
            }//end if
            else if(v instanceof String && !v.equals("null")){
                if(((String)v).equals((String)tree.value)) branch = tree.tb;
                else branch = tree.fb;
            }//end if            
            else{
                //should not be here, something goes wrong
            }//end else
        }//end else
        return this.classify(observation, branch);
    }//end classify() method
    
    /**
     * This method classify a new entry based on the decision tree
     * @param observation A <code>Vector</code> containing the set of data
     * @param tree A <code>DecisionNode</code> representing the tree
     * @return A <code>HashMap</code> containing the result
     */
    public HashMap<String,Double> mdclassify(Vector observation, DecisionNode tree){
        if(tree.results != null){
            for(Iterator<String>iterator = tree.results.keySet().iterator(); iterator.hasNext();){
                String key = iterator.next();
                tree.results.put(key, tree.results.get(key) / this.sum(tree.results.values()));
            }//end for
            return tree.results;
        }//end if
        else{
            Object v = observation.get(tree.col);            
            if(v == null || (tree.value instanceof String && tree.value.equals("null"))){
                HashMap<String,Double>tr = this.mdclassify(observation, tree.tb);
                HashMap<String,Double>fr = this.mdclassify(observation, tree.fb);                
                double tcount = this.sum(tr.values());
                double fcount = this.sum(fr.values());
                double tw = (double)tcount/(tcount + fcount);
                double fw = (double)fcount/(tcount + fcount);
                
                HashMap<String,Double>result = new HashMap<String,Double>();                
                for(Iterator<String>iterator = tr.keySet().iterator(); iterator.hasNext();){
                    String key = iterator.next();
                    Double value = tr.get(key);
                    if(result.containsKey(key) == false)
                        result.put(key, value * tw);                    
                }//end for                
                for(Iterator<String>iterator = fr.keySet().iterator(); iterator.hasNext();){
                    String key = iterator.next();
                    Double value = fr.get(key);
                    if(result.containsKey(key) == false)
                        result.put(key, value * fw);                    
                }//end for
                return result;
            }//end if
            else if(v instanceof Vector && ((Vector)v).size() > 0 && ((Vector)v).get(0) instanceof Double){
                double min = (Double)((Vector)v).get(0);
                double max = (Double)((Vector)v).get(1);
                DecisionNode branch = null;
                if(((Double)tree.value) < min || ((Double)tree.value) > max){
                    branch = tree.fb;
                }//end if
                else
                    branch = tree.tb;
                return this.mdclassify(observation, branch);
            }//end if            
            else if(v instanceof String && !v.equals("null")){
                DecisionNode branch = null;
                if(v instanceof Double){                
                    double val = ((Double)v);
                    double value = (Double)tree.value;
                    if(val >= value) branch = tree.tb;
                    else branch = tree.fb;                        
                }//end if
                else if(v instanceof String){
                    if(((String)v).equals((String)tree.value)) branch = tree.tb;
                    else branch = tree.fb;
                }//end if
                else{
                    //should not be here
                }//end else                
                return this.mdclassify(observation, branch);
            }//end else
            else{
                return null;
            }//end else
        }//end else        
    }//end mdclassify() method
    
    /**
     * This method prune a tree by merging common nodes
     * @param tree A <code>DecisionNode</code> representing the tree
     * @param mingain A <code>double</code> representing the min gain to do the merge
     */
    public void prune(DecisionNode tree, double mingain){
        /* if branches are not leaves, then prune them */
        if(tree.tb.results == null)
            prune(tree.tb, mingain);
        if(tree.fb.results == null)
            prune(tree.fb, mingain);        
        /* if both subbranches are now leaves, see if they should be merged */
        if(tree.tb.results != null && tree.fb.results != null){
            /* build a combined dataset */
            TheTable tb = new TheTable(tree.tb.results);
            TheTable fb = new TheTable(tree.fb.results);
            TheTable sum = tb.add(fb);
            /* Test the reduction in entropy */
            double delta = this.entropy(sum) - ((this.entropy(tb) + this.entropy(fb)) / 2.0);
            if(delta < mingain){
                /* merge the branches */
                tree.tb.results = null;
                tree.fb.results = null;
                tree.results = this.uniquecounts(sum, String.class);
            }//end if
        }//end if
    }//end prune() method

}//End MainApp class
