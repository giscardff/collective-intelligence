/*
 * Created on 21 December 2008
 */


/**
 *
 * @author giscardf
 */
public class NMF {

    public double difcost(MMatrix a, MMatrix b){ 
        double dif = 0.0;
        /* Loop over all elements */
        for(int i = 0; i < a.getRowSize(); i++){
            for(int j = 0; j < a.getColumnSize(); j++){
                dif += Math.abs(a.getValue(i, j) - b.getValue(i, j));
            }//end for
        }//end for
        return dif;
    }//end difcost() method
    
    public MMatrix[] factorize(MMatrix v, int pc, int iter){
        /* get number of rows and columns */
        int row = v.getRowSize();
        int column = v.getColumnSize();
        /* generate random matrices to start calculation */
        MMatrix w = new MMatrix(row, pc);        
        MMatrix h = new MMatrix(pc, column);
        this.randommatrix(w, 1.0, 10.0);
        this.randommatrix(h, 1.0, 10.0);
        /* iterate n times */        
        for(int i = 0; i < iter; i++){
            MMatrix wh = w.multiply(h);            
            /* calculate current difference */
            double cost = this.difcost(v, wh);            
            System.out.println("====== Diff cost(" + i + "): " + cost);            
            /* if there is not cost the matrix is perfect, let's get out */
            if(cost == 0.0)break;
            
            /* update feature matrix */
            MMatrix hn = w.transpond().multiply(v);
            MMatrix hd = w.transpond().multiply(w).multiply(h);                        
            h = h.multiplyAsArray(hn).divideAsArray(hd);
            /* update weight matrix */
            MMatrix wn = v.multiply(h.transpond());
            MMatrix wd = w.multiply(h).multiply(h.transpond());
            /* update w matrix trying decrease costs */                        
            w = w.multiplyAsArray(wn).divideAsArray(wd);
        }//end for
        return new MMatrix[]{w, h};        
    }//end factorize() method
    
    public MMatrix[] factorize(MMatrix v, int pc, double criteria){
        /* get number of rows and columns */        
        double lowestCost = Double.MAX_VALUE;
        double lastCost = Double.MAX_VALUE;        
        int row = v.getRowSize();
        int column = v.getColumnSize();
        /* generate random matrices to start calculation */
        MMatrix w = new MMatrix(row, pc);        
        MMatrix h = new MMatrix(pc, column);
        this.randommatrix(w, 1.0, 10.0);
        this.randommatrix(h, 1.0, 10.0);
        /* iterate n times */        
        while(true){
            MMatrix wh = w.multiply(h);            
            /* calculate current difference */
            double cost = this.difcost(v, wh);
            /* check if cost has matched criteria */
            if(cost > (lowestCost * (1 + criteria))){
                System.out.println("cost=" + cost + " x lastCost=" + lastCost);
                break;
            }//end if
            if(cost < lowestCost)
                lowestCost = cost;
            lastCost = cost;            
            System.out.println("====== Diff cost: " + cost + " Best Cost: " + lowestCost);            
            /* if there is not cost the matrix is perfect, let's get out */
            if(cost == 0.0)break;
            
            /* update feature matrix */
            MMatrix hn = w.transpond().multiply(v);
            MMatrix hd = w.transpond().multiply(w).multiply(h);                        
            h = h.multiplyAsArray(hn).divideAsArray(hd);
            /* update weight matrix */
            MMatrix wn = v.multiply(h.transpond());
            MMatrix wd = w.multiply(h).multiply(h.transpond());
            /* update w matrix trying decrease costs */                        
            w = w.multiplyAsArray(wn).divideAsArray(wd);
        }//end for
        return new MMatrix[]{w, h};        
    }//end factorize() method
    
    
    public void randommatrix(MMatrix v, double low, double max){
        for(int i = 0; i < v.getRowSize(); i++){
            for(int j = 0; j < v.getColumnSize(); j++){
                v.setValue(i, j, (low + (Math.random() * (max - low))));
            }//end for
        }//end for
    }//end randommatrx() method
    
    public double[] optimize(MMatrix v, int lowfeature, int maxfeature, int stepfeature, int iter){        
        double bestCost = Double.MAX_VALUE;
        double bestFeature = lowfeature;        
        for(int i = lowfeature; i < maxfeature; i += stepfeature){
            MMatrix[] result = factorize(v, i, iter);
            double cost = difcost(v, result[0].multiply(result[1]));            
            if(cost < bestCost){
                bestCost = cost;
                bestFeature = i;
            }//end if            
        }//end for
        return new double[]{bestFeature,bestCost};        
    }//end for
    
}//End NMF class
