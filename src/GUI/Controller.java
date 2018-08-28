package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    private JFrame appFrame;
    private EmployeesMenuPage employeesMenuPage;
    private NewOrderPanel newOrderPanel;
    private Login login;

    // Controller constructor holds all the app pages (panels)
    public Controller() throws Exception {

        login = new Login(this);
    }

    public void loadApp() {

        appFrame = new JFrame();
        buildAppFrame();

        employeesMenuPage = new EmployeesMenuPage(this);
        showEmployeesMenuPage();

        appFrame.setVisible(true);
    }

    public void showEmployeesMenuPage() {
        appFrame.setTitle("Employees Menu Page");

        employeesMenuPage.setVisible(true);
        appFrame.setContentPane(employeesMenuPage);
    }

    public void showNewOrderPanel() {
        try{
            newOrderPanel = new NewOrderPanel(this);
            appFrame.setTitle("New Order" + "                 " + new SimpleDateFormat("dd/MM/yy" + "           " + "HH:mm").format(new Date()));
            newOrderPanel.setVisible(true);
            appFrame.setContentPane(newOrderPanel);
        }
        catch (Exception e)
        {
            //TODO:write to logger
            e.printStackTrace();
        }

    }

    public void buildAppFrame(){
        //Take user screen size
        int screenSizeWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenSizeHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().height;

        //Build the frame
        appFrame.setTitle("App Main Frame");
        int frameSizeWidth = (int)(screenSizeWidth*0.5);
        int frameSizeHeight = (int)(screenSizeHeight*0.8);
        appFrame.setSize(frameSizeWidth,frameSizeHeight);
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Get logo icon
        URL iconURL = getClass().getResource("appLogo.png"); // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        appFrame.setIconImage(icon.getImage());
    }

    public NewOrderPanel getNewOrderPanel() {
        return newOrderPanel;
    }

    public static void main(String[] args) throws Exception {

        //Run GUI in a thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Controller controller = new Controller();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JFrame getAppFrame() {
        return appFrame;
    }
}
