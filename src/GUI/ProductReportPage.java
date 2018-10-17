package GUI;

import BL.AuthService;
import DTO.InventoryDto;
import Entities.Employee.Employee;
import Entities.Inventory;
import Entities.Product;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ProductReportPage extends JFrame{
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel;

    private Inventory inventory =new Inventory();

    //Defining table headers and columns type
    private String[] colNames = {"Product Code", "Product Name", "Number of Items", "Price", "Total"};
    private Class[] colClasses = {Integer.class, String.class, Integer.class, Integer.class, Integer.class};

    private ProductTableModel pTableModel = new ProductTableModel(colNames, colClasses);
    private JTable productTable;
    private JScrollPane scrollPane;

    private Product chosenProduct;

    private Employee emp = AuthService.getInstance().getCurrentEmployee();

    public ProductReportPage(Controller in_controller){

        this.controller = in_controller;
        setIconImage(controller.getAppFrame().getIconImage());

        setTitle("Report by Product");
        setLayout(theLayout);

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.8);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.8);

        setSize(frameSizeWidth, frameSizeHeight);
        setLocationRelativeTo(null);

        mainPanel = new CJPanel(theLayout);

        buildTable();

        setContentPane(mainPanel);
    }

    private void buildTable() {

        productTable = new JTable(pTableModel);
        productTable.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(productTable);
        scrollPane.setPreferredSize(new Dimension((int) (frameSizeWidth * 0.95), (int) (frameSizeHeight * 0.95)));

        theLayout.putConstraint(SpringLayout.NORTH, scrollPane, 20, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollPane, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);

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

        mainPanel.add(scrollPane);
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

    private void chooseProductFromTable() throws Exception {

        // get the selected row index
        int selectedRowIndex = productTable.getSelectedRow();
        selectedRowIndex = productTable.convertRowIndexToModel(selectedRowIndex);

        // set the selected row data into BL.Client
        int pCode = (int) (pTableModel.getValueAt(selectedRowIndex, 0));
        String pName = (pTableModel.getValueAt(selectedRowIndex, 1).toString());
        int pAmount = (int) (pTableModel.getValueAt(selectedRowIndex, 2));
        double pPrice = (double) (pTableModel.getValueAt(selectedRowIndex, 3));

        //Build Product object
        this.chosenProduct = new Product(pName,pPrice,pAmount,pCode);

        // TODO: 17/10/2018 open word file with the report of the chosen product.

    }
}