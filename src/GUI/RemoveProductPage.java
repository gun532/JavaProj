package GUI;

import BL.AuthService;
import Entities.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


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

    private int inventoryCode = AuthService.getInstance().getCurrentEmployee().getBranchNumber();

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

        setTitle("Remove Product");

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

        fieldProductAmount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (fieldProductAmount.getText().length() >= 10 || e.getKeyChar() < '0' || e.getKeyChar() > '9') // limits text field to 9 characters
                        e.consume();
            }
        });

        btnCancel.addActionListener(e -> {
            setVisible(false);
        });

        btnRemove.addActionListener(e -> {


          });
    }


}
