package GUI;

import BL.*;
import DAL.ClientsDataAccess;
import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;
import DTO.InventoryDto;
import DTO.ProductDto;
import Entities.Employee.*;
import Entities.Inventory;
import Entities.Product;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class InventoryPage extends CJPanel {

    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Inventory inventory =new Inventory();// = managerBL.getCashierBL().selectFromInventory(emp.getBranchNumber());

    private CJPanel subPanel1;
    private JTextField searchField = new JTextField("Search Product...");

    private CJButton btnAddProduct;
    private CJButton btnRemoveProduct;
    private CJButton btnUpdateProduct;

    private CJPanel subPanel2;
    private CJButton btnFinish;
    private CJButton btnCancel;

    //Defining table headers and columns type
    private String[] colNames = {"Product Code", "Product Name", "Number of Items", "Price", "Total"};
    private Class[] colClasses = {Integer.class, String.class, Integer.class, Integer.class, Integer.class};

    private ProductTableModel pTableModel = new ProductTableModel(colNames, colClasses);
    private JTable productTable;
    private JScrollPane subPanel3;
    private TableRowSorter<ProductTableModel> sorter = new TableRowSorter<>(pTableModel);

    private Font font = new Font("Candara", 0, 20); //Custom page font
    private Controller controller;

    private SpringLayout theLayout = new SpringLayout();

    private Product chosenProduct;

    private RemoveProductPage removeProductPage;
    private AddNewProductPage addNewProductPage;
    private UpdateProductPage updateProductPage;



    public InventoryPage(Controller in_controller){

        this.controller = in_controller;

        //Create the layout and populate the main panel.
        setLayout(theLayout);

        buildTable();

        //Build sub panel #1.
        buildSubPanel1();

        //Build sub panel #2.
        buildSubPanel2();

        //productTable.setRowSorter(sorter);
    }

    private void buildSubPanel1() {
        subPanel1 = new CJPanel(new SpringLayout(), getFrameSizeWidth() * 0.95, getFrameSizeHeight() * 0.09);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel1, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.SOUTH, subPanel3);


        subPanel1.add(searchField);

        btnAddProduct = new CJButton("Add", font);
        subPanel1.add(btnAddProduct);

        btnRemoveProduct = new CJButton("Remove", font);
        subPanel1.add(btnRemoveProduct);

        btnUpdateProduct = new CJButton("Update", font);
        subPanel1.add(btnUpdateProduct);

        searchField.setFont(font);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates
            }
        });

        //Add to inventory button pressed
        btnAddProduct.addActionListener(e -> new SwingWorker() {
            @Override
            protected Object doInBackground() {
                addNewProductPage = new AddNewProductPage(controller);
                return null;
            }
        }.execute());

        //Remove product button pressed
        btnRemoveProduct.addActionListener(e -> new SwingWorker() {
            @Override
            protected Object doInBackground() {
                if (chosenProduct != null) {
                    //removeProductPage = new RemoveProductPage(controller);

                    removeProductFromInventory(chosenProduct, emp.getBranchNumber());
                    updateTable();

                    chosenProduct = null;
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Please chose a product from the table, to be removed.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        }.execute());

        btnUpdateProduct.addActionListener(e -> {
            //if first time entry -> create the page
            if (chosenProduct != null) {
                updateProductPage = new UpdateProductPage(controller, chosenProduct);
                updateProductPage.setVisible(true);
                chosenProduct = null;
            }else{
                JOptionPane.showMessageDialog(new JFrame(), "Please choose a product from table, to be updated", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        subPanel1.add(btnUpdateProduct);

        SpringUtilities.makeCompactGrid(subPanel1, 1, 4, 20, 10, 5, 0);

        add(subPanel1);
    }

    private void buildSubPanel2() {
        subPanel2 = new CJPanel(new SpringLayout(), getFrameSizeWidth(), getFrameSizeHeight() * 0.3);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel2, 0, SpringLayout.SOUTH, subPanel1);

        btnFinish = new CJButton("Finish", new Font("Candara", 0, 40));
        btnFinish.setPreferredSize(new Dimension(300, 150));
        subPanel2.add(btnFinish);

        //Finish button was pressed
        btnFinish.addActionListener(e -> {
            // TODO: 01/09/2018 apply changes to DB... currently not working.
            setVisible(false);
            //inventoryBL.createNewOrder(inventory);
            try {
                controller.showMainMenuPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnCancel = new CJButton("Cancel", new Font("Candara", 0, 40));
        btnCancel.setPreferredSize(new Dimension(300, 150));
        subPanel2.add(btnCancel);

        btnCancel.addActionListener(e -> {
            setVisible(false);
            try {
                controller.showMainMenuPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 100, 60, 100, 60);
        add(subPanel2);
    }

    private void buildTable() {

        productTable = new JTable(pTableModel);
        productTable.setFillsViewportHeight(true);
        productTable.setRowSorter(sorter);


        subPanel3 = new JScrollPane(productTable);
        subPanel3.setPreferredSize(new Dimension((int) (getFrameSizeWidth() * 0.95), (int) (getFrameSizeHeight() * 0.6)));

        theLayout.putConstraint(SpringLayout.NORTH, subPanel3, 20, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel3, 0, SpringLayout.HORIZONTAL_CENTER, this);

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 1) {
                    try {
                        chooseProductFromTable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        updateTable();

        add(subPanel3);
    }

    private void updateTable() {
        //Clear old data stored in table
        pTableModel.clearData();

        createInventory();

        //Get new data from shopping cart and send it into table modal data vector.
        inventory.getMyInventory().entrySet().forEach(entry -> {
            pTableModel.addToVectorM_Data(entry.getValue());
        });

        //update table graphics
        pTableModel.fireTableDataChanged();

        productTable.validate();
        productTable.repaint();
    }

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

//    public void removeFromInventory(int productCode, int amount) {
//        try {
//
//            inventory.takeFromInventory(productCode, amount);
//            updateTable();
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
//            //e.printStackTrace();
//        }
//
//    }

    public void addToInventory(Product p) {
        try {

            inventory.addToInventory(p);
            JOptionPane.showMessageDialog(new JFrame(), "New product: " + p.getName() + " was added successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
            updateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }
    }

    public void updateInventory(Product p){
        try {
            inventory.updateInventory(p);
            updateTable();

        }catch (Exception e){

        }
    }

    private void chooseProductFromTable() throws Exception {

        // get the selected row index
        int selectedRowIndex = productTable.getSelectedRow();
        selectedRowIndex = productTable.convertRowIndexToModel(selectedRowIndex);

        // set the selected row data into Client
        int pCode = (int) (pTableModel.getValueAt(selectedRowIndex, 0));
        String pName = (pTableModel.getValueAt(selectedRowIndex, 1).toString());
        int pAmount = (int) (pTableModel.getValueAt(selectedRowIndex, 2));
        double pPrice = (double) (pTableModel.getValueAt(selectedRowIndex, 3));

        //Build Product object
        this.chosenProduct = new Product(pName,pPrice,pAmount,pCode);
    }

    public Product getChosenProduct() { return chosenProduct; }

    private void removeProductFromInventory(Product product, int inventoryCode) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            ProductDto productDto = new ProductDto("removeProductFromInventory", product.getName(),product.getPrice(),
                    product.getAmount(),product.getProductCode(), inventoryCode);
            out.println(gson.toJson(productDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();

            if (response.equals("true")) {
                //setVisible(false);

                //controller.getInventoryPage().removeFromInventory(chosenProduct.);
                JOptionPane.showMessageDialog(new JFrame(), "product with code - " + chosenProduct.getProductCode() + " were removed successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Error removing product to DB", "Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
