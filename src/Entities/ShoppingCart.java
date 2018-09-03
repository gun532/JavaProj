package Entities;

import Entities.Employee.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class ShoppingCart {
    private int cartID;
    private Map<Integer, Product> cart;
    private int branchCode;
    private int employeeCode;
    private double totalPrice;
    private int totalItems;
    private Date cartDate;

    public ShoppingCart(){
        this.cartID = hashCode()/50000;
        this.cart = new LinkedHashMap<>();
        this.branchCode = 0;
        this.employeeCode = 0;
        this.totalPrice = 0;
        this.totalItems = 0;
        this.cartDate = new Date();
    }

    public ShoppingCart(Employee employee){
        this.cartID = hashCode()/50000;
        this.cart = new LinkedHashMap<>();
        this.branchCode = employee.getBranchNumber();
        this.employeeCode = employee.getEmployeeNumber();
        this.totalPrice = 0;
        this.totalItems = 0;
        this.cartDate = new Date();
    }

    public void addToCart(Product product) throws Exception {
            if (cart.containsKey(product.getProductCode())) {
                this.cart.get(product.getProductCode()).incAmount(product.getAmount());
            } else {
                this.cart.put(product.getProductCode(), product);
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

    public int getCartID() {
        return cartID;
    }


    public Map<Integer, Product> getCart() {
        return cart;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Date getCartDate() {
        return cartDate;
    }

    public int getBranchCode() {
        return branchCode;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setCart(Map<Integer, Product> cart) {
        this.cart = cart;
    }

    public void setCartDate(Date cartDate) {
        this.cartDate = cartDate;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
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
}
