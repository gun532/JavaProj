package GUI;

import BL.AuthService;
import BL.ClientSocket;
import BL.ManagerBL;
import DAL.ManagerDataAccess;
import DTO.ClientDto;
import DTO.EmployeeDto;
import Entities.Clients.ClientType;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class UpdateEmployeePage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();

    private JLabel labelFullName = new JLabel("Full Name:", JLabel.TRAILING);
    private JTextField fieldFullName = new JTextField(10);

    private JLabel labelEmpID = new JLabel("Employee ID:", JLabel.TRAILING);
    private JTextField fieldEmpID = new JTextField(10);

    private JLabel labelPassword = new JLabel("Password:", JLabel.TRAILING);
    private JPasswordField fieldPassword = new JPasswordField(10);

    private JLabel labelPhoneNumber = new JLabel("Phone Number:", JLabel.TRAILING);
    private JTextField fieldPhoneNumber = new JTextField(10);

    private JLabel labelAccountNum = new JLabel("Account Number:", JLabel.TRAILING);
    private JTextField fieldAccountNum = new JTextField(10);

    private JLabel labelEmpType = new JLabel("Profession:", JLabel.TRAILING);
    private JComboBox<Profession> cmbEmpType = new JComboBox<Profession>();

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnUpdate = new CJButton("Update", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());

    private String newEncryptedPass = null;
    private Employee chosenEmp;

    public UpdateEmployeePage(Controller in_controller, Employee chosenEmployee) {
        this.controller = in_controller;
        this.chosenEmp = chosenEmployee;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.6);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Edit Employee");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.7);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelFullName.setFont(font);
        subPanel1.add(labelFullName);

        fieldFullName.setFont(font);
        fieldFullName.setText(chosenEmployee.getName());
        labelFullName.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        labelEmpID.setFont(font);
        subPanel1.add(labelEmpID);

        fieldEmpID.setFont(new Font("Arial", Font.BOLD, 20));
        fieldEmpID.setText(String.valueOf(chosenEmployee.getId()));
        labelEmpID.setLabelFor(fieldEmpID);
        subPanel1.add(fieldEmpID);

        labelPassword.setFont(font);
        subPanel1.add(labelPassword);

        fieldPassword.setFont(new Font("Arial", Font.BOLD, 20));
        labelPassword.setLabelFor(fieldPassword);
        subPanel1.add(fieldPassword);

        labelPhoneNumber.setFont(font);
        subPanel1.add(labelPhoneNumber);

        fieldPhoneNumber.setFont(new Font("Arial", Font.BOLD, 20));
        fieldPhoneNumber.setText(chosenEmployee.getPhone());
        labelPhoneNumber.setLabelFor(fieldPhoneNumber);
        subPanel1.add(fieldPhoneNumber);

        labelAccountNum.setFont(font);
        subPanel1.add(labelAccountNum);

        fieldAccountNum.setFont(new Font("Arial", Font.BOLD, 20));
        fieldAccountNum.setText(String.valueOf(chosenEmployee.getAccountNum()));
        labelAccountNum.setLabelFor(fieldAccountNum);
        subPanel1.add(fieldAccountNum);

        labelEmpType.setFont(font);
        subPanel1.add(labelEmpType);

        cmbEmpType.setModel(new DefaultComboBoxModel<>(Profession.values()));
        subPanel1.add(cmbEmpType);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                6, 2, //rows, cols
                6, 6,        //initX, initY
                10, 20);       //xPad, yPad

        mainPanel.add(subPanel1);

        fieldFullName.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldFullName.getText().length() >= 40 || !((e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') || (e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z'))) {
                    if (e.getKeyChar() != ' ')
                        e.consume();
                }
            }
        });

        fieldEmpID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldEmpID.getText().length() >= 9 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    e.consume();
            }
        });

        fieldPhoneNumber.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldPhoneNumber.getText().length() >= 10 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    if (e.getKeyChar() != '-')
                        e.consume();
            }
        });

        fieldAccountNum.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldAccountNum.getText().length() >= 10 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    e.consume();
            }
        });

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);

        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel2,0,SpringLayout.HORIZONTAL_CENTER,mainPanel);

        subPanel2.add(btnUpdate);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 120, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);


        btnUpdate.addActionListener(e -> {
            if (!fieldEmpID.getText().isEmpty() && !fieldFullName.getText().isEmpty() && !fieldPhoneNumber.getText().isEmpty()) {
                if (!isAlreadyExists()) {


                    updateEmployee();


                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Employee " + fieldEmpID.getText() + " already in the Employees list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    private void updateEmployee() {
        // TODO: make the phone number field no more than 9 or 7 digits
        try
        {
            if(fieldPassword.getPassword().length >0) {
                newEncryptedPass = managerBL.getEncryptedPass(String.valueOf(fieldPassword.getPassword()));
            }
            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();
            EmployeeDto employeeDto = new EmployeeDto("updateEmployee",fieldFullName.getText(),
                    Integer.parseInt(fieldEmpID.getText()),chosenEmp.getEmployeeNumber(),fieldPhoneNumber.getText(),
                    Integer.parseInt(fieldAccountNum.getText()),chosenEmp.getBranchNumber(),
                    Profession.valueOf(cmbEmpType.getSelectedItem().toString()),newEncryptedPass);

            out.println(gson.toJson(employeeDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            if(response.equals("true"))
            {

                JOptionPane.showMessageDialog(new JFrame(), "Employee " + fieldFullName.getText() + " was updated successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                controller.getEmployeesPage().setVisible(false);
                controller.showEmployeesPage();
                setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /*---/Page functions methods/-------------------------------------------------------------------------*/

    private boolean isAlreadyExists() {
        if(chosenEmp.getId() != Integer.parseInt(fieldEmpID.getText())) {
            ArrayList<Employee> listEmp = controller.getEmployeesPage().getListOfEmployees();
            int empID = Integer.parseInt(fieldEmpID.getText());

            for (Employee e : listEmp) {
                if (e.getId() == empID)
                    return true;
            }
        }
        return false;
    }
}
