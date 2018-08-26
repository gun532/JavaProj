package Entities.Employee;

import BL.CashierBL;
import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;

public class Cashier extends Employee{
    private CashierBL cashierBL;

    public Cashier(String name, int id, String phone, int accountNum, int branchNumber)
    {
        super(name, id, phone, accountNum,branchNumber, Profession.CASHIER);
        cashierBL = new CashierBL(new EmployeeDataAccess(),new InventoryDataAccess());
    }

    public Employee selectEmpDetailsById(int id) {
        return cashierBL.selectEmpDetailsById(id);
    }

    public Employee selectEmpDetailsByEmployeeNum(int id) {
        return cashierBL.selectEmpDetailsByEmployeeNum(id);
    }

//    void createNewOrder(int branchName, int productCode, int amount) throws Exception {
//        cashierBL.createNewOrder(branchName,productCode,amount);
//}




//    //we will wrap this function with an on click changed event to make it run few times
//    //product code and amount will be generated from a textbox. when the gui is ready we will take the
//    //values from the textbox inside the function
//    void createNewOrder(int branchName, int productCode, int amount) throws Exception {
//        Inventory inventory = managerDataAccess.selectFromInventory(branchName);
//        Product p = inventory.takeFromInventory(productCode, amount);
//        ShoppingCart cart = new ShoppingCart();
//        cart.addToCart(p);
//        managerDataAccess.updateInventory(inventory);
//    }


//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
