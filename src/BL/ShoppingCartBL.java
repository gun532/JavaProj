package BL;

import DAL.InventoryDataAccess;
import Entities.Inventory;
import Entities.Product;
import Entities.ShoppingCart;

public class ShoppingCartBL {

    private InventoryDataAccess inventoryDataAccess;

    public ShoppingCartBL(InventoryDataAccess inventoryDataAccess) {
        this.inventoryDataAccess = inventoryDataAccess;
    }


}
