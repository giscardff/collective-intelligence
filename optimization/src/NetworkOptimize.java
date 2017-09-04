/*
 * Created on 15 November, 2008
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author giscardf
 */
public class NetworkOptimize extends Optimization {

    public static String[] people = {"Charlie", "Augustus", "Veruca", "Violet", "Mike", "Joe", "Willy", "Miranda"};
    public static String[][] links = {
                                      {"Augustus" , "Willy"},
                                      {"Mike"     , "Joe"},
                                      {"Miranda"  , "Mike"},
                                      {"Violet"   , "Augustus"},
                                      {"Miranda"  , "Willy"},
                                      {"Charlie"  , "Mike"},
                                      {"Veruca"   , "Joe"},
                                      {"Miranda"  , "Augustus"},
                                      {"Willy"    , "Augustus"},
                                      {"Joe"      , "Charlie"},
                                      {"Veruca"   , "Augustus"},
                                      {"Miranda"  , "Joe"}
                                     };
    
    public static int domain[][] = new int[people.length * 2][2];
    static{
        for(int i = 0; i < domain.length; i++){
            domain[i][0] = 0;
            domain[i][1] = 600;
        }//end for
    }//end static block
    
    /**
     * This method print a solution
     * @param solution A <code>int[]</code> representing the solution
     */
    public void printSolution(int[] solution){
        
        /* get (x,y) for each person */
        HashMap<String,PeopleCoordenate>locations = new HashMap<String,PeopleCoordenate>();
        for(int i = 0; i < people.length; i++){
            locations.put(people[i], new PeopleCoordenate(solution[i * 2], solution[i * 2 + 1]));
        }//end for
        
        JFrame frame = new JFrame("Network View");                              //main frame window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                   //set default operation
        DrawPanel panel = new DrawPanel(locations, links);                      //draw panel to print the view        
        panel.setPreferredSize(new Dimension(800, 800));                        //set panel preferred size                        
        JScrollPane scroll = new JScrollPane();                                 //scroll pane to fit the view        
        scroll.add(panel);                                                      //add panel to scroll
        frame.add(scroll.add(panel));                                           //add scroll to the frame        
        panel.setVisible(true);                                                 //display panel
        frame.setVisible(true);                                                 //display frame
        frame.setSize(800, 800);                                                //set frame size
        
    }//end printSolution() method
    
