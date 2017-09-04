/*
 * Created on 21 December 2008
 */

/**
 *
 * @author giscardf
 */
public class MainApp {

    public static void main(String args[]) throws Exception {
        
//        MMatrix a = new MMatrix(2, 3);
//        MMatrix b = new MMatrix(3, 4);        
//        /* generate a handle matrix */
//        for(int i = 0; i < a.getRowSize(); i++){
//            for(int j = 0; j < a.getColumnSize(); j++){
//                a.setValue(i, j, Math.floor(Math.random() * 10));
//                b.setValue(j, i, Math.floor(Math.random() * 10));
//            }//end for
//        }//end for
//        
//        MMatrix m = a.multiply(b);
//        MMatrix t = a.transpond();
//        
//        System.out.println("Matrix A");
//        System.out.println(a.toString());
//        System.out.println("Matrix B");
//        System.out.println(b.toString());
//        System.out.println("Matrix M");
//        System.out.println(m.toString());
//        System.out.println("Matrix A**T");
//        System.out.println(t.toString());        
//        NMF nmf = new NMF();
//        System.out.println("Diff: " + nmf.difcost(t, b));
//        
//        MMatrix[] result = nmf.factorize(a, 2, 100);
//        System.out.println("MATRIX A");
//        System.out.println(a);
//        System.out.println("MATRIX HxW");
//        System.out.println(result[0].multiply(result[1]));
        
        NMFStockMarket nmf = new NMFStockMarket();
        String dir = System.getProperty("user.dir") + "/src/COTAHIST_A2008.TXT";
        MMatrix data = nmf.loaddata(dir);
        MMatrix[] result = nmf.factorize(data, 5, 20);
        nmf.displayResult(data, result[0], result[1]);
        
//        NMFBlog nmf = new NMFBlog();
//        String dir = System.getProperty("user.dir") + "/src/blogdata.txt";
//        MMatrix data = nmf.loaddata(dir);
//        MMatrix[] result = nmf.factorize(data, 5, 20);
//        nmf.displayResult(data, result[0], result[1]);
        
        /* Exercise 2 */
//        NMFBlog nmf = new NMFBlog();
//        String dir = System.getProperty("user.dir") + "/src/blogdata.txt";
//        MMatrix data = nmf.loaddata(dir);        
//        MMatrix[] result = nmf.factorize(data, 5, 10000);
//        nmf.displayResult(data, result[0], result[1]);
        
        /* Exercise 3 */
//        NMFBlog nmf = new NMFBlog();
//        String dir = System.getProperty("user.dir") + "/src/blogdata.txt";
//        MMatrix data = nmf.loaddata(dir);  
//        double[] optResult = nmf.optimize(data, 1, 500, 10, 10);
//        System.out.println("NMFBlog(feature=" + optResult[0] + ",cost=" + optResult[1] + ")");
        
        /* Exercise 4 */
//        NMFBlog nmf = new NMFBlog();
//        String dir = System.getProperty("user.dir") + "/src/blogdata.txt";
//        MMatrix data = nmf.loaddata(dir);  
//        nmf.factorize(data, 10, 0.001);
        
        
    }//end main() method
    
}//End MainApp class
