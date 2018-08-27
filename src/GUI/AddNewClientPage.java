package GUI;

import javax.swing.*;
import java.awt.*;

public class AddNewClientPage extends JFrame {
    private Controller controller = null;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();;

    private JLabel labelFullname = new JLabel("Full Name:",JLabel.TRAILING);
    private JTextField fieldFullName = new JTextField();

    private JLabel labelClientID = new JLabel("Client ID:",JLabel.TRAILING);
    private JTextField fieldClientID = new JTextField();

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnAdd = new CJButton("Add",font);
    private CJButton btnCancel = new CJButton("Cancel", font);

   private JLabel labelPhoneNumber = new JLabel("Phone Number");
    private JTextField fieldPhoneNumber = new JTextField();


    public AddNewClientPage(Controller in_controller){
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.5);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);

        setTitle("Add New Client");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth*0.9 , frameSizeHeight * 0.5);
        //subPanel1.setBackground(Color.YELLOW);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelFullname.setFont(font);
        subPanel1.add(labelFullname);

        fieldFullName = new JTextField(10);
        fieldFullName.setFont(font);
        labelFullname.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        labelClientID.setFont(font);
        subPanel1.add(labelClientID);

        fieldClientID = new JTextField(10);
        fieldClientID.setFont(new Font("Arial", Font.BOLD, 20));
        labelClientID.setLabelFor(fieldClientID);
        subPanel1.add(fieldClientID);

        labelPhoneNumber.setFont(font);
        subPanel1.add(labelPhoneNumber);

        fieldFullName = new JTextField(10);
        fieldFullName.setFont(new Font("Arial", Font.BOLD, 20));
        labelPhoneNumber.setLabelFor(fieldFullName);
        subPanel1.add(fieldFullName);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                3, 2, //rows, cols
                6, 6,        //initX, initY
                10, 20);       //xPad, yPad

        mainPanel.add(subPanel1);

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(),frameSizeWidth,frameSizeHeight*0.3);
        //subPanel2.setBackground(Color.ORANGE);

        theLayout.putConstraint(SpringLayout.SOUTH,subPanel2,0,SpringLayout.SOUTH,mainPanel);
        theLayout.putConstraint(SpringLayout.NORTH,subPanel2,0,SpringLayout.SOUTH,subPanel1);


        subPanel2.add(btnAdd);

        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2,1,2,120,6,30,6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);
        setVisible(true);
    }
}
