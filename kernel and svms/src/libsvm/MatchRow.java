package libsvm;

/*
 * Created on 17 December 2008
 */

import java.io.*;
import java.util.*;
import org.jfree.data.xy.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import libsvm.*;

/**
 *
 * @author giscardf
 */
public class MatchRow {

    private ArrayList data = new ArrayList();
    private int match = 0;
    
    /**
     * Creates a new MatchRow object
     * @param row A <code>String[]</code> representing all elements from that row
     * @param allnum A <code>boolena</code> pointing if all elements are numeric or not
     */
    public MatchRow(String row[], boolean allnum){
        if(allnum){
            for(int i = 0; i < row.length - 1; i++)
                data.add(Double.parseDouble(row[i]));
        }//end for
        else{
             for(int i = 0; i < row.length - 1; i++)
                data.add(row[i]);
        }//end else
        match = Integer.parseInt(row[row.length - 1]);        
    }//End MatchRow() constructor
    
    /**
     * Creates a new MatchRow object
     * @param row A <code>double[]</code> representing all elements of that row
     */
    public MatchRow(double row[], int match){
        for(int i = 0; i < row.length; i++)
            data.add(row[i]);
        this.match = match;
    }//end MatchRow() constructor
    
    /**
     * This method check the this specific rows got a match or not
     * @return A <code>int</code> pointing if this row got a match or not
     */
    public int match(){
        return match;
    }//end match
    
    /**
     * This method get the age (first element of the array
     * @return A <code>Double</code> representing the first age
     */
    public Double getAge1(){
        return (Double)data.get(0);
    }//end getAge1() method
    
    /**
     * This method get the age (second element of the array)     
     * @return A <code>Double</code> representing the second age
     */
    public Double getAge2(){
        return (Double)data.get(1);
    }//end getAge1() method
    
    /**
     * This method retrieve the object of the array on a specific index
     * @param index A <code>int</code> representing the array index
     * @return A <code>Double</code> representing the value
     */
    public Double getIndex(int index){
        return (Double)data.get(index);
    }//end getIndex() method
    
    /**
     * This method return the size of the array
     * @return A <code>int</code> representing the array size
     */
    public int size(){
        return data.size();
    }//end size() method
    
    /**
     * This method load a file and put the data into an array
     * @param filename A <code>String</code> representing the filename
     * @param allnum A <code>boolean</code> telling if the file has only numbers or not
     * @return A <code>ArrayList</code> containing all rows
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static ArrayList<MatchRow> loadmatch(String filename, boolean allnum) throws FileNotFoundException, IOException {        
        ArrayList<MatchRow>rows = new ArrayList<MatchRow>();
        BufferedReader input = new BufferedReader(new FileReader(filename));
        String line = input.readLine();
        while(line != null){
            rows.add(new MatchRow(line.split(","), allnum));
            line = input.readLine();
        }//end while
        input.close();
        return rows;        
    }//end loadmatch() method
    
    /**
     * This method load a file and put the data into an array
     * @param filename A <code>String</code> representing the filename
     * @return A <code>ArrayList</code> containing all rows
     */
    public static ArrayList<MatchRow> loadnumerical(String filename) throws FileNotFoundException, IOException {
        ArrayList<MatchRow>rows = new ArrayList<MatchRow>();
        BufferedReader input = new BufferedReader(new FileReader(filename));
        String line = input.readLine();
        while(line != null){
            String tokens[] = line.split(",");
            rows.add(new MatchRow(new String[]{tokens[0], "" + yesno(tokens[1]), "" + yesno(tokens[2]), tokens[5], "" + yesno(tokens[6]), "" + yesno(tokens[7]), "" + matchcount(tokens[3], tokens[8]), tokens[10]}, true));
            line = input.readLine();
        }//end while
        input.close();
        return rows;  
    }//end loadnumerical() method
    
