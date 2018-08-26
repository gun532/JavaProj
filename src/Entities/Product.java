package Entities;

public class Product {
    //private int productCode;
    private String name;
    private double price;
    private int amount;
    private int productCode;

    public Product(){
        this.name = null;
        this.price = 0;
        this.amount = 0;
        this.productCode = 0;
    }
    public Product(String name, double price, int amount, int productCode) throws Exception {
        if(price >= 0 && amount >= 0) {
            this.name = name;
            this.price = price;
            this.amount = amount;
            this.productCode = productCode;
        }
        else {
            throw new Exception("Invalid input! price and amount can't be a negative number.");
        }
    }

    public Product(Product product)
    {
        this.productCode = product.productCode;
        this.name = product.name;
        this.price = product.price;
        this.amount = product.amount;
    }

    public int getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) throws Exception {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new Exception("Amount must be positive.");
        }
    }

    public void setAmount(int amount) throws Exception {
        if(amount >=0){
            this.amount = amount;
        } else{
            throw new Exception("Amount must be positive.");
        }
    }

    public void incAmount(int amount) throws Exception {
        if(amount >=0){
            this.amount+= amount;
        } else{
          throw new Exception("Amount must be positive.");
        }
    }

    public void decAmount(int amount) throws Exception {
        if(amount >=0){
            this.amount-= amount;
        } else{
            throw new Exception("Amount must be positive.");
        }
    }

    @Override
    public String toString() {
        return "Product code" + "\nName: " + this.name + "\nPrice: " + this.price
                + "\nAmount: " + this.amount + "\nTotal Value: " + this.price*this.amount + '\n';
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = (int) (hash * 17 + this.price);
        hash = hash * 31 + this.name.hashCode();
        return hash;
    }
}
