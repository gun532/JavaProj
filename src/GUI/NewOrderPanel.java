package GUI;

import BL.AuthService;
import BL.CashierBL;
import DAL.ClientsDataAccess;
import DAL.EmployeeDataAccess;
import Entities.Clients.Client;
import Entities.Employee.Cashier;
import Entities.Inventory;
import Entities.Product;
import Entities.Employee.Employee;
import BL.InventoryBL;
import DAL.InventoryDataAccess;
import Entities.ShoppingCart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewOrderPanel extends CJPanel {
    private JLabel labelProduct;
    private JTextField productCodeField;

    private JLabel labelAmount;
    private JTextField amountField;

    private CJButton btnAddProduct;
    private CJButton btnRemoveProduct;
    private CJButton btnChooseClient;

    private CJButton btnFinish;

    private Font font = new Font("Candara", 0, 20); //Custom page font
    private Controller controller = null;

    private SpringLayout theLayout = new SpringLayout();

    private ProductTableModel pTableModel;
    private JTable productTable;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());
    private Inventory inventory = cashierBL.selectFromInventory(emp.getBranchNumber());
    private ShoppingCart shoppingCart = new ShoppingCart(emp);
    private Client chosenClient;

    private ClientPage clientPage;


    public NewOrderPanel(Controller in_controller) throws Exception {

        this.controller = in_controller;

        //Create the layout and populate the main panel.
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.4, getFrameSizeHeight() * 0.33);
        theLayout.putConstraint(SpringLayout.WEST, subPanel1, 0, SpringLayout.WEST, this);

        labelProduct = new JLabel("Product Code: ", JLabel.TRAILING);
        labelProduct.setFont(font);
        subPanel1.add(labelProduct);

        productCodeField = new JTextField(10);
        productCodeField.setFont(new Font("Arial", Font.BOLD, 20));
        productCodeField.setHorizontalAlignment(JTextField.CENTER);
        labelProduct.setLabelFor(productCodeField);
        subPanel1.add(productCodeField);

        labelAmount = new JLabel("Number of Items: ", JLabel.TRAILING);
        labelAmount.setFont(font);
        subPanel1.add(labelAmount);

        amountField = new JTextField(10);
        amountField.setFont(new Font("Arial", Font.BOLD, 20));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        labelAmount.setLabelFor(amountField);
        subPanel1.add(amountField);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                10, 60);       //xPad, yPad

        add(subPanel1);

        //Build sub Panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.4, getFrameSizeHeight() * 0.33);

        theLayout.putConstraint(SpringLayout.WEST, subPanel2, 0, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);

        btnRemoveProduct = new CJButton("Remove", font);
        subPanel2.add(btnRemoveProduct);

        btnAddProduct = new CJButton("Add", font);
        subPanel2.add(btnAddProduct);

        //Add to cart button pressed
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker() { //Open a new thread for add to cart.

                    @Override
                    protected Object doInBackground() throws Exception {
                        addToCart(Integer.parseInt(productCodeField.getText()),
                                Integer.parseInt(amountField.getText()));
                        return null;
                    }
                }.execute();
            }
        });

        //Remove product button pressed
        btnRemoveProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker() { //Open a new thread.

                    @Override
                    protected Object doInBackground() throws Exception {
                removeFromCart(Integer.parseInt(productCodeField.getText()),
                        Integer.parseInt(amountField.getText()));
                        return null;
                    }
                }.execute();
            }
        });

        SpringUtilities.makeGrid(subPanel2, 1, 2, 50, 20, 30, 6);

        //Create "Choose a Client" button
        btnChooseClient = new CJButton("Choose a Client",font);
        btnChooseClient.setSize(50,50);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnChooseClient, 0, SpringLayout.HORIZONTAL_CENTER, subPanel2);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnChooseClient, 0, SpringLayout.VERTICAL_CENTER, subPanel2);

        btnChooseClient.addActionListener(new ActionListener() {
            @Override
            // TODO: add a thread and make it a singleton page
            public void actionPerformed(ActionEvent e) {

                 clientPage = new ClientPage(controller);
            }
        });

        add(btnChooseClient);

        add(subPanel2);

        //Build sub Panel #3
        CJPanel subPanel3 = new CJPanel(new BorderLayout(), getFrameSizeWidth(), getFrameSizeHeight() * 0.33);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel3, 0, SpringLayout.SOUTH, subPanel2);

        btnFinish = new CJButton("Finish", new Font("Candara", 0, 50));
        subPanel3.add(btnFinish, BorderLayout.CENTER);

        //Finish button was pressed
        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(clientPage == null)
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Please choose a client", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
                else if(!clientPage.isActive())
                {
                    chosenClient = clientPage.getChosenClient();
                    cashierBL.createNewOrder(inventory, chosenClient, shoppingCart);

                    setVisible(false);
                    controller.showEmployeesMenuPage();
                }
                else
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Please choose a client", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(subPanel3);

        buildTable();
    }

    //Class functions

    private void removeFromCart(int productCode, int amount) {
        try {
            Product p = inventory.returnToInventory(productCode, amount);
            shoppingCart.removeFromCart(p);

            updateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }

    }

    private void addToCart(int productCode, int amount) {
            try {

                Product p = inventory.takeFromInventory(productCode, amount);
                shoppingCart.addToCart(p);

                updateTable();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
                //e.printStackTrace();
            }
    }

    private void updateTable()
    {
        //Clear old data stored in table
        pTableModel.clearDate();

        //Get new data from shopping cart and send it into table modal data vector.
        shoppingCart.getCart().entrySet().forEach(entry -> {
            pTableModel.addToVectorM_Data(entry.getValue());
        });

        //update table graphics
        pTableModel.fireTableDataChanged();

        productTable.validate();
        productTable.repaint();
    }

    public void setChosenClient(Client chosenClient) {
        this.chosenClient = chosenClient;
    }

    private void buildTable()
    {


        //Defining table headers and columns type
        String[] colNames = {"Product Code", "Product Name", "Number of Items", "Price", "Total"};
        Class[] colClasses = {Integer.class, String.class, Integer.class, Integer.class, Integer.class};

        pTableModel = new ProductTableModel(colNames, colClasses);
        productTable = new JTable(pTableModel);
        productTable.setFillsViewportHeight(true);

        JScrollPane subPanel4 = new JScrollPane(productTable);
        subPanel4.setPreferredSize(new Dimension((int) (getFrameSizeWidth() * 0.6), (int) (getFrameSizeHeight() * 0.66)));

        theLayout.putConstraint(SpringLayout.NORTH, subPanel4, 0, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.EAST, subPanel4, 0, SpringLayout.EAST, this);

        add(subPanel4);
    }
}
