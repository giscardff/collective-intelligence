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
public class Crawler {
    
    /* define some html parser objects */
    private HtmlCleaner htmlParser;
    private CleanerProperties props;
    
    /* define database connection objects */
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    /* Create the list of words to ignore */
    private static HashSet<String>ignoreWords = new HashSet<String>();    
    static{
        ignoreWords.add("the");
        ignoreWords.add("of");
        ignoreWords.add("to");
        ignoreWords.add("and");
        ignoreWords.add("a");
        ignoreWords.add("in");
        ignoreWords.add("is");
        ignoreWords.add("it");
    }//end static method
    
    public Crawler(){
        
        /* create the html parser objects */
        htmlParser = new HtmlCleaner();
        props = htmlParser.getProperties();
        
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
        
    }//end Crawler constructor
    
    /**
     * This method check if a word is valid or not for the index
     * @param word A <code>String</code> representing the word to be validated
     * @return A <code>boolean</code> representing if the word is valid or not
     */
    public boolean isValidWord(String word){
        
        /* do not add empty or one letter words */
        if(word.length() < 2)
            return false;
        /* do not add words in the list to be ignored */
        if(ignoreWords.contains(word))
            return false;
                
        return true;
        
    }//end isValidWord() method
    
    /**
     * This method get the word based on its ID
     * @param wordIds A <code>ArrayList</code> representing the words
     * @return A <code>String</code> representing the words spearate by space
     * @throws java.sql.SQLException
     */
    public String getWords(ArrayList<Integer>wordIds) throws SQLException {
        
        String words = "";
        String sqlQuery = "SELECT word FROM WordList WHERE ";        
        for(int i = 0; i < wordIds.size(); i++){
            sqlQuery += "rowId=" + wordIds.get(i) + " OR ";
        }//end for
        sqlQuery += "rowId=0";
        
        rs = stmt.executeQuery(sqlQuery);
        
        while(rs.next())
            words += rs.getString(1) + " ";
        
        return words.trim();
        
    }//end getWords() method
    
    /**
     * This method check if a table has a entry
     * @param table A <code>String</code> representing the table to be checked
     * @param column A <code>String</code> representing the column name
     * @param value A <code>String</code> representing the value to index
     * @return A <code>int</code> representing the value indexed
     */
    public int getEntryId(String table, String column, String value) throws SQLException {
        
        rs = stmt.executeQuery("SELECT rowid FROM " + table + " WHERE " + column + "=\'" + value + "\'");       //check if webpage was indexed
        
        /* if the entry does not exist, add it */
        if(rs.next() != true){
            stmt.execute("INSERT INTO " + table + " (" + column + ") VALUES (\'" + value + "\')");              //insert the entry
            rs = stmt.executeQuery("SELECT rowid FROM " + table + " WHERE " + column + "=\'" + value + "\'");   //get the row id created
            rs.next();
        }//end if          
        
        return rs.getInt(1);                                                //return the row id
        
    }//end getEntryId() method
    
    /**
     * This method index a webpage by parsing all this text content
     * @param webpage A <code>String</code> representing the webpage url
     * @param text A <code>text</code> representing the webpage text content
     */
    public void addToIndex(String webpage, String text) throws SQLException {
        
        /* check if page was already indexed */
        if(this.isIndexed(webpage)){
            System.out.println("Page " + webpage + " already indexed");
            return;
        }//end if
        
        String[] tokens = this.separateWords(text);                             //get all words of the document
        int urlId = this.getEntryId("UrlList", "Url", webpage);                 //index the webpage
        System.out.println("Indexing page: " + webpage);
        
        /* run over each word and index it */
        for(int i = 0; i < tokens.length; i++){            
            /* check if the word can be added */
            if(this.isValidWord(tokens[i]) == false)
                continue;            
            /* index the word into the database */
            int wordId = this.getEntryId("WordList", "word", tokens[i]);                        
            stmt.execute("INSERT INTO WordLocation (urlId, wordId, location) VALUES (" + urlId + "," + wordId + "," + i + ")");            
        }//end for
        
    }//end addToIndex() method
    
    /**
     * This method get a html page content and split all words from the whole text
     * @param text A <code>String</code> representing the html text content (not including tags)
     * @return A <code>String[]</code> containing the list of word tokens
     */
    public String[] separateWords(String text){        
        return text.split("\\W");
    }//end separateWords() method
    
