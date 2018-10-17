package BL;

import BL.CashierBL;
import BL.LoginUtility;
import BL.ManagerBL;
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
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class SocketServer extends Thread{
    public static ArrayList<Employee> connectedUsers = new ArrayList<>();
//    public  ArrayList<SSLSocket> connectionArray = new ArrayList<>(); //hold all the connections
    public  static ArrayList<SSLSocketData> connectionArray = new ArrayList<>(); //hold all the connections
    public  static ArrayList<Integer> listeningPorts = new ArrayList<>();
    public static Map<Integer, Message> messagesToSend = new LinkedHashMap<>();

    SSLSocketData sslSocketData;


    public final int PORT_NUMBER = 9999;
     DataInputStream in = null;
     PrintStream out = null;
    //static Socket socket;
    //static ServerSocket server = null;
    private ManagerBL managerBL = new ManagerBL(new ManagerDataAccess());
    private CashierBL cashierBL = new CashierBL(new EmployeeDataAccess(), new InventoryDataAccess(), new ClientsDataAccess());

    Employee loggedInEmployee = null;

    SSLSocket sslSocket;

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

                    switch (dtoBase.getFunc()) {
                        case "login":
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
                            //ArrayList<BL.Client> clientArrayList = cashierBL.selectAllClients();
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
                        case "selectAllEmployessByBranch":
                            EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String employees = gson.toJson(managerBL.selectAllEmployeesByBranch(employeeArrayDto.getBranch()));
                            jsArray = new JSONArray(employees);
                            out.println(jsArray);
                            break;
                        case "selectAllEmployees":
//                            EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String allEmployees = gson.toJson(managerBL.selectAllEmployees());
                            jsArray = new JSONArray(allEmployees);
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
                            boolean isProductAdded = managerBL.addNewProduct(productDto.getName(), productDto.getPrice());
                            out.println(gson.toJson(isProductAdded));
                            break;
                        case "addProductAmountToInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            int isProductAmountAdded = managerBL.addProductAmountToInventory(productDto.getInventoryCode(),
                                    productDto.getAmount(), productDto.getName());
                            out.println(gson.toJson(isProductAmountAdded));
                            break;
                        case "updateProduct":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductUpdated = managerBL.updateProduct(productDto.getName(), productDto.getPrice(),
                                    productDto.getProductCode());
                            out.println(gson.toJson(isProductUpdated));
                            break;
                        case "updateProductAmountInInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductAmountUpdated = managerBL.updateProductAmountInInventory
                                    (productDto.getAmount(), productDto.getInventoryCode(), productDto.getProductCode());
                            out.println(gson.toJson(isProductAmountUpdated));
                            break;
                        case "removeProductFromInventory":
                            productDto = gson.fromJson(request, ProductDto.class);
                            boolean isProductDeleted = managerBL.removeProductFromInventory(
                                    productDto.getProductCode(), productDto.getInventoryCode());
                            out.println(gson.toJson(isProductDeleted));
                            break;
                        case "selectAllProducts":
                            //EmployeeArrayDto employeeArrayDto = gson.fromJson(request, EmployeeArrayDto.class);
                            String products = gson.toJson(managerBL.selectAllProducts());
                            jsArray = new JSONArray(products);
                            out.println(jsArray);
                            break;
                        case "reportByDate":
                            DateReportDto dateReportDto = gson.fromJson(request, DateReportDto.class);
                            boolean isReportCreated =
                                    managerBL.createReport_totalPurchasesInBranchByDate
                                            (dateReportDto.getBranchNumber(), dateReportDto.getDate());
                            out.println(gson.toJson(isReportCreated));
                            break;
                        case "getPeerDetails":
                            employeeDto = gson.fromJson(request, EmployeeDto.class);
                            String details = searchConnectedPeer(employeeDto.getId());
                            out.println(gson.toJson(details));
                            break;
                        case "getPeerByPornAndAddress":
                            PeerDto peerDto = gson.fromJson(request, PeerDto.class);
                            String empDetails = gson.toJson(searchPeerByPort(peerDto.getPeerAddress(),peerDto.getPeerPort()));
                            out.println(empDetails);
                            break;
                        case "sendMessageLater":
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            messagesToSend.put(sendMessageLaterDto.getReceiver(), sendMessageLaterDto.getMsg());
                            //out.println(messagesToSend);
                            break;
                        case "checkMessages":
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            String messages = gson.toJson(checkMessages(sendMessageLaterDto.getReceiver()));
                            out.println(messages);
                            break;
                        case "CheckWhoSent":
                            sendMessageLaterDto = gson.fromJson(request, SendMessageLaterDto.class);
                            int senderId = checkSenderId(sendMessageLaterDto.getReceiver());
                            if(senderId != 0)
                            {
                                Employee emp = cashierBL.selectEmpDetailsById(senderId);
                                details = gson.toJson(emp);
                                out.println(details);
                            }
                            else
                            {
                                out.println("null");
                            }
                            break;
                        case "messageBoardNotEmpty":
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
                    if (connectedUsers.size() != 0 && connectionArray.size() != 0) {
                        connectedUsers.remove(loggedInEmployee);
                        connectionArray.remove(sslSocketData);
                    }
                    sslSocket.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Same Employee can't logged in twice", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    private int checkSenderId(int receiver) {
        if(messagesToSend.containsKey(receiver)) {
            return messagesToSend.get(receiver).getSender();
        }
        else return 0;
    }


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

    private String searchConnectedPeer(int id) {

        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectedUsers.get(i).getId() == id) {
             return connectedUsers.get(i).getName() + "," + connectionArray.get(i).getClientAddress()
                        +","+ listeningPorts.get(i).toString();//connectionArray.get(i).getSslSocket().getPort();
            }
        }
        return "false";
    }



    public ArrayList<Employee> getConnectedUsers() {
        return connectedUsers;
    }


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


