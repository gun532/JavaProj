package DTO;

import Entities.Clients.Client;
import Entities.Inventory;
import Entities.ShoppingCart;

public class OrderDto extends DtoBase {
    private Inventory inventory;
    private int clientId;
    private ShoppingCart shoppingCart;
    private double total;


    public OrderDto(String func, Inventory inventory, int clientId, ShoppingCart shoppingCart, double total) {
        super(func);
        this.inventory = inventory;
        this.clientId = clientId;
        this.shoppingCart = shoppingCart;
        this.total = total;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
