package EmployeePackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI {
    //globals

    private static ChatClient chatClient;
    public static String userName = "Mr. Master";

    //GUI global - main window
    public static JFrame MainWindow = new JFrame();
    private static JButton B_ABOUT = new JButton();
    private static JButton B_CONNECT = new JButton();
    private static JButton B_DISCONNECT = new JButton();
    private static JButton B_HELP = new JButton();
    private static JButton B_SEND = new JButton();

    private static JLabel L_MESSAGE = new JLabel("Message: ");
    public static JTextField TF_MESSAGE = new JTextField(20);
    private static JLabel L_CONVERSATION = new JLabel();
    public static JTextArea TA_CONVERSATION = new JTextArea();

    private static  JScrollPane SP_CONVERSATION = new JScrollPane();
    private static JLabel L_ONLINE = new JLabel();

    public static JList JL_ONLINE = new JList();
    private static  JScrollPane SP_ONLINE = new JScrollPane();
    private static JLabel L_LoggedInAs = new JLabel();
    private static JLabel L_LoggedInAsBox = new JLabel();


    //GUI globals - log in window
    public static JFrame LogInWindow = new JFrame();
    public static JTextField TF_UserNameBox = new JTextField(20);
    private static JButton B_ENTER = new JButton("ENTER");
    private static JLabel L_EnterUserName = new JLabel("Enter UserName: ");
    private static JPanel P_LOGIN = new JPanel();

    public static void main(String args[])
    {
        buildMainWindow();
        initialize();
    }

    public static void CONNECT()
    {
        try{
            final int port = 444;
            final String host = "localhost";
            Socket SOCK = new Socket(host,port);
            System.out.println("You connected to: " + host);

            chatClient = new ChatClient(SOCK);

            //Send Name to add to online list
            PrintWriter out = new PrintWriter(SOCK.getOutputStream());
            out.println(userName);
            out.flush();

            Thread connThread = new Thread(chatClient);
            connThread.start();
        }
        catch (Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Server not responding");
            System.exit(0);
        }
    }

    public static void initialize(){
        B_SEND.setEnabled(false);
        B_DISCONNECT.setEnabled(false);
        B_CONNECT.setEnabled(true);
    }

    public static void buildLoginWindow()
    {
        LogInWindow.setTitle("What's your name?");
        LogInWindow.setSize(400,100);
        LogInWindow.setLocation(250,200);
        LogInWindow.setResizable(false);
        P_LOGIN = new JPanel();
        P_LOGIN.add(L_EnterUserName);
        P_LOGIN.add(TF_UserNameBox);
        P_LOGIN.add(B_ENTER);
        LogInWindow.add(P_LOGIN);

        login_action();
        LogInWindow.setVisible(true);
    }

    public static void buildMainWindow()
    {
        MainWindow.setTitle(userName + "'s chat box");
        MainWindow.setSize(450,500);
        MainWindow.setLocation(220,180);
        MainWindow.setResizable(false);
        configureMainWindow();
        mainWindow_Action();
        MainWindow.setVisible(true);
    }

    public static void configureMainWindow(){
        MainWindow.setBackground(new Color(255,255,255));
        MainWindow.setSize(500,500);
        MainWindow.getContentPane().setLayout(null);

        B_SEND.setBackground(new Color(0,0,150));
        B_SEND.setForeground(new Color(255,255,255));
        B_SEND.setText("SEND");
        MainWindow.getContentPane().add(B_SEND);
        B_SEND.setBounds(250,40,81,25);

        B_DISCONNECT.setBackground(new Color(0,0,150));
        B_DISCONNECT.setForeground(new Color(255,255,255));
        B_DISCONNECT.setText("DISCONNECT");
        MainWindow.getContentPane().add(B_DISCONNECT);
        B_DISCONNECT.setBounds(10,40,110,25);

        B_CONNECT.setBackground(new Color(0,0,150));
        B_CONNECT.setForeground(new Color(255,255,255));
        B_CONNECT.setText("CONNECT");
        B_CONNECT.setToolTipText("");
        MainWindow.getContentPane().add(B_CONNECT);
        B_CONNECT.setBounds(130,40,110,25);

        B_HELP.setBackground(new Color(0,0,150));
        B_HELP.setForeground(new Color(255,255,255));
        B_HELP.setText("HELP");
        MainWindow.getContentPane().add(B_HELP);
        B_HELP.setBounds(420,40,70,25);

        B_ABOUT.setBackground(new Color(0,0,150));
        B_ABOUT.setForeground(new Color(255,255,255));
        B_ABOUT.setText("ABOUT");
        MainWindow.getContentPane().add(B_ABOUT);
        B_ABOUT.setBounds(340,40,75,25);

        L_MESSAGE.setText("Message:");
        MainWindow.getContentPane().add(L_MESSAGE);
        L_MESSAGE.setBounds(10,10,60,20);

        TF_MESSAGE.setForeground(new Color(0,0,150));
        TF_MESSAGE.requestFocus();
        MainWindow.getContentPane().add(TF_MESSAGE);
        TF_MESSAGE.setBounds(70,4,260,30);

        L_CONVERSATION.setHorizontalAlignment(SwingConstants.CENTER);
        L_CONVERSATION.setText("Conversation");
        MainWindow.getContentPane().add(L_CONVERSATION);
        L_CONVERSATION.setBounds(100,70,140,16);

        TA_CONVERSATION.setColumns(20);
        TA_CONVERSATION.setFont(new Font("Tahoma",0,12));
        TA_CONVERSATION.setForeground(new Color(0,0,150));
        TA_CONVERSATION.setLineWrap(true);
        TA_CONVERSATION.setRows(5);
        TA_CONVERSATION.setEditable(false);

        SP_CONVERSATION.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_CONVERSATION.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_CONVERSATION.setViewportView(TA_CONVERSATION);
        MainWindow.getContentPane().add(SP_CONVERSATION);
        SP_CONVERSATION.setBounds(10,90,330,180);

        L_ONLINE.setHorizontalAlignment(SwingConstants.CENTER);
        L_ONLINE.setText("Currently Online");
        L_ONLINE.setToolTipText("");
        MainWindow.getContentPane().add(L_ONLINE);
        L_ONLINE.setBounds(350,70,130,16);

        //Change names to those from the DB - select query here
        //String [] testNames = {"Guy", "Roy", "Mr. Incredible", "Pamela"};
        JL_ONLINE.setForeground(new Color(0,0,150));
        //JL_ONLINE.setListData(testNames);

        SP_ONLINE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_ONLINE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_ONLINE.setViewportView(JL_ONLINE);
        MainWindow.getContentPane().add(SP_ONLINE);
        SP_ONLINE.setBounds(350,90,130,180);

        L_LoggedInAs.setFont(new Font("Tahoma",0,12));
        L_LoggedInAs.setText("Currently Logged in As:");
        MainWindow.getContentPane().add(L_LoggedInAs);
        L_LoggedInAs.setBounds(348,0,140,15);

        L_LoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
        L_LoggedInAsBox.setFont(new Font("Tahoma",0,12));
        L_LoggedInAsBox.setForeground(new Color(0,0,150));
        L_LoggedInAsBox.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        MainWindow.getContentPane().add(L_LoggedInAsBox);
        L_LoggedInAsBox.setBounds(340,17,150,20);
    }

    public static void login_action(){
        B_ENTER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ACTION_B_ENTER();
            }
        });
    }

    public static void ACTION_B_ENTER(){
        if(!TF_UserNameBox.getText().equals(""))
        {
            userName = TF_UserNameBox.getText().trim();
            L_LoggedInAsBox.setText(userName);
            ChatServer.currentUsers.add(userName);
            MainWindow.setTitle(userName + "'s chat box");
            LogInWindow.setVisible(false);
            B_SEND.setEnabled(true);
            B_DISCONNECT.setEnabled(true);
            B_CONNECT.setEnabled(false);
            CONNECT();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter a name!");
        }
    }
    public static void mainWindow_Action()
    {
       B_SEND.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               ACTION_B_SEND();
           }
       });

       B_DISCONNECT.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               ACTION_B_DISCONNECT();
           }
       });

       B_CONNECT.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              buildLoginWindow();
           }
       });

       B_HELP.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               ACTION_B_HELP();
           }
       });

       B_ABOUT.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               ACTION_B_HELP();
           }
       });
    }

    public static void ACTION_B_SEND(){
        if(!TF_MESSAGE.getText().equals("")) {
            chatClient.SEND(TF_MESSAGE.getText());
            TF_MESSAGE.requestFocus();
        }
    }

    public static  void ACTION_B_DISCONNECT()
    {
        try{
            chatClient.DISCONNECT();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void ACTION_B_HELP(){
        JOptionPane.showMessageDialog(null, "Multi-Client CHAT program");
    }

}
