package BL;

import DTO.DtoBase;
import DTO.LoginDetailsDto;
import Entities.Employee.Employee;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerTest {
    private static ArrayList<Employee> connectedUsers = new ArrayList<>();



    public static class SocketServer extends Thread {

        public static final int PORT_NUMBER = 8081;
        static DataInputStream in = null;
        static PrintStream out = null;
        static Socket socket;
        static ServerSocket server = null;


        private SocketServer(Socket socket) {
            this.socket = socket;
            System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
            start();
        }

        public void run() {
            String line;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new PrintStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String request;
                while ((request = br.readLine()) != null) {

                    System.out.println("Message received");
                    //line = br.readLine();
                    LoginUtility loginUtility = new LoginUtility();
                    Gson gson = new Gson();

                    DtoBase dtoBase = gson.fromJson(request, DtoBase.class);


                    switch (dtoBase.getFunc()) {
                        case "login":
                            LoginDetailsDto loginDetailsDto = gson.fromJson(request, LoginDetailsDto.class);
                            Employee loggedInEmployee =
                                    loginUtility.login(loginDetailsDto.getEmployeeId(), loginDetailsDto.getPassword());
                            Boolean ifConnected = checkIfConnected(loggedInEmployee);
                            if(ifConnected == false || ifConnected == null)
                            {
                                String employeeJson = gson.toJson(loggedInEmployee);
                                out.println(employeeJson);
                            }
                            break;
                    }
                }

            } catch (Exception ex) {
                System.out.println("Unable to get streams from client");
//            } catch (JSONException e) {
//                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Closed connection \n");
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Same Employee can't logged in twice", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


        private boolean checkIfConnected(Employee loggedInEmployee) throws Exception {
            if (connectedUsers.contains(loggedInEmployee)) {
                socket.close();
                throw new Exception("user already logged in");

            } else {
                if(loggedInEmployee!= null) connectedUsers.add(loggedInEmployee);
                return false;
            }
        }



        public static void main(String[] args) {
            System.out.println("SocketServer Example");
            //ServerSocket server = null;
            try {
                server = new ServerSocket(PORT_NUMBER);
                while (true) {
                    //*create a new {
                    //      @link SocketServer
                    // } object for each connection
                    //*this will allow multiple client connections
                    new SocketServer(server.accept());
                }
            } catch (IOException ex) {
                System.out.println("Unable to start server." + ex.getMessage());
            } finally {
                try {
                    if (server != null)
                        server.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public ArrayList<Employee> getConnectedUsers() {
            return connectedUsers;
        }



//        public static  void main(String[] args) {
//            System.out.println("SocketServer Example");
//            try {
//                server = new ServerSocket(PORT_NUMBER);
//                while (true) {
//                    //create a new {@link SocketServer} object for each connection
//                    //this will allow multiple client connections
//                    new SocketServer(server.accept());
//                }
//            } catch (IOException ex) {
//                System.out.println("Unable to start server." + ex.getMessage());
//            } finally {
//                try {
//                    if (server != null)
//                        server.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }

    }

    public static ArrayList<Employee> getConnectedUsers() {
        return connectedUsers;
    }
}
