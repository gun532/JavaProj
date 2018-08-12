package ClientsPackage;

//MAY NEED TO DISTINCT BETWEEN PRODUCT TO ITEM
public class Product {
    private static int counter = 0;
    private  int productCode;
    private String name;
    private double price;
    private int amount;

    public Product(){}
    public Product(String name, double price, int amount)
    {
        this.productCode = counter++;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product(int productCode, int amount)
    {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.amount = amount;
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
        return "Product code: " + this.productCode + ", Name: " + this.name + ", Price: " + this.price
                + ", Amount: " + this.amount;
    }
}
