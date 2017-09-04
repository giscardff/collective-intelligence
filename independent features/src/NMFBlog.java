/*
 * Created on 21 December 2008
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author giscardf
 */
public class NMFBlog extends NMF {

    public MMatrix loaddata(String file) throws FileNotFoundException, IOException {        
        /* first parse the file to get quantity of companies and days */
        HashSet<String> words = new HashSet<String>();
        HashSet<String> blogs = new HashSet<String>();        
        BufferedReader input = new BufferedReader(new FileReader(file));
        
        /* first let's read the columns and rows names */
        String line = input.readLine();
        String columns[] = line.split("\t");
        for(int i = 1; i < columns.length; i++)
            words.add(columns[i]);     
        line = input.readLine();
        while(line != null){
            columns = line.split("\t");
            blogs.add(columns[0]);
            line = input.readLine();
        }//end while
        input.close();        
        
        /* second parse the file to get the volume */        
        MMatrix datamatrix = new MMatrix(blogs.toArray(new String[0]), words.toArray(new String[0]));        
        input = new BufferedReader(new FileReader(file));
        line = input.readLine();                                                //first line will be discarded
        line = input.readLine();                                                //first line will be discarded
        for(int i = 0; i < datamatrix.getRowSize(); i++){
            columns = line.split("\t");
            for(int j = 1; j < columns.length; j++){
                datamatrix.setValue(i, j - 1, Double.parseDouble(columns[j]));                
            }//end for
            line = input.readLine();                                                //first line will be discarded
        }//end for
        input.close();
        return datamatrix;
    }//end loaddata() method
    
    public String displayResult(MMatrix m, MMatrix w, MMatrix h){
        /* let's get the companies the feature applies to */        
        for(int i = 0; i < w.getColumnSize(); i++){
            TreeMap<Double,Integer> wmap = new TreeMap<Double,Integer>();
            TreeMap<Double,Integer> hmap = new TreeMap<Double,Integer>();
            for(int j = 0; j < w.getRowSize(); j++){
                wmap.put(w.getValue(j, i), j);
            }//end for
            for(int j = 0; j < h.getColumnSize(); j++){
                hmap.put(h.getValue(i, j), j);
            }//end for
            System.out.println("====== Feature " + i + "======");
            int topw = 10;            
            System.out.println("=== Blogs x Feature");            
            for(Iterator<Double>iterator = wmap.descendingKeySet().iterator(); iterator.hasNext();){
                double volume = iterator.next();
                System.out.println(m.getRowName(wmap.get(volume)) + "-> " + volume);                
                if(--topw == 0)break;
            }//end for
            int toph = 10;            
            System.out.println("=== Words x Feature");
            for(Iterator<Double>iterator = hmap.descendingKeySet().iterator(); iterator.hasNext();){
                double volume = iterator.next();
                System.out.println(m.getColumnName(hmap.get(volume)) + "-> " + volume);                
                if(--toph == 0)break;
            }//end for
        }//end for        
        return "";
    }//end dispplayResult() method
    
}//End NMFBlog class
