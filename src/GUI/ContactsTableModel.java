package GUI;

import Entities.Employee.Employee;
import com.sun.org.apache.xpath.internal.operations.String;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class ContactsTableModel extends AbstractTableModel {
    private java.lang.String[] m_colNames;
    private Class[] m_colTypes;

    private Vector<Employee> m_Data;

    public ContactsTableModel(java.lang.String[] colNames, Class[] colClasses){
        super();
        this.m_colNames = colNames;
        this.m_Data = new Vector<>();
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
        Employee employee =  this.m_Data.elementAt(rowIndex);

        switch (columnIndex) {
            case 0:
                return employee.getEmployeeNumber();
            case 1:
                return employee.getName();
            case 2:
                return employee.getJobPos();
        }
        return new String();
    }

    public void addToVectorM_Data(Employee employee) {
        this.m_Data.add(employee);
    }

    public void clearData()
    {
        this.m_Data.clear();
    }
}