    /**
     * This method load a file and put the data into an array
     * @param filename A <code>String</code> representing the filename
     * @return A <code>ArrayList</code> representing all interests
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static ArrayList<String>[] loadinterests(String filename) throws FileNotFoundException, IOException {
        ArrayList rows[] = {new ArrayList<String>(), new ArrayList<String>()};
        BufferedReader input = new BufferedReader(new FileReader(filename));
        String line = input.readLine();
        while(line != null){
            String tokens[] = line.split(",");
            rows[0].add(tokens[tokens.length - 1]);
            rows[1].add(tokens[3] + ":" + tokens[8]);
            line = input.readLine();
        }//end while
        input.close();
        return rows;  
    }//end loadinterests() method
    
    /**
     * This method prints the graph with the data
     * @param rows A <code>ArrayList</code> containing the data to print
     */
    public static void printagesgraph(ArrayList<MatchRow> rows){
        double matchdata[][] = new double[2][rows.size()];        
        double notmatchdata[][] = new double[2][rows.size()];
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            if(row.match() == 1){
                matchdata[0][i] = row.getAge1();
                matchdata[1][i] = row.getAge2();
            }//end if
            else{
                notmatchdata[0][i] = row.getAge1();
                notmatchdata[1][i] = row.getAge2();
            }//end else            
        }//end for        
        DefaultXYDataset matchdataset = new DefaultXYDataset();
        matchdataset.addSeries("match"/* matcheddata */, matchdata);
        matchdataset.addSeries("no match"/* notmatchdata */, notmatchdata);        
        JFreeChart chart = ChartFactory.createScatterPlot("Distribution", "Man ages", "Woman ages", matchdataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame = new ChartFrame("Match Graph", chart);        
        frame.pack();
        frame.setVisible(true);        
    }//end printgraph() method
    
    /**
     * This method prints the graph with the data
     * @param rows A <code>ArrayList</code> containing the data to print
     */
    public static void printagesgraph(ArrayList<MatchRow> rows, double[][] averages){
        double matchdata[][] = new double[2][rows.size()];        
        double notmatchdata[][] = new double[2][rows.size()];        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            if(row.match() == 1){
                matchdata[0][i] = row.getAge1();
                matchdata[1][i] = row.getAge2();
            }//end if
            else{
                notmatchdata[0][i] = row.getAge1();
                notmatchdata[1][i] = row.getAge2();
            }//end else            
        }//end for 
        DefaultXYDataset matchdataset = new DefaultXYDataset();
        matchdataset.addSeries("match"/* matcheddata */, matchdata);
        matchdataset.addSeries("no match"/* notmatchdata */, notmatchdata);        
        matchdataset.addSeries("average", averages);
        JFreeChart chart = ChartFactory.createScatterPlot("Distribution", "Man ages", "Woman ages", matchdataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame = new ChartFrame("Match Graph", chart);        
        frame.pack();
        frame.setVisible(true);        
    }//end printgraph() method
    
    /**
     * This method perform a linear train on the dataset
     * @param rows A <code>ArrayList</code> representing the data to train
     */
    public static ArrayList<MatchRow> lineartrain(ArrayList<MatchRow> rows){
        ArrayList<MatchRow> rowAvg = new ArrayList<MatchRow>();
        MatchRow row = rows.get(0);        
        double[][] averages = new double[2][row.size()];
        double[] counts = new double[2];        
        for(int i = 0; i < rows.size(); i++){
            for(int j = 0; j < row.size(); j++){
                row = rows.get(i);
                averages[row.match][j] += row.getIndex(j);
            }//end for
            counts[row.match]++;
        }//end for
        for(int match = 0; match < averages.length; match++){
            for(int i = 0; i < averages[match].length; i++){
                averages[match][i] = averages[match][i] / counts[match];
            }//end for
            rowAvg.add(new MatchRow(averages[match], match));
        }//end for
        return rowAvg;
    }//End lineartrain() method    
    
    /**
     * This method retrieve the dot-product between two vectors
     * @param v1 A <code>MatchRow</code> representing the first vector
     * @param v2 A <code>MatchRow</code> representing the second vector
     * @return A <code>double</code> representing the result
     */
    public static double dotproduct(MatchRow v1, MatchRow v2){
        double sum = 0.0;
        for(int i =0; i < v1.size(); i++)
            sum += v1.getIndex(i) * v2.getIndex(i);
        return sum;
    }//end dotproduct() method
    
