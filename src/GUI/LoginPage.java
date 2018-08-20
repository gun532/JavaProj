package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private JTextField  employeeID;
    private JPasswordField password;

    public LoginPage(){
        JFrame loginFrame = new JFrame();
        loginFrame.setTitle("Employees Login");
        loginFrame.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.2),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.2));
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginMainPanel = new JPanel();
        SpringLayout theMainLayout = new SpringLayout();
        loginMainPanel.setLayout(theMainLayout);

        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(500,150));
        SpringLayout theLayout = new SpringLayout();
        loginPanel.setLayout(theLayout);

        JLabel labelID = new JLabel("Employee ID:");
        labelID.setFont(new Font("Arial",Font.PLAIN, 30));
        loginPanel.add(labelID);
        this.employeeID = new JTextField("Enter your ID number");
        this.employeeID.setPreferredSize(new Dimension(200,40));
        loginPanel.add(employeeID);

        JLabel labelPassWord = new JLabel("Password:");
        labelPassWord.setFont(new Font("Arial",Font.PLAIN, 30));
        loginPanel.add(labelPassWord);
        this.password = new JPasswordField(18);
        loginPanel.add(password);

        //constraining elements
        theLayout.putConstraint(SpringLayout.WEST, labelID, 50, SpringLayout.WEST, loginPanel);
        theLayout.putConstraint(SpringLayout.NORTH, labelID, 40, SpringLayout.NORTH, loginPanel);

        theLayout.putConstraint(SpringLayout.WEST, employeeID, 10, SpringLayout.EAST, labelID);
        theLayout.putConstraint(SpringLayout.NORTH, employeeID, 40, SpringLayout.NORTH, loginPanel);

        theLayout.putConstraint(SpringLayout.WEST, labelPassWord, 50, SpringLayout.WEST, loginPanel);
        theLayout.putConstraint(SpringLayout.NORTH, labelPassWord, 20, SpringLayout.SOUTH, labelID);

        theLayout.putConstraint(SpringLayout.WEST, password, 50, SpringLayout.EAST, labelPassWord);
        theLayout.putConstraint(SpringLayout.NORTH, password, 20, SpringLayout.SOUTH, employeeID);

        loginMainPanel.add(loginPanel);
        theMainLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, loginPanel, 0, SpringLayout.HORIZONTAL_CENTER, loginMainPanel);

        JButton btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(200, 40));
        loginMainPanel.add(btnLogin);

        theMainLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnLogin, 0, SpringLayout.HORIZONTAL_CENTER, loginMainPanel);
        theMainLayout.putConstraint(SpringLayout.NORTH, btnLogin, 10, SpringLayout.SOUTH, loginPanel);

        loginFrame.setContentPane(loginMainPanel);
        //loginFrame.pack();
        loginFrame.setVisible(true);
    }
}
