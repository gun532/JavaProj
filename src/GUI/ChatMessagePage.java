package GUI;

import BL.AuthService;
import BL.Channel;
import BL.ManagerBL;
import DAL.ManagerDataAccess;
import DTO.EmployeeDto;
import DTO.SendMessageLaterDto;
import Entities.Employee.*;
import Entities.Message;
import GUI.CJButton;
import GUI.CJPanel;
import GUI.ContactsTableModel;
import GUI.Controller;
import com.google.gson.Gson;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ChatMessagePage extends JFrame {

    private Controller controller;
    private Font font = new Font("Candara", 0, 16); //Custom page font
    private SpringLayout theLayout = new SpringLayout();
    private JPanel mainPanel;

    //private CJButton btnBack = new CJButton("Back", font);

    private JTextArea textArea = new JTextArea();
    private JScrollPane areaScrollPane = new JScrollPane(textArea);

    private CJPanel textPanel;
    private JTextField textField = new JTextField(10);

    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());

    private Channel channel;
    private String destAddress;
    private int destPort;
    private String myAddress;
    Boolean peerDetails = false;

    private Employee chosenEmployee;
    private Employee currentEmployee = AuthService.getInstance().getCurrentEmployee();
    private static int firstTime = 0;

    private int peerPort;

    public ChatMessagePage(Controller in_controller, Employee emp, Channel channel) throws HeadlessException {

        this.controller = in_controller;
        this.chosenEmployee = emp;
        this.channel = channel;

        mainPanel = new CJPanel(theLayout);

        setSize((int) (controller.getAppFrame().getWidth() * 0.6), (int) (controller.getAppFrame().getHeight() * 0.5));
        setIconImage(controller.getAppFrame().getIconImage());
        setLocationRelativeTo(null);
        setTitle("Chat with: " + chosenEmployee.getName());

        buildTextPanel();

        buildMessageBoard();

        setContentPane(mainPanel);

        peerDetails = searchPeerLocation(chosenEmployee);
        if (peerDetails == false) {
            JOptionPane.showMessageDialog(new JFrame(), chosenEmployee.getName() +
                    " is not connected. The messages will be sent when " + chosenEmployee.getName() + " will connect", "Chat", JOptionPane.INFORMATION_MESSAGE);
        }
        //else

        //change later to if the window is not shown
        if (messageBoardNotEmpty(currentEmployee.getEmployeeNumber())) {
            checkForNewMessages(currentEmployee);
            firstTime++;
        }
        //connected for the first time
//        if(firstTime == 0)
//        {
//            firstTime = 1;
//            checkForNewMessages(currentEmployee);
//        }

    }

    private boolean messageBoardNotEmpty(int employeeNumber) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            SendMessageLaterDto sendMessageLaterDto = new SendMessageLaterDto("messageBoardNotEmpty", employeeNumber, null);
            out.println(gson.toJson(sendMessageLaterDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();
            //response = response.substring(1, response.length() - 1);


            if (response.equals("true")) {
                return true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    private void checkForNewMessages(Employee employee) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            //insert peer details here
//            if (searchPeerLocation(employee)) //if false - not connected
//            {

            if (peerDetails) {
                InetSocketAddress address = new InetSocketAddress(destAddress, destPort);
                SendMessageLaterDto sendMessageLaterDto = new SendMessageLaterDto("checkMessages", employee.getEmployeeNumber(), null);
                out.println(gson.toJson(sendMessageLaterDto));

                DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                String response = in.readLine();
                response = response.substring(1, response.length() - 1);


                if (!response.equals("false")) {
                    String[] allMsg = response.split("~");
                    for(int i = 0; i<allMsg.length; i++)
                    {
                        textArea.append(allMsg[i] + "\n");
                    }
//                    channel.sendTo(address, response);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTextPanel() {

        textPanel = new CJPanel(new BorderLayout(), controller.getAppFrame().getWidth() * 0.9, controller.getAppFrame().getHeight() * 0.1);

        theLayout.putConstraint(SpringLayout.SOUTH, textPanel, -5, SpringLayout.SOUTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, textPanel, 5, SpringLayout.WEST, mainPanel);
        theLayout.putConstraint(SpringLayout.EAST, textPanel, -5, SpringLayout.EAST, mainPanel);

        textField.setFont(font);
        textField.setText("Type a message...");
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().matches("Type a message..."))
                    textField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty())
                    textField.setText("Type a message...");
            }
        });

        Message msgSendLater = new Message(currentEmployee.getId());


        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER && !textField.getText().isEmpty()) {

                    if (searchPeerLocation(chosenEmployee)) {
                        System.out.println("Destination IP : ");
                        System.out.println(destAddress);

                        System.out.println("Destination Port : ");
                        System.out.println(destPort);
                        try {
//                            Scanner scanner = new Scanner(System.in);
                            InetSocketAddress address = new InetSocketAddress(destAddress, destPort);


//                            while (true) {
//                                String msg = scanner.nextLine();

                            String msg = textField.getText();

//                                if (msg.isEmpty())
//                                    break;

                            msg = currentEmployee.getName() + ": " + msg;


                            channel.sendTo(address, msg);
                            System.out.println(msg);
                            textArea.append(msg + "\n");
//                            }

//                            scanner.close();
//                            channel.stop();
//                            System.out.println("Closed.");
                        } catch (SocketException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        String msgToSend = textField.getText();
                        msgToSend = currentEmployee.getName() + ": " + msgToSend;
//                        Message msg = new Message(msgToSend, currentEmployee.getId());
                        msgSendLater.getAllMessages().add(msgToSend);
                        sendLater(chosenEmployee.getEmployeeNumber(), msgSendLater);
//                        messagesToSend.put(chosenEmployee.getEmployeeNumber(), msg);
                        textArea.append(msgToSend + "\n");

                    }

                }
            }
        });

        textPanel.add(textField);
        mainPanel.add(textPanel);


    }

    private void sendLater(int employeeNumber, Message msg) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            SendMessageLaterDto sendMessageLaterDto = new SendMessageLaterDto("sendMessageLater", employeeNumber, msg);
            out.println(gson.toJson(sendMessageLaterDto));

