package EmployeePackage;

import ClientsPackage.Inventory;
import ClientsPackage.Product;
import ClientsPackage.ShoppingCart;

import java.sql.SQLException;

public class Cashier extends Employee{
    private ManagerUtility managerUtility;
    {
        try {
            managerUtility = new ManagerUtility();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cashier(String name, int id, String phone, int accountNum, int branchNumber)
    {
        super(name, id, phone, accountNum,branchNumber, Profession.CASHIER);
    }


    public void addEmployee(Employee emp) {
        managerUtility.addEmployee(emp);
    }


    public void deleteEmployee(int empID) {
        managerUtility.deleteEmployee(empID);
    }

    void updateEmployee(Employee e1) {
        managerUtility.updateEmployee(e1);
    }


    Employee selectDetails(int empNum) {
        return managerUtility.selectDetails(empNum);
    }

    void newOrder(int branchName, int productCode, int amount) throws Exception {
        managerUtility.newOrder(branchName,productCode,amount);
    }



//    //we will wrap this function with an on click changed event to make it run few times
//    //product code and amount will be generated from a textbox. when the gui is ready we will take the
//    //values from the textbox inside the function
//    void newOrder(int branchName, int productCode, int amount) throws Exception {
//        Inventory inventory = managerUtility.selectFromInventory(branchName);
//        Product p = inventory.takeFromInventory(productCode, amount);
//        ShoppingCart cart = new ShoppingCart();
//        cart.addToCart(p);
//        managerUtility.updateInventory(inventory);
//    }


//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
