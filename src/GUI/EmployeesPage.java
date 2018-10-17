package GUI;

import BL.AuthService;
import DTO.EmployeeArrayDto;
import DTO.EmployeeDto;
import Entities.Employee.*;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class EmployeesPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel;

    private JTextField searchField = new JTextField("Search Employee...");

    private CJButton btnAddEmp = new CJButton("Add", font);
    private CJButton btnRemoveEmp = new CJButton("Remove", font);
    private CJButton btnUpdateEmp = new CJButton("Update", font);

    //Defining table headers and columns type
    private String[] colNames = {"Employee Code", "Employee ID", "Employee Name", "Phone Number", "Account Number", "Employee Type"};
    private Class[] colClasses = {Integer.class, Integer.class, String.class, String.class, Integer.class, Profession.class};

    private EmployeeTableModel employeeTableModel = new EmployeeTableModel(colNames, colClasses);
    private JTable employeesTable = new JTable(employeeTableModel);
    private JScrollPane tablePanel;

    private TableRowSorter<EmployeeTableModel> sorter = new TableRowSorter<>(employeeTableModel);

    private AddEmployeePage addEmployeePage;
    private UpdateEmployeePage updateEmployeePage;

    //    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
    private ArrayList<Employee> listOfEmployees = new ArrayList<>();

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Employee chosenEmployee;

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

        btnAddEmp.addActionListener(e -> {
            //if first time entry -> create the page
            if (addEmployeePage == null)
                addEmployeePage = new AddEmployeePage(controller);

            addEmployeePage.setVisible(true);
        });
        subPanel1.add(btnAddEmp);


        btnRemoveEmp.addActionListener(e -> {
            if (chosenEmployee != null) {

                removeEmployee();

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please choose employee from table, to be removed", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        subPanel1.add(btnRemoveEmp);

        btnUpdateEmp.addActionListener(e -> {
            //if first time entry -> create the page
            if (chosenEmployee != null) {
                updateEmployeePage = new UpdateEmployeePage(controller, chosenEmployee);
                updateEmployeePage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please choose employee from table, to be updated", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        subPanel1.add(btnUpdateEmp);


        SpringUtilities.makeCompactGrid(subPanel1, 1, 4, 5, 10, 5, 0);

        if (emp.getJobPos() != Profession.MANAGER) {
            btnAddEmp.setEnabled(false);
            btnRemoveEmp.setEnabled(false);
            btnUpdateEmp.setEnabled(false);
        }
        mainPanel.add(subPanel1);
        setContentPane(mainPanel);
    }

    private void removeEmployee() {
            try {
                PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                Gson gson = new Gson();
                EmployeeDto employeeDto = new EmployeeDto("removeEmployee",chosenEmployee.getName(),chosenEmployee.getId(),
                        chosenEmployee.getEmployeeNumber(),chosenEmployee.getPhone(),chosenEmployee.getAccountNum(),
                        chosenEmployee.getBranchNumber(),chosenEmployee.getJobPos(),"null");
                out.println(gson.toJson(employeeDto));

                DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                String response = in.readLine();
                if(response.equals("true"))
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Employee " + chosenEmployee.getName() + " was removed successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                    controller.getEmployeesPage().setVisible(false);
                    controller.showEmployeesPage();
                }
                else
                    JOptionPane.showMessageDialog(new JFrame(), "Please choose employee from table, to be removed", "Invalid input", JOptionPane.ERROR_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private JScrollPane buildTable() {
        employeesTable.setFillsViewportHeight(true);
        employeesTable.setRowSorter(sorter);

        tablePanel = new JScrollPane(employeesTable);
        tablePanel.setPreferredSize(new Dimension((int) (frameSizeWidth * 0.98), (int) (frameSizeHeight * 0.8)));

        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, mainPanel);

        employeesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 1) {
                    chooseEmployeeFromTable();
                }
            }
        });

        updateTable();

        return tablePanel;
    }

    private void chooseEmployeeFromTable() {

        // get the selected row index
        int selectedRowIndex = employeesTable.getSelectedRow();
        selectedRowIndex = employeesTable.convertRowIndexToModel(selectedRowIndex);

        // set the selected row data into BL.Client
        int employeeCode = (int) (employeeTableModel.getValueAt(selectedRowIndex, 0));
        int employeeID = (int) (employeeTableModel.getValueAt(selectedRowIndex, 1));
        String employeeName = (employeeTableModel.getValueAt(selectedRowIndex, 2).toString());
        String employeePhone = (employeeTableModel.getValueAt(selectedRowIndex, 3).toString());
        int employeeAccount = (int) (employeeTableModel.getValueAt(selectedRowIndex, 4));
        Profession profession = (Profession) employeeTableModel.getValueAt(selectedRowIndex, 5);

        //Build Employee object
        selectedEmployee(employeeCode, employeeID, employeeName, employeePhone, employeeAccount, profession);
    }

    private void updateTable() {
        //Clear old data stored in table
        employeeTableModel.clearData();


        selectAllEmployees();

        //update table graphics
        employeeTableModel.fireTableDataChanged();

        employeesTable.validate();
        employeesTable.repaint();
    }

    private void selectAllEmployees() {

        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            EmployeeArrayDto employeeArrayDto = new EmployeeArrayDto("selectAllEmployessByBranch", listOfEmployees, emp.getBranchNumber());
            out.println(gson.toJson(employeeArrayDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();

            Employee employee;

            if (response != null) {
                JSONArray array = new JSONArray(response); //object.getJSONArray("");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject js = array.getJSONObject(i);
                    int employeeNum = js.getInt("employeeNumber");
                    String name = js.getString("name");
                    int id = js.getInt("id");
                    String phone = js.getString("phone");
                    int accountNumber = js.getInt("accountNum");
                    String type = js.getString("jobPos");
                    int branchNumber = js.getInt("branchNumber");
                    switch (type) {
                        case "SELLER":
                            employee = new Seller(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                        case "CASHIER":
                            employee = new Cashier(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                        case "MANAGER":
                            employee = new Manager(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                    }
                }

                for (int i = 0; i < listOfEmployees.size(); i++) {
                    employeeTableModel.addToVectorM_Data(listOfEmployees.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectedEmployee(int empCode, int empID, String empName, String empPhone, int empAccount, Profession profession) {

        switch (profession) {
            case SELLER:
                this.chosenEmployee = new Seller(empCode, empName, empID, empPhone, empAccount, emp.getBranchNumber());
                break;
            case CASHIER:
                this.chosenEmployee = new Cashier(empCode, empName, empID, empPhone, empAccount, emp.getBranchNumber());
                break;

            case MANAGER:
                this.chosenEmployee = new Manager(empCode, empName, empID, empPhone, empAccount, emp.getBranchNumber());
                break;
        }
    }

    public ArrayList<Employee> getListOfEmployees() {
        return listOfEmployees;
    }
}
