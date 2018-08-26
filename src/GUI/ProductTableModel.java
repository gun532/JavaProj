package GUI;

import Entities.Product;
import com.sun.org.apache.xpath.internal.operations.String;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

class ProductTableModel extends AbstractTableModel {
    private java.lang.String[] m_colNames;
    private Class[] m_colTypes;

    private Vector<Product> m_Data;

    public ProductTableModel(java.lang.String[] colNames, Class[] colClasses){
        super();
        this.m_colNames = colNames;
        this.m_Data = new Vector<Product>();
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

//    public void removeRow(int row) {
//        // remove a row from your internal data structure
//        fireTableRowsDeleted(row, row);
//    }

//    @Override
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        Product pData = (Product) (this.m_Data.elementAt(rowIndex));
//
//        switch (columnIndex){
//            case 0:
//                pData.setName((java.lang.String) aValue);
//                break;
//            case 1:
//                pData.se((Integer) aValue);
//                break;
//            case 2:
//                foodData.setPrice((Integer) aValue);
//                break;
//        }
//    }


    public void addToVectorM_Data(Product p) {
        this.m_Data.add(p);
    }

    public void clearDate()
    {
        this.m_Data.clear();
    }
}