    /**
     * This method retrieve if a point is on the match/notmatch side of the line
     * @param row A <code>MatchRow</code> representing the data to be compared
     * @param avgs A <code>ArrayList</code> representing the averages
     * @return A <code>int</code> representing the side of the line
     */
    public static int dpclassify(MatchRow point, ArrayList<MatchRow> avgs){
        double b = (dotproduct(avgs.get(1), avgs.get(1)) - dotproduct(avgs.get(0), avgs.get(0))) / 2.0;
        double y = dotproduct(point, avgs.get(0)) - dotproduct(point, avgs.get(1)) + b;
        return (y > 0 ? 0 : 1);
    }//end dpclassify() method
    
    /**
     * This method retrieve -1 for no and 1 for yes
     * @param v A <code>String</code> representing yes or no string
     * @return A <code>double</code> representing the yes no value
     */
    public static double yesno(String v){
        if(v.equalsIgnoreCase("yes"))return 1.0;
        else if(v.equalsIgnoreCase("no"))return -1.0;
        return 1.0;
    }//end yestno() method
    
    /**
     * This method retrieve the number of match interest between two person
     * @param interests1 A <code>String</code> representing the list of interest of person 1
     * @param interests2 A <code>String</code> representing the list of interest of person 2
     * @return A <code>double</code> representing the number of matches
     */
    public static double matchcount(String interests1, String interests2){
        String[] list1 = interests1.split(":");
        String[] list2 = interests2.split(":");
        double common = 0.0;
        for(int i = 0; i < list1.length; i++)
            for(int j = 0; j < list2.length; j++)
                if(list1[i].equalsIgnoreCase(list2[j]))common += 1.0;
        return common;
    }//end matchcount() method
    
