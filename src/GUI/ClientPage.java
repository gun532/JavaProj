package GUI;

import BL.AuthService;
import BL.CashierBL;
import DAL.ClientsDataAccess;
import Entities.Clients.*;
import Entities.Employee.Employee;
import Entities.Employee.Profession;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
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

    private JTextField searchField = new JTextField("Search Client...");

    private CJButton btnAddNewClient = new CJButton("Add", font);
    private CJButton btnRemoveClient = new CJButton("Remove", font);
    private CJButton btnUpdateClient = new CJButton("Update", font);

    private CJButton btnChoose = new CJButton("Choose", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    //Defining table headers and columns type
    private String[] colNames = {"Client Code", "Client ID", "Client Name", "Phone Number", "Client Type"};
    private Class[] colClasses = {Integer.class, Integer.class, String.class, String.class, ClientType.class};

    private ClientTableModal clientTableModal = new ClientTableModal(colNames, colClasses);
    private JTable clientTable = new JTable(clientTableModal);
    private JScrollPane tablePanel;

    private TableRowSorter<ClientTableModal> sorter = new TableRowSorter<>(clientTableModal);
    private AddNewClientPage addNewClientPage;

    //Client data
    private CashierBL cashierBL = new CashierBL(new ClientsDataAccess());
    private Client chosenClient;
    private ArrayList<Client> listOfClients;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();

    private UpdateClientPage updateClientPage;

    public ClientPage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        setTitle("Clients List");
        setLayout(theLayout);

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.8);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.75);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);

        mainPanel = new CJPanel(theLayout);

        //Build and add table panel
        mainPanel.add(buildTable());

        //Build sub panel #1
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth*0.97, frameSizeHeight * 0.1);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.SOUTH, tablePanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel1, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);

        subPanel1.add(searchField);

        searchField.setFont(font);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                searchField.setText("Search Client...");
//            }
//        });

//        labelSearch.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //if()
//                filterByName();
//            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates
            }
        });


        btnAddNewClient.addActionListener(e -> {
            //if first time entry -> create the page
            if (addNewClientPage == null)
                addNewClientPage = new AddNewClientPage(controller);

            addNewClientPage.setVisible(true);
        });
        subPanel1.add(btnAddNewClient);

        btnRemoveClient.addActionListener(e -> {
            if (chosenClient != null){

                // TODO: 03/09/2018 remove employee from DB.

                JOptionPane.showMessageDialog(new JFrame(), "Client " + chosenClient.getFullName() + " was removed successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                controller.getClientPage().setVisible(false);
                controller.showClientPage();
            }else {
                JOptionPane.showMessageDialog(new JFrame(), "Please choose client from table, to be removed", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        subPanel1.add(btnRemoveClient);

        btnUpdateClient.addActionListener(e -> {
            //if first time entry -> create the page
            if (chosenClient != null) {
                updateClientPage = new UpdateClientPage(controller,  chosenClient);
                updateClientPage.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(new JFrame(), "Please choose client from table, to be updated", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        subPanel1.add(btnUpdateClient);

        SpringUtilities.makeCompactGrid(subPanel1, 1, 4, 5, 10, 5, 0);

        mainPanel.add(subPanel1);

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.15);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel2, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);
        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);


        //Choose button was pressed
        btnChoose.addActionListener(e -> {
            if (chosenClient != null) {
                //send chosen client data back to new order and main page
                controller.getMainMenuPage().setChosenClient(chosenClient);

                if(controller.getNewOrderPanel().isShowing()) {
                    controller.getNewOrderPanel().setChosenClient(chosenClient);
                    controller.getNewOrderPanel().getFieldChosenClient().setText(chosenClient.getFullName());
                    controller.getNewOrderPanel().updateShoppingCartDeal();
                    controller.getNewOrderPanel().validate();
                }
                //reset chosen client on client page
                chosenClient = null;

                searchField.setText("Search client...");

                //hide client page
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please choose a client from the table above.", "Choose a client", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));

        subPanel2.add(btnChoose);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 200, 20, 60, 0);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);
        setResizable(false);

        //----Employees Permissions----------
        switch (emp.getJobPos()) {
            case SELLER:
                btnAddNewClient.setEnabled(false);
                btnRemoveClient.setEnabled(false);
                btnUpdateClient.setEnabled(false);
            case CASHIER:
                btnRemoveClient.setEnabled(false);
                btnUpdateClient.setEnabled(false);
        }
    }

    private JScrollPane buildTable() {


        clientTable.setFillsViewportHeight(true);
        clientTable.setRowSorter(sorter);

        tablePanel = new JScrollPane(clientTable);
        tablePanel.setPreferredSize(new Dimension((int) (frameSizeWidth * 0.98), (int) (frameSizeHeight * 0.7)));


        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, mainPanel);

        clientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 1) {
                    chooseClientFromTable();
                }
            }
        });

        updateTable();

        return tablePanel;
    }

    public Client chooseClientFromTable() {

        // get the selected row index
        int selectedRowIndex = clientTable.getSelectedRow();
        selectedRowIndex = clientTable.convertRowIndexToModel(selectedRowIndex);

        // set the selected row data into Client
        int clientCode = (int) (clientTableModal.getValueAt(selectedRowIndex, 0));
        int clientID = (int) (clientTableModal.getValueAt(selectedRowIndex, 1));
        String clientName = (clientTableModal.getValueAt(selectedRowIndex, 2).toString());
        String clientPhone = (clientTableModal.getValueAt(selectedRowIndex, 3).toString());
        ClientType clientType = (ClientType) clientTableModal.getValueAt(selectedRowIndex, 4);

        //Build client object
        selectedClient(clientCode, clientID, clientName, clientPhone, clientType);
        return chosenClient;
    }

    private void updateTable() {
        //Clear old data stored in table
        clientTableModal.clearDate();

        listOfClients = cashierBL.selectAllClients();
        for (int i = 0; i < listOfClients.size(); i++) {
            clientTableModal.addToVectorM_Data(listOfClients.get(i));
        }

        //update table graphics
        clientTableModal.fireTableDataChanged();

        clientTable.validate();
        clientTable.repaint();
    }

    private void selectedClient(int clientCode, int clientID, String clientName, String clientPhone, ClientType clientType) {

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

    public ArrayList<Client> getListOfClients() {
        return listOfClients;
    }
}
