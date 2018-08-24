package EmployeePackage;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable{

    //globals

    Socket SOCK;
    Scanner input;
    Scanner send = new Scanner(System.in);
    PrintWriter out;
    //----------------

    public ChatClient(Socket socket)
    {
        this.SOCK = socket;
    }

    @Override
    public void run() {
        try
        {
            try {
                input = new Scanner(SOCK.getInputStream());
                out = new PrintWriter(SOCK.getOutputStream());
                out.flush();
                checkStream();
            }
            finally {
                SOCK.close();
            }
        }
        catch (Exception e){System.out.println(e);}
    }

    public void DISCONNECT() throws IOException {
        out.println(ChatClientGUI.userName + " had disconnected.");
        out.flush();
        SOCK.close();
        JOptionPane.showMessageDialog(null, "You disconnected!");
    }

    public void checkStream(){
        while(true){
            RECEIVE();
        }
    }

    public void RECEIVE(){
        if(input.hasNext())
        {
            String msg = input.nextLine();
            if(msg.contains("#?!"))
            {
                String temp1 = msg.substring(3);
                temp1 = temp1.replace("[","");
                temp1 = temp1.replace("]","");


                String[] currentUsers = temp1.split(", ");
                ChatClientGUI.JL_ONLINE.setListData(currentUsers);

            }
            else
            {
                ChatClientGUI.TA_CONVERSATION.append(msg + "\n");
            }
        }
    }

    public void SEND(String name)
    {
        out.println(ChatClientGUI.userName + ": " + name);
        out.flush();
        ChatClientGUI.TF_MESSAGE.setText("");
    }
}
