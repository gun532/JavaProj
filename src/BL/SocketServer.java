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
import Entities.Message;
import Entities.ShoppingCart;
import com.google.gson.Gson;
import org.json.JSONArray;

import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


//Utility function that implement endpoint to the server.
//each function does a different thing, and is called by the client from different pages across the application

public class SocketServer extends Thread{
    public static ArrayList<Employee> connectedUsers = new ArrayList<>(); // saves all connected users
//    public  ArrayList<SSLSocket> connectionArray = new ArrayList<>(); //hold all the connections
    public  static ArrayList<SSLSocketData> connectionArray = new ArrayList<>(); //hold all the connections
    public  static ArrayList<Integer> listeningPorts = new ArrayList<>(); //hold all the port the clients send the server that they are listening on
    public static Map<Integer, Message> messagesToSend = new LinkedHashMap<>(); //hold all the messages to be sent later to each client

    SSLSocketData sslSocketData; //custom object. hold the main socket data


//    public final int PORT_NUMBER = 9999;
     DataInputStream in = null;
     PrintStream out = null;
    //static Socket socket;
    //static ServerSocket server = null;
    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
    private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());

    Employee loggedInEmployee = null;

    SSLSocket sslSocket;

    //create client connection to the server
    public SocketServer(SSLSocket socket) {
        this.sslSocket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    @Override
    public void run() {
            try {
                sslSocketData = new SSLSocketData(sslSocket);
                Inventory inventory;
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
                    SendMessageLaterDto sendMessageLaterDto;
                    DateReportDto dateReportDto;

                    //implementing end-points from different pages in the application
                    //the data is transfered in json between the client and the server.
                    switch (dtoBase.getFunc()) {
                        case "login": //login
                            LoginUtility loginUtility = new LoginUtility();
                            LoginDetailsDto loginDetailsDto = gson.fromJson(request, LoginDetailsDto.class);
                            loggedInEmployee =
                                    loginUtility.login(loginDetailsDto.getEmployeeId(), loginDetailsDto.getPassword());
                            if (loggedInEmployee == null) out.println("null");
                            else {
                                Random rand = new Random();

                                int  n = rand.nextInt(35000) + 10000;
                                Boolean ifConnected = checkIfConnected(loggedInEmployee,n);
                                if (ifConnected) out.println(ifConnected);
                                else {
                                    if (ifConnected == false) {
                                        String employeeJson = gson.toJson(loggedInEmployee);
                                        out.println(employeeJson);
                                    }
                                }
                            }
                            break;
                        case "inverntoryByBranch": //create the inventory for each branch
                            InventoryDto inventoryDto = gson.fromJson(request, InventoryDto.class);
                            inventory = cashierBL.selectFromInventory(inventoryDto.getInventoryNumber());
                            String inventoryJson = gson.toJson(inventory);
                            out.println(inventoryJson);
                            break;

                        case "createNewOrder": //server side method to support new order
                            OrderDto orderDto = gson.fromJson(request, OrderDto.class);
                            inventory = orderDto.getInventory();
                            int clientID = orderDto.getClientId();
                            client = cashierBL.selectClientByID(clientID);
                            ShoppingCart shoppingCart = orderDto.getShoppingCart();
                            Boolean orderConfirm = cashierBL.createNewOrder(inventory, client, shoppingCart, orderDto.getTotal());
                            String orderConfirmJson = gson.toJson(orderConfirm);
                            out.println(orderConfirmJson);
                            break;

                        case "selectAllClients": //a function that takes all the clients and sends it to the client
                            String clients = gson.toJson(cashierBL.selectAllClients());
                            jsArray = new JSONArray(clients);
                            //  out.println(object);
                            out.println(jsArray);
                            break;
                        case "removeClient": //a supporting function that supports in a client removal
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientDeleted = managerBL.deleteClient(clientDto.getId());
                            out.println(gson.toJson(isClientDeleted));
                            break;

                        case "addNewClient": //a supporting function that supports client addition
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientAdded = cashierBL.addNewClient(clientDto.getId(), clientDto.getFullName(),
                                    clientDto.getPhoneNumber(), clientDto.getType().toString());
                            out.println(gson.toJson(isClientAdded));
                            break;
                        case "updateClient": //a supporting function that supports client update
                            clientDto = gson.fromJson(request, ClientDto.class);
                            boolean isClientUpdated = managerBL.updateClient(clientDto.getId(),
                                    clientDto.getFullName(), clientDto.getPhoneNumber(), clientDto.getType().toString(),
                                    clientDto.getClientCode());
                            out.println(gson.toJson(isClientUpdated));
                            break;
                        case "selectAllEmployessByBranch": //a function that sends back the client side all the employees by branch
                            EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String employees = gson.toJson(managerBL.selectAllEmployeesByBranch(employeeArrayDto.getBranch()));
                            jsArray = new JSONArray(employees);
                            out.println(jsArray);
                            break;
                        case "selectAllEmployees": //select all the employees and send it to the client side. using the DAL to access the DB.
                            String allEmployees = gson.toJson(managerBL.selectAllEmployees());
                            jsArray = new JSONArray(allEmployees);
                            out.println(jsArray);
                            break;

                        case "addNewEmployee": //server side function to add a new employee to the DB. using the DAL to access the DB.
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeAdded = managerBL.addEmployee(employeeDto.getName(), employeeDto.getPass(), employeeDto.getId(),
                                    employeeDto.getPhone(), employeeDto.getAccountNum(),
                                    employeeDto.getBranchNumber(), employeeDto.getJobPos().toString());
                            boolean isIncreasedInBranch = managerBL.increaseEmployeeInBranch(employeeDto.getBranchNumber());
                            out.println(gson.toJson(isEmployeeAdded && isIncreasedInBranch));
                            break;
                        case "removeEmployee": //server side function to remove an employee from the DB. using the DAL to access the DB.
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeDeleted = managerBL.deleteEmployee(employeeDto.getEmployeeNumber());
                            boolean isDecreasedInBranch = managerBL.decreaseEmployeeInBranch(employeeDto.getBranchNumber());

                            out.println(gson.toJson(isEmployeeDeleted && isDecreasedInBranch));
                            break;
                        case "updateEmployee": //server side function to update an employee in the DB. using the DAL to access the DB.
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            boolean isEmployeeUpdated = managerBL.updateEmployee(employeeDto.getName(), employeeDto.getId(),
                                    employeeDto.getPhone(), employeeDto.getAccountNum(), employeeDto.getBranchNumber(),
                                    employeeDto.getJobPos().toString(), employeeDto.getPass(), employeeDto.getEmployeeNumber());
                            out.println(gson.toJson(isEmployeeUpdated));
                            break;
                        case "selectBranchDetails": //select from the db all branch details
                            branchDto = gson.fromJson(request, BranchDto.class);
                            Branch branchInUse = managerBL.selectBranchDetails(branchDto.getBranchNumber());
                            String branchJson = gson.toJson(branchInUse);
                            out.println(branchJson);
                            break;
                        case "updateBranchPhoneNumber": //server side function to update a phone number of a specific branch in the DB. using the DAL to access the DB.
                            branchDto = gson.fromJson(request, BranchDto.class);
                            boolean isPhoneUpdated = managerBL.updateBranchPhoneNumber(branchDto.getPhone(),
                                    branchDto.getBranchNumber());
                            out.println(gson.toJson(isPhoneUpdated));
                            break;
                        case "addNewProduct": //server side function to add a new product. using the DAL to access the DB.
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductAdded = managerBL.addNewProduct(productDto.getName(), productDto.getPrice());
                            out.println(gson.toJson(isProductAdded));
                            break;
                        case "addProductAmountToInventory": //server side function to add an amount to a specific product. using the DAL to access the DB.
                            productDto = gson.fromJson(request, ProductDto.class);
                            int isProductAmountAdded = managerBL.addProductAmountToInventory(productDto.getInventoryCode(),
                                    productDto.getAmount(), productDto.getName());
                            out.println(gson.toJson(isProductAmountAdded));
                            break;
                        case "updateProduct": //server side function to update a product in the DB. using the DAL to access the DB.
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductUpdated = managerBL.updateProduct(productDto.getName(), productDto.getPrice(),
                                    productDto.getProductCode());
                            out.println(gson.toJson(isProductUpdated));
                            break;
                        case "updateProductAmountInInventory": //server side function to update an amount to a specific product in the DB. using the DAL to access the DB.
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductAmountUpdated = managerBL.updateProductAmountInInventory
                                    (productDto.getAmount(), productDto.getInventoryCode(), productDto.getProductCode());
                            out.println(gson.toJson(isProductAmountUpdated));
                            break;
                        case "removeProductFromInventory": //server side function to remove a product from the DB. using the DAL to access the DB.
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductDeleted = managerBL.removeProductFromInventory(
                                    productDto.getProductCode(), productDto.getInventoryCode());
                            out.println(gson.toJson(isProductDeleted));
                            break;
                        case "selectAllProducts": //server side function to take all the products in the DB. using the DAL to access the DB.
                            //EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String products = gson.toJson(managerBL.selectAllProducts());
                            jsArray = new JSONArray(products);
                            out.println(jsArray);
                            break;
                        case "reportByDate": //server side function to create a report by date. using the DAL to access the DB.
                            dateReportDto = gson.fromJson(request, DateReportDto.class);
                            boolean isReportCreated =
                                    managerBL.createReport_totalPurchasesInBranchByDate
                                            (dateReportDto.getBranchNumber(), dateReportDto.getDate());
                            out.println(gson.toJson(isReportCreated));
                            break;
                        case "salesReportByBranch": //server side function to create a sales report for a specific branch. using the DAL to access the DB.
                            dateReportDto = gson.fromJson(request, DateReportDto.class);
                            boolean isSalesReportCreated =
                                    managerBL.createReport_totalPurchasesInBranch(dateReportDto.getBranchNumber());
                            out.println(gson.toJson(isSalesReportCreated));
                            break;
                        case "productReports": //server side function to create sales report for a specific product by branch. using the DAL to access the DB.
                            ProductReportDto productReportDto = gson.fromJson(request, ProductReportDto.class);
                            boolean isProductReportCreated =
                                    managerBL.createReport_productSalesByBranch(productReportDto.getBranchNumber(),
                                            productReportDto.getProduct());
                            out.println(gson.toJson(isProductReportCreated));
                            break;
//                        case "getPeerByPornAndAddress": //server side function that sends back to the client the peer address and port so you would be able to chat with
//                            PeerDto peerDto = gson.fromJson(request, PeerDto.class);
//                            String empDetails = gson.toJson(searchPeerByPort(peerDto.getPeerAddress(),peerDto.getPeerPort()));
//                            out.println(empDetails);
//                            break;
                        case "getPeerDetails": //server side function to take details of a specific peer I want to talk to.
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            String details = searchConnectedPeer(employeeDto.getId());
                            out.println(gson.toJson(details));
                            break;
                        case "sendMessageLater": //server side function to support sending messages later to a not connected peer
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            messagesToSend.put(sendMessageLaterDto.getReceiver(), sendMessageLaterDto.getMsg());
                            //out.println(messagesToSend);
                            break;
                        case "checkMessages": //server side function to pull all the messages send for a disconnected user when connects
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            String messages = gson.toJson(checkMessages(sendMessageLaterDto.getReceiver()));
                            out.println(messages);
                            break;
                        case "CheckWhoSent": //server side function that sends the information about who sent the messages
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            int senderId = checkSenderId(sendMessageLaterDto.getReceiver());
                            if(senderId != 0)
                            {
                                Employee emp = cashierBL.selectEmpDetailsById(senderId);
                                details = gson.toJson(emp);
                                out.println(details);
                            }
                            else
                                out.println("null");
                            break;
                        case "messageBoardNotEmpty": //a supporting function that check if a message board is not empty
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            Boolean isEmpty = messagesToSend.containsKey(sendMessageLaterDto.getReceiver());
                            out.println(gson.toJson(isEmpty));
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
                    //when a socket is closed, removed the employee from the connected users array and connection array
                    if (connectedUsers.size() != 0 && connectionArray.size() != 0) {
                        connectedUsers.remove(loggedInEmployee);
                        connectionArray.remove(sslSocketData);
                    }
                    sslSocket.close(); //close the sslSocket
                    managerBL.closeConnection(); //close the connection to the db
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Same Employee can't logged in twice", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    //utility function to check sender id in chat
    private int checkSenderId(int receiver) {
        if(messagesToSend.containsKey(receiver)) {
            return messagesToSend.get(receiver).getSender();
        }
        else return 0;
    }

    //utility function to check if there are messages waiting for the employee send to the function
    private String checkMessages(int employeeNumber) {
        String msg ="";
        if(messagesToSend.containsKey(employeeNumber))
        {
            for(int i =0; i<messagesToSend.get(employeeNumber).getAllMessages().size(); i++)
                msg += messagesToSend.get(employeeNumber).getAllMessages().get(i) + "~";
            //messagesToSend.get(employeeNumber).getAllMessages().clear();
            messagesToSend.remove(employeeNumber);
            return msg;
        }
        else

            return "false";

    }

    //utility function to search a peer in the connected users and connection array by his port
    private Employee searchPeerByPort(InetAddress peerSocketAddress, int peerPort) {
        String address = peerSocketAddress.getHostAddress();
        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectionArray.get(i).getClientAddress().equals(address) &&
            listeningPorts.get(i).equals(peerPort)) {
                return connectedUsers.get(i);
            }
        }
        return null;
    }

    //utility function to search for a connected peer
    private String searchConnectedPeer(int id) {

        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectedUsers.get(i).getId() == id) {
             return connectedUsers.get(i).getName() + "," + connectionArray.get(i).getClientAddress()
                        +","+ listeningPorts.get(i).toString();//connectionArray.get(i).getSslSocket().getPort();
            }
        }
        return "false";
    }


    //get the connected users array
    public ArrayList<Employee> getConnectedUsers() {
        return connectedUsers;
    }


    //utility function that check if a user is already connected to prevent him from login twice
    private boolean checkIfConnected(Employee loggedInEmployee, int port) throws Exception {
        if (connectedUsers.size() == 0) {
            connectedUsers.add(loggedInEmployee);
            sslSocketData.setEmployee(loggedInEmployee);
            connectionArray.add(sslSocketData);
            listeningPorts.add(port);
            return false;
        } else {
            for (int i = 0; i < connectedUsers.size(); i++) {
                if (connectedUsers.get(i).getId() == loggedInEmployee.getId()) {
                    return true;

                } else {
                    if (loggedInEmployee != null) {
                        connectedUsers.add(loggedInEmployee);
                        sslSocketData.setEmployee(loggedInEmployee);
                        connectionArray.add(sslSocketData);
                        listeningPorts.add(port);
                        return false;
                    } else return true;
                }
            }

        }
        return true;

    }

}


