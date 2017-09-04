/*
 * Created on 21 October, 2008
 */

import java.net.URL;
import java.util.*;
import java.sql.*;

/**
 *
 * @author giscardf
 */
public class MainApp {

    public static String base = "D:/eBooks/java/java-tutorial/tutorial/";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, Throwable {
        
        /* create the seed pages to start looking for */
//        HashSet<String>webpages = new HashSet<String>();
//        webpages.add(base + "index.html");
//        webpages.add(base + "reallybigindex.html");
//        webpages.add(base + "search.html");
//        HashMap<Integer,Double> queryResult = null;
//        Crawler crawler = new Crawler();
//        crawler.calculatePageRank(2);
//        crawler.craw(webpages, 2);
//        NeuralNetwork neural = new NeuralNetwork();
//        
//        
//        /* first, score by frequency */        
//        System.out.println("===== FREQUENCY SCORE =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"frequencyScore"}, new double[]{1.0}, new boolean[]{false});
//        
//        /* second, score by location */
//        System.out.println("===== LOCATION SCORE =====");
//        queryResult = crawler.query("Linux", new String[]{"locationScore"}, new double[]{1.0}, new boolean[]{true});
//        
//        /* third, score by distance */
//        System.out.println("===== DISTANCE SCORE =====");
//        queryResult = crawler.query("Linux", new String[]{"distanceScore"}, new double[]{1.0}, new boolean[]{true});
//        
//        /* third, score by page rank */
//        System.out.println("===== PAGE RANK SCORE =====");
//        queryResult = crawler.query("Linux", new String[]{"pageRankScore"}, new double[]{1.0}, new boolean[]{false});
//        
//        /* third, score by page rank */
//        System.out.println("===== LINK TEXT SCORE =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"linktextScore"}, new double[]{1.0}, new boolean[]{false});
//
//
//        /* fourth, score by document size */
//        System.out.println("===== DOCUMENT SIZE SCORE =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"documentSizeScore"}, new double[]{1.0}, new boolean[]{false});
        
//        /* fifth, score by word bias */
//        System.out.println("===== WORD BIAS SCORE =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"wordFrequencyBiasScore"}, new double[]{1.0}, new boolean[]{false});
        
//        /* sixth, score by exact match */
//        System.out.println("===== EXACT MATCH SCORE =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"exactMatchScore"}, new double[]{1.0}, new boolean[]{false});
//        
//        /* seventh, score by using complex searches */
//        System.out.println("===== COMPLEX SERACH SCORE =====");
//        queryResult = crawler.complexSearch("(Hello AND (Linux OR Java))");
//        
//        /* eighth, score by location and distance */
//        System.out.println("===== ALL =====");
//        queryResult = crawler.query("Hello Linux", new String[]{"frequencyScore", "locationScore", "distanceScore", "pageRankScore", "linktextScore"}, new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, new boolean[]{false, true, true, false, false});
//        
//        crawler.printResult(queryResult);
//
//        /* nineth, score by neural network */
//        ArrayList<Integer>wordIds = new ArrayList<Integer>();        
//        String[] words = "Hello Linux".split(" ");
//        for(String word : words)
//            wordIds.add(crawler.getEntryId("WordList", "word", word));
//        
//        ArrayList<Integer>urlIds = new ArrayList<Integer>();
//        for(Iterator<Integer>iterator = queryResult.values().iterator(); iterator.hasNext();)
//            urlIds.add(iterator.next());
//        
//        neural.trainQuery(wordIds, urlIds, new ArrayList<Integer>());  
//        HashMap<Integer,Double> neuralScore = crawler.normalizeScores(neural.getResult(urlIds), false);
//        SortedMap<Double,Integer>sortedScore = crawler.sortMap(neuralScore);
//        System.out.println("===== NEURAL NETWORK RESULT ====");
//        for(Iterator<Double>iterator = sortedScore.keySet().iterator(); iterator.hasNext();){
//            double score = iterator.next();
//            int urlId = sortedScore.get(score);
//            System.out.println("Webpage: " + crawler.getUrlName(urlId) + " was scored as " + score);
//        }//end for        
        
        /*
         * Training the Neural Network K times
         */
        for(int k = 0; k < 1000; k++){
        
            try{
                
                Crawler crawler = new Crawler();
                
                /* get a random word id */
                ArrayList<Integer> wordIds = new ArrayList<Integer>();
                for(int i = 0; i < ((k % 1) + 1); i ++){
                    wordIds.add((int)(Math.random() * 1000 + 1));
                }//end for

                /* perform the search based on the word ids */        
                String search = crawler.getWords(wordIds);
                HashMap<Integer,Double> result = crawler.query(search, new String[]{"frequencyScore", "locationScore", "distanceScore", "pageRankScore", "linktextScore"}, new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, new boolean[]{false, true, true, false, false});
                ArrayList<Integer> urlIds = new ArrayList<Integer>(result.keySet());
                Collections.reverse(urlIds);

                /* select the url of the search */
                ArrayList<Integer>selectedUrls = new ArrayList<Integer>();
                for(int i = 0; i < urlIds.size(); i++){
                    Integer urlId = urlIds.get(i);
                    /* let's consider the user has 60% of change to pick a url - the toppest url will be more clicked */
                    if((int)(Math.random() * 99 + 1) >= 60){
                        selectedUrls.add(urlId);
                        break;
                    }//end if
                    /* let's consider the user has 25% of chance to score a url - uncomment to run using ranking */
//                    if((int)(Math.random() * 99 + 1) >= 25){
//                        selectedUrls.add(urlId);
//                        break;
//                    }//end if
                    
                }//end for

                if(selectedUrls.size() != 0){
                    crawler.close();
                    NeuralNetwork neural = new NeuralNetwork();
                    neural.trainQuery(wordIds, new ArrayList<Integer>(result.keySet()), selectedUrls);
                    neural.close();
                }//end if
                
                System.out.println("SEARCH[" + k + "]: " + search + ", picked url " + selectedUrls.toString());
                
            }//end try
            catch(Exception ex){
                System.out.println("SERACH[" + k + "]: FAILED, " + ex.getMessage());
                continue;
            }
            
        }//end for
        
    }//End main() method

}//End MainApp class
