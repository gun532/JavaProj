package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    private JFrame appFrame;
    //Take user screen size
    private int screenSizeWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screenSizeHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().height;

    private ImageIcon icon;
    private Image innerPageImage;

    private MainMenuPage mainMenuPage;
    private NewOrderPanel newOrderPanel;
    private ClientPage clientPage;
    private InventoryPage inventoryPage;
    private EmployeesPage employeesPage;
    private Login login;

    // Controller constructor holds all the app pages (panels)
    public Controller() throws Exception { }

    public void loadApp() throws IOException {

        appFrame = new JFrame();
        buildAppFrame();

        showLoginPage();
    }

    public void showLoginPage() throws IOException {

        appFrame.setSize((int)(screenSizeWidth* 0.45),(int)(screenSizeHeight* 0.4));
        appFrame.setLocationRelativeTo(null);

        login = new Login(this);

        appFrame.setTitle("Login Page");
        appFrame.setContentPane(login);
        appFrame.setVisible(true);
    }

    public void showMainMenuPage() throws IOException {

        if (this.mainMenuPage == null) { //if first time opened
            this.mainMenuPage = new MainMenuPage(this);

            int frameSizeWidth = (int) (screenSizeWidth * 0.5);
            int frameSizeHeight = (int) (screenSizeHeight * 0.8);

            appFrame.setSize(frameSizeWidth, frameSizeHeight);
            appFrame.setLocationRelativeTo(null);

            appFrame.setTitle("Main Menu Page");
            mainMenuPage.setVisible(true);
            appFrame.setContentPane(mainMenuPage);

            //build in the meanwhile the New Order page in a new thread
            Controller controller = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        newOrderPanel = new NewOrderPanel(controller);
                        innerPageImage = ImageIO.read(new File("src/GUI/Res/inner_BG.jpg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else { //main page already exists...
            appFrame.setTitle("Main Menu Page");
            mainMenuPage.setVisible(true);
            appFrame.setContentPane(mainMenuPage);
        }
    }

    public void showNewOrderPanel() {
        try{
            newOrderPanel = new NewOrderPanel(this);
            appFrame.setTitle("New Order" + "                 " + new SimpleDateFormat("dd/MM/yy" + "           " + "HH:mm").format(new Date()));
            appFrame.setContentPane(newOrderPanel);
        }
        catch (Exception e)
        {
            //TODO:write to logger
            e.printStackTrace();
        }
    }

    public void showClientPage() {
        try{
            clientPage = new ClientPage(this);
            clientPage.setVisible(true);
        }
        catch (Exception e)
        {
            //TODO:write to logger
            e.printStackTrace();
        }
    }

    public void showInventoryPage() {
        try{
            inventoryPage = new InventoryPage(this);
            appFrame.setTitle("Inventory");
            inventoryPage.setVisible(true);
            appFrame.setContentPane(inventoryPage);
        }
        catch (Exception e)
        {
            //TODO:write to logger
            e.printStackTrace();
        }
    }

    public void showEmployeesPage() {
        try {
            employeesPage = new EmployeesPage(this);
            employeesPage.setVisible(true);

        } catch (Exception e) {
            //TODO:write to logger
            e.printStackTrace();
        }
    }

    public void buildAppFrame(){

        //Build the frame
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Get and set app image icon
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Get logo icon
                URL iconURL = getClass().getResource("Res/appLogo.png"); // iconURL is null when not found
                icon = new ImageIcon(iconURL);
                appFrame.setIconImage(icon.getImage());
            }
        });
    }

    public static void main(String[] args) throws Exception {

        //Run GUI in a thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Controller().loadApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NewOrderPanel getNewOrderPanel() {
        return newOrderPanel;
    }

    public ClientPage getClientPage() {
        return clientPage;
    }

    public EmployeesPage getEmployeesPage() {
        return employeesPage;
    }

    public MainMenuPage getMainMenuPage() {
        return mainMenuPage;
    }

    public JFrame getAppFrame() {
        return appFrame;
    }

    public Image getInnerPageImage() {
        return innerPageImage;
    }
}
