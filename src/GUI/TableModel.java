package GUI;

import Entities.Product;
import com.sun.org.apache.xpath.internal.operations.String;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

class TableModel extends AbstractTableModel {
    private java.lang.String[] m_colNames;
    private Class[] m_colTypes;

    private Vector<Product> m_Data;

    public TableModel(Vector<Product> products, java.lang.String[] colNames, Class[] colClasses){
        super();
        this.m_colNames = colNames;
        this.m_Data = products;
        this.m_colTypes = colClasses;
    }

    @Override
    public int getRowCount() {
        return m_Data.size();
    }

    @Override
    public int getColumnCount() {
        return m_colNames.length;
    }

    @Override
    public java.lang.String getColumnName(int column) {
        return m_colNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return m_colTypes[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product pData = (Product) (this.m_Data.elementAt(rowIndex));

        switch (columnIndex) {
            case 0:
                return pData.getProductCode();
            case 1:
                return pData.getName();
            case 2:
                return pData.getAmount();
            case 3:
                return pData.getPrice();
            case 4:
                return pData.getPrice()*pData.getAmount();
        }
        return new String();
    }

//    @Override
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        Product pData = (Product) (this.m_Data.elementAt(rowIndex));
//
//        switch (columnIndex){
//            case 0:
//                pData.set((java.lang.String) aValue);
//                break;
//            case 1:
//                foodData.setCalories((Integer) aValue);
//                break;
//            case 2:
//                foodData.setPrice((Integer) aValue);
//                break;
//        }
//    }
}
