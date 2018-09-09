package GUI;

import Entities.Product;

import javax.swing.*;
import java.awt.*;

public class RemoveProductPage extends JFrame {

    private Controller controller;

    private int frameSizeHeight;
    private int frameSizeWidth;

    private SpringLayout theLayout = new SpringLayout();
    private Font font = new Font("Candara", 0, 20); //Custom page font

    private JLabel labelProductCode = new JLabel("Product Code:", JLabel.TRAILING);
    private JTextField fieldProductCode = new JTextField(10);

    private JLabel labelAmount = new JLabel("How many to be removed? ", JLabel.TRAILING);
    private JTextField fieldProductAmount = new JTextField(10);

    private CJButton btnRemove = new CJButton("Remove", font);
    private CJButton btnCancel = new CJButton("Cancel", font);

    private CJPanel mainPanel;

    private Product chosenProduct;

    public RemoveProductPage(Controller in_controller) {

        this.controller = in_controller;
        chosenProduct = controller.getInventoryPage().getChosenProduct();

        setIconImage(controller.getAppFrame().getIconImage());

        //Get and set new frame size
        frameSizeWidth = (int) (controller.getAppFrame().getWidth() * 0.5);
        frameSizeHeight = (int) (controller.getAppFrame().getHeight() * 0.2);

        setSize(frameSizeWidth, frameSizeHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        setTitle("Add New Employee");

        mainPanel = new CJPanel(theLayout, frameSizeWidth*0.9, frameSizeHeight*0.9);

        labelProductCode.setFont(font);
        mainPanel.add(labelProductCode);

        fieldProductCode.setFont(new Font("Arial", Font.BOLD, 20));
        fieldProductCode.setHorizontalAlignment(JTextField.CENTER);
        fieldProductCode.setText(String.valueOf(chosenProduct.getProductCode()));
        fieldProductCode.setEditable(false);
        labelProductCode.setLabelFor(fieldProductCode);
        mainPanel.add(fieldProductCode);


        labelAmount.setFont(font);
        mainPanel.add(labelAmount);

        fieldProductAmount.setFont(new Font("Arial", Font.BOLD, 20));
        fieldProductAmount.setText(String.valueOf(chosenProduct.getAmount()));
        fieldProductAmount.setHorizontalAlignment(JTextField.CENTER);
        labelAmount.setLabelFor(fieldProductAmount);
        mainPanel.add(fieldProductAmount);

        mainPanel.add(btnRemove);
        mainPanel.add(btnCancel);

        SpringUtilities.makeCompactGrid(mainPanel, 3, 2, 5, 5, 5, 5);

        setContentPane(mainPanel);
        setVisible(true);

        btnCancel.addActionListener(e -> {
            setVisible(false);
        });

        btnRemove.addActionListener(e -> {
            setVisible(false);
            controller.getInventoryPage().removeFromInventory(Integer.parseInt(fieldProductCode.getText()),Integer.parseInt(fieldProductAmount.getText()));
            JOptionPane.showMessageDialog(new JFrame(), fieldProductAmount.getText() + " item/s of product code - " + fieldProductCode.getText() + " were removed successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}