    /**
     * This method calculate the cost for a specfici problem
     * @param vec A <code>int[]</code> representing the solution
     * @return A <code>double</code> representing the cost
     */
    public double crossCost(int[] solution){
        
        double total = 0.0;                                                     //number of lines crossed
        HashMap<String,PeopleCoordenate>location = new HashMap<String,PeopleCoordenate>();
        
        /* get (x,y) for each person */
        for(int i = 0; i < people.length; i++){
            location.put(people[i], new PeopleCoordenate(solution[i * 2], solution[i * 2 + 1]));
        }//end for
        
        /* run over each pair of link */
        for(int i = 0; i < links.length; i++){
            for(int j = 0; j < links.length; j++){
                
                /* get the location */
                PeopleCoordenate pc1 = location.get(links[i][0]);
                PeopleCoordenate pc2 = location.get(links[i][1]);                
                PeopleCoordenate pc3 = location.get(links[j][0]);
                PeopleCoordenate pc4 = location.get(links[j][1]);
                
                /* check if line cross */
                double den = (pc4.getY() - pc3.getY()) * (pc2.getX() - pc1.getX()) - (pc4.getX() - pc3.getX()) * (pc2.getY() - pc1.getY());
                
                /* lines are parallel */
                if(den == 0)
                    continue;
                
                double ua = ((pc4.getX() - pc3.getX()) * (pc1.getY() - pc3.getY()) - (pc4.getY() - pc3.getY()) * (pc1.getX() - pc3.getX())) / den;                
                double ub = ((pc2.getX() - pc1.getX()) * (pc1.getY() - pc3.getY()) - (pc2.getY() - pc1.getY()) * (pc1.getX() - pc3.getX())) / den;
                
                /* check if line cross each other */
                if(ua > 0 && ua < 1 && ub > 0 && ub < 1)
                    total += 100;
                
            }//end for
        }//end for
        
        /* run over each two links from same person to another ones */                
        for(int i = 0; i < people.length; i++){
            PeopleCoordenate firstDest = null;
            PeopleCoordenate secondDest = null;
            for(int j = 0; j < links.length; j++){
                /* when people has a link */                
                if(people[i].equals(links[j][0])){
                    if(firstDest == null)
                        firstDest = location.get(links[j][1]);                  //get the first link to a person
                    else
                        secondDest = location.get(links[j][1]);                 //get the second link to a person
                }//end if
                /* if already has both links */
                if(firstDest != null && secondDest != null){
                    PeopleCoordenate from = location.get(people[i]);            //get the person to see angle between links
                    
                    /* let's normalize both vectors to have origin on (0,0) */
                    firstDest.setX(firstDest.getX() - from.getX());
                    firstDest.setY(firstDest.getY() - from.getY());
                    secondDest.setX(secondDest.getX() - from.getX());
                    secondDest.setY(secondDest.getY() - from.getY());
                    from.setX(0);
                    from.setY(0);
                    
                    /*
                     * let's calculate the cos(angle) between the vectors
                     * cos(angle) = (x2*x3 + y2*y3) / (x2^2 + y2^2)^1/2 * (x3^2 + y3^2)^1/2
                     * cos(angle) = 0 means highest angle
                     * cos(angle) = 1 means lowest angle
                     */                    
                    double nom = firstDest.getX()* secondDest.getX() + firstDest.getY()*secondDest.getY();
                    double modFirst = Math.sqrt(Math.pow(firstDest.getX(), 2) + Math.pow(firstDest.getY(), 2));
                    double modSecond = Math.sqrt(Math.pow(secondDest.getX(), 2) + Math.pow(secondDest.getY(), 2));
                    double den = modFirst * modSecond;
                    
                    /* links does not cross each other */
                    if(den == 0)
                        continue;
                    
                    /* get the cos of the angle */
                    double cos = nom / den;
                    
                    /* penalize links with angle to close */
                    total += cos;
                    
                    firstDest = secondDest;                                     //second person will be the first link in next iteration
                }//end if
            }//end for
        }//end for
        
        return total;                                                           //return total costs
        
    }//end crossCost() method
                
    
}//End NetworkOptmize class

class PeopleCoordenate {
    
    private int x;      //x coordenate
    private int y;      //y coordenate
    
    /**
     * Creates a new instance of PeopleCoordenate object
     * @param x A <code>int</code> representing the x coordenate
     * @param y A <code>int</code> representing the y coordenate
     */
    public PeopleCoordenate(int x, int y){
        this.x = x;
        this.y = y;
    }//end PeopleCoordenate() constructor

    /**
     * This method retrieve the x coordenate
     * @return A <code>int</code> representing the x coordenate
     */
    public int getX() {
        return x;
    }//End getX() method

    public void setX(int x) {
        this.x = x;
    }//End setX() method
    

    /**
     * This method retrieve the y coordenate
     * @return A <code>int</code> representing the y coordenate
     */
    public int getY() {
        return y;
    }//end getY() method

    public void setY(int y) {
        this.y = y;
    }//End setY() method
    
    
    
}//end PeopleCoordenate class

class DrawPanel extends JPanel {
    
    private String[][] links;
    private HashMap<String,PeopleCoordenate>locations;
    
    public DrawPanel(HashMap<String,PeopleCoordenate>location, String[][] links){
        this.locations = location;
        this.links = links;
    }//end DrawPanel() class
    
    /**
     * This method override the default paint method in order to draw the dendogram
     * @param g A <code>Graphics</code> representing the panel renderer
     */
    @Override    
    public void paint(Graphics g) {
        
        /* run over each person */
        for(Iterator<String>iterator = locations.keySet().iterator(); iterator.hasNext();){
            String person = iterator.next();            
            PeopleCoordenate location = locations.get(person);
            g.drawString(person, location.getX(), location.getY());
        }//end for
        
        /* run over each link */
        for(int i = 0; i < links.length; i++){
            PeopleCoordenate loc1 = locations.get(links[i][0]);
            PeopleCoordenate loc2 = locations.get(links[i][1]);
            g.drawLine(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
        }//end for
        
    }//End paint() method
    
}//end DrawPanel() method