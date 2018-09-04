package GUI;

import BL.AuthService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Login extends JPanel {
    private Controller controller;
    private Font font = new Font("Candara",Font.BOLD,30);
    private Image image;

    private JLabel labelEmployeeID = new JLabel("Employee ID:", JLabel.TRAILING);
    private JTextField fieldID = new JTextField(9);

    private JPasswordField fieldPassword = new JPasswordField(10);
    private JLabel labelPassword = new JLabel("Password:", JLabel.TRAILING);

    private CJButton loginButton = new CJButton("Login",new Font("Candara",0,30));

    public Login(Controller in_controller) throws IOException {

        this.controller = in_controller;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    image = ImageIO.read(new File("src/GUI/Res/clothing_login_BG.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        SpringLayout theLayout = new SpringLayout();
        setLayout(theLayout);

        CJPanel subPanel = new CJPanel(new SpringLayout(),controller.getAppFrame().getWidth()*0.7, controller.getAppFrame().getHeight()*0.5);
        subPanel.setOpaque(false);

        theLayout.putConstraint(SpringLayout.NORTH,subPanel,0,SpringLayout.NORTH,this);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel,0,SpringLayout.HORIZONTAL_CENTER,this);

        labelEmployeeID.setFont(font);
        labelEmployeeID.setBackground(new Color(255, 230, 230));
        labelEmployeeID.setOpaque(true);
        labelEmployeeID.setForeground(Color.BLACK);
        subPanel.add(labelEmployeeID);

        fieldID.setFont(new Font("Arial", Font.BOLD, 30));
        labelEmployeeID.setLabelFor(fieldID);
        subPanel.add(fieldID);

        labelPassword.setFont(font);
        labelPassword.setBackground(new Color(255, 230, 230));
        labelPassword.setOpaque(true);
        labelPassword.setForeground(Color.BLACK);
        subPanel.add(labelPassword);

        fieldPassword.setFont(new Font("Arial", Font.BOLD, 30));
        labelPassword.setLabelFor(fieldPassword);
        subPanel.add(fieldPassword);

        SpringUtilities.makeCompactGrid(subPanel,2,2,10,40,10,30);
        add(subPanel);

        add(loginButton);
        theLayout.putConstraint(SpringLayout.SOUTH,loginButton,-50,SpringLayout.SOUTH,this);
        theLayout.putConstraint(SpringLayout.EAST,loginButton,-40,SpringLayout.EAST,this);

        fieldID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldID.getText().length() >= 9 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text fieldPassword to 9 characters
                    e.consume();
            }
        });

        fieldPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (fieldPassword.getPassword().length >= 20) // limits password fieldPassword to 20 characters
                    e.consume();
                if (e.getKeyChar() == KeyEvent.VK_ENTER && !fieldID.getText().isEmpty())
                {
                    new SwingWorker() { //Open a login input check in a new thread.
                        @Override
                        protected Object doInBackground() throws Exception {
                            login();
                            return null;
                        }
                    }.execute();
                }
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

    private void login() {

        try {
            int employeeInputId = Integer.parseInt(fieldID.getText());
            String passwordInput = String.valueOf(fieldPassword.getPassword());
            fieldID.selectAll();
            boolean isLoggedIn = AuthService.getInstance().login(employeeInputId, passwordInput);

            if (isLoggedIn) {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            controller.showMainMenuPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Unsuccessful login, please try again.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid ID. Please type again", "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            //clear password fieldPassword
            fieldPassword.selectAll();
            fieldPassword.setText(null);

            //clear ID fieldPassword
            fieldID.selectAll();
            fieldID.setText(null);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image,0,0,null);
    }
}
