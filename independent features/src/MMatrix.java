/*
 * Created on 20 December 2008
 */

/**
 *
 * @author giscardf
 */
public class MMatrix {

    private double[][] matrix;      //represent the matrix data
    private String[] columns;       //represent the columm names
    private String[] rows;          //represent the row names
    
    public MMatrix(int rowSize, int columnSize){
        this.matrix = new double[rowSize][columnSize];
    }//end MMatrix() constructor
    
    public MMatrix(int rowSize, String[] columns){
        this.matrix = new double[rowSize][columns.length];
        this.columns = columns;
    }//emd MMatrix() constructor
    
    public MMatrix(String[] rows, int columnSize){
        this.matrix = new double[rows.length][columnSize];
        this.rows = rows;
    }//end MMatrix() constructor
    
    public MMatrix(String[] rows, String[] columns){
        this.matrix = new double[rows.length][columns.length];
        this.rows = rows;
        this.columns = columns;
    }//end MMatrix() constructor
    
    public String getRowName(int row){
        return this.rows[row];
    }//end getRowName() method
    
    public String getColumnName(int col){
        return this.columns[col];
    }//end getColumnName() method

    public void setColumns(String[] columns) {
        this.columns = columns;
    }//end setColumns() method

    public String[] getColumns() {
        return columns;
    }//End getColumns() method

    public void setRows(String[] rows) {
        this.rows = rows;
    }//End setRows() method

    public String[] getRows() {
        return rows;
    }//End getRows() method
    
    public int getRowSize(){
        return this.matrix.length;
    }//end getRowSize() method
    
    public int getColumnSize(){
        return this.matrix[0].length;
    }//end getColumnSize() method
    
    public void setValue(int row, int column, double value){
        this.matrix[row][column] = value;
    }//end setValue() method
    
    public void setValue(String row, String column, double value){
        int r = -1;
        int c = -1;
        for(int i = 0; i < rows.length; i++)
            if(rows[i].equals(row))r = i;
        for(int i = 0; i < columns.length; i++)
            if(columns[i].equals(column))c = i;
        setValue(r, c, value);
    }//end setValue() method
    
    public double getValue(int row, int column){
        return this.matrix[row][column];
    }//end getValue() method
    
    public double getValue(String row, String column){
        int r = -1;
        int c = -1;
        for(int i = 0; i < rows.length; i++)
            if(rows[i].equals(row))r = i;
        for(int i = 0; i < columns.length; i++)
            if(columns[i].equals(column))c = i;
        return getValue(r, c);
    }//end getValue() method
    
    public double[] getRowValues(int row){
        return this.matrix[row];
    }//end getRowValues() method
    
    public MMatrix multiply(MMatrix mMatrix){
        if(this.getColumnSize() != mMatrix.getRowSize())
            return null;        
        /* multiply the matrices */
        MMatrix rMatrix = new MMatrix(this.getRowSize(), mMatrix.getColumnSize());        
        for(int i = 0; i < rMatrix.getRowSize(); i++){
            for(int j = 0; j < rMatrix.getColumnSize(); j++){
                double value = 0.0;
                for(int k = 0; k < this.getColumnSize(); k++){
                    value += this.getValue(i, k) * mMatrix.getValue(k, j);
                }//end for                
                rMatrix.setValue(i, j, value);
            }//end for
        }//end for        
        return rMatrix;
    }//end multiply() method
    
    public double[] toArray(){
        double[] array = new double[this.getRowSize() * this.getColumnSize()];
        for(int i = 0; i < array.length; i++)
            array[i] = this.getValue(i / this.getColumnSize(), i % this.getColumnSize());
        return array;
    }//end toArray() method
    
    public MMatrix multiplyAsArray(MMatrix mMatrix){
        double[] array = this.toArray();
        double[] mArray = mMatrix.toArray();        
        MMatrix rMatrix = new MMatrix(this.getRowSize(), this.getColumnSize());
        for(int i = 0; i < array.length; i++)
            rMatrix.setValue(i / rMatrix.getColumnSize(), i % rMatrix.getColumnSize(), array[i] * mArray[i]);
        return rMatrix;
    }//end multiplyAsArray() method
    
    public MMatrix divideAsArray(MMatrix dMatrix){
        double[] array = this.toArray();
        double[] dArray = dMatrix.toArray();        
        MMatrix rMatrix = new MMatrix(this.getRowSize(), this.getColumnSize());
        for(int i = 0; i < array.length; i++)
            rMatrix.setValue(i / rMatrix.getColumnSize(), i % rMatrix.getColumnSize(), array[i] / dArray[i]);
        return rMatrix;
    }//end divideAsArray() method
    
    public MMatrix transpond(){
        MMatrix tMatrix = new MMatrix(this.getColumnSize(), this.getRowSize());        
        /* transpond the matrix */
        for(int i = 0; i < this.getRowSize(); i++){
            for(int j = 0; j < this.getColumnSize(); j++){
                tMatrix.setValue(j, i, this.getValue(i, j));
            }//end for
        }//end for        
        return tMatrix;
    }//end transpond() method

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < this.getRowSize(); i++){
            if(rows != null)buffer.append(rows[i] + " ");            
            buffer.append("| ");
            for(int j = 0; j < this.getColumnSize(); j++){
                buffer.append(" " + this.getValue(i, j) + " ");
            }//end for 
            buffer.append("|\n");
        }//end for
        return buffer.toString();
    }//End toString() method
    
    public boolean hasZero(){
        for(int i = 0; i < this.getRowSize(); i++){
            for(int j = 0; j < this.getColumnSize(); j++){
                if(this.getValue(i, j) == 0.0)return true;
            }//end for
        }//end for
        return false;
    }//end hasZero() method
    
    public boolean hasZeroColumn(){        
        for(int j = 0; j < this.getColumnSize(); j++){
            double zero = 0.0;
            for(int i = 0; i < this.getRowSize(); i++){
                zero += this.getValue(i, j);
            }//end for
            if(zero == 0.0)return true;
        }//end for
        return false;
    }//end hasZeroColumn() method
    
}//End MMatrix class
