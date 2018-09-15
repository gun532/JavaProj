package GUI;

import BL.AuthService;
import BL.ManagerBL;
import DAL.ManagerDataAccess;
import Entities.Clients.Client;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainMenuPage extends CJPanel {
    private Image image = ImageIO.read(new File("src/GUI/Res/menu_BG.jpg"));

    private CJButton btnInventory;
    private CJButton btnClients;
    private CJButton btnBranch;
    private CJButton btnChat;
    private CJButton btnEmployees;
    private CJButton btnNewOrder;

    private Font font = new Font("Candara",0,50);
    private Controller controller;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Client chosenClient;


    public MainMenuPage(Controller in_controller) throws IOException {

        this.controller = in_controller;

        //Get frame size
        int frameSizeWidth = (controller.getAppFrame().getWidth());
        int frameSizeHeight = (controller.getAppFrame().getHeight());

        //Set page layout
        SpringLayout theLayout = new SpringLayout();
        setLayout(theLayout);

        //Inner sub panel
        CJPanel subPanel = new CJPanel(new GridLayout(3,2,50,50),frameSizeWidth,frameSizeHeight);

        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER,subPanel,0,SpringLayout.VERTICAL_CENTER,this);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel,0,SpringLayout.HORIZONTAL_CENTER,this);

        //Build page components
        btnInventory = new CJButton("Inventory", font);
        btnInventory.addActionListener(e -> {
            //Go to Inventory page using the Controller
            setVisible(false);
            controller.showInventoryPage();
        });
        subPanel.add(btnInventory);

        btnClients = new CJButton("Clients", font);
        btnClients.addActionListener(e -> {
            //Go to New Order page using the Controller
            controller.showClientPage();
        });
        subPanel.add(btnClients);

        // TODO: 02/09/2018 branch page
        btnBranch = new CJButton("Branch", font);
        btnBranch.addActionListener(e -> {
            //Go to Branch page using the Controller
            setVisible(false);
            controller.showBranchPage();
        });
        subPanel.add(btnBranch);

        btnChat = new CJButton("Chat", font);
        subPanel.add(btnChat);
        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to chat page use the controller

//                ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
//                JSONArray jsonArray = managerBL.createReportTotalPurchasesInBranch(2);
//                managerBL.writeJSONToFile(jsonArray);

            }
        });

        btnEmployees = new CJButton("Employees", font);
        btnEmployees.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showEmployeesPage();
            }
        });
        subPanel.add(btnEmployees);

        btnNewOrder = new CJButton("New Order", font);
        btnNewOrder.addActionListener(e -> {
            //Go to New Order page using the Controller
            setVisible(false);

            controller.showNewOrderPanel();
        });
        subPanel.add(btnNewOrder);

        subPanel.setOpaque(false);
        add(subPanel);

        //Page buttons authorizations
        if (emp.getJobPos() == Profession.SELLER)
        {
            btnNewOrder.setEnabled(false);
        }

        if(emp.getJobPos() == Profession.MANAGER)
        {
            btnInventory.setEnabled(true);
        }else {
            btnInventory.setEnabled(false);
        }
    }

    public void setChosenClient(Client chosenClient) {
        this.chosenClient = chosenClient;
    }

    public Client getChosenClient() {
        return chosenClient;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image,0,0,null);
    }
}
