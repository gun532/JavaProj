package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

public class Login extends JFrame {
    private JPanel LoginPanel;
    private JButton loginButton;
    private JFormattedTextField employeeIDFormattedTextField;
    private JPasswordField passwordField;
    private JLabel labelEmployee;

    private Controller controller = null;

    private GUIUtility guiUtility;

    {
        try {
            guiUtility = new GUIUtility();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                        checkLogin();
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void checkLogin() throws NoSuchAlgorithmException {

        String employeeInputId = employeeIDFormattedTextField.getText();
        String passwordInput = String.valueOf(passwordField.getPassword());

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(passwordInput.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hasedPass = sb.toString().toUpperCase();

        //String hasedPass = String.valueOf(hash);
        String DBPass = guiUtility.checkPass(Integer.parseInt(employeeInputId));

        if (hasedPass.equals(DBPass)) {
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