    /**
     * This method check if a webpage was already indexed
     * @param webpage A <code>String</code> representing the webpage to be chekced
     * @return A <code>boolean</code> defining if the webpage was indexed or not
     */
    public boolean isIndexed(String webpage) throws SQLException {        
        rs = stmt.executeQuery("SELECT rowid FROM UrlList WHERE url=\'" + webpage + "\'");      //check if webpage was indexed            
        if(rs.next() == true){
            rs = stmt.executeQuery("SELECT * FROM WordLocation WHERE urlId=" + rs.getInt(1));   //check if webpage has words indexed
            if(rs.next() == true)
                return true;                                                                    //return true
        }//end if
        return false;                                                                           //return false        
    }//end isIndexed() method
    
    /**
     * This method create a reference between two webpages
     * @param webpageFrom A <code>String</code> representing the webpage whom is pointing to a link
     * @param webpageTo A <code>String</code> representing the webpage whom is being referenced
     * @param text A <code>String</code> representing the source page text content
     */
    public void addLinkRef(String webpageFrom, String webpageTo, String text) throws SQLException {
                
        String[] linkWords = text.split("\\W");                                 //link may have more than one words
        int fromId = this.getEntryId("UrlList", "Url", webpageFrom);            //get the webpage from id from the database
        int toId = this.getEntryId("UrlList", "Url", webpageTo);                //get the webpage to id from the database
        
        /* add link information into the database */
        stmt.execute("INSERT INTO PageLink (fromId,toId) VALUES (" + fromId + "," + toId + ")");        
        /* run over each word of the link */
        for(int i = 0; i < linkWords.length; i++){            
            /* check if the word can be added */
            if(this.isValidWord(linkWords[i]) == false)
                continue;            
            int wordId = this.getEntryId("WordList", "Word", linkWords[i]);     //get the word id into the database                       
            /* get the link id and add bind the word to the link into the database */
            rs = stmt.executeQuery("SELECT rowId FROM PageLink WHERE FromId=" + fromId + " AND toId=" + toId);                    
            if(rs.next()){
                int linkId = rs.getInt(1);                                                      //get the link id
                stmt.execute("INSERT INTO LinkWord VALUES (" + wordId + "," + linkId + ")");    //added to the database
            }//end if            
        }//end for
        
    }//end addLinkRef() method
    
    /**
     * This method get all links that contains the word id
     * @param wordIds A <code>HashSet</code> representing the words to search
     * @return A <code>ResultSet</code> representing the query result
     * @throws java.sql.SQLException
     */
    public ResultSet getLinktextRows(HashSet<Integer>wordIds) throws SQLException {
        
        Statement stmtx = conn.createStatement();                                                   //create the statement
        String sqlQuery = "SELECT fromId, toId FROM PageLink INNER JOIN LinkWord ON PageLink.rowId=LinkWord.linkId WHERE ";     //sql query
        
        /* run over each word to find */
        for(Iterator<Integer>iterator = wordIds.iterator(); iterator.hasNext();){
            sqlQuery += "wordId=" + iterator.next() + " OR ";
        }//end for
        sqlQuery += "wordId=0";
        
        ResultSet rows = stmtx.executeQuery(sqlQuery);                          //perform the query
        return rows;                                                            //return the query result
        
    }//end getLinktextRows() method
    
    /**
     * This method get all urls (and their word location)
     * @param wordIds A <code>HashSet</code> representing the word  ids the url must contain
     * @return A <code>ResultSet</code> representing the query result
     * @throws java.sql.SQLException
     */
    public ResultSet getDocumentSizeRows(HashSet<Integer>wordIds) throws SQLException {
        
        /* create and execute sql query and get all urlIds that contains the word */
        Statement stmtx = conn.createStatement();        
        String sqlQuery = "SELECT UrlId, MAX(Location),COUNT(wordId) FROM WordLocation WHERE ";        
        for(Iterator<Integer>iterator = wordIds.iterator(); iterator.hasNext();){
            sqlQuery += "wordId=" + iterator.next() + " OR ";
        }//end for
        sqlQuery += "wordId=0 GROUP BY UrlId";        
        ResultSet rows = stmtx.executeQuery(sqlQuery);
        
        return rows;
        
    }//end getDocumentSizeRows() method
    
