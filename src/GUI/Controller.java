package GUI;

import BL.Channel;
import BL.GlobalLogger;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

//This project was created by Guy Cohen - ID 307992958 and Roy Bar - ID 304989171
public class Controller {
    private JFrame appFrame;
    //Take user screen size
    private int screenSizeWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screenSizeHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    private ImageIcon icon;
    private Image innerPageImage;

    private MainMenuPage mainMenuPage;
    private NewOrderPanel newOrderPanel;
    private ClientPage clientPage;
    private InventoryPage inventoryPage;
    private EmployeesPage employeesPage;
    private ReportsPage reportsPage;
    private ChatPage chatPage;


    private final String host = "localhost";
    private final int port = 9999;
    public static SSLSocket echoSocket;
//    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private GlobalLogger log = new GlobalLogger("logs.log");

    // Controller constructor holds all the app pages (panels)
    public Controller() {

    }

    public void loadApp() throws IOException {
//        Thread connThread = new Thread(Controller);
//        connThread.start();
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null,null,null);

            //establish ssl connection with the server
            echoSocket = (SSLSocket) context.getSocketFactory().getDefault().createSocket(host, port);
            System.out.println("Connecting to host " + host + " on port " + port + ".");
            echoSocket.setEnabledCipherSuites(echoSocket.getSupportedCipherSuites());
            appFrame = new JFrame();

            buildAppFrame();
            showLoginPage();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

//        new Controller(host, port);

    }

    public void showLoginPage() throws IOException {

        Login login;
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
            SwingUtilities.invokeLater(() -> {
                try {
                    newOrderPanel = new NewOrderPanel(controller);
                    innerPageImage = ImageIO.read(new File("src/GUI/Res/inner_BG.jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
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
            log.logger.severe(e.getMessage());
        }
    }

    public void showClientPage() {
        try{
            clientPage = new ClientPage(this);
            clientPage.setVisible(true);
        }
        catch (Exception e)
        {
            log.logger.severe(e.getMessage());
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
            log.logger.severe(e.getMessage());
        }
    }

    public void showBranchPage() {
        BranchPage branchPage;
        try{
            branchPage = new BranchPage(this);
            appFrame.setTitle("Branch Details");
            branchPage.setVisible(true);
            appFrame.setContentPane(branchPage);
        }
        catch (Exception e)
        {
            log.logger.severe(e.getMessage());
        }
    }

    public void showReportsPage() {
        try{
            reportsPage = new ReportsPage(this);
            appFrame.setTitle("Reports");
            reportsPage.setVisible(true);
            appFrame.setContentPane(reportsPage);
        }
        catch (Exception e)
        {
            log.logger.severe(e.getMessage());
        }
    }

    public void showEmployeesPage() {
        try {
            employeesPage = new EmployeesPage(this);
            employeesPage.setVisible(true);

        } catch (Exception e) {
            log.logger.severe(e.getMessage());
        }
    }

    public void showChatPage() {

        chatPage = new ChatPage(this);
        appFrame.setTitle("Chat");
        chatPage.setVisible(true);
        appFrame.setContentPane(chatPage);
    }

    public void buildAppFrame(){

        //Build the frame
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);
        appFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

        appFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    try {
                        echoSocket.close(); //closes the connection to the server when a window is closed
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
//        new Controller(host, port, this);
    }


    public static void main(String[] args) throws Exception {

        //Run GUI in a thread
        SwingUtilities.invokeLater(() -> {
            try {
                new Controller().loadApp();
            } catch (Exception e) {
                e.printStackTrace();
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

    public InventoryPage getInventoryPage() { return inventoryPage; }

    public JFrame getAppFrame() {
        return appFrame;
    }

    public Image getInnerPageImage() {
        return innerPageImage;
    }
}