//            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
//            String response = in.readLine();
//            if(!response.equals("User is not connected"))
//            {
//                String [] splits = response.split(",");
//                destAddress = splits[1];
//                destPort = Integer.parseInt(splits[2]);
//                return true;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildMessageBoard() {

        textArea.setEditable(false);
        textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setOpaque(false);

        areaScrollPane.setOpaque(false);
        areaScrollPane.setPreferredSize(new Dimension((int) (controller.getAppFrame().getWidth() * 0.9), (int) (controller.getAppFrame().getHeight() * 0.9)));


        theLayout.putConstraint(SpringLayout.NORTH, areaScrollPane, 10, SpringLayout.NORTH, mainPanel);
        theLayout.putConstraint(SpringLayout.WEST, areaScrollPane, 5, SpringLayout.WEST, mainPanel);
        theLayout.putConstraint(SpringLayout.EAST, areaScrollPane, -5, SpringLayout.EAST, mainPanel);
        theLayout.putConstraint(SpringLayout.SOUTH, areaScrollPane, -5, SpringLayout.NORTH, textPanel);

        mainPanel.add(areaScrollPane);
    }

    private Boolean searchPeerLocation(Employee employee) {
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            EmployeeDto employeeDto = new EmployeeDto("getPeerDetails", employee);
            out.println(gson.toJson(employeeDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();
            response = response.substring(1, response.length() - 1);
            if (!response.equals("false")) {
                String[] splits = response.split(",");
                destAddress = splits[1];
                destPort = Integer.parseInt(splits[2]);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
