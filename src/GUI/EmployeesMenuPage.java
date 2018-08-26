package GUI;

import BL.AuthService;
import Entities.Employee.Employee;
import Entities.Employee.Profession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeesMenuPage extends CJPanel {
    private CJButton btnInventory;
    private CJButton btnClients;
    private CJButton btnBranch;
    private CJButton btnChat;
    private CJButton btnNewOrder;
    private Font font = new Font("Candara",0,50);
    private Controller controller;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();


    public EmployeesMenuPage(Controller in_controller) {

        this.controller = in_controller;

        //Get frame size
        int frameSizeWidth = (int)(controller.getAppFrame().getWidth());
        int frameSizeHeight = (int)(controller.getAppFrame().getHeight());

        //Set the panel layout
        SpringLayout theLayout = new SpringLayout();
        setLayout(theLayout);

        //Second sub panel
        CJPanel subPanel = new CJPanel(new GridLayout(2,2,50,50),(int)(frameSizeWidth*0.8),(int)(frameSizeHeight*0.4));

        //Build page components
        btnInventory = new CJButton("Inventory", font);
        subPanel.add(btnInventory);

        btnClients = new CJButton("Clients", font);
        subPanel.add(btnClients);

        btnBranch = new CJButton("Branch", font);
        subPanel.add(btnBranch);

        btnChat = new CJButton("Chat", font);
        subPanel.add(btnChat);
        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to chat page use the controller
            }
        });

        add(subPanel);

        btnNewOrder = new CJButton("New Order", font);
        btnNewOrder.setPreferredSize(new Dimension((int)(frameSizeWidth*0.5),(int)(frameSizeHeight*0.3)));
        btnNewOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Go to New Order page using the Controller
                setVisible(false);
                controller.showNewOrderPanel();
            }
        });

        add(btnNewOrder);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel, 50, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnNewOrder, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnNewOrder, 200, SpringLayout.VERTICAL_CENTER, this);


        if (emp.getJobPos() == Profession.SELLER)
        {
            btnNewOrder.setEnabled(false);
        }
    }
}
