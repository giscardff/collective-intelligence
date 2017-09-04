/*
 * Created on 17 November, 2008
 */

/**
 *
 * @author giscardf
 */
public class MainApp {

    /**
     * This is the main method for execute the application
     * @param args
     */
    public static void main(String[] args) {
        
//        DoCClass cClass = new DoCClass();                                       //create the instance
//        String[] words = cClass.getWords(args[0]);                              //get all words
//        /* print the words */
//        for(int i = 0; i < words.length; i++)
//            System.out.printf("words[%d]: %s\n", i, words[i]);
        
//        /* sample training of the classifier */
//        Classifier classifier = new Classifier("getFeatures", "none");
//        classifier.train("the quick brown fox jumps over the lazy dog", "good");
//        classifier.train("make quick money in the online casino", "bad");
//        System.out.println("P(quick|good) = " + classifier.getFeature("quick", "good"));
//        System.out.println("P(quick|bad) = " + classifier.getFeature("quick", "bad"));
        
//        /* testing feature probability */
//        Classifier classifier = new Classifier("getFeatures", "none");
//        classifier.sampleTrain();
//        double prob = classifier.featureProbability("quick", "good");
//        System.out.println("The Pr(quick|good) = " + prob);
//        double wprob = classifier.weightedProbability("money", "good", 1.0, 0.5);
//        System.out.println("The Pr(money|good) = " + wprob);
//        classifier.sampleTrain();
//        wprob = classifier.weightedProbability("money", "good", 1.0, 0.5);
//        System.out.println("The Pr(money|good) = " + wprob);
        
        /* Testing Naive Bayes */
//        NaiveBayes naive = new NaiveBayes("getFeatures", "none");
//        naive.sampleTrain();
//        double probGood = naive.prob("quick rabbit", "good");
//        System.out.println("P(good|quick rabbit) = " + probGood);
//        double probBad = naive.prob("quick rabbit", "bad");
//        System.out.println("P(bad|quick rabbit) = " + probBad);        
//        String bestCat = naive.classify("quick rabbit", "unknown");
//        System.out.println("C(quick rabbit) = " + bestCat);
//        bestCat = naive.classify("quick money", "unknown");
//        System.out.println("C(quick money) = " + bestCat);
//        naive.setThreshold("bad", 3);
//        bestCat = naive.classify("quick money", "unknown");
//        System.out.println("C(quick money) = " + bestCat);
//        for(int i = 0; i < 10; i++)
//            naive.sampleTrain();
//        bestCat = naive.classify("quick money", "unknown");
//        System.out.println("C(quick money) = " + bestCat);
        
        /* Testing Fisher Classifier */
        FisherClassifier fisher = new FisherClassifier("getFeatures", "none");
        fisher.sampleTrain();        
        System.out.println("P(quick|good) = " + fisher.cprob("quick", "good"));
        System.out.println("P(money|bad) = " + fisher.cprob("money", "bad"));
        System.out.println("F(quick rabbit|good) = " + fisher.fisherProb("quick rabbit", "good"));
        System.out.println("F(quick rabbit|bad) = " + fisher.fisherProb("quick rabbit", "bad"));
        System.out.println("CF(quick rabbit) = " + fisher.classify("quick rabbit", "unknown"));
        System.out.println("CF(quick money) = " + fisher.classify("quick money", "unknown"));
        fisher.setMinimus("bad", 0.8);
        System.out.println("CF(quick money) = " + fisher.classify("quick money", "unknown"));
        fisher.setMinimus("good", 0.45);
        System.out.println("CF(quick money) = " + fisher.classify("quick money", "unknown"));
    }//end main() method

}//End MainApp class
