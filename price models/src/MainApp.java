/*
 * Created on 13 December 2008
 */

/**
 *
 * @author giscardf
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        


//        /* printing some distances, price average and error */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet wineSet = predict.wineset1();
//        System.out.println(wineSet);
//
//        for(int i = 0; i < 10; i++){
//            TheData<Double,Double> v1 = wineSet.getRandom();
//            TheData<Double,Double> v2 = wineSet.getRandom();            
//            double dist = predict.euclidean(v1.getKeys(new Double[v1.keySize()]) , v2.getKeys(new Double[v2.keySize()]));
//            System.out.println("=== " + v1 + " x " + v2 + " ===");
//            System.out.println("Distance: " + dist);
//            System.out.println("Inverse : " + predict.inverseweight(dist, 1.0, 0.1));
//            System.out.println("Subtract: " + predict.subtractweight(dist, 10.0));
//            System.out.println("Gaussian: " + predict.gaussian(dist, 10.0));
//        }//end for        
//        
//        TheData<Double,Double> newWine = new TheData<Double,Double>();
//        newWine.set(0.0, 60.0, 4.0);        
//        newWine.setData(predict.knnestimate(newWine, wineSet, 5, ""));
//        System.out.println("New Wine (knn): " + newWine);
//        newWine.setData(predict.weightedknn(newWine, wineSet, 5, "gaussian"));
//        System.out.println("New Wine (weight knn): " + newWine);
//                
//        TheDataSet divide[] = predict.dividedata(wineSet, 0.05);
//        System.out.println("Error (knn): " + predict.testalgorithm(divide[0], divide[1], "knnestimate"));
//        System.out.println("Error (weight knn): " + predict.testalgorithm(divide[0], divide[1], "weightedknn"));        
//        
//        System.out.println("Error (knn): " + predict.crossvalidate(wineSet, 100, 0.05, "knnestimate"));
//        System.out.println("Error (weight knn): " + predict.crossvalidate(wineSet, 100, 0.05, "weightedknn"));
        
        /* creating the second dataset */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet wineSet = predict.wineset2();
//        System.out.println("===== Not Scale =====");
//        System.out.println("Error (knn): " + predict.crossvalidate(wineSet, 50, 0.05, "knnestimate"));
//        System.out.println("Error (weight knn): " + predict.crossvalidate(wineSet, 50, 0.05, "weightedknn"));        
//        wineSet = predict.rescale(wineSet, new int[]{1, 2, 0, 1});
//        System.out.println("===== Scale =====");        
//        System.out.println("Error (knn): " + predict.crossvalidate(wineSet, 50, 0.05, "knnestimate"));
//        System.out.println("Error (weight knn): " + predict.crossvalidate(wineSet, 50, 0.05, "weightedknn"));        
//        
//        int domain[][] = {{0,20},{0,20},{0,20},{0,20}};
//        int best[] = new int[domain.length];
//        predict.annealingOptimize(best, domain, 10000.0, 0.1, 1, wineSet, "weightedknn");
//        System.out.println("Best Scale: ");
//        for(int i = 0; i < best.length; i++)
//            System.out.println("[" + i + "] = " + best[i]);
        
        /* getting a probability guess */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet wineSet = predict.wineset3();
//        System.out.println(wineSet);
//        TheData<Double,Double> newWine = new TheData<Double,Double>();
//        newWine.set(0.0, 60.0, 4.0);                        
//        for(double i = 0; i < 991.0; i+= 10.0){            
//            double prob = predict.probguess(wineSet, newWine, i, (i + 10.0), 100, "weightedknn");
//            if(prob > 0.0)
//                System.out.println(newWine + " has " + (int)(prob * 100) + "% of be in the range [" + (int)i + "..." + (int)(i + 10.0) + "]");
//        }//end for
        
        /* printing the graphs */
//        NumberPredict predict = new NumberPredict();
//        TheData<Double,Double> newWine = new TheData<Double,Double>();
//        newWine.set(0.0, 60.0, 4.0);                        
//        TheDataSet<Double,Double> wineSet = predict.wineset3();
//        predict.cumulativegraph(wineSet, newWine, 100, 5, "weightedknn");        
//        predict.probabilitygraph(wineSet, newWine, 100, 5, 1.0, "weightedknn");        
        
        /* Exercise 1 */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet<Double,Double> wineSet = predict.wineset1();
//        int best[] = new int[1];
//        int domain[][] = {{1, 20}};
//        predict.annealingOptimizeForK(best, domain, 10000, 0.1, 1, wineSet, "weightedknn");
//        System.out.println("Best Scale: ");
//        for(int i = 0; i < best.length; i++)
//            System.out.println("[" + i + "] = " + best[i]);
        
        /* Exercise 2 */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet<Double,Double> wineSet = predict.wineset1();
//        System.out.println("Leave-One-Out (knn): " + predict.leaveoneoutcrossvalidation(wineSet, "knnestimate"));
//        System.out.println("Leave-One-Out (weighted knn): " + predict.leaveoneoutcrossvalidation(wineSet, "weightedknn"));
        
        /* Exercise 3 */
//        NumberPredict predict = new NumberPredict();
//        TheDataSet<Double,Double> winseSet = predict.wineset2();
//        int best[] = new int[4];
//        predict.optimizeZero(best, winseSet, "knnestimate");
//        predict.optimizeZero(best, winseSet, "weightedknn");
//        System.out.println("Best Zeroes: ");
//        for(int i = 0; i < best.length; i++)
//            System.out.println("[" + i + "] = " + (best[i] == 0 ? "useless":"useful"));
        
        /* Exercise 4 */
        NumberPredict predict = new NumberPredict();
        TheData<Double,Double> newWine = new TheData<Double,Double>();
        newWine.set(0.0, 60.0, 4.0);                        
        TheDataSet<Double,Double> wineSet = predict.wineset3();
        double ss = predict.optimizeForGauss(wineSet, newWine, 100.0, 5, 1.0, 10.0, 1.0, true, "weightedknn");        
        predict.probabilitygraph(wineSet, newWine, 100, 5, ss, "weightedknn");        
        System.out.println("Best SS = " + ss);
        
        
        
    }//end main() method

}//End MainApp class
