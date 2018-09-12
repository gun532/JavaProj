package Entities;

import BL.InventoryBL;
import DAL.InventoryDataAccess;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer, Product> myInventory;
    private int inventoryNumber;
    private int totalProducts;
    private int totalItems;
    private double totalValue;

    private InventoryBL inventoryBL;

    public Inventory(){
        this.myInventory = new LinkedHashMap<>();
        this.totalProducts = 0;
        inventoryBL = new InventoryBL(new InventoryDataAccess());
    }

    public void addToInventory(Product product) throws Exception {

        //Check if inventory already has this product.
        if(this.myInventory.containsKey(product.getProductCode())) {
            throw new Exception("The product is already in the store's inventory.");
            //possible to ask if admin wants to update
        }
        //else add a new product to inventory
        myInventory.put(product.getProductCode(), product);
        this.totalProducts++;
        this.totalItems += product.getAmount();
        this.totalValue += product.getPrice()*product.getAmount();
    }

//    public void removeFromInventory(Product product) throws Exception {
//        Product refProduct = this.myInventory.get(product.getProductCode());
//        if(refProduct != null){
//            this.totalProducts--;
//            this.totalItems -= product.getAmount();
//            this.totalValue += product.getPrice()*product.getAmount();
//            this.myInventory.remove(refProduct.getProductCode());
//        }
//        else{
//            throw new Exception("The product is not in the inventory.");
//        }
//    }

    public Product returnToInventory(int productCode, int amount) throws Exception {
        if(amount >= 0) {
            Product refProduct = this.myInventory.get(productCode);
            Product product = new Product(refProduct.getName(),refProduct.getPrice(),amount,productCode);
            if (refProduct != null) {
                //update new data
                refProduct.setAmount(refProduct.getAmount() + amount);
                this.totalItems += amount;
                this.totalValue += refProduct.getPrice()*amount;

                return product;
            } else {
                throw new Exception("The product is not in the inventory.");
            }
        }
        else {
            throw new Exception("The product amount can't be negative.");
        }
    }

    public Product takeFromInventory(int productCode, int amount) throws Exception {
        if(amount >= 0) {
            Product refProduct = this.myInventory.get(productCode);
            if (refProduct != null) {
                if (refProduct.getAmount() - amount >= 0) {
                    refProduct.setAmount(refProduct.getAmount() - amount);
                    this.totalItems -= amount;
                    this.totalValue -= refProduct.getPrice() * amount;
                    Product product = new Product(refProduct);
                    product.setAmount(amount);
                    return product;
                } else
                    throw new Exception("There's not enough items in the inventory.");
            } else
                throw new Exception("The product is not in the inventory.");
        }else {
            throw new Exception("The product amount can't be negative.");
        }
    }

    public void updateInventory(Product p) throws Exception {

        Product refProduct = this.myInventory.get(p.getProductCode());

        if (refProduct != null) {
            this.myInventory.replace(refProduct.getProductCode(),refProduct,p);
        }else {
            throw new Exception("The product is not in the inventory.");
        }
    }

    public Map<Integer, Product> getMyInventory() {
        return myInventory;
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

    public double getTotalValue() {
        return totalValue;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public void setMyInventory(Map<Integer, Product> myInventory) {
        this.myInventory = myInventory;
    }

    @Override
    public String toString() {
        return "\nAll products in store's inventory:\n"
                + this.myInventory.toString()
                + "\nNumber of products: " + this.totalProducts
                + "\nNumber of items: " + this.totalItems
                + "\nTotal Value: " + this.totalValue;
    }
}
