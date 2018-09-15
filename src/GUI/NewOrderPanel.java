package GUI;

import BL.AuthService;
import DTO.InventoryDto;
import DTO.OrderDto;
import Entities.Clients.Client;
import Entities.Clients.ClientType;
import Entities.Deal;
import Entities.Inventory;
import Entities.Product;
import Entities.Employee.Employee;
import Entities.ShoppingCart;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class NewOrderPanel extends CJPanel {
    private JLabel labelProduct;
    private JTextField productCodeField;

    private JLabel labelAmount;
    private JTextField amountField;

    private CJButton btnAddProduct;
    private CJButton btnRemoveProduct;
    private CJButton btnChooseClient;

    private JLabel labelChosenClient;
    private JTextField fieldChosenClient;

    private CJButton btnFinish;
    private CJButton btnCancel;

    private Font font = new Font("Candara", 0, 20); //Custom page font
    private Controller controller;

    private SpringLayout theLayout = new SpringLayout();

    private ProductTableModel pTableModel;
    private JTable productTable;
    private JScrollPane subPanel4;

    private JTextField fieldInTotal;
    private JTextField fieldDiscount;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Inventory inventory = new Inventory();
    private ShoppingCart shoppingCart = new ShoppingCart(emp);

    private Client chosenClient;
    private ClientPage clientPage;
    private Map<ClientType, Deal> clientsDeals = getClientsDeals();
    private double discountRate;


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
                6, 20,        //initX, initY
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

        SpringUtilities.makeGrid(subPanel2, 1, 2, 50, 10, 30, 6);

        //Create "Choose a Client" button
        btnChooseClient = new CJButton("Choose a Client", font);
        btnChooseClient.setSize(50, 50);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnChooseClient, 0, SpringLayout.HORIZONTAL_CENTER, subPanel2);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnChooseClient, 0, SpringLayout.VERTICAL_CENTER, subPanel2);

        btnChooseClient.addActionListener(e -> controller.showClientPage());
        add(btnChooseClient);

        //Choose a client label and field
        labelChosenClient = new JLabel("Chosen Client: ", JLabel.TRAILING);
        labelChosenClient.setFont(font);
        labelChosenClient.setLabelFor(fieldChosenClient);
        add(labelChosenClient);

        fieldChosenClient = new JTextField();
        fieldChosenClient.setFont(new Font("Canadra", 0, 15));
        fieldChosenClient.setEditable(false);

        labelChosenClient.setLabelFor(fieldChosenClient);
        add(fieldChosenClient);

        theLayout.putConstraint(SpringLayout.WEST, labelChosenClient, 20, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, labelChosenClient, 60, SpringLayout.VERTICAL_CENTER, subPanel2);
        theLayout.putConstraint(SpringLayout.VERTICAL_CENTER, fieldChosenClient, 57, SpringLayout.VERTICAL_CENTER, subPanel2);
        theLayout.putConstraint(SpringLayout.WEST, fieldChosenClient, 10, SpringLayout.EAST, labelChosenClient);

        add(subPanel2);

        //Build sub Panel #3
        CJPanel subPanel3 = new CJPanel(new SpringLayout(), getFrameSizeWidth(), getFrameSizeHeight() * 0.33);
        //subPanel3.setBackground(null);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel3, 0, SpringLayout.SOUTH, subPanel2);

        btnFinish = new CJButton("Finish", new Font("Candara", 0, 40));
        btnFinish.setPreferredSize(new Dimension(300, 150));
        subPanel3.add(btnFinish);

        //Finish button was pressed
        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenClient == null) {
                    JOptionPane.showMessageDialog(new JFrame(), "Please choose a client", "Invalid input", JOptionPane.ERROR_MESSAGE);
                } else {

                    createNewOrder();
                }
            }
        });

        btnCancel = new CJButton("Cancel", new Font("Candara", 0, 40));
        btnCancel.setPreferredSize(new Dimension(300, 150));
        subPanel3.add(btnCancel);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        buildTable();

        //build table bottom field panel
        CJPanel subPanelTable = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.6, getFrameSizeHeight() * 0.06);
        subPanelTable.setBackground(Color.YELLOW);

        theLayout.putConstraint(SpringLayout.NORTH, subPanelTable, 0, SpringLayout.SOUTH, subPanel4);
        theLayout.putConstraint(SpringLayout.EAST, subPanelTable, 0, SpringLayout.EAST, this);
        theLayout.putConstraint(SpringLayout.SOUTH, subPanelTable, 0, SpringLayout.NORTH, subPanel3);

        JButton labelDiscount = new JButton("Discount Rate:");
        labelDiscount.setEnabled(false);
        labelDiscount.setFont(font);
        subPanelTable.add(labelDiscount);

        fieldDiscount = new JTextField();
        fieldDiscount.setFont(new Font("Arial", Font.BOLD, 20));
        fieldDiscount.setHorizontalAlignment(JTextField.CENTER);
        subPanelTable.add(fieldDiscount);

        JButton labelInTotal = new JButton("In Total:");
        labelInTotal.setEnabled(false);
        labelInTotal.setFont(font);
        subPanelTable.add(labelInTotal);

        fieldInTotal = new JTextField();
        fieldInTotal.setFont(new Font("Arial", Font.BOLD, 20));
        fieldInTotal.setHorizontalAlignment(JTextField.CENTER);
        subPanelTable.add(fieldInTotal);


        SpringUtilities.makeCompactGrid(subPanelTable, 1, 4, 6, 0, 0, 0);

        add(subPanelTable);

        chosenClient = controller.getMainMenuPage().getChosenClient();
        if (chosenClient == null)
            fieldChosenClient.setText("Please choose a client");
        else {
            fieldChosenClient.setText(chosenClient.getFullName());
            updateShoppingCartDeal();
        }

        createInventory();

    }

    private void createNewOrder() {
            try {
                PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                Gson gson = new Gson();

                OrderDto orderDto = new OrderDto("createNewOrder", inventory, chosenClient.getId(), shoppingCart, Double.parseDouble(fieldInTotal.getText()));

                out.println(gson.toJson(orderDto));

                DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                String response = in.readLine();

                if (response.equals("false")) {
                    JOptionPane.showMessageDialog(new JFrame(), "can't create order", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    setVisible(false);
                    try {
                        controller.showMainMenuPage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }


    //Class functions
    private void createInventory() {

            try {
                PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                Gson gson = new Gson();

                InventoryDto inventoryDto = new InventoryDto("inverntoryByBranch", inventory.getMyInventory(),
                        emp.getBranchNumber(), inventory.getTotalProducts(), inventory.getTotalItems(), inventory.getTotalValue());
                out.println(gson.toJson(inventoryDto));

                DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                String response = in.readLine();

                if (response.equals("null")) {
                    JOptionPane.showMessageDialog(new JFrame(), "Inventory does no exist", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    inventory = gson.fromJson(response, Inventory.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

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

    private void updateTable() {
        //Clear old data stored in table
        pTableModel.clearData();

        //Get new data from shopping cart and send it into table modal data vector.
        shoppingCart.getCart().entrySet().forEach(entry -> {
            pTableModel.addToVectorM_Data(entry.getValue());
        });

        if (discountRate != 0)
            fieldInTotal.setText(String.format("%1$,.2f", shoppingCart.getTotalPrice() * discountRate));
        else
            fieldInTotal.setText(String.format("%1$,.2f", shoppingCart.getTotalPrice()));

        //update table graphics
        pTableModel.fireTableDataChanged();

        productTable.validate();
        productTable.repaint();
    }

    public void setChosenClient(Client chosenClient) {
        this.chosenClient = chosenClient;
    }

    public JTextField getFieldChosenClient() {
        return fieldChosenClient;
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

        add(subPanel4);
    }

    public void updateShoppingCartDeal() {
        Deal currentDeal = this.clientsDeals.get(chosenClient.getType());

        // TODO: may need to add a discount method to ShoppingCart class for saving and displaying the correct order total price in DB.
        fieldDiscount.setText(String.format("%,.0f%%", currentDeal.getDiscount()));
        discountRate = (100 - currentDeal.getDiscount()) / 100;

        // TODO: add free gits to inventory and then uncomment this section
//        new SwingWorker() { //Open a new thread for add to cart.
//            @Override
//            protected Object doInBackground() throws Exception {
//                for(int n:currentDeal.getGifts()) {
//                    addToCart(n,1);
//                }
//                return null;
//            }
//        }.execute();

        updateTable();
    }

    private Map<ClientType, Deal> getClientsDeals() {

        Map<ClientType, Deal> mapClientDeals = new LinkedHashMap<>();

        //need to get Deals from a data source

        //---Example Data---------------------------------
        Deal deal1 = new Deal(0);
        mapClientDeals.put(ClientType.NEWCLIENT, deal1);

        Deal deal2 = new Deal(30);
        deal2.addGiftToDeal(1232323);
        mapClientDeals.put(ClientType.RETURNCLIENT, deal2);

        Deal deal3 = new Deal(50);
        deal3.addGiftToDeal(2254323);
        mapClientDeals.put(ClientType.VIPCLIENT, deal3);
        //----------------------------------------------

        return mapClientDeals;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(controller.getInnerPageImage(), 0, 0, null);
    }
}
