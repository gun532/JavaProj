package BL.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

    //globals
    public static ArrayList<Socket> connectionArray = new ArrayList<>(); //hold all the connections
    public static ArrayList<String> currentUsers = new ArrayList<>(); //hold all the users active
    //--------------

    public static void main(String [] args) throws IOException {
        final int PORT = 444;
        ServerSocket SERVER = new ServerSocket(PORT);
        System.out.println("Waiting for clients...");
        try {
            while (true) {
                Socket SOCK = SERVER.accept();
                connectionArray.add(SOCK);

                System.out.println("Client Connected from: " + SOCK.getLocalAddress().getHostName());

                addUserName(SOCK);

                chat_server_return CHAT = new chat_server_return(SOCK);

                Thread chatThread = new Thread(CHAT);
                //Thread chatThread = new Thread(CHAT);
                chatThread.start();

            }
        }
        catch(Exception e){System.out.println(e);}

    }

    public static void addUserName(Socket sockect) throws IOException {
        Scanner input = new Scanner(sockect.getInputStream());
        String userName = input.nextLine();
        currentUsers.add(userName);
        for(int i = 1; i<= ChatServer.connectionArray.size();i++)
        {
            Socket tempSocket = (Socket)ChatServer.connectionArray.get(i-1);
            PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
            out.println("#?!" + currentUsers);
            out.flush();
        }
    }


}
