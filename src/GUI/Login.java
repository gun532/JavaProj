package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Login extends JFrame {
    private JPanel LoginPanel;
    private JButton loginButton;
    private JFormattedTextField employeeIDFormattedTextField;
    private JPasswordField passwordField;
    private JLabel labelEmployee;

    public Login(){
        //Build page frame

        setTitle("Login");

        URL iconURL = getClass().getResource("appLogo.png");
        // iconURL is null when not found
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {

    }
}
