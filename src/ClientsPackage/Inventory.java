package ClientsPackage;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer, Product> myInventory;
    private int totalProducts;

    public Inventory(){
        this.myInventory = new LinkedHashMap<Integer, Product>();
        this.totalProducts = 0;
    }

    public void addToInventory(Product product) throws Exception {

        //Check if inventory already has this product.
        if(this.myInventory.containsValue(product)) {
            throw new Exception("The product is already in the store's inventory.");
            //possible to ask if admin wants to update
        }
        //else add a new product to inventory
        this.myInventory.put(product.hashCode(), product);
        this.totalProducts++;
    }

    public void removeFromInventory(Product product) throws Exception {
        Product refProduct = this.myInventory.get(product.getProductCode());
        if(refProduct != null){
            this.myInventory.remove(refProduct.getProductCode());
            this.totalProducts--;
        }
        else{
            throw new Exception("The product is not in the inventory.");
        }
    }

    public void updateInventory(int productCode, int amount) throws Exception {
        if(amount >= 0) {
            Product refProduct = this.myInventory.get(productCode);
            if (refProduct != null) {
                refProduct.setAmount(amount);
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

    public int getTotalProducts() {
        return totalProducts;
    }

    @Override
    public String toString() {
        return "\nAll products in store's inventory:\n"
                + this.myInventory.toString()
                + "\nTotal products: " + this.totalProducts;
    }
}