    /**
     * This method get all urls (and their word location)
     * @param wordIds A <code>HashSet</code> representing the word  ids the url must contain
     * @return A <code>ResultSet</code> representing the query result
     * @throws java.sql.SQLException
     */
    public ResultSet getWordLocationRows(HashSet<Integer>wordIds) throws SQLException {
        
        /* create and execute sql query and get all urlIds that contains the word */
        Statement stmtx = conn.createStatement();        
        String sqlQuery = "SELECT UrlId, Location, wordId FROM WordLocation WHERE ";        
        for(Iterator<Integer>iterator = wordIds.iterator(); iterator.hasNext();){
            sqlQuery += "wordId=" + iterator.next() + " OR ";
        }//end for
        sqlQuery += "wordId=0";        
        ResultSet rows = stmtx.executeQuery(sqlQuery);
        
        return rows;
        
    }//end getDocumentSizeRows() method
    
    
    /**
     * This method get all url page rank values
     * @param wordIds A <code>wordIds</code> representing the words to looking for
     * @return A <code>ResultSet</code> representing the query result
     * @throws java.sql.SQLException
     */
    public ResultSet getRankedRows(HashSet<Integer> wordIds) throws SQLException {
        
        Statement stmtx = conn.createStatement();
        String sqlQuery = "SELECT DISTINCT(PageRank.urlId),score FROM PageRank INNER JOIN WordLocation ON PageRank.urlId=WordLocation.urlId WHERE ";
        
        /* run over each word to find */
        for(Iterator<Integer>iterator = wordIds.iterator(); iterator.hasNext();){
            sqlQuery += "wordId=" + iterator.next() + " OR ";
        }//end for
        sqlQuery += "wordId=0";
        
        ResultSet rows = stmtx.executeQuery(sqlQuery);                
        return rows;
        
    }//End getRankedRows() method
    
    /**
     * This method get all url that match all words in the search string
     * @param searchString A <code>String</code> representing the search string
     * @return A <code>HashSet</code> to populate with all word ids
     */
    public ResultSet getMatchRows(String searchString, HashSet<Integer>wordIds) throws SQLException {
        
        String[] words = searchString.split(" ");                               //get all words in the search string
        String tables = "";                                                     //list of tables to search
        String fields = "w0.urlId";                                             //list of fields to search
        String clauses = "";                                                    //list of clauses to search
        int tableCounter = 0;                                                   //table index
        
        /* run over all words to create the final query */
        for(int i = 0; i < words.length; i++){
            rs = stmt.executeQuery("SELECT rowId FROM wordList WHERE word=\'" + words[i] + "\'");
            /* if found the row id */
            if(rs.next() == true){
                wordIds.add(rs.getInt(1));                                  //add word id to the list
                /* if has more than one tables let's put all SQL separators */
                if(tableCounter > 0){
                    tables += ",";
                    clauses += " AND w" + (tableCounter - 1) + ".urlId=w" + tableCounter + ".urlId AND ";
                }//end if
                fields += ",w" + tableCounter + ".location";
                tables += "wordLocation w" + tableCounter;
                clauses += "w" + tableCounter + ".wordId=" + rs.getInt(1);
                tableCounter++;
            }//end if
        }//end for
        
        rs = stmt.executeQuery("SELECT " + fields + " FROM " + tables + " WHERE " + clauses);   //perform the final query
        
        return rs;                                                                              //return the result
        
    }//end getMatchRows() method
    
