package DTO;

import Entities.Product;

import java.util.Map;

public class InventoryDto extends DtoBase {
    private Map<Integer, Product> myInventory;
    private int inventoryNumber;
    private int totalProducts;
    private int totalItems;
    private double totalValue;

    public InventoryDto(String func, Map<Integer, Product> myInventory, int inventoryNumber, int totalProducts, int totalItems, double totalValue) {
        super(func);
        this.myInventory = myInventory;
        this.inventoryNumber = inventoryNumber;
        this.totalProducts = totalProducts;
        this.totalItems = totalItems;
        this.totalValue = totalValue;
    }

    public Map<Integer, Product> getMyInventory() {
        return myInventory;
    }

    public void setMyInventory(Map<Integer, Product> myInventory) {
        this.myInventory = myInventory;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
