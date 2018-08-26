package GUI;

import Entities.Clients.Client;
import Entities.Clients.ClientType;
import Entities.Clients.ReturnClient;
import Entities.Clients.VipClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Entities.Clients.ClientType.NEWCLIENT;

public class ClientPage extends JFrame {
    private Controller controller = null;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel;

    private CJButton btnOk;
    private CJButton btnCancel;

    private CJButton btnSearch;
    private JTextField searchField;

    private CJButton btnAddNewClient;

    private JTable clientTable;
    private ClientTableModal clientTableModal;
    private JScrollPane tablePanel;

    private Font font = new Font("Candara", 0, 20); //Custom page font

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
        CJPanel subPanel1 = new CJPanel(new SpringLayout(),frameSizeWidth*0.98,frameSizeHeight*0.1);
        //subPanel1.setBackground(Color.YELLOW);

        theLayout.putConstraint(SpringLayout.NORTH,subPanel1,0,SpringLayout.SOUTH,tablePanel);

        // TODO: see how to search and display client on client table
        btnSearch = new CJButton("Search",font);
        subPanel1.add(btnSearch);

        searchField = new JTextField("Search Client");
        subPanel1.add(searchField);

        btnAddNewClient = new CJButton("Add New Client", font);
        btnAddNewClient.addActionListener(new ActionListener() {
            @Override
            // TODO: add a thread and make it a singleton page
            public void actionPerformed(ActionEvent e) {
                AddNewClientPage addNewClientPage = new AddNewClientPage(controller);
            }
        });
        subPanel1.add(btnAddNewClient);

        SpringUtilities.makeCompactGrid(subPanel1,1,3,6,6,6,6);

        mainPanel.add(subPanel1);

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(),frameSizeWidth*0.95,frameSizeHeight*0.4);
        //subPanel2.setBackground(Color.ORANGE);

        theLayout.putConstraint(SpringLayout.SOUTH,subPanel2,0,SpringLayout.SOUTH,mainPanel);
        theLayout.putConstraint(SpringLayout.NORTH,subPanel2,0,SpringLayout.SOUTH,subPanel1);

        btnOk = new CJButton("OK",font);
        subPanel2.add(btnOk);

        btnCancel = new CJButton("Cancel", font);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2,1,2,200,100,50,6);

        mainPanel.add(subPanel2);

        //mainPanel.setVisible(true);
        setContentPane(mainPanel);
        setVisible(true);

        //Example remove later
        updateTable();
    }

    private JScrollPane buildTable() {
        //Defining table headers and columns type
        String[] colNames = {"Client Code","Client ID", "Client Name", "Client Type"};
        Class[] colClasses = {Integer.class, Integer.class, String.class, ClientType.class};

        clientTableModal = new ClientTableModal(colNames, colClasses);

        clientTable = new JTable(clientTableModal);
        clientTable.setFillsViewportHeight(true);


        clientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                // get the model from the jtable
                ClientTableModal model = (ClientTableModal)clientTable.getModel();

                // get the selected row index
                int selectedRowIndex = clientTable.getSelectedRow();

                // set the selected row data into Client
                int clientCode = (int)(model.getValueAt(selectedRowIndex, 0));
                int clientID = (int)(model.getValueAt(selectedRowIndex, 1));
                String clientName = (model.getValueAt(selectedRowIndex, 2).toString());
                ClientType clientType = (ClientType) model.getValueAt(selectedRowIndex, 3);

                //Build client object
                selectedClient(clientCode,clientID,clientName,clientType);
            }
        });

        tablePanel = new JScrollPane(clientTable);
        tablePanel.setPreferredSize(new Dimension((int) (frameSizeWidth), (int) (frameSizeHeight * 0.5)));

        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, mainPanel);

        return tablePanel;
    }

    private void updateTable()
    {
        //Clear old data stored in table
        clientTableModal.clearDate();

        //Example data | remove later
        VipClient cl1 = new VipClient(304989171,"Dani Rose", "050-6797973");
        clientTableModal.addToVectorM_Data(cl1);
        //----------------------------------------------------

        //TODO: Get new data from Client list and send to table modal vector.
//        ClientList.entrySet().forEach(entry -> {
//            clientTableModal.addToVectorM_Data(entry.getValue());
//        });

        //update table graphics
        clientTableModal.fireTableDataChanged();

        clientTable.validate();
        clientTable.repaint();
    }

    public void selectedClient(int clientCode, int clientID, String clientName, ClientType clientType)
    {
        // TODO: add set client code to clients class and "clean the classes types"
        switch (clientType) {
            case NEWCLIENT:
                //this.chosenClient = new NewClient
                break;
            case RETURNCLIENT:
                this.chosenClient = new ReturnClient(clientID, clientName, "");
                break;

            case VIPCLIENT:
                this.chosenClient = new VipClient(clientID, clientName, "");
                break;
        }
    }
}
