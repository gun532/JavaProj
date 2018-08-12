package ClientsPackage;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Integer, Product> cart;
    private int branchCode;
    private int employeeCode;
    private int totalAmount;
    private int totalProducts;
    private Date cartDate;

    public ShoppingCart(){
        this.cart = new LinkedHashMap<Integer, Product>();
        this.branchCode = 0;
        this.employeeCode = 0;
        this.totalAmount = 0;
        this.totalProducts = 0;
        this.cartDate = new Date();
    }

    public ShoppingCart(Employee employee, Client client){
        this.cart = new LinkedHashMap<Integer, Product>();
        this.branchCode = employee.getBranchNumber();
        this.employeeCode = employee.getEmployeeNumber();
        this.totalAmount = 0;
        this.totalProducts = 0;
        this.cartDate = new Date();
        client.addNewCartToHistory(this);
    }


//    public void addToCart(int productKey, int amount)
//    {
//        //Product product =
//            if (cart.containsKey(productKey)) {
//                int amountInCart = this.cart.get(productKey).getAmount();
//                this.cart.get(productKey).setAmount(amountInCart + amount);
//                //update branch inventory
//            } else {
//                //copy form inventory
//                Product newProduct = new Product(branch.inventory.get(productKey));
//                this.cart.put(newProduct.getProductCode(), newProduct);
//            }
//
//            this.totalProducts++;
//            this.totalAmount += newProduct.getPrice();
//    }
//
//    public void removeFromCart(Product removeProduct) throws Exception {
//        Product refProduct = this.cart.get(removeProduct.getProductCode());
//        if(refProduct != null){
//            refProduct.decAmount();
//            if(refProduct.getAmount() == 0)
//                this.cart.remove(removeProduct.getProductCode());
//
//            this.totalProducts--;
//            this.totalAmount -= removeProduct.getPrice();
//        }
//        else{
//            throw new Exception("The product is not in the shopping cart.");
//        }
//    }

    public Map<Integer, Product> getCart() {
        return cart;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public Date getCartDate() {
        return cartDate;
    }

    @Override
    public String toString() {
        return "All products in cart:\n"
                + this.cart.toString()
                + "\nTotal products: " + this.totalProducts
                + "\nTotal amount: " + this.totalAmount
                + "\nOrder Date: " + this.cartDate;
    }

    public static void main(String[] args) throws Exception {
        Branch branch = new Branch("Tel-Aviv", 6, "03-690370");
        Client cl1 = new Client(304989171,"Roy Bar","0506797973") {};
        Product p1 = new Product("shirt", 30, 1);
        Product p2 = new Product("jeans", 120, 1);
        Inventory inventory = branch.getBranchInventory();
        inventory.addToInventory(p1);
        branch.getBranchInventory().addToInventory(p2);
        //ShoppingCart testShopCart1 = new ShoppingCart();
        //testShopCart1.removeFromCart(newProduct1);
        //testShopCart1.removeFromCart(newProduct1);
        //cl1.addNewCartToHistory(testShopCart1);
        //System.out.println(testShopCart1);
        //System.out.println(cl1);
        System.out.println(branch);
    }
}
