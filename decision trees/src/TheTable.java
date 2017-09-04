/*
 * Created on 30 November, 2008
 */

import java.util.*;
import javax.swing.table.*;

/**
 *
 * @author giscardf
 */
public class TheTable extends DefaultTableModel {
    
    private Class[] columnTypes;
    
    /**
     * Creates a new instance of object
     */
    public TheTable(){
        super();
    }//end TheTable() constructor
    
    /**
     * Creates a new instance of object
     * @param result A <code>HashMap</code> representing a result
     */
    public TheTable(HashMap<String,Double> result){
        for(Iterator<String>iterator = result.keySet().iterator(); iterator.hasNext();){
            String columnName = iterator.next();
            this.addColumn(columnName);
            Double value = result.get(columnName);
            for(int i = 0; i < value; i++)
                this.addRow(new Object[]{columnName});
        }//end for
    }//end TheTable() constructor
    
    /**
     * This method add two tables
     * @param toAdd A <code>TheTable</code> representing the table to add
     * @return A <code>TheTable</code> representing the final table
     */
    public TheTable add(TheTable toAdd){        
        TheTable addTable = new TheTable();        
        /* first let's get the columns */
        for(int col = 0; col < this.getColumnCount(); col++)
            addTable.addColumn(this.getColumnName(col));        
        /* now let's add all data */
        for(int row = 0; row < this.getRowCount(); row++){
            addTable.addRow(this.getRow(row));
        }//end for
        for(int row = 0; row < toAdd.getRowCount(); row++){
            addTable.addRow(toAdd.getRow(row));
        }//end for        
        return addTable;        
    }//end add() method
    
    /**
     * This method set the column types of the model
     * @param columnTypes A <code>Class[]</code> representing all column types
     */
    public void setColumnTypes(Class[] columnTypes){
        this.columnTypes = columnTypes;
    }//end setColumnTypes() method
    
    /**
     * This method get the colum types of the model
     * @return A <code>Class[]</code> representing all column types
     */
    public Class[] getColumnTypes(){
        return this.columnTypes;
    }//end getColumnTypes() method
    
    /**
     * This method retrieve the data type of a column
     * @param column A <code>int</code> representing the column index
     * @return A <code>Class</code> representing the column datatype
     */
    public Class getColumnType(int column){
        return columnTypes[column];
    }//end getColumnType() method
    
    /**
     * This method retrieve all rows values of a specific column
     * @param column A <code>int</code> representing the column index
     * @return A <code>Vector</code> representing the values
     */
    public Vector getColumnRows(int column){
        Vector rows = new Vector();
        /* run over all rows */
        for(int i = 0; i < this.getRowCount(); i++){
            rows.add(this.getValueAt(i, column));               //add the value
        }//end for
        return rows;                                            //return all row values
    }//end getColumnValues() method
    
    /**
     * This method retrieve all column values of a specific row
     * @param row A <code>int</code> representing the row index
     * @return A <code>Vector</code> representing the values
     */
    public Vector getRow(int row){
        Vector rowData = new Vector();
        for(int i = 0; i < this.getColumnCount(); i++)
            rowData.add(this.getValueAt(row, i));
        return rowData;
    }//End getRow() method

    @Override
    public String toString() {        
        String output = "";        
        for(int i = 0; i < this.getRowCount(); i++){
            for(int j = 0; j < this.getColumnCount(); j++){
                output = output + this.getValueAt(i, j).toString() + "\t";
            }//end for
            output += "\n";
        }//end for
        return output;        
    }//End toString() method
    
}//End TheTable class
