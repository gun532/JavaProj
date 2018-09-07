package GUI;

import BL.CashierBL;
import DAL.ClientsDataAccess;
import Entities.Clients.Client;
import Entities.Clients.ClientType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class AddNewClientPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();

    private JLabel labelFullname = new JLabel("Full Name:", JLabel.TRAILING);
    private JTextField fieldFullName = new JTextField(10);

    private JLabel labelClientID = new JLabel("Client ID:", JLabel.TRAILING);
    private JTextField fieldClientID = new JTextField(10);

    private JLabel labelPhoneNumber = new JLabel("Phone Number:", JLabel.TRAILING);
    private JTextField fieldPhoneNumber = new JTextField(10);

    private JLabel labelClientType = new JLabel("Client Type:", JLabel.TRAILING);
    private JComboBox<ClientType> cmbClientType = new JComboBox<ClientType>();

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnAdd = new CJButton("Add", font);
    private CJButton btnCancel = new CJButton("Cancel", font);
    private CJButton btnRemove = new CJButton("Remove", font);


    private CashierBL cashierBL = new CashierBL(new ClientsDataAccess());


    public AddNewClientPage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.5);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Edit Client");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.7);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelFullname.setFont(font);
        subPanel1.add(labelFullname);

        fieldFullName.setFont(font);
        labelFullname.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        labelClientID.setFont(font);
        subPanel1.add(labelClientID);

        fieldClientID.setFont(new Font("Arial", Font.BOLD, 20));
        labelClientID.setLabelFor(fieldClientID);
        subPanel1.add(fieldClientID);

        labelPhoneNumber.setFont(font);
        subPanel1.add(labelPhoneNumber);

        fieldPhoneNumber.setFont(new Font("Arial", Font.BOLD, 20));
        labelPhoneNumber.setLabelFor(fieldPhoneNumber);
        subPanel1.add(fieldPhoneNumber);

        labelClientType.setFont(font);
        subPanel1.add(labelClientType);

        cmbClientType.setModel(new DefaultComboBoxModel<>(ClientType.values()));
        subPanel1.add(cmbClientType);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                4, 2, //rows, cols
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

        fieldClientID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldClientID.getText().length() >= 9 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
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

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);
        //subPanel2.setBackground(Color.YELLOW);
        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        //theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel2,0,SpringLayout.HORIZONTAL_CENTER,mainPanel);

        subPanel2.add(btnRemove);
        subPanel2.add(btnAdd);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 3, 60, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fieldClientID.getText().isEmpty()){
                    if(isAlreadyExists()) {
                        // TODO: 03/09/2018 remove client from DB.
                        //cashierBL
                        JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldClientID.getText() + " was removed successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                        controller.getClientPage().setVisible(false);
                        controller.showClientPage();
                    }else {
                        JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldClientID.getText() + " is not in the clients list!", "Not exists!", JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(new JFrame(), "Please choose client ID to be removed", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fieldClientID.getText().isEmpty() && !fieldFullName.getText().isEmpty() && !fieldPhoneNumber.getText().isEmpty()) {
                    if (!isAlreadyExists()) {
                        // TODO: 03/09/2018 need the option to add client type of a new client into DB.
                        cashierBL.addNewClient(Integer.parseInt(fieldClientID.getText()), fieldFullName.getText(), fieldPhoneNumber.getText(),cmbClientType.getSelectedItem().toString());

                        JOptionPane.showMessageDialog(new JFrame(), "New client " + fieldFullName.getText() + " was added successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                        controller.getClientPage().setVisible(false);
                        controller.showClientPage();

                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldClientID.getText() + " already in the clients list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    /*---/Page functions methods/-------------------------------------------------------------------------*/

    private boolean isAlreadyExists() {
        ArrayList<Client> listClients = controller.getClientPage().getListOfClients();
        int clientID = Integer.parseInt(fieldClientID.getText());

        for (Client c : listClients) {
            if (c.getId() == clientID)
                return true;
        }

        return false;
    }
}
