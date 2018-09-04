package GUI;

import BL.AuthService;
import BL.CashierBL;
import BL.InventoryBL;
import DAL.ClientsDataAccess;
import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;
import Entities.Employee.Employee;
import Entities.Inventory;
import Entities.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InventoryPage extends CJPanel {

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private InventoryDataAccess inventoryDataAccess = new InventoryDataAccess();
    private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), inventoryDataAccess, new ClientsDataAccess());
    private Inventory inventory = cashierBL.selectFromInventory(emp.getBranchNumber());
    private InventoryBL inventoryBL = new InventoryBL(inventoryDataAccess);

    private CJPanel subPanel1;

    private JLabel labelProductCode;
    private JTextField fieldProductCode;

    private JLabel labelProductName;
    private JTextField fieldProductName;

    private JLabel labelProductPrice;
    private JTextField fieldProductPrice;

    private JLabel labelAmount;
    private JTextField fieldAmount;


    private CJPanel subPanel2;
    private CJButton btnAddProduct;
    private CJButton btnRemoveProduct;

    private CJPanel subPanel3;
    private CJButton btnFinish;
    private CJButton btnCancel;

    private ProductTableModel pTableModel;
    private JTable productTable;
    private JScrollPane subPanel4;

    private Font font = new Font("Candara", 0, 20); //Custom page font
    private Controller controller;

    private SpringLayout theLayout = new SpringLayout();


    public InventoryPage(Controller in_controller){

        this.controller = in_controller;

        //Create the layout and populate the main panel.
        setLayout(theLayout);

        //Build sub panel #1.
        buildSubPanel1();

        //Build sub panel #2.
        buildSubPanel2();

        //Build sub panel #3.
        buildSubPanel3();

        buildTable();
    }

    private void buildSubPanel1()
    {
        subPanel1 = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.4, getFrameSizeHeight() * 0.5);
        theLayout.putConstraint(SpringLayout.WEST, subPanel1, 0, SpringLayout.WEST, this);

        labelProductCode = new JLabel("Product Code: ", JLabel.TRAILING);
        labelProductCode.setFont(font);
        subPanel1.add(labelProductCode);

        fieldProductCode = new JTextField(10);
        fieldProductCode.setFont(new Font("Arial", Font.BOLD, 20));
        fieldProductCode.setHorizontalAlignment(JTextField.CENTER);
        labelProductCode.setLabelFor(fieldProductCode);
        subPanel1.add(fieldProductCode);

        labelProductName = new JLabel("Product Name: ", JLabel.TRAILING);
        labelProductName.setFont(font);
        subPanel1.add(labelProductName);

        fieldProductName = new JTextField(10);
        fieldProductName.setFont(new Font("Candara", Font.BOLD, 15));
        fieldProductName.setHorizontalAlignment(JTextField.CENTER);
        labelProductName.setLabelFor(fieldProductName);
        subPanel1.add(fieldProductName);

        labelProductPrice = new JLabel("Product Price: ", JLabel.TRAILING);
        labelProductPrice.setFont(font);
        subPanel1.add(labelProductPrice);

        fieldProductPrice = new JTextField(10);
        fieldProductPrice.setFont(new Font("Arial", Font.BOLD, 20));
        fieldProductPrice.setHorizontalAlignment(JTextField.CENTER);
        labelProductPrice.setLabelFor(fieldProductPrice);
        subPanel1.add(fieldProductPrice);


        labelAmount = new JLabel("Number of Items: ", JLabel.TRAILING);
        labelAmount.setFont(font);
        subPanel1.add(labelAmount);

        fieldAmount = new JTextField(10);
        fieldAmount.setFont(new Font("Arial", Font.BOLD, 20));
        fieldAmount.setHorizontalAlignment(JTextField.CENTER);
        labelAmount.setLabelFor(fieldAmount);
        subPanel1.add(fieldAmount);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                4, 2, //rows, cols
                6, 20,        //initX, initY
                10, 40);       //xPad, yPad

        add(subPanel1);
        // TODO: 04/09/2018 check fields input
    }

    private void buildSubPanel2() {
        subPanel2 = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.4, getFrameSizeHeight() * 0.2);

        theLayout.putConstraint(SpringLayout.WEST, subPanel2, 0, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);

        btnRemoveProduct = new CJButton("Remove", font);
        subPanel2.add(btnRemoveProduct);

        btnAddProduct = new CJButton("Add", font);
        subPanel2.add(btnAddProduct);

        //Add to inventory button pressed
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        if(!fieldAmount.getText().isEmpty() && !fieldProductCode.getText().isEmpty() && !fieldProductName.getText().isEmpty() && !fieldProductPrice.getText().isEmpty()) {
                            addToInventory();
                            JOptionPane.showMessageDialog(new JFrame(), "New item " + fieldProductName.getText() + " was added successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                            clearPageFields();
                        } else
                            JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }.execute();
            }
        });

        //Remove product button pressed
        btnRemoveProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        if(!fieldProductCode.getText().isEmpty() && !fieldAmount.getText().isEmpty()) {
                            removeFromInventory();
                            clearPageFields();
                        } else{
                            JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }
                }.execute();
            }
        });

        SpringUtilities.makeGrid(subPanel2, 1, 2, 50, 10, 30, 6);

        add(subPanel2);
    }

    private void buildSubPanel3()
    {
        subPanel3 = new CJPanel(new SpringLayout(), getFrameSizeWidth(), getFrameSizeHeight() * 0.3);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel3, 0, SpringLayout.SOUTH, subPanel2);

        btnFinish = new CJButton("Finish", new Font("Candara", 0, 40));
        btnFinish.setPreferredSize(new Dimension(300, 150));
        subPanel3.add(btnFinish);

        //Finish button was pressed
        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // TODO: 01/09/2018 apply changes to DB... currently not working.
                //cashierBL.createNewOrder(inventory, chosenClient, shoppingCart);
                clearPageFields();
                setVisible(false);
                inventoryBL.createNewOrder(inventory);
                try {
                    controller.showMainMenuPage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnCancel = new CJButton("Cancel", new Font("Candara", 0, 40));
        btnCancel.setPreferredSize(new Dimension(300, 150));
        subPanel3.add(btnCancel);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPageFields();
                setVisible(false);
                try {
                    controller.showMainMenuPage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        SpringUtilities.makeCompactGrid(subPanel3, 1, 2, 100, 60, 100, 60);
        add(subPanel3);
    }

    private void buildTable() {
        //Defining table headers and columns type
        String[] colNames = {"Product Code", "Product Name", "Number of Items", "Price", "Total"};
        Class[] colClasses = {Integer.class, String.class, Integer.class, Integer.class, Integer.class};

        pTableModel = new ProductTableModel(colNames, colClasses);
        productTable = new JTable(pTableModel);
        productTable.setFillsViewportHeight(true);

        subPanel4 = new JScrollPane(productTable);
        subPanel4.setPreferredSize(new Dimension((int) (getFrameSizeWidth() * 0.6), (int) (getFrameSizeHeight() * 0.6)));

        theLayout.putConstraint(SpringLayout.NORTH, subPanel4, 0, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.EAST, subPanel4, 0, SpringLayout.EAST, this);

        updateTable();

        add(subPanel4);
    }

    private void updateTable() {
        //Clear old data stored in table
        pTableModel.clearData();

        //Get new data from shopping cart and send it into table modal data vector.
        inventory.getMyInventory().entrySet().forEach(entry -> {
            pTableModel.addToVectorM_Data(entry.getValue());
        });

        //update table graphics
        pTableModel.fireTableDataChanged();

        productTable.validate();
        productTable.repaint();
    }

    private void removeFromInventory() {
        try {
            int productCode = Integer.parseInt(fieldProductCode.getText());
            int amount = Integer.parseInt(fieldAmount.getText());

            Product p = inventory.takeFromInventory(productCode, amount);
            JOptionPane.showMessageDialog(new JFrame(), fieldAmount.getText() + " item/s of product code - " + fieldProductCode.getText() + " were removed successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            updateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }

    }

    private void addToInventory() {
        try {
            String pName = fieldProductName.getText();
            double pPrice = Double.parseDouble(fieldProductPrice.getText());
            int pAmount = Integer.parseInt(fieldAmount.getText());
            int pCode = Integer.parseInt(fieldProductCode.getText());

            Product p = new Product(pName, pPrice, pAmount, pCode);

            inventory.addToInventory(p);
            updateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }
    }

    private void clearPageFields() {
        fieldProductCode.setText("");
        fieldProductName.setText("");
        fieldProductPrice.setText("");
        fieldAmount.setText("");
    }

    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
