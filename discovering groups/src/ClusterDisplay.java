/*
 * ClusterDisplay.java
 *
 * Created on 8 de Outubro de 2008, 20:41
 */

import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

/**
 *
 * @author  giscardf
 */
public class ClusterDisplay extends javax.swing.JFrame {

    private DrawPanel panel = null;                                     //draw panel to print the dendogram    
    private JScrollPane scroll = null;                                  //scroll pane to fit the dendogram
    
    /**
     * Creates new form ClusterDisplay
     * @param A <code>BiCluster</code> representing the root cluster
     */
    public ClusterDisplay(BiCluster cluster) {
        super("Dendogram");                                             //set the window title
        panel = new DrawPanel(cluster);                                 //create the draw panel
        scroll = new JScrollPane(panel);                                //create the scroll panel
        this.getContentPane().add(scroll, BorderLayout.CENTER);         //add the scroll to the main frame
        initComponents();                                               //initialize components
    }//End ClusterDisplay() constructor

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}//End ClusterDisplay class

/**
 * This class defines a panel whom default paint method render the dendogram
 * @author giscardf
 * @version 1.0
 */
class DrawPanel extends JPanel {

    private BiCluster cluster;                                          //point to the root cluster
    
    /**
     * Creates a new DrawPanel object
     * @param cluster A <code>BiCluster</code> representing the root cluster
     */
    public DrawPanel(BiCluster cluster){
        super(null);                                            //set the default layout to null
        this.cluster = cluster;                                 //point to root cluster
        this.setPreferredSize(new Dimension(100, 100));         //set a default preferred size
    }//End DrawPanel() constructor
    
    /**
     * This method override the default paint method in order to draw the dendogram
     * @param g A <code>Graphics</code> representing the panel renderer
     */
    @Override    
    public void paint(Graphics g) {
        super.paint(g);        
        this.printDendogram(cluster, g);
    }//End paint() method
    
    /**
     * This method retrieve the height of a cluster, i.e, how many cluster there
     * are under it
     * @param cluster A <code>BiCluster</code> pointing to the curent cluster
     * @return A <code>int</code> representing how much cluster are under it
     */
    private int getHeight(BiCluster cluster){
        
        if(cluster == null)
            return 0;
        
        /* final cluster has height of one */
        if(cluster.getLeft() == null && cluster.getRight() == null)
            return 1;
        
        /* keep calling recursevely up until reach the end */
        return (getHeight(cluster.getLeft()) + getHeight(cluster.getRight()));
        
    }//end getHeight() method
    
    /**
     * THis method get the depth of a cluster, i.e. how much the cluster is far from its "brother"
     * @param cluster A <code>BiCluster</code> representing the node to calculate the depth
     * @return A <code>double</code> representing the cluster distance
     */
    private double getDepth(BiCluster cluster){
        
        /* final cluster has height of one */
        if(cluster.getLeft() == null && cluster.getRight() == null)
            return 0;
        
        /* keep calling recursevely up until reach the end */
        return (getDepth(cluster.getLeft()) + getDepth(cluster.getRight()) + cluster.getDistance());
        
    }//end getDepth() method
    
    /**
     * This method print the hierarchical cluster as a dendrogram     * 
     * @param rootCluster A <code>BiCluster</code> representing the root cluster
     */
    public void printDendogram(BiCluster rootCluster, Graphics g){        
        
        int height = this.getHeight(rootCluster) * 20;
        int width = 2500;
        double depth = this.getDepth(rootCluster);
        double scaling = (width - 150)/depth;
        
        g.drawLine(0, height / 2, 10, height / 2);
        this.drawNode(g, rootCluster, 10, height / 2, scaling);
        
    }//End printDendogram() method
   
    /**
     * This method renderer a simple cluster node into the draw panel
     * @param g A <code>Graphics</code> representing the panel renderer
     * @param cluster A <code>BiCluster</code> representing the cluster to renderer
     * @param x A <code>int</code> representing the x position
     * @param y A <code>int</code> representing the y position
     * @param scaling A <code>double</code> representing the scaling to draw the nodes
     */
    private void drawNode(Graphics g, BiCluster cluster, int x, int y, double scaling){
        
        int height1 = getHeight(cluster.getLeft()) * 20;
        int height2 = getHeight(cluster.getRight()) * 20;
        int top = y - ((height1 + height2)/2);
        int bottom = y + ((height1 + height2)/2);            
        int ll = (int)(cluster.getDistance() * scaling);

        g.drawLine(x, top + height1 / 2, x, bottom - height2 / 2);
        g.drawLine(x, top + height1 / 2, x + ll, top + height1 / 2);
        g.drawLine(x, bottom - height2 / 2, x + ll, bottom - height2 / 2);

        if(cluster.getLeft() != null)
            this.drawNode(g, cluster.getLeft(), x + ll, top + height1 / 2, scaling);

        if(cluster.getRight() != null)
            this.drawNode(g, cluster.getRight(), x + ll, bottom - height2 / 2, scaling);
        
        if(cluster.getLeft() == null && cluster.getRight() == null){
            g.drawString(cluster.getId(), x + 5, y + 5);
            
            if(this.getPreferredSize().height < (y + 5)){
                this.setPreferredSize(new Dimension((int)this.getPreferredSize().getWidth(), (int)(y + 15)));                
            }//end if
            
            if(this.getPreferredSize().width < (x + 5)){
                this.setPreferredSize(new Dimension(x + 100, (int)this.getPreferredSize().getHeight()));                
            }//end if
            
        }//end if            
        
    }//end drawNode() method
    
}//end DrawPanel class