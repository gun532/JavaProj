package GUI;

import BL.CashierBL;
import DAL.ClientsDataAccess;
import Entities.Clients.Client;
import Entities.Clients.NewClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewClientPage extends JFrame {
    private Controller controller;


    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();;

    private JLabel labelFullname = new JLabel("Full Name:",JLabel.TRAILING);
    private JTextField fieldFullName = new JTextField(10);

    private JLabel labelClientID = new JLabel("Client ID:",JLabel.TRAILING);
    private JTextField fieldClientID = new JTextField(10);

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnAdd = new CJButton("Add",font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private JLabel labelPhoneNumber = new JLabel("Phone Number");
    private JTextField fieldPhoneNumber = new JTextField(10);

    private CashierBL cashierBL = new CashierBL(new ClientsDataAccess());

    private ClientTableModal clientTableModal;
    private JTable clientTable;


    public AddNewClientPage(Controller in_controller){
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.4);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);

        setTitle("Add New Client");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth*0.9 , frameSizeHeight * 0.5);

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

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                3, 2, //rows, cols
                6, 6,        //initX, initY
                10, 20);       //xPad, yPad

        mainPanel.add(subPanel1);


        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(),frameSizeWidth,frameSizeHeight*0.3);

        theLayout.putConstraint(SpringLayout.SOUTH,subPanel2,0,SpringLayout.SOUTH,mainPanel);
        theLayout.putConstraint(SpringLayout.NORTH,subPanel2,0,SpringLayout.SOUTH,subPanel1);

        subPanel2.add(btnAdd);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2,1,2,120,6,30,6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 28/08/2018 need to check input fields, and if client already exists in DB -> send the user a matching message.
                cashierBL.addNewClient(Integer.parseInt(fieldClientID.getText()),fieldFullName.getText(), fieldPhoneNumber.getText());

                JOptionPane.showMessageDialog(new JFrame(), "New client " + fieldFullName.getText() + " was added successfuly", "Success!", JOptionPane.INFORMATION_MESSAGE);

                controller.getNewOrderPanel().getClientPage().setVisible(false);
                controller.getNewOrderPanel().setClientPage(new ClientPage(controller));
                controller.getNewOrderPanel().getClientPage().setVisible(true);

                setVisible(false);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
}
