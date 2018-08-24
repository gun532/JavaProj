package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class EmployeesMenuPage extends JFrame {
    private JPanel mainPanel;
    private CJButton btnInventory;
    private CJButton btnClients;
    private CJButton btnBranch;
    private CJButton btnNewOrder;
    private Color backgroundColor = new Color(155, 187, 180);
    private Font font = new Font("Candara",0,90);

    public EmployeesMenuPage() {
        int screenSizeWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenSizeHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().height;

        //Build the frame
        setTitle("Employees Menu Page");
        int frameSizeWidth = (int)(screenSizeWidth*0.5);
        int frameSizeHeight = (int)(screenSizeHeight*0.8);
        setSize(frameSizeWidth,frameSizeHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Get logo icon
        URL iconURL = getClass().getResource("appLogo.png"); // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        //Build the main panel
        SpringLayout theLayout = new SpringLayout();
        mainPanel = new JPanel(theLayout);
        mainPanel.setBackground(backgroundColor);

        //Second sub panel
        JPanel subPanel = new JPanel(new GridLayout(1,3,50,0));
        subPanel.setPreferredSize(new Dimension((int)(frameSizeWidth*0.9),(int)(frameSizeHeight*0.3)));
        subPanel.setBackground(backgroundColor);

        //Build components
        btnInventory = new CJButton("Inventory", font);
        subPanel.add(btnInventory);

        btnClients = new CJButton("Clients", font);
        subPanel.add(btnClients);

        btnBranch = new CJButton("Branch", font);
        subPanel.add(btnBranch);

        mainPanel.add(subPanel);

        btnNewOrder = new CJButton("New Order", font);
        btnNewOrder.setPreferredSize(new Dimension((int)(frameSizeWidth*0.5),(int)(frameSizeHeight*0.3)));

        mainPanel.add(btnNewOrder);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel, 100, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnNewOrder, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnNewOrder, 300, SpringLayout.VERTICAL_CENTER, mainPanel);

        setContentPane(mainPanel);
        //pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        EmployeesMenuPage employeesMenuPage = new EmployeesMenuPage();
    }
}
