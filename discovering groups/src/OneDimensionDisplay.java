/*
 * TwoDimensionalDisplay.java
 *
 * Created on 8 de Outubro de 2008, 20:41
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author  giscardf
 */
public class OneDimensionDisplay extends javax.swing.JFrame {

    private OneDimDrawPanel panel = null;                                       //draw panel to print the dendogram    
    private JScrollPane scroll = null;                                          //scroll pane to fit the dendogram    
    
    /**
     * Creates new form TwoDimensionalDisplay
     * @param A <code>BiCluster</code> representing the root cluster
     */
    public OneDimensionDisplay(HashMap<String,HashMap<String,Double>> locations) {
        super("Two Dimension");                                         //set the window title
        panel = new OneDimDrawPanel(locations);                                 //create the draw panel
        scroll = new JScrollPane(panel);                                //create the scroll panel
        this.getContentPane().add(scroll, BorderLayout.CENTER);         //add the scroll to the main frame
        initComponents();                                               //initialize components
    }//End TwoDimensionalDisplay() constructor

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    // End of variables declaration                   

}//End TwoDimensionalDisplay class

/**
 * This class defines a panel whom default paint method render the dendogram
 * @author giscardf
 * @version 1.0
 */
class OneDimDrawPanel extends JPanel {

    private HashMap<String,HashMap<String,Double>> locations;                   //point to the root cluster
    private double pixelRate = 1000;    
    
    /**
     * Creates a new OneDimDrawPanel object
     * @param cluster A <code>BiCluster</code> representing the root cluster
     */
    public OneDimDrawPanel(HashMap<String,HashMap<String,Double>> locations){
        super(null);                                            //set the default layout to null
        this.locations = locations;                             //point to root cluster
        this.setPreferredSize(new Dimension(300, 100));         //set a default preferred size
    }//End OneDimDrawPanel() constructor
    
    /**
     * This method override the default paint method in order to draw the dendogram
     * @param g A <code>Graphics</code> representing the panel renderer
     */
    @Override    
    public void paint(Graphics g) {
        super.paint(g);        
        this.print(locations, g);
    }//End paint() method
    
    /**
     * This method print the hierarchical cluster as a dendrogram     * 
     * @param rootCluster A <code>BiCluster</code> representing the root cluster
     */
    public void print(HashMap<String,HashMap<String,Double>> locations, Graphics g){        
        
        double lowerX = Integer.MAX_VALUE;        
        
        for(Iterator<String>itemIterator = locations.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            double xPos = locations.get(item).get("XPos");            
            if(lowerX > xPos)
                lowerX = xPos;
        }//end for
        
        for(Iterator<String>itemIterator = locations.keySet().iterator(); itemIterator.hasNext();){
            String item = itemIterator.next();
            double xPos = (locations.get(item).get("XPos") - lowerX) * pixelRate;            
            g.drawString(item, 200, (int)xPos);            
            System.out.println(item + ": (" + (int)xPos + "," + 200 + ")");
                        
            if(this.getPreferredSize().height < (xPos)){
                this.setPreferredSize(new Dimension((int)this.getPreferredSize().getWidth(), (int)(xPos + 100)));                
            }//end if
            
        }//end for
        
    }//End printDendogram() method
    
}//end OneDimDrawPanel class