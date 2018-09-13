package GUI;

import BL.AuthService;
import BL.ClientSocket;
import DTO.BranchDto;
import Entities.Branch;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BranchPage extends JPanel {

    private Controller controller;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private int currentBranchNumber = emp.getBranchNumber();


    private SpringLayout theLayout = new SpringLayout();

    private int fontSize = 40;
    private Font font = new Font("Candara", 0, fontSize); //Custom page font

    private CJPanel subPanel1;

    private JLabel labelBranchNumber = new JLabel("Branch Number:", JLabel.TRAILING);
    private JTextField fieldBranchNumber = new JTextField(10);

    private JLabel labelLocation = new JLabel("Location:", JLabel.TRAILING);
    private JTextField fieldLocation = new JTextField(10);

    private JLabel labelPhoneNumber = new JLabel("Phone Number:", JLabel.TRAILING);
    private JTextField fieldPhoneNumber = new JTextField(10);

    private JLabel labelNumberEmp = new JLabel("Number of Employees:", JLabel.TRAILING);
    private JTextField fieldNumberEmp = new JTextField(10);

    private CJPanel subPanel2;
    private CJButton btnReports = new CJButton("Reports", font);
    private CJButton btnUpdate = new CJButton("Update Phone", font);
    private CJButton btnBack = new CJButton("Back", font);

    public BranchPage(Controller controller) {

        this.controller = controller;

        buildSubPanel1();

        buildSubPanel2();
    }

    private void buildSubPanel1(){

        subPanel1 = new CJPanel(new SpringLayout(), controller.getAppFrame().getWidth()*0.9,controller.getAppFrame().getHeight()*0.7);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel1, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, subPanel1, 0, SpringLayout.VERTICAL_CENTER, this);

        labelBranchNumber.setFont(font);
        subPanel1.add(labelBranchNumber);

        fieldBranchNumber.setFont(new Font("Arial", Font.BOLD, fontSize));
        fieldBranchNumber.setText(String.valueOf(currentBranchNumber));
        fieldBranchNumber.setEditable(false);
        fieldBranchNumber.setHorizontalAlignment(JTextField.CENTER);
        labelBranchNumber.setLabelFor(fieldBranchNumber);
        subPanel1.add(fieldBranchNumber);

        labelLocation.setFont(font);
        subPanel1.add(labelLocation);

        fieldLocation.setFont(font);
        fieldLocation.setHorizontalAlignment(JTextField.CENTER);
        fieldLocation.setEditable(false);
        labelLocation.setLabelFor(fieldLocation);
        subPanel1.add(fieldLocation);

        labelPhoneNumber.setFont(font);
        subPanel1.add(labelPhoneNumber);

        fieldPhoneNumber.setFont(new Font("Arial", Font.BOLD, fontSize));
        fieldPhoneNumber.setHorizontalAlignment(JTextField.CENTER);
        fieldPhoneNumber.setEditable(false);
        labelPhoneNumber.setLabelFor(fieldPhoneNumber);
        subPanel1.add(fieldPhoneNumber);

        labelNumberEmp.setFont(font);
        subPanel1.add(labelNumberEmp);

        fieldNumberEmp.setFont(new Font("Arial", Font.BOLD, fontSize));
        fieldNumberEmp.setHorizontalAlignment(JTextField.CENTER);
        fieldNumberEmp.setEditable(false);
        labelNumberEmp.setLabelFor(fieldNumberEmp);
        subPanel1.add(fieldNumberEmp);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                4, 2, //rows, cols
                10, 50,        //initX, initY
                10, 30);       //xPad, yPad

        add(subPanel1);

        selectDetails();
    }

    private void selectDetails() {
        try {
            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();
            BranchDto branchDto = new BranchDto("selectBranchDetails", currentBranchNumber, null, 0, null);
            out.println(gson.toJson(branchDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            if (response.equals("null")) {
                throw new Exception();
            } else {
                Branch branch = null;
                branch = gson.fromJson(response, Branch.class);
                fieldLocation.setText(branch.getLocation());
                fieldNumberEmp.setText(String.valueOf(branch.getNumberOfEmployees()));
                fieldPhoneNumber.setText(branch.getPhone());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildSubPanel2(){

        subPanel2 = new CJPanel(new SpringLayout(), controller.getAppFrame().getWidth()*0.8,controller.getAppFrame().getHeight()*0.1);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel2, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, subPanel2, -20, SpringLayout.VERTICAL_CENTER, subPanel1);

        subPanel2.add(btnBack);
        subPanel2.add(btnUpdate);
        subPanel2.add(btnReports);

        SpringUtilities.makeCompactGrid(subPanel2,
                1, 3, //rows, cols
                30, 10,        //initX, initY
                10, 0);       //xPad, yPad

        add(subPanel2);

        btnBack.addActionListener(e -> {
            setVisible(false);
            try {
                controller.showMainMenuPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnUpdate.addActionListener(e -> {
            if(!fieldPhoneNumber.isEditable()) {
                fieldPhoneNumber.setEditable(true);
                btnUpdate.setText("Confirm");
            }else {
                fieldPhoneNumber.setEditable(false);
                btnUpdate.setText("Update Phone");

                updatePhone();

            }
        });

        btnReports.addActionListener(e -> {
            setVisible(false);
            controller.showReportsPage();
        });

        if(emp.getJobPos() != Profession.MANAGER)
        {
            btnUpdate.setEnabled(false);
            btnReports.setEnabled(false);
        }
    }

    private void updatePhone() {
        try
        {
            String newPhoneNumber = fieldPhoneNumber.getText();

            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();
            BranchDto branchDto = new BranchDto("updateBranchPhoneNumber", currentBranchNumber,
                    fieldLocation.getText(), Integer.parseInt(fieldNumberEmp.getText()), newPhoneNumber);

            out.println(gson.toJson(branchDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            if (response.equals("true")) {

                JOptionPane.showMessageDialog(new JFrame(), "Branch " + currentBranchNumber + " phone number was updated successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new Exception("could'nt write the change");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
