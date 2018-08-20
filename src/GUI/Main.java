package GUI;

import javax.swing.*;

public class Main extends JFrame {
    public Main(){
        createAndRunLoginPage();
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

    public static void createAndRunLoginPage(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage();
            }
        });
    }
}
