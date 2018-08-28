package GUI;

import BL.CashierBL;
import DAL.ClientsDataAccess;
import Entities.Clients.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClientPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel;

    private CJButton btnOk = new CJButton("OK", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private JTextField searchField = new JTextField("Search Client...");

    private CJButton btnSearch = new CJButton("Search", font);

    private CJButton btnAddNewClient = new CJButton("Add New Client", font);


    //Defining table headers and columns type
    private String[] colNames = {"Client Code", "Client ID", "Client Name", "Phone Number", "Client Type"};
    private Class[] colClasses = {Integer.class, Integer.class, String.class, String.class, ClientType.class};

    private ClientTableModal clientTableModal = new ClientTableModal(colNames, colClasses);
    private JTable clientTable = new JTable(clientTableModal);
    private JScrollPane tablePanel;



    private CashierBL cashierBL = new CashierBL(new ClientsDataAccess());
    private Client chosenClient;


    public ClientPage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        setTitle("Add/Choose a Client");
        setLayout(theLayout);

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.8);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.8);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);


        mainPanel = new CJPanel(theLayout);

        //Build table panel
        mainPanel.add(buildTable());

        //Build sub panel #1
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.98, frameSizeHeight * 0.1);
        //subPanel1.setBackground(Color.YELLOW);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.SOUTH, tablePanel);

        // TODO: see how to search and display client on client table
        subPanel1.add(btnSearch);

        subPanel1.add(searchField);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setText("Search Client...");
            }
        });

        btnAddNewClient.addActionListener(new ActionListener() {
            @Override
            // TODO: add a thread and make it a singleton page
            public void actionPerformed(ActionEvent e) {
                AddNewClientPage addNewClientPage = new AddNewClientPage(controller);

            }

        });

        subPanel1.add(btnAddNewClient);

        SpringUtilities.makeCompactGrid(subPanel1, 1, 3, 6, 6, 6, 6);

        mainPanel.add(subPanel1);

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.95, frameSizeHeight * 0.4);
        //subPanel2.setBackground(Color.ORANGE);

        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);


        subPanel2.add(btnOk);


        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 200, 100, 50, 6);

        mainPanel.add(subPanel2);

        //mainPanel.setVisible(true);
        setContentPane(mainPanel);
        setVisible(true);

        //Example remove later
        updateTable();

    }

    private JScrollPane buildTable() {

        clientTable.setFillsViewportHeight(true);


        tablePanel = new JScrollPane(clientTable);
        tablePanel.setPreferredSize(new Dimension((int) (frameSizeWidth), (int) (frameSizeHeight * 0.5)));

        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, mainPanel);

        clientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 2 ) {
                    chooseClientFromTable();

                    setVisible(false);

                }
            }
        });

        return tablePanel;
    }

    public Client chooseClientFromTable() {

        // get the model from the jtable
        ClientTableModal model = (ClientTableModal) clientTable.getModel();

        // get the selected row index
        int selectedRowIndex = clientTable.getSelectedRow();

        // set the selected row data into Client
        int clientCode = (int) (model.getValueAt(selectedRowIndex, 0));
        int clientID = (int) (model.getValueAt(selectedRowIndex, 1));
        String clientName = (model.getValueAt(selectedRowIndex, 2).toString());
        String clientPhone = (model.getValueAt(selectedRowIndex, 3).toString());
        ClientType clientType = (ClientType) model.getValueAt(selectedRowIndex, 4);

        //Build client object
        selectedClient(clientCode, clientID, clientName, clientPhone, clientType);
        return chosenClient;
    }

    private void updateTable() {
        //Clear old data stored in table
        clientTableModal.clearDate();

        //Example data | remove later
        //VipClient cl1 = new VipClient(304989171,"Dani Rose", "050-6797973");

        ArrayList<Client> listofclients = cashierBL.selectAllClients();
        for (int i = 0; i < listofclients.size(); i++) {
            clientTableModal.addToVectorM_Data(listofclients.get(i));
        }

        //----------------------------------------------------

        //update table graphics
        clientTableModal.fireTableDataChanged();

        clientTable.validate();
        clientTable.repaint();
    }

    private void selectedClient(int clientCode, int clientID, String clientName, String clientPhone, ClientType clientType) {
        // TODO: add set client code to clients class and "clean the classes types"
        switch (clientType) {
            case NEWCLIENT:
                this.chosenClient = new NewClient(clientID, clientName, clientPhone, clientCode);
                break;
            case RETURNCLIENT:
                this.chosenClient = new ReturnClient(clientID, clientName, clientPhone, clientCode);
                break;

            case VIPCLIENT:
                this.chosenClient = new VipClient(clientID, clientName, clientPhone, clientCode);
                break;
        }
    }

    public Client getChosenClient(){
        return chosenClient;
    }

    public void setChosenClient(Client chosenClient) {
        this.chosenClient = chosenClient;
    }
}
