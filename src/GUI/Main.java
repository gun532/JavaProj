package GUI;

import javax.swing.*;

public class Main extends JFrame {
    public Main(){

    }

    public static void main(String[] args) {
        createAndRunLoginPage();
    }

    public static void createAndRunLoginPage(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login loginPage = new Login();
                loginPage.setVisible(true);
            }
        });
    }
}
