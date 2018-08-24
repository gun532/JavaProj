package ClientsPackage;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import EmployeePackage.Cashier;
import EmployeePackage.Employee;

public class ShoppingCart {
    private Map<Integer, Product> cart;
    private int branchCode;
    private int employeeCode;
    private int totalPrice;
    private int totalItems;
    private Date cartDate;

    public ShoppingCart(){
        this.cart = new LinkedHashMap<Integer, Product>();
        this.branchCode = 0;
        this.employeeCode = 0;
        this.totalPrice = 0;
        this.totalItems = 0;
        this.cartDate = new Date();
    }

    public ShoppingCart(Employee employee, Client client){
        this.cart = new LinkedHashMap<Integer, Product>();
        this.branchCode = employee.getBranchNumber();
        this.employeeCode = employee.getEmployeeNumber();
        this.totalPrice = 0;
        this.totalItems = 0;
        this.cartDate = new Date();
        client.addNewCartToHistory(this);
    }

    public void addToCart(Product product) throws Exception {
            if (cart.containsValue(product)) {
                this.cart.get(product.getProductCode()).incAmount(product.getAmount());
            } else {
                this.cart.put(product.hashCode(), product);
            }

            this.totalItems += product.getAmount();
            this.totalPrice += product.getPrice() * product.getAmount();
    }

   public void removeFromCart(Product product) throws Exception {
        Product refProduct = this.cart.get(product.getProductCode());
        if(refProduct != null){
            if(refProduct.getAmount() - product.getAmount() >= 0) {
                refProduct.decAmount(product.getAmount());
                if (refProduct.getAmount() == 0)
                    this.cart.remove(product.getProductCode());

                this.totalItems -= product.getAmount();
                this.totalPrice -= product.getPrice()*product.getAmount();
            }
            else{
                throw new Exception("You are trying to remove more items than you have in cart.");
            }
        }
        else{
            throw new Exception("The product is not in the shopping cart.");
        }
    }

    public Map<Integer, Product> getCart() {
        return cart;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Date getCartDate() {
        return cartDate;
    }

    @Override
    public String toString() {
        return "Cart Number"
                + "\nContains:\n"
                + this.cart.toString()
                + "\nTotal products: " + this.totalItems
                + "\nTotal amount: " + this.totalPrice
                + "\nOrder Date: " + this.cartDate;
    }

    public static void main(String[] args) throws Exception {
        Branch branch = new Branch("Tel-Aviv", 6, "03-690370");
        Cashier e1 = new Cashier("Dani", 302343567, "052343567", 6, branch.getBranchNumber());
        Product p1 = new Product("shirt", 30, 100);
        Product p2 = new Product("jeans", 120, 12);
        branch.getBranchInventory().addToInventory(p1);
        branch.getBranchInventory().addToInventory(p2);
        System.out.println(branch);
        Client cl1 = new Client(304989171,"Roy Bar","0506797973") {};
        ShoppingCart testShopCart1 = new ShoppingCart(e1,cl1);
        Product p3 = branch.getBranchInventory().takeFromInventory(p1.getProductCode(),2);
        testShopCart1.addToCart(p3);
        System.out.println(branch);
        Product p1Return = new Product("shirt", 30, 1);
        testShopCart1.removeFromCart(p1Return);
        branch.getBranchInventory().returnToInventory(p1Return.getProductCode(),p1Return.getAmount());
        cl1.addNewCartToHistory(testShopCart1);

        System.out.println(testShopCart1);
        System.out.println(cl1);
        System.out.println(branch);
    }
}
