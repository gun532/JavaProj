package GUI;

import BL.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Login extends JFrame {
    private JPanel LoginPanel;
    private JButton loginButton;
    private JFormattedTextField employeeIDFormattedTextField;
    private JPasswordField passwordField;
    private JLabel labelEmployee;

    private Controller controller;


    public Login(Controller in_controller) {

        this.controller = in_controller;

        //Build page frame
        setTitle("Login");

        //Get logo icon
        URL iconURL = getClass().getResource("appLogo.png"); // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.2),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.17));
        setMinimumSize(new Dimension(626, 268));
        setResizable(false);

        setLocationRelativeTo(null);
        setContentPane(LoginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setVisible(true);

        employeeIDFormattedTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (employeeIDFormattedTextField.getText().length() >= 9) // limits text field to 9 characters
                    e.consume();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (passwordField.getPassword().length >= 20) // limits password field to 20 characters
                    e.consume();
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker() { //Open a login input check in a new thread.

                    @Override
                    protected Object doInBackground() throws Exception {
                        login();
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void login()  {

        int employeeInputId = Integer.parseInt(employeeIDFormattedTextField.getText());
        String passwordInput = String.valueOf(passwordField.getPassword());


        boolean isLoggedIn = AuthService.getInstance().login(employeeInputId, passwordInput);

        if (isLoggedIn) {
            setVisible(false);
            dispose();
            controller.getAppFrame().setVisible(true);
            controller.showEmployeesMenuPage();

        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Unsuccessful login, please try again.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }

        passwordField.selectAll();
        passwordField.setText(null);
    }

    public static void main(String[] args) {

    }
}
