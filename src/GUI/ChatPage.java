package GUI;

import BL.*;
import DAL.ManagerDataAccess;
import DTO.EmployeeArrayDto;
import DTO.EmployeeDto;
import DTO.SendMessageLaterDto;
import Entities.Employee.*;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class ChatPage extends JPanel{

    private Controller controller;
    private Font font = new Font("Candara", 0, 20); //Custom page font
    private SpringLayout theLayout = new SpringLayout();

    private CJButton btnBack = new CJButton("Back", font);

    private JTextArea textArea = new JTextArea();
    private JScrollPane areaScrollPane = new JScrollPane(textArea);

    private CJPanel textPanel;
    private JTextField textField = new JTextField(10);

    private String[] colNames = {"Emp Number", "Name", "Profession"};
    private Class[] colClasses = {Integer.class, String.class, Profession.class};

    private ContactsTableModel contactsTableModel = new ContactsTableModel(colNames, colClasses);
    private JTable employeesTable = new JTable(contactsTableModel);
    private JScrollPane tablePanel;

    private ArrayList<Employee> listOfEmployees = new ArrayList<>();
    private Map<Integer,ChatMessagePage> chatFramesMap = new LinkedHashMap<>();
    private Employee emp = AuthService.getInstance().getCurrentEmployee();
    private Employee chosenEmployee = null;


    private int myPort;
    Channel channel;


    public ChatPage(Controller in_controller) throws HeadlessException {

        this.controller = in_controller;
        setLayout(theLayout);

        buildContactsTable();

        //buildTextPanel();

        //buildMessageBoard();

        addBackButton();

        Boolean myDetails = searchPeerLocation(emp);
        if (myDetails) {
            try {
                System.out.println("Name : ");
                System.out.println(emp.getName());

                System.out.println("Source Port : ");
                System.out.println(myPort);

                channel = new Channel();
                channel.bind(myPort, this);
                channel.start();
                System.out.println("Binded.");
//                if(checkIfIHaveMessages(emp.getEmployeeNumber()))
                    checkForNewMessages(emp.getEmployeeNumber());

            } catch (SocketException e) {
                e.printStackTrace();
            }


        }
    }

//    private boolean checkIfIHaveMessages

    private void checkForNewMessages(int employeeNumber) {
        checckWhoSent(employeeNumber);
        if(chosenEmployee != null) {
            chatFramesMap.put(chosenEmployee.getEmployeeNumber(), new ChatMessagePage(controller, chosenEmployee, channel));
            chatFramesMap.get(chosenEmployee.getEmployeeNumber()).setVisible(true);
        }

    }

    private void checckWhoSent(int employeeNumber) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            SendMessageLaterDto sendMessageLaterDto = new SendMessageLaterDto("CheckWhoSent", employeeNumber, null);
            out.println(gson.toJson(sendMessageLaterDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();
            if (!response.equals("null"))
            {
                String profession = new JSONObject(response).getString("jobPos");
                switch (profession) {
                    case "SELLER":
                        chosenEmployee = gson.fromJson(response, Seller.class);
                        break;
                    case "CASHIER":
                        chosenEmployee = gson.fromJson(response, Cashier.class);
                        break;
                    case "MANAGER":
                        chosenEmployee = gson.fromJson(response, Manager.class);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBackButton() {

        theLayout.putConstraint(SpringLayout.NORTH, btnBack, 10, SpringLayout.SOUTH, tablePanel);
        theLayout.putConstraint(SpringLayout.WEST, btnBack, 100, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.EAST, btnBack, -100, SpringLayout.EAST, this);
        theLayout.putConstraint(SpringLayout.SOUTH, btnBack, -10, SpringLayout.SOUTH, this);

        btnBack.addActionListener(e -> {
            setVisible(false);
            try {
                channel.stop();
                controller.showMainMenuPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        add(btnBack);
    }

    private void buildContactsTable() {
        employeesTable.setFillsViewportHeight(true);

        tablePanel = new JScrollPane(employeesTable);
        tablePanel.setPreferredSize(new Dimension((int) (controller.getAppFrame().getWidth() * 0.9), (int) (controller.getAppFrame().getHeight() * 0.84)));

        theLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, this);
        theLayout.putConstraint(SpringLayout.WEST, tablePanel, 5, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.EAST, tablePanel, -5, SpringLayout.EAST, this);

        employeesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 1) {
                    chooseEmployeeFromTable();

                }
            }
        });

        updateTable();
        add(tablePanel);
    }

    private void chooseEmployeeFromTable() {

        // get the selected row index
        int selectedRowIndex = employeesTable.getSelectedRow();
        selectedRowIndex = employeesTable.convertRowIndexToModel(selectedRowIndex);

        // set the selected row data into BL.Client
        int employeeCode = (int) (contactsTableModel.getValueAt(selectedRowIndex, 0));

        for (Employee e : listOfEmployees) {
            if (e.getEmployeeNumber() == employeeCode)
                this.chosenEmployee = e;
        }

        if (chosenEmployee != null) {
            if (chatFramesMap.get(chosenEmployee.getEmployeeNumber()) == null) {
                chatFramesMap.put(chosenEmployee.getEmployeeNumber(), new ChatMessagePage(controller, chosenEmployee, channel));
            }

            chatFramesMap.get(chosenEmployee.getEmployeeNumber()).setVisible(true);
        }
    }

    public Map<Integer, ChatMessagePage> getChatFramesMap() {
        return chatFramesMap;
    }

    private Boolean searchPeerLocation(Employee employee) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            EmployeeDto employeeDto = new EmployeeDto("getPeerDetails", employee);
            out.println(gson.toJson(employeeDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();
            response = response.substring(1,response.length()-1);
            if(response.equals("false"))
            {
                return false;
            }
            else
            {
                String [] splits = response.split(",");
//                if(employee.equals(chosenEmployee)) {
//                    destAddress = splits[1];
//                    destPort = Integer.parseInt(splits[2]);
//                }
//                else
//                {
//                    myAddress = splits[1];
                    myPort=Integer.parseInt(splits[2]);
//                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateTable() {
        //Clear old data stored in table
        contactsTableModel.clearData();

        selectAllEmployees();

        //update table graphics
        contactsTableModel.fireTableDataChanged();

        employeesTable.validate();
        employeesTable.repaint();
    }


    private void selectAllEmployees() {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            EmployeeArrayDto employeeArrayDto = new EmployeeArrayDto("selectAllEmployees", listOfEmployees, 0);
            out.println(gson.toJson(employeeArrayDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();

            Employee employee;

            if (response != null) {
                JSONArray array = new JSONArray(response); //object.getJSONArray("");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject js = array.getJSONObject(i);
                    int employeeNum = js.getInt("employeeNumber");
                    String name = js.getString("name");
                    int id = js.getInt("id");
                    String phone = js.getString("phone");
                    int accountNumber = js.getInt("accountNum");
                    String type = js.getString("jobPos");
                    int branchNumber = js.getInt("branchNumber");
                    switch (type) {
                        case "SELLER":
                            employee = new Seller(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                        case "CASHIER":
                            employee = new Cashier(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                        case "MANAGER":
                            employee = new Manager(employeeNum, name, id, phone, accountNumber, branchNumber);
                            listOfEmployees.add(employee);
                            break;
                    }
                }

                for (int i = 0; i < listOfEmployees.size(); i++) {
                    if(listOfEmployees.get(i).getEmployeeNumber() != emp.getEmployeeNumber()) {
                        contactsTableModel.addToVectorM_Data(listOfEmployees.get(i));
                        chatFramesMap.put(listOfEmployees.get(i).getEmployeeNumber(), null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void pause(){
        //create random number between 1 and 15
        Random rand = new Random();
        int breath = rand.nextInt(5) + 1;
        try {
            Thread.sleep(breath*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Controller getController(){
        return controller;
    }

    private static String getTime(){
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
