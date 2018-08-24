package ClientsPackage;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer, Product> myInventory;
    private int inventoryNumber;
    private int totalProducts;
    private int totalItems;
    private double totalValue;

    public Inventory(){
        this.myInventory = new LinkedHashMap<Integer, Product>();
        this.totalProducts = 0;
    }

//    public void addToInventory(Product product) throws Exception {
//
//        //Check if inventory already has this product.
//        if(this.myInventory.containsValue(product)) {
//            throw new Exception("The product is already in the store's inventory.");
//            //possible to ask if admin wants to update
//        }
//        //else add a new product to inventory
//        this.myInventory.put(product.hashCode(), product);
//        this.totalProducts++;
//        this.totalItems += product.getAmount();
//        this.totalValue += product.getPrice()*product.getAmount();
//    }

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

    public void returnToInventory(int productCode, int amount) throws Exception {
        if(amount >= 0) {
            Product refProduct = this.myInventory.get(productCode);
            if (refProduct != null) {
                //update new data
                refProduct.setAmount(refProduct.getAmount() + amount);
                this.totalItems += amount;
                this.totalValue += refProduct.getPrice()*amount;
            } else {
                throw new Exception("The product is not in the inventory.");
            }
        }
        else {
            throw new Exception("The product amount can't be negative.");
        }
    }

    public Product takeFromInventory(int productCode, int amount) throws Exception {
        Product refProduct = this.myInventory.get(productCode);
        if(refProduct != null){
            if(refProduct.getAmount() - amount >=0) {
                refProduct.setAmount(refProduct.getAmount() - amount);
                this.totalItems -= amount;
                this.totalValue -= refProduct.getPrice()*amount;
                Product product = new Product(refProduct);
                product.setAmount(amount);
                return product;
            }
            else
                throw new Exception("There's not enough items in the inventory.");
        }
        else
            throw new Exception("The product is not in the inventory.");
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
