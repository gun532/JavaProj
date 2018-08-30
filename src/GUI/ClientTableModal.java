package GUI;

import Entities.Clients.Client;
import Entities.Clients.VipClient;
import com.sun.org.apache.xpath.internal.operations.String;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class ClientTableModal extends AbstractTableModel {
    private java.lang.String[] m_colNames;
    private Class[] m_colTypes;

    private Vector<Client> m_Data;

    public ClientTableModal(java.lang.String[] colNames, Class[] colClasses){
        super();
        this.m_colNames = colNames;
        this.m_Data = new Vector<Client>();
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
        Client client = (Client) (this.m_Data.elementAt(rowIndex));

        switch (columnIndex) {
            case 0:
                return client.getClientCode();
            case 1:
                return client.getId();
            case 2:
                return client.getFullName();
            case 3:
                return client.getPhoneNumber();
            case 4:
                return client.getType();
        }
        return new String();
    }

    public void addToVectorM_Data(Client client) {
        this.m_Data.add(client);
    }

    public void clearDate()
    {
        this.m_Data.clear();
    }

}
