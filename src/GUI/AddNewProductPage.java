package GUI;

import BL.AuthService;
import BL.CashierBL;
import BL.ClientSocket;
import DAL.ClientsDataAccess;
import DTO.ProductArrayDto;
import DTO.ProductDto;
import Entities.Clients.Client;
import Entities.Clients.ClientType;
import Entities.Product;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class AddNewProductPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();

    private JLabel labelProductName = new JLabel("Product Name:", JLabel.TRAILING);
    private JTextField fieldProductName = new JTextField(10);

    private JLabel labelPrice = new JLabel("Price:", JLabel.TRAILING);
    private JTextField fieldPrice = new JTextField(10);

    private JLabel labelAmount = new JLabel("Number of items:", JLabel.TRAILING);
    private JTextField fieldAmount = new JTextField(10);

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnAdd = new CJButton("Add", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private int inventoryCode = AuthService.getInstance().getCurrentEmployee().getBranchNumber();
    private ArrayList<Product> listOfProducts = new ArrayList<>();


    public AddNewProductPage(Controller in_controller) {
        this.controller = in_controller;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.5);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Add New Product");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.6);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelProductName.setFont(font);
        subPanel1.add(labelProductName);

        fieldProductName.setFont(font);
        fieldProductName.setHorizontalAlignment(JTextField.CENTER);
        labelProductName.setLabelFor(fieldProductName);
        subPanel1.add(fieldProductName);

        labelPrice.setFont(font);
        subPanel1.add(labelPrice);

        fieldPrice.setFont(new Font("Arial", Font.BOLD, 20));
        fieldPrice.setHorizontalAlignment(JTextField.CENTER);
        labelPrice.setLabelFor(fieldPrice);
        subPanel1.add(fieldPrice);

        labelAmount.setFont(font);
        subPanel1.add(labelAmount);

        fieldAmount.setFont(new Font("Arial", Font.BOLD, 20));
        fieldAmount.setHorizontalAlignment(JTextField.CENTER);
        labelAmount.setLabelFor(fieldAmount);
        subPanel1.add(fieldAmount);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                3, 2, //rows, cols

                6, 6,        //initX, initY
                10, 20);       //xPad, yPad

        mainPanel.add(subPanel1);

        /*---Fields Input Checks-------------------------------------*/

        fieldProductName.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldProductName.getText().length() >= 40 || !((e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') || (e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z'))) {
                    if (e.getKeyChar() != ' ')
                        e.consume();
                }
            }
        });

        fieldPrice.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldPrice.getText().length() >= 9 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    if (e.getKeyChar() != '.')
                        e.consume();
            }
        });

        fieldAmount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldAmount.getText().length() >= 10 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    if (e.getKeyChar() != '-')
                        e.consume();
            }
        });
        /*---------------------------------------------------------------------------------------------------*/


        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);

        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel2, 0, SpringLayout.HORIZONTAL_CENTER, mainPanel);

        subPanel2.add(btnAdd);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 120, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);


        btnAdd.addActionListener(e -> {
            if (!fieldPrice.getText().isEmpty() && !fieldProductName.getText().isEmpty() && !fieldAmount.getText().isEmpty()) {
                String pName = fieldProductName.getText();
                double pPrice = Double.parseDouble(fieldPrice.getText());
                int pAmount = Integer.parseInt(fieldAmount.getText());

                try {
                    Product p;
                    p = existsInProducts(pName);
                    if(p != null) {
                        int success = addProductAmountToInventory(inventoryCode,pName,pAmount);
                        if(success != 0)
                        {
                            controller.getInventoryPage().addToInventory(p);
                            setVisible(false);
                        }
                        else
                            JOptionPane.showMessageDialog(new JFrame(), "Error adding new product to inventory", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                    else
                    {
                        p = new Product(pName, pPrice, pAmount);
                        addNewProduct(p,inventoryCode);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));

        setVisible(true);
    }

    private Product existsInProducts(String pName) {

        try {
            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();

            ProductArrayDto productArrayDto = new ProductArrayDto("selectAllProducts", listOfProducts);
            out.println(gson.toJson(productArrayDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            Product product;

            if (response != null) {
                JSONArray array = new JSONArray(response); //object.getJSONArray("");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject js = array.getJSONObject(i);
                    int productCode = js.getInt("productCode");
                    String name = js.getString("name");
                    double price = js.getDouble("price");
                    product = new Product(productCode, name, price);
                    listOfProducts.add(product);
                }
            }

            for (int i = 0; i < listOfProducts.size(); i++) {
                if(listOfProducts.get(i).getName().equals(pName)) return listOfProducts.get(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int addProductAmountToInventory(int inventoryCode, String pName, int pAmount) {
        try {
            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();

            ProductDto productDto = new ProductDto("addProductAmountToInventory",
                    pName,0,
                    pAmount,0,inventoryCode);
            out.println(gson.toJson(productDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            if (response.equals("null")) {
                //TODO: write succes to logger
                JOptionPane.showMessageDialog(new JFrame(), "Error adding new product to inventory", "Error", JOptionPane.ERROR_MESSAGE);

            } else {
                int productCode = gson.fromJson(response, Integer.class);
                return productCode;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void addNewProduct(Product product, int inventoryCode) {
        try {
            PrintStream out = new PrintStream(ClientSocket.echoSocket.getOutputStream());
            Gson gson = new Gson();

            ProductDto productDto = new ProductDto("addNewProduct", product.getName(),product.getPrice(),
                    product.getAmount(),0, inventoryCode);
            out.println(gson.toJson(productDto));

            DataInputStream in = new DataInputStream(ClientSocket.echoSocket.getInputStream());
            String response = in.readLine();

            if (response.equals("null")) {
                JOptionPane.showMessageDialog(new JFrame(), "Error adding new product to DB", "Error", JOptionPane.ERROR_MESSAGE);

            } else {
                int productCode = addProductAmountToInventory(inventoryCode,product.getName(),product.getAmount());
                product.setProductCode(productCode);
                controller.getInventoryPage().addToInventory(product);
                JOptionPane.showMessageDialog(new JFrame(), "New Product " + product.getName() + " was added successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*---/Page functions methods/-------------------------------------------------------------------------*/

//    private boolean isAlreadyExists() {
//        ArrayList<Client> listClients = controller.getClientPage().getListOfClients();
//        int clientID = Integer.parseInt(fieldPrice.getText());
//
//        for (Client c : listClients) {
//            if (c.getId() == clientID)
//                return true;
//        }
//
//        return false;
//    }
}
