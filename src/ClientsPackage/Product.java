package ClientsPackage;

public class Product {
    private int productCode;
    private String name;
    private double price;
    private int amount;

    public Product(){
        this.name = null;
        this.price = 0;
        this.amount = 0;
        this.productCode = hashCode();
    }
    public Product(String name, double price, int amount)
    {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productCode = hashCode();
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incAmount(){
        this.amount++;
    }

    public void decAmount(){
        this.amount--;
    }

    @Override
    public String toString() {
        return "Product code" + "\nName: " + this.name + "\nPrice: " + this.price
                + "\nAmount: " + this.amount + "\nTotal Value: " + this.price*this.amount + '\n';
    }
}
