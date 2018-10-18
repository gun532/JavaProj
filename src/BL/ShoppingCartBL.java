package BL;

import DAL.ClientsDataAccess;
import DAL.InventoryDataAccess;
import Entities.Inventory;
import Entities.Product;
import Entities.ShoppingCart;

public class ShoppingCartBL {

    private transient InventoryDataAccess inventoryDataAccess;
    private transient ClientsDataAccess clientsDataAccess;

    public ShoppingCartBL(InventoryDataAccess inventoryDataAccess, ClientsDataAccess clientsDataAccess) {
        this.inventoryDataAccess = inventoryDataAccess;
        this.clientsDataAccess = clientsDataAccess;
    }

    public ShoppingCartBL(ClientsDataAccess clientsDataAccess) {
        this.clientsDataAccess = clientsDataAccess;
    }

    public ShoppingCartBL(InventoryDataAccess inventoryDataAccess) {
        this.inventoryDataAccess = inventoryDataAccess;
    }

}
