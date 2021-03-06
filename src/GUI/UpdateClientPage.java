package GUI;

import BL.ManagerBL;
import DAL.ManagerDataAccess;
import DTO.ClientDto;
import Entities.Clients.Client;
import Entities.Clients.ClientType;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class UpdateClientPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();

    private JLabel labelFullName = new JLabel("Full Name:", JLabel.TRAILING);
    private JTextField fieldFullName = new JTextField(10);

    private JLabel labelClientID = new JLabel("Client ID:", JLabel.TRAILING);
    private JTextField fieldClientID = new JTextField(10);

    private JLabel labelPhoneNumber = new JLabel("Phone Number:", JLabel.TRAILING);
    private JTextField fieldPhoneNumber = new JTextField(10);


    private JLabel labelClientType = new JLabel("Client Type:", JLabel.TRAILING);
    private JComboBox<ClientType> cmbClientType = new JComboBox<ClientType>();


    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnUpdate = new CJButton("Update", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());

    private Client chosenClient;

    public UpdateClientPage(Controller in_controller, Client chosenClient) {
        this.controller = in_controller;
        this.chosenClient = chosenClient;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.6);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Edit Client");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.7);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelFullName.setFont(font);
        subPanel1.add(labelFullName);

        fieldFullName.setFont(font);
        fieldFullName.setText(chosenClient.getFullName());
        labelFullName.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        labelClientID.setFont(font);
        subPanel1.add(labelClientID);

        fieldClientID.setFont(new Font("Arial", Font.BOLD, 20));
        fieldClientID.setText(String.valueOf(chosenClient.getId()));
        labelClientID.setLabelFor(fieldClientID);
        subPanel1.add(fieldClientID);


        labelPhoneNumber.setFont(font);
        subPanel1.add(labelPhoneNumber);

        fieldPhoneNumber.setFont(new Font("Arial", Font.BOLD, 20));
        fieldPhoneNumber.setText(chosenClient.getPhoneNumber());
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
                if (fieldPhoneNumber.getText().length() >= 11 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    if (e.getKeyChar() != '-')
                        e.consume();
            }
        });


        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);

        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel2,0,SpringLayout.HORIZONTAL_CENTER,mainPanel);

        subPanel2.add(btnUpdate);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 120, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);


        btnUpdate.addActionListener(e -> {
            if (!fieldClientID.getText().isEmpty() && !fieldFullName.getText().isEmpty() && !fieldPhoneNumber.getText().isEmpty()) {
                if (!isAlreadyExists()) {
                    updateClient();


                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldClientID.getText() + " already in the Clients list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    private void updateClient() {
        try
        {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();
            ClientDto clientDto = new ClientDto("updateClient",Integer.parseInt(fieldClientID.getText()),
                    fieldFullName.getText(),fieldPhoneNumber.getText(),
                    ClientType.valueOf(cmbClientType.getSelectedItem().toString()),chosenClient.getClientCode());
            out.println(gson.toJson(clientDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();

            if(response.equals("true"))
            {

                JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldFullName.getText() + " was updated successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);

                controller.getClientPage().setVisible(false);
                controller.showClientPage();

                setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(new JFrame(), "Client " + fieldClientID.getText() + " already in the Clients list!", "Already exists!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /*---/Page functions methods/-------------------------------------------------------------------------*/

    private boolean isAlreadyExists() {
        if(chosenClient.getId() != Integer.parseInt(fieldClientID.getText())) {
            ArrayList<Client> listClient = controller.getClientPage().getListOfClients();
            int clientID = Integer.parseInt(fieldClientID.getText());

            for (Client c : listClient) {
                if (c.getId() == clientID)
                    return true;
            }
        }
        return false;
    }
}
