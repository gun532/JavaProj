package GUI;

import BL.AuthService;
import BL.ShoppingCartBL;
import Entities.Inventory;
import Entities.Product;
import Entities.Employee.Employee;
import BL.InventoryBL;
import DAL.InventoryDataAccess;
import Entities.Employee.Profession;
import Entities.ShoppingCart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class NewOrderPanel extends CJPanel {
    private JLabel labelProduct;
    private JTextField productCodeField;

    private JLabel labelAmount;
    private JTextField amountField;

    private CJButton btnAddProduct;
    private CJButton btnRemoveProduct;

    private CJButton btnFinish;

    private Font font = new Font("Candara", 0, 20); //Custom page font
    private Controller controller = null;

    private InventoryBL inventoryBL = new InventoryBL(new InventoryDataAccess());

    //private ShoppingCartBL shoppingCartBL;

    private SpringLayout theLayout = new SpringLayout();



    private Vector<Product> pData = new Vector<Product>();


    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Inventory inventory = inventoryBL.selectFromInventory(emp.getBranchNumber());;
    private ShoppingCart shoppingCart = new ShoppingCart();


    public NewOrderPanel(Controller in_controller) throws Exception {

        this.controller = in_controller;
        inventoryBL = new InventoryBL(new InventoryDataAccess());

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

        //Lay out the sub panel.
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



        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart(Integer.parseInt(productCodeField.getText()),
                        Integer.parseInt(amountField.getText()));

            }
        });



        btnRemoveProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCart(Integer.parseInt(productCodeField.getText()),
                        Integer.parseInt(amountField.getText()));
            }
        });



        SpringUtilities.makeGrid(subPanel2, 1, 2, 50, 20, 30, 6);

        add(subPanel2);

        //Build sub Panel #3
        CJPanel subPanel3 = new CJPanel(new BorderLayout(), getFrameSizeWidth(), getFrameSizeHeight() * 0.33);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel3, 0, SpringLayout.SOUTH, subPanel2);

        btnFinish = new CJButton("Finish", new Font("Candara", 0, 50));
        subPanel3.add(btnFinish, BorderLayout.CENTER);

        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryBL.createNewOrder(inventory);
                setVisible(false);
                controller.showEmployeesMenuPage();
            }
        });

        add(subPanel3);
    }

    private void removeFromCart(int productCode, int amount) {
        try {
            Product p = inventory.returnToInventory(productCode, amount);
            shoppingCart.removeFromCart(p);
            pData.remove(p);
            buildTable(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }

    }


    private void addToCart(int productCode, int amount) {
            try {

                Product p = inventory.takeFromInventory(productCode, amount);
                shoppingCart.addToCart(p);

                buildTable(p);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
                //e.printStackTrace();
            }
    }


    private void removeFromTable()
    {

    }

    private void buildTable(Product p)
    {

        //Defining table headers and columns type
        String[] colNames = {"Product Code", "Product Name", "Number of Items", "Price", "Total"};
        Class[] colClasses = {Integer.class, String.class, Integer.class, Integer.class, Integer.class};

        TableModel pTableModel = new TableModel(pData, colNames, colClasses);
        JTable productTable = new JTable(pTableModel);

        JScrollPane subPanel4 = new JScrollPane(productTable);
        subPanel4.setPreferredSize(new Dimension((int) (getFrameSizeWidth() * 0.6), (int) (getFrameSizeHeight() * 0.66)));

        theLayout.putConstraint(SpringLayout.NORTH, subPanel4, 0, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.EAST, subPanel4, 0, SpringLayout.EAST, this);

        pData.add(shoppingCart.getCart().get(p.getProductCode()));


        add(subPanel4);
    }

}
