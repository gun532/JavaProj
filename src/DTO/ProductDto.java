package DTO;

public class ProductDto extends DtoBase{
    private String name;
    private double price;
    private int amount;
    private int productCode;
    private int inventoryCode;

    public ProductDto(String func, String name, double price, int amount, int productCode, int inventoryCode) {
        super(func);
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productCode = productCode;
        this.inventoryCode = inventoryCode;
    }

    public int getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(int inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }
}
