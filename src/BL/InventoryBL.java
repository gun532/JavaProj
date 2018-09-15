package BL;

import Entities.Inventory;
import Entities.Product;
import Entities.ShoppingCart;
import DAL.InventoryDataAccess;

public class InventoryBL {

    private transient InventoryDataAccess inventoryDataAccess;

    public InventoryBL(InventoryDataAccess inventoryDataAccess) {
        this.inventoryDataAccess = inventoryDataAccess;
    }

    public Inventory selectFromInventory(int branch)
    {
        return inventoryDataAccess.selectFromInventory(branch);
    }

    public void createNewOrder(Inventory inventory) {

        inventoryDataAccess.updateInventory(inventory);
    }
}
