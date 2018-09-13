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
import Entities.Product;
import Entities.ShoppingCart;
import com.google.gson.Gson;
import org.json.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;


public class Server {
    private static ArrayList<Employee> connectedUsers = new ArrayList<>();
    public static ArrayList<SSLSocket> connectionArray = new ArrayList<>(); //hold all the connections
    private JFrame appFrame;


    public static class SocketServer extends Thread {


        public static final int PORT_NUMBER = 8081;
        static DataInputStream in = null;
        static PrintStream out = null;
        static Socket socket;
        static ServerSocket server = null;
        private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
        private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());
        static SSLServerSocket sslServerSocket;
        static SSLServerSocketFactory sslServerSocketfactory;
        static SSLSocket sslSocket;
        static Employee  loggedInEmployee = null;

        private SocketServer(SSLSocket socket) {
            this.sslSocket = socket;
            System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
            start();
        }

        public void run() {
            try {
                Inventory inventory;
//                in = new DataInputStream(socket.getInputStream());
//                out = new PrintStream(socket.getOutputStream());
                in = new DataInputStream(sslSocket.getInputStream());
                out = new PrintStream(sslSocket.getOutputStream());
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
                    ProductDto productDto;

                    switch (dtoBase.getFunc()) {
                        case "login":
                            LoginUtility loginUtility = new LoginUtility();
                            LoginDetailsDto loginDetailsDto = gson.fromJson(request, LoginDetailsDto.class);
                            loggedInEmployee =
                                    loginUtility.login(loginDetailsDto.getEmployeeId(), loginDetailsDto.getPassword());
                            if (loggedInEmployee == null) out.println("null");
                            else {
                                Boolean ifConnected = checkIfConnected(loggedInEmployee);
                                if (ifConnected) out.println(ifConnected);
                                else {
                                    if (ifConnected == false) {
                                        String employeeJson = gson.toJson(loggedInEmployee);
                                        out.println(employeeJson);
                                    }
                                }
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
                                    clientDto.getFullName(), clientDto.getPhoneNumber(), clientDto.getType().toString(),
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
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeAdded = managerBL.addEmployee(employeeDto.getName(), employeeDto.getPass(), employeeDto.getId(),
                                    employeeDto.getPhone(), employeeDto.getAccountNum(),
                                    employeeDto.getBranchNumber(), employeeDto.getJobPos().toString());
                            boolean isIncreasedInBranch = managerBL.increaseEmployeeInBranch(employeeDto.getBranchNumber());
                            out.println(gson.toJson(isEmployeeAdded && isIncreasedInBranch));
                            break;
                        case "removeEmployee":
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeDeleted = managerBL.deleteEmployee(employeeDto.getEmployeeNumber());
                            boolean isDecreasedInBranch = managerBL.decreaseEmployeeInBranch(employeeDto.getBranchNumber());

                            out.println(gson.toJson(isEmployeeDeleted && isDecreasedInBranch));
                            break;
                        case "updateEmployee":
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeUpdated = managerBL.updateEmployee(employeeDto.getName(), employeeDto.getId(),
                                    employeeDto.getPhone(), employeeDto.getAccountNum(), employeeDto.getBranchNumber(),
                                    employeeDto.getJobPos().toString(), employeeDto.getPass(), employeeDto.getEmployeeNumber());
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
                        case "addNewProduct":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductAdded = managerBL.addNewProduct(productDto.getName(),productDto.getPrice());
                            out.println(gson.toJson(isProductAdded));
                            break;
                        case "addProductAmountToInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            int isProductAmountAdded = managerBL.addProductAmountToInventory(productDto.getInventoryCode(),
                                    productDto.getAmount(),productDto.getName());
                            out.println(gson.toJson(isProductAmountAdded));
                            break;
                        case "updateProduct":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductUpdated = managerBL.updateProduct(productDto.getName(),productDto.getPrice(),
                                    productDto.getProductCode());
                            out.println(gson.toJson(isProductUpdated));
                            break;
                        case "updateProductAmountInInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductAmountUpdated = managerBL.updateProductAmountInInventory
                                    (productDto.getAmount(),productDto.getInventoryCode(),productDto.getProductCode());
                            out.println(gson.toJson(isProductAmountUpdated));
                            break;
                        case "removeProductFromInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductDeleted = managerBL.removeProductFromInventory(
                                    productDto.getProductCode(),productDto.getInventoryCode());
                            out.println(gson.toJson(isProductDeleted));
                            break;
                        case "selectAllProducts":
                            //EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String products = gson.toJson(managerBL.selectAllProducts());
                            jsArray = new JSONArray(products);
                            out.println(jsArray);
                            break;
                    }

                }

            } catch (Exception ex) {
//                ex.printStackTrace();
                System.out.println("Connection reset");
//            } catch (JSONException e) {
//                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Closed connection \n");
                    in.close();
                    out.close();
                    if(connectedUsers.size() != 0 && connectionArray.size() != 0)
                    {
                        connectedUsers.remove(loggedInEmployee);
                        connectionArray.remove(sslSocket);
                    }
                    sslSocket.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Same Employee can't logged in twice", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


        public static void runSocket() {
            //System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
            //System.setProperty("javax.net.ssl.keyStorePassword","123456");
            //System.setProperty("javax.net.debug","all");
            System.out.println("SocketServer started");
            //ServerSocket server = null;
            try {
                //Provider provider = SSLContext.getDefault().getProvider();
                SSLContext context = SSLContext.getInstance("TLSv1.2");
                context.init(null, null, null);
                sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(PORT_NUMBER);
                sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
                //server = new ServerSocket(PORT_NUMBER);
                while (true) {
                    //*create a new {
                    //      @link SocketServer
                    // } object for each connection
                    //*this will allow multiple client connections
                    //new SocketServer(server.accept());
                    new SocketServer((SSLSocket) sslServerSocket.accept());
                    //sslSocket = (SSLSocket)sslServerSocket.accept();

                    //connectionArray.add(socket);
                }
            } catch (IOException ex) {
                System.out.println("Unable to start server." + ex.getMessage());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } finally {
                try {
//                    if (server != null)
//                        server.close();
                    if (sslServerSocket != null) {
                        sslServerSocket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public ArrayList<Employee> getConnectedUsers() {
            return connectedUsers;
        }


        private boolean checkIfConnected(Employee loggedInEmployee) throws Exception {
            if (connectedUsers.size() == 0) {
                connectedUsers.add(loggedInEmployee);
                connectionArray.add(sslSocket);
                return false;
            } else {
                for (int i = 0; i < connectedUsers.size(); i++) {
                    if (connectedUsers.get(i).getId() == loggedInEmployee.getId()) {
                        return true;

                    } else {
                        if (loggedInEmployee != null) {
                            connectedUsers.add(loggedInEmployee);
                            connectionArray.add(sslSocket);
                            return false;
                        } else return true;
                    }
                }

            }
            return true;

        }
    }

    public static void main(String[] args) {
        SocketServer.runSocket();
    }
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