    /**
     * This method invokes dinamically one type of similarity metnod based on methodName param
     * @param rows A <code>ResultSet</code> representing the result of a query
     * @param methodName A <code>String</code> representing the method name
     * @return A <code>double</code> representing how far is one person from another
     */
    public HashMap<Integer,Double> scoreMethod(ResultSet rows, String methodName){
        
        /* defining the variables to invoke similarity method */
        Class[] parameters = {ResultSet.class};                                 //An array for all attributes to invoke the method dynamically
        Method compareMethod = null;                                            //The method to be invoked dynamically
        
        /*
         * Getting the method name and invoking it
         */
        try{
            compareMethod = this.getClass().getMethod(methodName, parameters);          //get the method name using reflection
            HashMap<Integer,Double> score = (HashMap)compareMethod.invoke(this, rows);  //invoke the method to compare
            return score;                                                               //return the comparison result
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
        
        return null;
        
    }//end scoreMethod() method
    
    /**
     * This method retrieve the WebPange name for a given id
     * @param id A <code>int</code> representing the webpage id
     * @return A <code>String</code> representing the webpage name
     * @throws java.sql.SQLException if something goes wrong during the query
     */
    public String getUrlName(int id) throws SQLException {
        
        rs = stmt.executeQuery("SELECT url FROM UrlList WHERE rowId=" + id);
        /* check if the url was found */
        if(rs.next())
            return rs.getString(1);
        
        return null;
        
    }//end getUrlName() method
    
    public SortedMap<Double,Integer> sortMap(HashMap<Integer,Double>dataset){
                
        TreeMap<Double,Integer> sortedMap = new TreeMap<Double,Integer>();      //contains the map in a sorted way
        
        /* run over the dataset */
        for(Iterator<Integer>iterator = dataset.keySet().iterator(); iterator.hasNext();){
            int id = iterator.next();                                           //get current element
            double score = dataset.get(id);                                     //get element score
            /*if already exist this score into the sorted map, increment it by one */
            while(sortedMap.containsKey(score))
                score += 0.00001;
            /* at this point we will not override any existing element */
            sortedMap.put(score, id);                                           //add the score with its id
        }//end for
        
        return sortedMap;                                                       //return the sorted map
        
    }//end sortMap() method
    
    /**
     * This method score the webpages by their importance to the user
     * @param rows a <code>ResultSet</code> containing all matched webpages
     * @param rankRows A <code>ResultSet</code> containing all ranked webpages
     * @param docmentSizeRows A <code>ResultSet</code> containing all rows
     * @param wordLocationRows A <code>ResultSet</code> containing all rows
     * @param wordIds A <code>HashSet</code> containing the word ids of the search
     * @return A <code>HashMap</code> representing the score by url id
     * @throws java.sql.SQLException if something goes wrong during score operation
     */
    public HashMap<Integer,Double> getScoredList(ResultSet rows, ResultSet rankRows, ResultSet linkTextRows, ResultSet documentSizeRows, ResultSet wordLocationRows, HashSet<Integer>wordIds, String[] scoreMethods, double[] weights, boolean[] smallIsBetter) throws SQLException {
        
        HashMap<Integer,Double> scores = null;                                  //list of urlIds scored
        HashMap<Integer,Double> finalScores = new HashMap<Integer,Double>();    //list of urlIds scored (final result)
        
        /* run over all weights */
        for(int i = 0; i < weights.length; i++){
            
            if(scoreMethods[i].equalsIgnoreCase("pagerankScore")){
                scores = this.pagerankScore(rankRows);
            }//end if
            else if(scoreMethods[i].equalsIgnoreCase("linktextScore")){
                scores = this.linktextScore(linkTextRows, wordIds);
            }//end if
            else if(scoreMethods[i].equalsIgnoreCase("documentSizeScore")){
                scores = this.documentSizeScore(documentSizeRows, wordIds);
            }//end for
            else if(scoreMethods[i].equalsIgnoreCase("wordFrequencyBiasScore")){
                scores = this.wordFrequencyBiasScore(documentSizeRows, wordIds);
            }//end if
            else if(scoreMethods[i].equalsIgnoreCase("exactMatchScore")){
                scores = this.exactMatchScore(wordLocationRows, wordIds);
            }//end if
            else{
                rows.first();                                                   //rewind the result set
                scores = this.scoreMethod(rows, scoreMethods[i]);               //get the webpage score                
            }//end else
            
            scores = this.normalizeScores(scores, smallIsBetter[i]);            //normalize the score
            
            /* run over the final scores in order to set it */
            for(Iterator<Integer>iterator = scores.keySet().iterator(); iterator.hasNext();){
                Integer urlId = iterator.next();
                /* merge the score to the final one */
                if(finalScores.containsKey(urlId)){
                    finalScores.put(urlId, finalScores.get(urlId) + scores.get(urlId) * weights[i]);
                }//end if
                else{
                    finalScores.put(urlId, scores.get(urlId) * weights[i]);
                }//end else
            }//end for
            
        }//end for
        
        return finalScores;
        
    }//end getScoredList() method
    
    public HashMap<Integer,Double> normalizeScores(HashMap<Integer,Double>scores, boolean smallIsBetter){
        
        HashMap<Integer,Double> normalScores = new HashMap<Integer,Double>();   //contains the score normalized
        double vsmall = 0.00001;                                                //avoid division by zero
        
        /* do not normalize empty scores */
        if(scores == null || scores.size() == 0)
            return scores;
        
        /* if small is better */
        if(smallIsBetter){
            double minScore = Collections.min(scores.values());                                 //get the lower score
            /* run over the dataset to normalize it */
            for(Iterator<Integer>iterator = scores.keySet().iterator(); iterator.hasNext();){                                
                int urlId = iterator.next();
                normalScores.put(urlId, (minScore / Math.max(vsmall, scores.get(urlId))));                
            }//end for
        }//end if
        else{
            double maxScore = Collections.max(scores.values());                                 //get the higher score
            /* if max score is 0, let's change it to a very small value */
            if(maxScore == 0)
                maxScore = vsmall;
            /* run over the dataset to normalize it */
            for(Iterator<Integer>iterator = scores.keySet().iterator(); iterator.hasNext();){                                
                int urlId = iterator.next();
                normalScores.put(urlId,  (scores.get(urlId) / maxScore));                
            }//end for
        }//end else
        
        return normalScores;
        
    }//end normalizeScores() method
    
    
    /**
     * This method run over the SQL result and score url based on word frequency
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> frequencyScore(ResultSet rows) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the socres
        
        /* count the number of words */
        while(rows.next()){                
            int urlId = rows.getInt(1);
            if(scores.containsKey(urlId))
                scores.put(urlId, scores.get(urlId) + 1);
            else
                scores.put(urlId, 1.0);
        }//end while
        
        return scores;
        
    }//end frequencyScore() method
    
