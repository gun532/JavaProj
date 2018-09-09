package GUI;

import BL.ManagerBL;
import DAL.ManagerDataAccess;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import Entities.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class UpdateProductPage extends JFrame {
    private Controller controller;

    private int frameSizeWidth;
    private int frameSizeHeight;

    private SpringLayout theLayout = new SpringLayout();
    private CJPanel mainPanel = new CJPanel();

    private JLabel labelProductName = new JLabel("Product Name:", JLabel.TRAILING);
    private JTextField fieldProductName = new JTextField(10);

    private JLabel labelPCode = new JLabel("Product Code:", JLabel.TRAILING);
    private JTextField fieldPCode = new JTextField(10);

    private JLabel labelPrice = new JLabel("Product Price:", JLabel.TRAILING);
    private JTextField fieldProductPrice = new JTextField(10);

    private JLabel labelAmount = new JLabel("Number of Items:", JLabel.TRAILING);
    private JTextField fieldAmount = new JTextField(10);

    private Font font = new Font("Candara", 0, 20); //Custom page font

    private CJButton btnUpdate = new CJButton("Update", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());

    private Product chosenProduct;

    public UpdateProductPage(Controller in_controller, Product chosenProduct) {
        this.controller = in_controller;
        this.chosenProduct = chosenProduct;

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.6);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Edit Product");
        setLayout(theLayout);

        //Build sub panel #1.
        CJPanel subPanel1 = new CJPanel(new SpringLayout(), frameSizeWidth * 0.9, frameSizeHeight * 0.7);

        theLayout.putConstraint(SpringLayout.NORTH, subPanel1, 0, SpringLayout.NORTH, mainPanel);

        labelProductName.setFont(font);
        subPanel1.add(labelProductName);

        fieldProductName.setFont(font);
        fieldProductName.setText(chosenProduct.getName());
        fieldProductName.setHorizontalAlignment(JTextField.CENTER);
        labelProductName.setLabelFor(fieldProductName);
        subPanel1.add(fieldProductName);

        labelPCode.setFont(font);
        subPanel1.add(labelPCode);

        fieldPCode.setFont(new Font("Arial", Font.BOLD, 20));
        fieldPCode.setText(String.valueOf(chosenProduct.getProductCode()));
        fieldPCode.setHorizontalAlignment(JTextField.CENTER);
        fieldPCode.setEditable(false);
        labelPCode.setLabelFor(fieldPCode);
        subPanel1.add(fieldPCode);


        labelPrice.setFont(font);
        subPanel1.add(labelPrice);

        fieldProductPrice.setFont(new Font("Arial", Font.BOLD, 20));
        fieldProductPrice.setText(String.valueOf(chosenProduct.getPrice()));
        fieldProductPrice.setHorizontalAlignment(JTextField.CENTER);
        labelPrice.setLabelFor(fieldProductPrice);
        subPanel1.add(fieldProductPrice);

        labelAmount.setFont(font);
        subPanel1.add(labelAmount);

        fieldAmount.setFont(new Font("Arial", Font.BOLD, 20));
        fieldAmount.setText(String.valueOf(chosenProduct.getAmount()));
        fieldAmount.setHorizontalAlignment(JTextField.CENTER);
        labelAmount.setLabelFor(fieldAmount);
        subPanel1.add(fieldAmount);

        //Lay out sub panel #1.
        SpringUtilities.makeCompactGrid(subPanel1,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                10, 20);       //xPad, yPad

        mainPanel.add(subPanel1);

        fieldProductName.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldProductName.getText().length() >= 40 || !((e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') || (e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z'))) {
                    if (e.getKeyChar() != ' ')
                        e.consume();
                }
            }
        });

        fieldAmount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldPCode.getText().length() >= 9 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    e.consume();
            }
        });

        fieldProductPrice.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldProductPrice.getText().length() >= 10 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                    e.consume();
            }
        });

        //Build Sub panel #2
        CJPanel subPanel2 = new CJPanel(new SpringLayout(), frameSizeWidth, frameSizeHeight * 0.2);

        theLayout.putConstraint(SpringLayout.SOUTH, subPanel2, 0, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,subPanel2,0,SpringLayout.HORIZONTAL_CENTER,mainPanel);

        subPanel2.add(btnUpdate);
        subPanel2.add(btnCancel);

        SpringUtilities.makeCompactGrid(subPanel2, 1, 2, 120, 6, 20, 6);

        mainPanel.add(subPanel2);

        setContentPane(mainPanel);


        btnUpdate.addActionListener(e -> {
            if (!fieldProductName.getText().isEmpty() && !fieldAmount.getText().isEmpty() && !fieldProductPrice.getText().isEmpty()) {
                try {
                    Product p = new Product(fieldProductName.getText(), Double.parseDouble(fieldProductPrice.getText()), Integer.parseInt(fieldAmount.getText()), chosenProduct.getProductCode());
                    controller.getInventoryPage().updateInventory(p);
                    JOptionPane.showMessageDialog(new JFrame(), "Product code - " + p.getProductCode() + " was updated successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "One of the input fields is empty!", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    /*---/Page functions methods/-------------------------------------------------------------------------*/
}
