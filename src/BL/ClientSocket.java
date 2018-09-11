package BL;

import Entities.Employee.Employee;
import GUI.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ClientSocket implements Runnable {
    private final String host = "127.0.0.1";
    private final int port = 8081;
    public static SSLSocket echoSocket = null;

    public ClientSocket(String host, int port) {
        //specifing the trustStore file which contains the certificate & public of the server
        //System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
        //specifing the password of the trustStore file
        //System.setProperty("javax.net.ssl.trustStorePassword","123456");
        //This optional and it is just to show the dump of the details of the handshake process
        //System.setProperty("javax.net.debug","all");

        //String serverHostname = new String("127.0.0.1");
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null,null,null);

            System.out.println("Connecting to host " + host + " on port " + port + ".");
            echoSocket = (SSLSocket) context.getSocketFactory().getDefault().createSocket(host, port);
            echoSocket.setEnabledCipherSuites(echoSocket.getSupportedCipherSuites());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

        PrintWriter out = null;
        BufferedReader in = null;


    //        finally {
//            try {
//                out.close();
//                in.close();
//                echoSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    @Override
    public void run() {
        try
        {
//            try {
//                input = new Scanner(SOCK.getInputStream());
//                out = new PrintWriter(SOCK.getOutputStream());
//                out.flush();
//                checkStream();
//            }
            try {

                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                while(true)
                {

                }
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            finally {
            try {
                echoSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
/**
 * Closing all the resources
 */
//            out.close();
//            in.close();
//            echoSocket.close();


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

