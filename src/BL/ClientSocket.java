package BL;

import Entities.Employee.Employee;
import GUI.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {
    private final String host = "127.0.0.1";
    private final int port = 8081;
    public static Socket echoSocket = null;

    public ClientSocket(String host, int port) {

        try {
            //String serverHostname = new String("127.0.0.1");

            System.out.println("Connecting to host " + host + " on port " + port + ".");


            PrintWriter out = null;
            BufferedReader in = null;

            try {
                echoSocket = new Socket(host, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

//            /** {@link UnknownHost} object used to read from console */
//            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//            while (true) {
//                System.out.print("client: ");
//                String userInput = stdIn.readLine();
//                /** Exit on 'q' char sent */
//                if ("q".equals(userInput)) {
//                    break;
//                }
//                out.println(userInput);
//                System.out.println("server: " + in.readLine());
//            }
//
            /** Closing all the resources */
//            out.close();
//            in.close();
//            stdIn.close();
//            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private boolean checkIfConnected(Employee loggedInEmployee) throws Exception {
//        if (connectedUsers.contains(loggedInEmployee)) {
//            echoSocket.close();
//            throw new Exception("user already logged in");
//
//        } else {
//            if (loggedInEmployee != null) connectedUsers.add(loggedInEmployee);
//            return false;
//        }
//    }

//    public ArrayList<Socket> getConnectedUsers() {
//        return connectedUsers;
//    }

}
