package GUI;

import BL.AuthService;
import BL.ManagerBL;
import DAL.ManagerDataAccess;
import DTO.ClientDto;
import DTO.EmployeeDto;
import Entities.Clients.ClientType;
import Entities.Employee.*;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class AddEmployeePage extends JFrame {
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

    private CJButton btnAdd = new CJButton("Add", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private Employee emp = AuthService.getInstance().getCurrentEmployee();

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());

    private boolean isPasswordValid = false;

    public AddEmployeePage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.6);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Add New Employee");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.7);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelFullName.setFont(font);
        subPanel1.add(labelFullName);

        fieldFullName.setFont(font);
        labelFullName.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        labelEmpID.setFont(font);
        subPanel1.add(labelEmpID);

        fieldEmpID.setFont(new Font("Arial", Font.BOLD, 20));
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
        labelPhoneNumber.setLabelFor(fieldPhoneNumber);
        subPanel1.add(fieldPhoneNumber);

        labelAccountNum.setFont(font);
        subPanel1.add(labelAccountNum);

        fieldAccountNum.setFont(new Font("Arial", Font.BOLD, 20));
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

        fieldPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(fieldPassword.getPassword().length>0)
                {
                    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                    if(!String.valueOf(fieldPassword.getPassword()).matches(pattern))
                    {
                        JOptionPane.showMessageDialog(new JFrame(), "A password must contain the following:\n" +
                                " -  a digit must occur at least once\n" +
                                " -  a lower case letter must occur at least once\n" +
                                " -  an upper case letter must occur at least once\n" +
                                " -  no whitespace allowed\n" +
                                " -  at least 8 characters long\n", "Invalid password", JOptionPane.ERROR_MESSAGE);
                        fieldPassword.setText("");
                        isPasswordValid = false;
                    }else {
                        isPasswordValid = true;
                    }
                }
            }
        });

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

        subPanel2.add(btnAdd);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 120, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);

        btnAdd.addActionListener(e -> {
            if (!fieldEmpID.getText().isEmpty() && !fieldFullName.getText().isEmpty() && !fieldPhoneNumber.getText().isEmpty() && isPasswordValid) {
                if (!isAlreadyExists()) {


                    addNewEmployee();


                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Employee " + fieldEmpID.getText() + " already in the Employees list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    private void addNewEmployee() {
            try
            {
                String encryptedPass = managerBL.getEncryptedPass(String.valueOf(fieldPassword.getPassword()));
                PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                Gson gson = new Gson();
                EmployeeDto employeeDto = new EmployeeDto("addNewEmployee",fieldFullName.getText(),Integer.parseInt(fieldEmpID.getText()),
                        0,fieldPhoneNumber.getText(),Integer.parseInt(fieldAccountNum.getText()),
                        emp.getBranchNumber(),Profession.valueOf(cmbEmpType.getSelectedItem().toString()),encryptedPass);
                out.println(gson.toJson(employeeDto));

                DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                String response = in.readLine();

                if(response.equals("true"))
                {

                    JOptionPane.showMessageDialog(new JFrame(), "New employee " + fieldFullName.getText() + " was added successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                    controller.getEmployeesPage().setVisible(false);
                    controller.showEmployeesPage();

                    setVisible(false);
                }
                else {
                    JOptionPane.showMessageDialog(new JFrame(), "Employee " + fieldEmpID.getText() + " already in the Employees list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }

    /*---/Page functions methods/-------------------------------------------------------------------------*/

    private boolean isAlreadyExists() {
        ArrayList<Employee> listEmp = controller.getEmployeesPage().getListOfEmployees();
        int empID = Integer.parseInt(fieldEmpID.getText());

        for (Employee e : listEmp) {
            if (e.getId() == empID)
                return true;
        }

        return false;
    }
}