    /**
     * This method run over the SQL result and score url based on word location
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> locationScore(ResultSet rows) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores                
        ResultSetMetaData rsMd = rows.getMetaData();                            //get the number of columns of the result set        
        
        /* sum the word locations */
        while(rows.next()){            
            int urlId = rows.getInt(1);
            double wordLocation = rows.getInt(2);
            for(int i = 3; i <= rsMd.getColumnCount(); i++){                
                wordLocation += rows.getInt(i);                
            }//end for
            /* always save the lower distance */
            if(scores.containsKey(urlId)){
                if(scores.get(urlId) > wordLocation)
                    scores.put(urlId, wordLocation);
            }//end if
            else
                scores.put(urlId, wordLocation);
        }//end while
        
        return scores;
        
    }//end locationScore() method
    
    /**
     * This method run over the SQL result and score url based on word distance
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> distanceScore(ResultSet rows) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores                
        ResultSetMetaData rsMd = rows.getMetaData();                            //get the number of columns of the result set        
        
        /* sum the word locations */
        while(rows.next()){            
            int urlId = rows.getInt(1);
            double wordDistance = 1.0;
            for(int i = 2; i <= rsMd.getColumnCount(); i++){                
                /* sum the distance between the words */
                if((i + 1) <= rsMd.getColumnCount())
                    wordDistance += Math.abs(rows.getInt(i) - rows.getInt(i + 1));
            }//end for
            /* always save the lower distance */
            if(scores.containsKey(urlId)){
                if(scores.get(urlId) > wordDistance)
                    scores.put(urlId, wordDistance);
            }//end if
            else
                scores.put(urlId, wordDistance);
        }//end while
        
        return scores;
        
    }//end distanceScore() method
    
    /**
     * This method run over the SQL result and score url based on page rank
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> pagerankScore(ResultSet rows) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores
        
        /* run over all rows */
        while(rows.next()){
            int urlId = rows.getInt(1);                                         //get the url id
            double score = rows.getDouble(2);                                   //get the url score
            scores.put(urlId, score);                                           //put into the dataset
        }//end while
        
        return scores;
        
    }//end pagerankScore() method
    
    /**
     * This method run over the SQL result and score url based on link text
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @param wordIds A <code>HashMap</code> representing the word ids to search
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> linktextScore(ResultSet rows, HashSet<Integer>wordIds) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores
        Statement stmtx = conn.createStatement();                               //create statement
        
        /* run over all rows */
        while(rows.next()){
            int fromId = rows.getInt(1);
            int toId = rows.getInt(2);
            
            /* get the score of the from page */
            ResultSet scoreRow = stmtx.executeQuery("SELECT score FROM PageRank WHERE urlId=" + fromId);
            if(scoreRow.next() == false)
                continue;
            double score = scoreRow.getDouble(1);
            
            /* update the score dataset */
            if(scores.containsKey(toId)){
                scores.put(toId, scores.get(toId) + score);
            }//end if
            else{
                scores.put(toId, score);
            }//end else
        }//end while
        
        return scores;
        
    }//end linktextScore() method
    
    /**
     * This method run over the SQL result and score url based on quantity of words
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @param wordIds A <code>HashSet</code> representing the word ids to search
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> documentSizeScore(ResultSet rows, HashSet<Integer>wordIds) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores
        
        /* run over all rows */
        while(rows.next()){
            int urlId = rows.getInt(1);
            double location = (double)rows.getInt(2);
            scores.put(urlId, location);            
        }//end while
        
        return scores;
        
    }//end documentSizeScore() method
    
    /**
     * This method run over the SQL result and score url based on quantity of word and page size
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @param wordIds A <code>HashSet</code> representing the word ids to search
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> wordFrequencyBiasScore(ResultSet rows, HashSet<Integer>wirdIds) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores
        
        /* run over all rows */
        while(rows.next()){
            int urlId = rows.getInt(1);
            double location = (double)rows.getInt(2);
            double wordCount = (double)rows.getInt(3);
            scores.put(urlId, wordCount / location);            
        }//end while
        
        return scores;
        
    }//end wordFrequencyScore() method
    
    /**
     * This method run over the SQL result and score url based on exact word locations
     * @param rows A <code>ResultSet</code> representing the SQL query result
     * @param wordIds A <code>HashSet</code> representing the word ids to search
     * @return A <code>HashMap</code> representing the dataset with the scores
     * @throws java.sql.SQLException     
     */
    public HashMap<Integer,Double> exactMatchScore(ResultSet rows, HashSet<Integer>wordIds) throws SQLException {
        
        HashMap<Integer,Double>scores = new HashMap<Integer,Double>();          //dataset with the scores
        Integer[] words = wordIds.toArray(new Integer[0]);
        
        /* run over all rows */
        while(rows.next()){
            int wordId = rows.getInt(3);
            int location = rows.getInt(2);
            int urlId = rows.getInt(1);
            
            /* if match the first word */
            if(wordId == words[0]){                
                for(int i = 1; i < words.length; i++){
                    /* check if does have more rows */
                    if(rows.next() == false)
                        break;
                    
                    /* check if the word exactly the sequence */
                    if((rows.getInt(3) != words[i]) || (rows.getInt(2) != location + i))
                        break;
                    
                    /* there is still more words to check */
                    if(i != (words.length - 1))
                        continue;
                    
                    /* update the score dataset */
                    if(scores.containsKey(urlId)){
                        scores.put(urlId, scores.get(urlId) + 1.0);             //increment the exact match
                    }//end if
                    else{
                        scores.put(urlId, 1.0);                                 //set the first exact match
                    }//end else
                }//end for
            }//end if
        }//end while
        
        return scores;
        
    }//end exactMatchScore() method
    
    /**
     * This method get a list of webpages and grab the amount of words in
     * each page. Also it keeps going depth based on how much deep it is desirable
     * to be.
     * @param webpages A <code>HashSet</code> representing a list of webpages to be parsed
     * @param depth A <code>int</code> representing the maximum depth when parsing webpages
     */
    public void craw(HashSet<String>webpages, int depth) throws SQLException {
        
        /* Defining the variables */
        HashSet<String>linkedPages = new HashSet<String>();                     //will keep all link found during webpage parsing
        
        /* keep getting page link up until depth */
        for(int i = 0; i < depth; i++){
            
            /* for each page (or found link) craw it */
            for(Iterator<String>iterator = webpages.iterator(); iterator.hasNext();){
                String webpage = iterator.next();
                
                try{                    
                    TagNode root = htmlParser.clean(new File(webpage));         //get the root element of html DOM
                    StringBuffer htmlText = root.getText();                     //get the text of the all subelements
                    this.addToIndex(webpage, htmlText.toString());              //index the webpage and its content
                    
                    /*
                     * Let's get all link this webpage refers to, and parse them
                     */                    
                    TagNode[] links = root.getElementsByName("a", true);        //get all <a> or link elements
                    /* run over all links */
                    for(int j = 0; j < links.length; j++){                                                
                        String linkRef = links[j].getAttributeByName("href");   // get the href attribute
                        String linkText = links[j].getText().toString();        //get the link text                        
                        /* check if the link reference is not null and contains a valid link */
                        if(linkRef == null)continue;                            //do not get reference for unkonwn links
                        if(linkRef.indexOf("#") >= 0)continue;                  //do not get reference as link
                        else if(linkRef.startsWith("http://"))continue;         //do not get reference to the internet
                        if(this.isIndexed(linkRef))continue;                    //do not get already indexed webpage                        
                        linkedPages.add(MainApp.base + linkRef);                    //add the link for the next search
                        this.addLinkRef(webpage, MainApp.base + linkRef, linkText); //create the reference dataset
                    }//end for                    
                }//end catch
                catch(IOException ioe){
                    System.out.println(ioe.getMessage());
                    continue;
                }//end catch 
            }//end for
            webpages = (HashSet)linkedPages.clone();                            //copy all link to the next webpage
            linkedPages.clear();                                                //clear the link for the next iteration
        }//end for
    }//end craw() method
    
    /**
     * This method calculate the page rank for each url existing in the database
     * @param iterations A <code>int</code> defining how many iterations will be used for the ranking process
     * @throws java.sql.SQLException if something goes wrong during ranking pages
     */
    public void calculatePageRank(int iterations) throws SQLException {
        
        double score = 1.0;                                                     //the current score of a table
        
        /* keep interating up until interations links */
        for(int i = 0; i < iterations; i++){
            System.out.println("====== ITERATION: [" + i + "]");            
            /* loop over all webpages */
            ResultSet urlRows = stmt.executeQuery("SELECT rowId FROM UrlList"); //get all webpages
            while(urlRows.next()){
                double pr = 0.15;                                                   //minimum page rank value
                int urlId = urlRows.getInt(1);                                  //get url id                                
                /* run over all pages that link to this one */
                Statement stmt2 = this.conn.createStatement();
                ResultSet linkRows = stmt2.executeQuery("SELECT distinct fromId FROM PageLink WHERE toId=" + urlId);                
                while(linkRows.next()){                    
                    int fromId = linkRows.getInt(1);                            //get from url id
                    
                    /* get the current score of the page pointing to the target one */
                    Statement stmt3 = this.conn.createStatement();
                    ResultSet scoreRows = stmt3.executeQuery("SELECT score FROM PageRank WHERE urlId=" + fromId);                    
                    if(scoreRows.next() == false)
                        continue;
                    score = scoreRows.getDouble(1);
                    
                    /* get the amount of links that this page pointing to */
                    Statement stmt4 = this.conn.createStatement();
                    ResultSet linkCountRows = stmt4.executeQuery("SELECT COUNT(*) FROM PageLink WHERE fromId=" + fromId);
                    if(linkCountRows.next() == false)
                        continue;
                    double linkCount = (double)linkCountRows.getInt(1);
                    
                    /* update the page rank score */
                    pr += 0.85 * (score / linkCount);                   //calculate the score                    
                }//end while
                Statement stmt5 = this.conn.createStatement();
                stmt5.execute("UPDATE  PageRank SET score=" + pr + " WHERE urlId=" + urlId);
                System.out.println("UPDATE  PageRank SET score=" + pr + " WHERE urlId=" + urlId);
            }//end while
        }//end for
        
    }//end calculatePAgeRank() method
    
    /**
     * 
     * @param searchStr
     * @throws java.sql.SQLException if something goes wrong during the query
     */
    public HashMap<Integer,Double> query (String searchStr, String[] scoreMethod, double[] scoreWeight, boolean[] normal) throws SQLException {
        
        HashMap<Integer, Double>scores = new HashMap<Integer, Double>();        //dataset of each score per url
        HashSet<Integer>wordIds = new HashSet<Integer>();                       //keep the word ids for each row in the search
        SortedMap<Double,Integer> sortedScore = null;                           //keep all scores in a sorted way
        
        /* get all url that contain both words and print them */
        ResultSet rows = this.getMatchRows(searchStr, wordIds);        
        ResultSet rankRows = this.getRankedRows(wordIds);
        ResultSet linkTextRows = this.getLinktextRows(wordIds);
        ResultSet documentSizeRows = this.getDocumentSizeRows(wordIds);
        ResultSet wordLocationRows = this.getWordLocationRows(wordIds);
        
        scores = this.getScoredList(rows, rankRows, linkTextRows, documentSizeRows, wordLocationRows, wordIds, scoreMethod, scoreWeight, normal);
        return scores;
        
    }//end query() method
    
    /**
     * This method order and print a result
     * @param result A <code>HashMap</code> containing the result
     */
    public void printResult(HashMap<Integer,Double>result) throws SQLException {
        
        SortedMap<Double,Integer>sorted = this.sortMap(result);        
        for(Iterator<Double>iterator = sorted.keySet().iterator(); iterator.hasNext();){
            double score = iterator.next();
            int urlId = sorted.get(score);
            System.out.println("Url " + this.getUrlName(urlId) + " scored " + score);
        }//end for
        
    }//end printResult() method
    
    /**
     * This method perform a complex search
     * @param search A <code>String</code> representing the complex search
     * @return A <code>HashMap</code> representing the search result
     * @throws java.sql.SQLException
     */
    public HashMap<Integer,Double> complexSearch(String search) throws SQLException {
        
        /* first of all, let's parse the query */        
        String[] methods = new String[] {"frequencyScore", "locationScore", "distanceScore", "pageRankScore", "linktextScore", "documentSizeScore", "wordFrequencyBiasScore"};
        double[] weight  = new double[] {1.0             , 1.0            , 1.0            , 1.0            , 1.0            , 1.0                , 1.0                     };
        boolean[] small  = new boolean[]{false           , true           , true           , false          , false          , false              , false                   };
        ArrayList<String>expressions = parseComplexSearch(search);
        HashMap<Integer,Double>finalResult = new HashMap<Integer,Double>();
        
        for(int i = expressions.size() - 1; i >= 0; i--){
            
            int emptyOp = 0;
            String expression = expressions.get(i);
            String[] words = expression.split("AND|OR");
            String[] operators = expression.split("[^(AND|OR)]");
                        
            /* run over the words and operators */
            for(int j = 0; j < operators.length; j++){
                
                if(operators[j].equals("")){
                    emptyOp++;
                    continue;
                }//end if
                
                String leftWord = words[j - emptyOp];                                       //get left element of expression
                String rightWord = (words.length > 1 ? words[j - emptyOp + 1] : leftWord);  //get right element of expression

                HashMap<Integer,Double> leftResult = (HashMap<Integer,Double>)(finalResult.size() != 0 ? finalResult.clone() : this.query(leftWord, methods, weight, small));
                HashMap<Integer,Double> rightResult = this.query(rightWord, methods, weight, small);

                if(operators[j].equals("OR")){

                    /* get all elements of left query */
                    for(Iterator<Integer>iterator = leftResult.keySet().iterator(); iterator.hasNext();){
                        int urlId = iterator.next();
                        if(finalResult.containsKey(urlId)){
                            finalResult.put(urlId, (finalResult.get(urlId) + leftResult.get(urlId)) / 2.0);
                        }//end if
                        else{
                            finalResult.put(urlId, leftResult.get(urlId));
                        }//end else
                    }//end for

                    /* get all elements of right query */
                    for(Iterator<Integer>iterator = rightResult.keySet().iterator(); iterator.hasNext();){
                        int urlId = iterator.next();
                        if(finalResult.containsKey(urlId)){
                            finalResult.put(urlId, (finalResult.get(urlId) + rightResult.get(urlId)) / 2.0);
                        }//end if
                        else{
                            finalResult.put(urlId, rightResult.get(urlId));
                        }//end else
                    }//end for

                }//end if
                else if(operators[j].equals("AND")){
                    /* get all elements of left query */
                    for(Iterator<Integer>iterator = leftResult.keySet().iterator(); iterator.hasNext();){
                        int urlId = iterator.next();

                        if(rightResult.containsKey(urlId)){
                            finalResult.put(urlId, (leftResult.get(urlId) + rightResult.get(urlId)) / 2.0 );                                
                        }//end if
                        else{
                            finalResult.remove(urlId);
                        }//end else
                    }//end for
                }//end if
            }//end for            
        }//end for
        
        return finalResult;
        
    }//end complextSearch() method
    
    /**
     * This method parse a query string in several expressions
     * @param search A <code>String</code> representing the string to search
     * @return A <code>ArrayList</code> representing the expressions
     */
    public ArrayList<String> parseComplexSearch(String search){
        
        ArrayList<String>expressions = new ArrayList<String>();         //contains all expressions separate by parentheses
        
        /* if expression has no parentheses */
        if(search.indexOf("(") < 0){
            expressions.add(search);                                    //only one expression
            return expressions;                                         //return it
        }//end if
        
        /* if open/close parentheses does not match */
        int open = 0;
        int close = 0;
        for(int i = 0 ; i < search.length(); i++){
            if(search.charAt(i) == '(')open++;
            if(search.charAt(i) == ')')close++;
        }//end for
        if(open != close)
            return expressions;                                         //return empty expression list
        
        /* mount the expressions */
        int parOpen = 0;
        for(int i = 0; i < search.length(); i++){
            char currChar = search.charAt(i);
            
            /* if find a open parentheses */
            if(currChar == '('){
                parOpen++;
                continue;
            }//end if
            else if(currChar == ')'){
                parOpen--;
                continue;
            }//end if
            else if(currChar == ' ')
                continue;
            
            /* if there is no element in expression list let`s put it */
            if(expressions.size() < parOpen)
                expressions.add(parOpen - 1, "");
            
            String expression = expressions.get(parOpen - 1);
            expression += currChar;
            expressions.set(parOpen - 1, expression);
            
        }//end for
        
        return expressions;
        
    }//end parseComplexSearch() method
    
//    @Override
//    /**
//     * @see java.lang.Object#finalize
//     */
//    protected void finalize() throws Throwable {        
//        DriverManager.getConnection("jdbc:derby:;shutdown=true");               //get the connection to db            
//        conn.close();                                                           //close connection to the database        
//    }//End finalize() method
    
    /**
     * This method commit all operations performed to the database
     */
    public void commit(){
        try{
            conn.commit();
        }//end try
        catch(SQLException sqle){
            sqle.printStackTrace();
        }//end catch
    }//End commit() method
    
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
    
}//End Crawler class
