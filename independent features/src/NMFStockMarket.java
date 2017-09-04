/*
 * Created on 21 December 2008
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author giscardf
 */
public class NMFStockMarket extends NMF {

    public MMatrix loaddata(String file) throws FileNotFoundException, IOException {        
        /* first parse the file to get quantity of companies and days */
        HashSet<String> days = new HashSet<String>();
        HashSet<String> company = new HashSet<String>();        
        BufferedReader input = new BufferedReader(new FileReader(file));
        String line = input.readLine();
        while(line != null){
            if(line.startsWith("01") == false || line.indexOf("PN") < 0){
                line = input.readLine();
                continue;
            }//end if
            days.add(line.substring(2, 10).trim());
            company.add(line.substring(27, 39).trim());
            line = input.readLine();
        }//end while
        input.close();        
        /* second parse the file to get the volume */        
        MMatrix datamatrix = new MMatrix(company.toArray(new String[0]), days.toArray(new String[0]));        
        input = new BufferedReader(new FileReader(file));
        line = input.readLine();
        while(line != null){
            if(line.startsWith("01") == false || line.indexOf("PN") < 0){
                line = input.readLine();
                continue;
            }//end if
            double volume = Double.parseDouble(line.substring(170, 188)) / 100.0;
            datamatrix.setValue(line.substring(27, 39).trim(), line.substring(2, 10).trim(), volume);
            line = input.readLine();
        }//end while
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
            System.out.println("=== Feature " + i);
            int topw = 10;            
            for(Iterator<Double>iterator = wmap.descendingKeySet().iterator(); iterator.hasNext();){
                double volume = iterator.next();
                System.out.println(m.getRowName(wmap.get(volume)) + "-> " + volume);                
                if(--topw == 0)break;
            }//end for
            int toph = 10;            
            for(Iterator<Double>iterator = hmap.descendingKeySet().iterator(); iterator.hasNext();){
                double volume = iterator.next();
                System.out.println(m.getColumnName(hmap.get(volume)) + "-> " + volume);                
                if(--toph == 0)break;
            }//end for
        }//end for
        
        return "";
    }//end dispplayResult() method
    
}//End NMFStockMarket class
