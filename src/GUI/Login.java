package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;

public class Login extends JFrame {
    private JPanel LoginPanel;
    private JButton loginButton;
    private JFormattedTextField employeeIDFormattedTextField;
    private JPasswordField passwordField;
    private JLabel labelEmployee;

    public Login(){

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
                if (employeeIDFormattedTextField.getText().length() >= 9 ) // limits text field to 9 characters
                    e.consume();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(passwordField.getPassword().length >= 20) // limits password field to 20 characters
                    e.consume();
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker(){ //Open a login input check in a new thread.

                    @Override
                    protected Object doInBackground() throws Exception {
                        //get login information and search for a match in DB
                        String employeeInputId = employeeIDFormattedTextField.getText();
                        char[] passwordInput = passwordField.getPassword();

                        //Example check
                        char[] exmp = {'1','2','3','4','5','6','7','8'};

                        //change to DB check
                        if(employeeInputId.equals("304989171") && Arrays.equals(exmp,passwordInput)){
                            //JOptionPane.showMessageDialog(null,"Successful login!","Login Information",JOptionPane.INFORMATION_MESSAGE);

                            //Go to main employees page.
                            setVisible(false);
                            dispose();
                            //add controller and go to employee page with it.
                            //EmployeesMenuPage emp = new EmployeesMenuPage();

                        }

                        else {
                            JOptionPane.showMessageDialog(new JFrame(),"Unsuccessful login, please try again.","Invalid input",JOptionPane.ERROR_MESSAGE);
                        }

                        //Clear password field for security reasons
                        Arrays.fill(passwordInput,'0');
                        passwordField.selectAll();
                        passwordField.setText(null);

                        return null;
                    }
                }.execute();
            }
        });
    }

    public static void main(String[] args) {

    }
}