    /**
     * This method retrieve a new rows after scaling the data
     * @param rows A <code>ArrayList</code> representing the rows to scale
     */
    public static ArrayList<MatchRow> scaledata(ArrayList<MatchRow> rows){
        
        ArrayList<MatchRow> newRows = new ArrayList<MatchRow>();
        
        double[] low = new double[rows.get(0).size()];
        for(int i = 0; i < low.length; i++)
            low[i] = Double.POSITIVE_INFINITY;
        
        double[] high = new double[rows.get(0).size()];
        for(int i = 0; i < high.length; i++)
            high[i] = Double.NEGATIVE_INFINITY;
        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            for(int j = 0; j < row.size(); j++){
                if(row.getIndex(j) > high[j])
                    high[j] = row.getIndex(j);
                if(row.getIndex(j) < low[j])
                    low[j] = row.getIndex(j);
            }//end for
        }//end for
        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            double[] values = new double[row.size()];
            for(int j = 0; j < row.size(); j++){
                values[j] = (row.getIndex(j) - low[j]) / (high[j] - low[j]);
            }//end for
            newRows.add(new MatchRow(values, row.match));
        }//end for
        
        return newRows;
        
    }//end scaledata() method
    
    public static double[] getDomainHigh(ArrayList<MatchRow> rows){
        ArrayList<MatchRow> newRows = new ArrayList<MatchRow>();
        
        double[] low = new double[rows.get(0).size()];
        for(int i = 0; i < low.length; i++)
            low[i] = Double.POSITIVE_INFINITY;
        
        double[] high = new double[rows.get(0).size()];
        for(int i = 0; i < high.length; i++)
            high[i] = Double.NEGATIVE_INFINITY;
        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            for(int j = 0; j < row.size(); j++){
                if(row.getIndex(j) > high[j])
                    high[j] = row.getIndex(j);
                if(row.getIndex(j) < low[j])
                    low[j] = row.getIndex(j);
            }//end for
        }//end for
        return high;
    }//end getHigh() method
    
    public static double[] getDomainLow(ArrayList<MatchRow> rows){
        ArrayList<MatchRow> newRows = new ArrayList<MatchRow>();
        
        double[] low = new double[rows.get(0).size()];
        for(int i = 0; i < low.length; i++)
            low[i] = Double.POSITIVE_INFINITY;
        
        double[] high = new double[rows.get(0).size()];
        for(int i = 0; i < high.length; i++)
            high[i] = Double.NEGATIVE_INFINITY;
        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            for(int j = 0; j < row.size(); j++){
                if(row.getIndex(j) > high[j])
                    high[j] = row.getIndex(j);
                if(row.getIndex(j) < low[j])
                    low[j] = row.getIndex(j);
            }//end for
        }//end for
        return low;
    }//end getDomainLow() method
    
    /**
     * This method apply a transform in a vector, so it will change
     * @param v1 A <code>MatchRow</code> representing the vector 1
     * @param v2 A <code>MatchRow</code> representing the vector 2
     * @param gamma A <code>double</code> representing how transformation will be applied
     * @return A <code>double</code> represneting some kind the size of the vector
     */
    public static double rbf(MatchRow v1, MatchRow v2, double gamma){
        double[] values = new double[v1.size()];
        for(int i = 0; i < v1.size(); i++){
            values[i] = v1.getIndex(i) - v2.getIndex(i);
        }//end for
        MatchRow dv = new MatchRow(values, 0);
        double l = veclength(dv);
        return Math.pow(Math.E, -gamma * l);
    }//end rbf() method
    
    /**
     * This method calculate the size of a vector
     * @param v A <code>MatchRow</code> representing the vector
     * @return A <code>double</code> representing its size
     */
    public static double veclength(MatchRow v){
        return Math.sqrt(dotproduct(v, v));
    }//end veclength() method
    
    /**
     * Thism method classify non-linear components
     * @param point A <code>MatchRow</code> representing the point to classify
     * @param rows A <code>ArrayList</code> representing all the other points
     * @param offset A <code>double</code> representing the offset
     * @param gamma A <code>double</code> representning transformation factor
     */
    public static double nlclassify(MatchRow point, ArrayList<MatchRow> rows, double offset, double gamma){
        
        double sum0 = 0.0;
        double sum1 = 0.0;
        double count0 = 0.0;
        double count1 = 0.0;        
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            if(row.match == 0){
                sum0 += rbf(point, row, gamma);
                count0++;
            }//end if
            else{
                sum1 += rbf(point, row, gamma);
                count1++;
            }//end else
        }//end for        
        double y = ((1.0 / count0) * sum0) - ((1.0 / count1) * sum1) + offset;
        return (y < 0 ? 0.0:1.0);
    }//end nlclassify() method
    
    public static double getoffset(ArrayList<MatchRow> rows, double gamma){
        ArrayList<MatchRow>l0 = new ArrayList<MatchRow>();
        ArrayList<MatchRow>l1 = new ArrayList<MatchRow>();
        for(int i = 0; i < rows.size(); i++){
            MatchRow row = rows.get(i);
            if(row.match() == 0)l0.add(row);
            else l1.add(row);            
        }//end for
        
        double sum0 = 0.0;        
        for(int i = 0; i < l0.size(); i++){
            for(int j = 0; j < l0.size(); j++){
                sum0 += rbf(l0.get(i), l0.get(j), gamma);
            }//end for
        }//end for
        
        double sum1 = 0.0;
        for(int i = 0; i < l1.size(); i++){
            for(int j = 0; j < l1.size(); j++){
                sum1 += rbf(l1.get(i), l1.get(j), gamma);
            }//end for
        }//end for
        
        return (1.0 / Math.pow(l1.size(),2)) * sum1 - (1.0 / Math.pow(l0.size(),2) * sum0);
        
    }//end getoffset() method
    
    /**
     * This method put an average and get the total error
     * @param rows A <code>ArrayList</code> representing all the data
     * @param avgs A<code>ArrayList</code> representing the averages
     * @return A <code>double</code> representing number of wrong predictions
     */
    public static double costfunction(ArrayList<MatchRow> rows, ArrayList<MatchRow> avgs){
        double error = 0.0;
        for(int i = 0; i < rows.size(); i++){
            MatchRow point = rows.get(i);
            double b = (dotproduct(avgs.get(1), avgs.get(1)) - dotproduct(avgs.get(0), avgs.get(0))) / 2.0;
            double y = dotproduct(point, avgs.get(0)) - dotproduct(point, avgs.get(1)) + b;
            error += Math.abs(point.match - (y > 0 ? 0:1));
        }//end for
        return error;
    }//end costfunction() method
    
    public static double[] costgamma(ArrayList<MatchRow>rows){        
        
        double bestCost = Double.MAX_VALUE;
        double bestGamma = 0.0;
        
        for(double i = 0.0; i < 100.0; i++){
            double error = 0.0;
            double offset = getoffset(rows, i);
            for(int r = 0; r < rows.size(); r++){
                MatchRow row = rows.get(r);
                double result = nlclassify(row, rows, offset, i);
                error += result;
            }//end for            
            if(error < bestCost){
                bestCost = error;
                bestGamma = i;
            }//end if                
            
        }//end for
        
        return new double[]{bestCost, bestGamma};
        
    }//end costgamma() method
    
    /**
     * This method define the best cost solution by using hill climbind idea
     * @param bestSolution A <code>int[]</code> representing the best find solution
     * @param domain A <code>int[][]</code> representing the domain of possible solutions
     * @param costFunction A <code>String</code> representing the cost function name
     * @return A <code>double</code> representing the cost
     */
    public static double hillClimbing(double[][] bestSolution, ArrayList<MatchRow>rows, int classes){
        
        double bestCost = Double.MAX_VALUE;                                     //consider worst case as best cost solution        
        double domain[][] = {getDomainLow(rows), getDomainHigh(rows)};          //get the domain        
        double[][] solution = new double[classes][domain[0].length];            //contains a random solution        
        
        
        /* create a random solution */
        for(int i = 0; i < classes; i++){
            for(int j = 0; j < domain[0].length; j++){
                solution[i][j] = domain[0][j] + (int)(Math.random() * (domain[1][j] - domain[0][j]));
            }//end for
        }//ennd for
        
        /* run up until stop to improve costs */
        while(true){
            
            double[][][]neighbor = new double[2][classes][solution[0].length];
            
            /* run over the solution to get the beighboord */
            for(int neigh = 0; neigh < 2;  neigh++){
                for(int c = 0; c < classes; c++){                
                    for(int i = 0; i < solution[c].length; i++){
                        /* neighbor at right */
                        if(solution[c][i] < domain[1][i])
                            neighbor[neigh][c][i] = solution[c][i] + 1;
                        /* neighbor at left */
                        if(solution[c][i] > domain[0][i])
                            neighbor[neigh][c][i] = solution[c][i] - 1;
                    }//end for
                }//end for
            }//end for
            
            
            /* get the cost for current */
            ArrayList<MatchRow>avgs = new ArrayList<MatchRow>();
            for(int c = 0; c < solution.length; c++)
                avgs.add(new MatchRow(solution[c], c));            
            double currentCost = costfunction(rows, avgs);
            bestCost = currentCost;
            
            /* get the cost for the left neighbors */
            ArrayList<MatchRow>avgsLeft = new ArrayList<MatchRow>();
            for(int c = 0; c < neighbor.length; c++)
                avgsLeft.add(new MatchRow(neighbor[0][c], c));            
            double leftCost = costfunction(rows, avgsLeft);
            
            /* get the cost for the left neighbors */
            ArrayList<MatchRow>avgsRight = new ArrayList<MatchRow>();
            for(int c = 0; c < neighbor.length; c++)
                avgsRight.add(new MatchRow(neighbor[1][c], c));            
            double rightCost = costfunction(rows, avgsRight);
            
            bestCost = Math.max(Math.max(leftCost, bestCost),currentCost);
            
            /* if cost was not improved in both neighbors */
            if(currentCost == bestCost)
                break;
            
        }//end while
        
        for(int c = 0; c < solution.length; c++)
            System.arraycopy(solution[c], 0, bestSolution[c], 0, solution[c].length);   //copy the best solution into array
        return bestCost;                                                                //return the best cost
        
    }//end hillClimbing
    
}//End MatchRow class