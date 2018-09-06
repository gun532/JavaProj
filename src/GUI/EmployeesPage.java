package GUI;

import BL.AuthService;
import BL.ManagerBL;
import DAL.ManagerDataAccess;
import Entities.Employee.Employee;
import Entities.Employee.Manager;
import Entities.Employee.Profession;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EmployeesPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel;

    private CJButton btnOk = new CJButton("OK", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private JTextField searchField = new JTextField("Search Employee...");

    private CJButton btnAddRemoveEmployee = new CJButton("Add/Remove Employee", font);


    //Defining table headers and columns type
    private String[] colNames = {"Employee Code", "Employee ID", "Employee Name", "Phone Number","Account Number", "Employee Type"};
    private Class[] colClasses = {Integer.class, Integer.class, String.class, String.class,Integer.class, Profession.class};

    private EmployeeTableModel employeeTableModel = new EmployeeTableModel(colNames,colClasses);
    private JTable employeesTable = new JTable(employeeTableModel);
    private JScrollPane tablePanel;

    private TableRowSorter<EmployeeTableModel> sorter = new TableRowSorter<>(employeeTableModel);
    private AddRemoveEmployee addRemoveEmployee;

    //Client data
    //private CashierBL cashierBL = new CashierBL(new ClientsDataAccess());
    //private Client chosenClient;

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
    private ArrayList<Employee> listOfEmployees;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();

    public EmployeesPage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        setTitle("Add/Remove an Employee");
        setLayout(theLayout);

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.8);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.8);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);

        mainPanel = new CJPanel(theLayout);

        //Build and add table panel
        mainPanel.add(buildTable());

        //Build sub panel #1
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.97, frameSizeHeight * 0.08);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.SOUTH, tablePanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel1, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);

        subPanel1.add(searchField);

        searchField.setFont(font);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }
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

        if(emp.getJobPos() != Profession.MANAGER)
            btnAddRemoveEmployee.setEnabled(false);

        btnAddRemoveEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if first time entry -> create the page
                if (addRemoveEmployee == null)
                    addRemoveEmployee = new AddRemoveEmployee(controller);

                addRemoveEmployee.setVisible(true);
            }
        });
        subPanel1.add(btnAddRemoveEmployee);

        SpringUtilities.makeCompactGrid(subPanel1, 1, 2, 0, 0, 0, 0);

        mainPanel.add(subPanel1);

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);

        //OK button was pressed
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    //searchField.setText("Search client...");
                    setVisible(false);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        subPanel2.add(btnOk);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 175, 20, 100, 60);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);

    }

    private JScrollPane buildTable() {
        employeesTable.setFillsViewportHeight(true);
        employeesTable.setRowSorter(sorter);

        tablePanel = new JScrollPane(employeesTable);
        tablePanel.setPreferredSize(new Dimension((int) (frameSizeWidth * 0.98), (int) (frameSizeHeight * 0.7)));

        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, mainPanel);

//        employeesTable.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent evt) {
//                if (evt.getClickCount() >= 1) {
//                    chooseClientFromTable();
//                }
//            }
//        });

        updateTable();

        return tablePanel;
    }

//    public Client chooseClientFromTable() {
//
//        // get the selected row index
//        int selectedRowIndex = employeesTable.getSelectedRow();
//        selectedRowIndex = employeesTable.convertRowIndexToModel(selectedRowIndex);
//
//        // set the selected row data into Client
//        int clientCode = (int) (employeeTableModel.getValueAt(selectedRowIndex, 0));
//        int clientID = (int) (employeeTableModel.getValueAt(selectedRowIndex, 1));
//        String clientName = (employeeTableModel.getValueAt(selectedRowIndex, 2).toString());
//        String clientPhone = (employeeTableModel.getValueAt(selectedRowIndex, 3).toString());
//        ClientType clientType = (ClientType) employeeTableModel.getValueAt(selectedRowIndex, 4);
//
//        //Build client object
//        //selectedClient(clientCode, clientID, clientName, clientPhone, clientType);
//        return chosenClient;
//    }

    private void updateTable() {
        //Clear old data stored in table
        employeeTableModel.clearData();

        //----Example-------------------------------------------------------
        listOfEmployees = managerBL.selectAllEmployees();
//        listOfEmployees.add(new Manager(3,"El Jefe",989131223,"02-893323",3332211,1));
        //------------------------------------------------------------------

        // TODO: 02/09/2018 get list of employees from DB.
        for (int i = 0; i < listOfEmployees.size(); i++) {
            employeeTableModel.addToVectorM_Data(listOfEmployees.get(i));
        }

        //update table graphics
        employeeTableModel.fireTableDataChanged();

        employeesTable.validate();
        employeesTable.repaint();
    }

//    private void selectedClient(int clientCode, int clientID, String clientName, String clientPhone, ClientType clientType) {
//
//        switch (clientType) {
//            case NEWCLIENT:
//                this.chosenClient = new NewClient(clientID, clientName, clientPhone, clientCode);
//                break;
//            case RETURNCLIENT:
//                this.chosenClient = new ReturnClient(clientID, clientName, clientPhone, clientCode);
//                break;
//
//            case VIPCLIENT:
//                this.chosenClient = new VipClient(clientID, clientName, clientPhone, clientCode);
//                break;
//        }
//    }

    public ArrayList<Employee> getListOfEmployees() {
        return listOfEmployees;
    }
}
