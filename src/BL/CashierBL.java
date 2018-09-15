package BL;

import DAL.ClientsDataAccess;
import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;
import Entities.Clients.Client;
import Entities.Employee.Employee;
import Entities.Inventory;
import Entities.ShoppingCart;

import java.util.ArrayList;

public class CashierBL {
    private EmployeeDataAccess employeeDataAccess;
    private InventoryDataAccess inventoryDataAccess;
    private ClientsDataAccess clientsDataAccess;

    public CashierBL(EmployeeDataAccess employeeDataAccess, InventoryDataAccess inventoryDataAccess, ClientsDataAccess clientsDataAccess) {
        this.employeeDataAccess = employeeDataAccess;
        this.inventoryDataAccess = inventoryDataAccess;
        this.clientsDataAccess = clientsDataAccess;
    }

    public CashierBL(ClientsDataAccess clientsDataAccess) {
        this.clientsDataAccess = clientsDataAccess;
    }


    public Employee selectEmpDetailsById(int id) {
        return employeeDataAccess.selectEmpDetailsById(id);
    }

    public Employee selectEmpDetailsByEmployeeNum(int id) {
        return employeeDataAccess.selectEmpDetailsByEmployeeNum(id);
    }

    public ArrayList<Client> selectAllClients()
    {
        return clientsDataAccess.selectAllClients();
    }

    public boolean addNewClient(int id, String name, String phone, String clientType)
    {
        return clientsDataAccess.addNewClient(id,name,phone, clientType);
    }

    public Client selectClientByID(int id)
    {
        return clientsDataAccess.selectClientByID(id);
    }

    public Inventory selectFromInventory(int branch)
    {
        return inventoryDataAccess.selectFromInventory(branch);
    }

    public boolean createNewOrder(Inventory inventory, Client client, ShoppingCart shoppingCart, double total) {

        clientsDataAccess.createNewOrder(shoppingCart,client.getClientCode(),total);
        inventoryDataAccess.updateInventory(inventory);
        return true;
    }


//    private void insertShoppingCart(ShoppingCart shoppingCart, Client client)
//    {
//        clientsDataAccess.insertShoppingCart(shoppingCart);
//        clientsDataAccess.insertCartDetails(shoppingCart);
//        clientsDataAccess.insertToShoppingHistory(client.getClientCode(),shoppingCart.getCartID());
//    }

}
