package BL;

import DAL.ClientsDataAccess;
import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;
import DAL.ManagerDataAccess;
import DTO.*;
import Entities.Branch;
import Entities.Clients.Client;
import Entities.Employee.Employee;
import Entities.Inventory;
import Entities.ShoppingCart;
import com.google.gson.Gson;
import org.json.*;

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
        private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
        private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());


        private SocketServer(Socket socket) {
            this.socket = socket;
            System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
            start();
        }

        public void run() {
            try {
                Inventory inventory;
                in = new DataInputStream(socket.getInputStream());
                out = new PrintStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String request;
                while ((request = br.readLine()) != null) {

                    System.out.println("Message received");
                    //line = br.readLine();
                    Gson gson = new Gson();


                    DtoBase dtoBase = gson.fromJson(request, DtoBase.class);
                    ClientDto clientDto;
                    Client client;
                    JSONArray jsArray;
                    EmployeeDto employeeDto;
                    BranchDto branchDto;

                    switch (dtoBase.getFunc()) {
                        case "login":
                            LoginUtility loginUtility = new LoginUtility();
                            LoginDetailsDto loginDetailsDto = gson.fromJson(request, LoginDetailsDto.class);
                            Employee loggedInEmployee =
                                    loginUtility.login(loginDetailsDto.getEmployeeId(), loginDetailsDto.getPassword());
                            Boolean ifConnected = checkIfConnected(loggedInEmployee);
                            if (ifConnected == false || ifConnected == null) {
                                String employeeJson = gson.toJson(loggedInEmployee);
                                out.println(employeeJson);
                            }
                            break;
                        case "inverntoryByBranch":
                            //cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());
                            InventoryDto inventoryDto = gson.fromJson(request, InventoryDto.class);
                            inventory = cashierBL.selectFromInventory(inventoryDto.getInventoryNumber());
                            String inventoryJson = gson.toJson(inventory);
                            out.println(inventoryJson);
                            break;

                        case "createNewOrder":
                            OrderDto orderDto = gson.fromJson(request, OrderDto.class);
                            inventory = orderDto.getInventory();
                            int clientID = orderDto.getClientId();
                            client = cashierBL.selectClientByID(clientID);
                            ShoppingCart shoppingCart = orderDto.getShoppingCart();
                            Boolean orderConfirm = cashierBL.createNewOrder(inventory, client, shoppingCart, orderDto.getTotal());
                            String orderConfirmJson = gson.toJson(orderConfirm);
                            out.println(orderConfirmJson);
                            break;

                        case "selectAllClients":
                            //ClientsArrayDto clientsArrayDto = gson.fromJson(request, ClientsArrayDto.class);
                            //ArrayList<Client> clientArrayList = cashierBL.selectAllClients();
                            //clientsArrayDto.setAllClients(cashierBL.selectAllClients());
                            String clients = gson.toJson(cashierBL.selectAllClients());
                            //JSONArray jsArray = new JSONArray(clientsArrayDto.getAllClients());
                            //JSONObject jsonObject = new JSONObject(clientsArrayDto.getAllClients());
                            //JSONObject object = new JSONObject(clients);
                            jsArray = new JSONArray(clients);
                            //  out.println(object);
                            out.println(jsArray);
                            break;
                        case "removeClient":
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientDeleted = managerBL.deleteClient(clientDto.getId());
                            out.println(gson.toJson(isClientDeleted));
                            break;

                        case "addNewClient":
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientAdded = cashierBL.addNewClient(clientDto.getId(), clientDto.getFullName(),
                                    clientDto.getPhoneNumber(), clientDto.getType().toString());
                            out.println(gson.toJson(isClientAdded));
                            break;
                        case "updateClient":
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientUpdated = managerBL.updateClient(clientDto.getId(),
                                    clientDto.getFullName(),clientDto.getPhoneNumber(),clientDto.getType().toString(),
                                    clientDto.getClientCode());
                            out.println(gson.toJson(isClientUpdated));
                            break;
                        case "selectAllEmployess":
                            //EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String employees = gson.toJson(managerBL.selectAllEmployees());
                            jsArray = new JSONArray(employees);
                            out.println(jsArray);
                            break;
                        case "addNewEmployee":
                            employeeDto = gson.fromJson(request,EmployeeDto.class);
                            boolean isEmployeeAdded = managerBL.addEmployee(employeeDto.getName(),employeeDto.getPass(),employeeDto.getId(),
                                    employeeDto.getPhone(),employeeDto.getAccountNum(),
                                    employeeDto.getBranchNumber(),employeeDto.getJobPos().toString());
                            boolean isIncreasedInBranch = managerBL.increaseEmployeeInBranch(employeeDto.getBranchNumber());
                            out.println(gson.toJson(isEmployeeAdded && isIncreasedInBranch));
                            break;
                        case "removeEmployee":
                            employeeDto = gson.fromJson(request,EmployeeDto.class);
                            boolean isEmployeeDeleted = managerBL.deleteEmployee(employeeDto.getEmployeeNumber());
                            boolean isDecreasedInBranch = managerBL.decreaseEmployeeInBranch(employeeDto.getBranchNumber());

                            out.println(gson.toJson(isEmployeeDeleted && isDecreasedInBranch));
                            break;
                        case "updateEmployee":
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeUpdated = managerBL.updateEmployee(employeeDto.getName(),employeeDto.getId(),
                                    employeeDto.getPhone(),employeeDto.getAccountNum(),employeeDto.getBranchNumber(),
                                    employeeDto.getJobPos().toString(),employeeDto.getPass(),employeeDto.getEmployeeNumber());
                            out.println(gson.toJson(isEmployeeUpdated));
                            break;
                        case "selectBranchDetails":
                            branchDto = gson.fromJson(request, BranchDto.class);
                            Branch branchInUse = managerBL.selectBranchDetails(branchDto.getBranchNumber());
                            String branchJson = gson.toJson(branchInUse);
                            out.println(branchJson);
                            break;
                        case "updateBranchPhoneNumber":
                            branchDto = gson.fromJson(request, BranchDto.class);
                            boolean isPhoneUpdated = managerBL.updateBranchPhoneNumber(branchDto.getPhone(),
                                    branchDto.getBranchNumber());
                            out.println(gson.toJson(isPhoneUpdated));
                            break;
                    }


                }

            } catch (Exception ex) {
                ex.printStackTrace();
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
                if (loggedInEmployee != null) connectedUsers.add(loggedInEmployee);
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
                    Thread thread = new Thread()
                    {
                        @Override
                        public void run() {
                            run();
                        }
                    };
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
