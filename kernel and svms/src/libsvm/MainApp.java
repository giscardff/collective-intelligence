package libsvm;

/*
 * Created on 17 December 2008
 */

import java.util.*;
import libsvm.*;
import org.jfree.data.xy.YInterval;

/**
 *
 * @author giscardf
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadmatch(dir + "/src/agesonly.csv", true);
//        MatchRow.printagesgraph(rows);
//        ArrayList<MatchRow> avg = MatchRow.lineartrain(rows);
//        MatchRow avgMatch = avg.get(0);
//        MatchRow avgNotMatch = avg.get(1);
//        double averages[][] = new double[2][avgMatch.size()];
//        for(int i = 0; i < avgMatch.size(); i++){
//            averages[avgMatch.match()][i] = avgMatch.getIndex(i);
//            averages[avgNotMatch.match()][i] = avgNotMatch.getIndex(i);
//        }//end for
//        MatchRow.printagesgraph(rows, averages);
//        System.out.println("M(30,30) = " + MatchRow.dpclassify(new MatchRow(new String[]{"30", "30", "-1"}, true), avg));
//        System.out.println("M(30,25) = " + MatchRow.dpclassify(new MatchRow(new String[]{"30", "25", "-1"}, true), avg));
//        System.out.println("M(25,40) = " + MatchRow.dpclassify(new MatchRow(new String[]{"25", "40", "-1"}, true), avg));
//        System.out.println("M(48,20) = " + MatchRow.dpclassify(new MatchRow(new String[]{"48", "20", "-1"}, true), avg));
        
//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/matchmaker.csv");        
//        ArrayList<MatchRow> scaleRows = MatchRow.scaledata(rows);
//        ArrayList<MatchRow> avg = MatchRow.lineartrain(rows);        
//        for(int i = 0; i < scaleRows.size(); i++){               
//            System.out.println("M(" + i + ") = " + MatchRow.dpclassify(scaleRows.get(i), avg));        
//        }//end for

//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/libsvm/matchmaker.csv");        
//        ArrayList<MatchRow> scaleRows = MatchRow.scaledata(rows);
//        double offset = MatchRow.getoffset(scaleRows, 10.0);
//        for(int i = 0; i < scaleRows.size(); i++){               
//            System.out.println("M(" + i + ") = " + MatchRow.nlclassify(scaleRows.get(i), scaleRows, offset, 10.0));
//        }//end for        
        
//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/libsvm/matchmaker.csv");        
//        ArrayList<MatchRow> scaleRows = MatchRow.scaledata(rows);        
//        svm_node[][] nodes = new svm_node[scaleRows.size()][];
//        double answers[] = new double[scaleRows.size()];
//        
//        for(int i = 0; i < scaleRows.size(); i++){
//            MatchRow curRow = scaleRows.get(i);            
//            answers[i] = curRow.match();
//            nodes[i] = new svm_node[curRow.size()];
//            for(int j = 0; j < curRow.size(); j++){
//                nodes[i][j] = new svm_node();
//                nodes[i][j].index = j;
//                nodes[i][j].value = curRow.getIndex(j);
//            }//end for
//        }//end for
//        
//        svm_parameter param = new svm_parameter();
//        param.kernel_type = svm_parameter.LINEAR;
//        param.cache_size = 50;
//        param.eps = 0.1;
//        param.C = 10;
//        svm_problem prob = new svm_problem();
//        prob.x = nodes;        
//        prob.y = answers;
//        prob.l = prob.y.length;
//        svm_model model = svm.svm_train(prob, param);        
//        String checkParam = svm.svm_check_parameter(prob, param);
//        int checkModel = svm.svm_check_probability_model(model);
//        model = svm.svm_train(prob, param);
//        
//        int randomC = (int)(Math.random() * nodes.length);                
//        svm.svm_predict(model, nodes[randomC]);
        
        /* Exercise 1 */
//        NaiveBayes classifier = new NaiveBayes("getFeatures");        
//        String dir = System.getProperty("user.dir");
//        ArrayList[] rows = MatchRow.loadinterests(dir + "/src/libsvm/matchmaker.csv");
//        ArrayList<String> classes = rows[0];
//        ArrayList<String> interests = rows[1];
//        for(int i = 0; i < classes.size(); i++){
//            classifier.train(interests.get(i), classes.get(i));
//        }//end for
//        System.out.println("Match Prob = " + classifier.prob("dancing", "1"));
//        System.out.println("Not Match Prob = " + classifier.prob("dancing", "0"));
        
        /* Exercise 2 */
//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/libsvm/matchmaker.csv");        
//        double[][] best = new double[2][rows.get(0).size()];
//        System.out.println("Best Cost = " + MatchRow.hillClimbing(best, rows, 2));
//        for(int c = 0; c < best.length; c++){
//            System.out.println("C(" + c + "):");
//            for(int i = 0; i < best[c].length; i++){
//                System.out.print(best[c][i] +  " ");
//            }//end for
//            System.out.println();
//        }//end for
        
        /* Exercise 3 */
//        String dir = System.getProperty("user.dir");
//        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/libsvm/matchmaker.csv");        
//        double[] result = MatchRow.costgamma(rows);
//        System.out.println("Best Cost= " + result[0]);
//        System.out.println("Best Gamma= " + result[1]);
        
        /* Exercise 5 */
        String dir = System.getProperty("user.dir");
        ArrayList<MatchRow> rows = MatchRow.loadnumerical(dir + "/src/libsvm/matchmaker.csv");        
        svm_node[][] nodes = new svm_node[rows.size()][];
        double answers[] = new double[rows.size()];        
        for(int i = 0; i < rows.size(); i++){
            MatchRow curRow = rows.get(i);            
            answers[i] = curRow.match();
            nodes[i] = new svm_node[curRow.size()];
            for(int j = 0; j < curRow.size(); j++){
                nodes[i][j] = new svm_node();
                nodes[i][j].index = j;
                nodes[i][j].value = curRow.getIndex(j);
            }//end for
        }//end for
        
        svm_problem prob = new svm_problem();
        prob.x = nodes;
        prob.y = answers;
        prob.l = answers.length;
        
        svm_parameter param = new svm_parameter();
        param.svm_type = param.C_SVC;
        param.kernel_type = param.POLY;
        param.degree = 4;
        param.gamma = 1 / answers.length;
        param.coef0 = 1.0;
        param.cache_size = 50;
        param.eps = 0.1;
        param.C = 10;
        System.out.println("Check Parameter: " + svm.svm_check_parameter(prob, param));
        
        svm_model model = svm.svm_train(prob, param);
        System.out.println("Check Model: " + svm.svm_check_probability_model(model));
        
        double error = 0.0;
        for(int i = 0; i < nodes.length; i++){
            double result = svm.svm_predict(model, nodes[i]);
            error += Math.abs(result - answers[i]);
        }//end for
        System.out.println("Error: " + error);
        
        svm.svm_cross_validation(prob, param, 5, answers);
        
    }//end main() method

}//End MainApp class
