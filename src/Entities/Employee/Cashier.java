package Entities.Employee;

//import BL.CashierBL;
//import DAL.EmployeeDataAccess;
//import DAL.InventoryDataAccess;

public class Cashier extends Employee{
    //private CashierBL cashierBL;

    public Cashier(int employeeNumber, String name, int id, String phone, int accountNum, int branchNumber)
    {
        super(employeeNumber, name, id, phone, accountNum,branchNumber, Profession.CASHIER);
        //cashierBL = new CashierBL(new EmployeeDataAccess());
    }

//    public Employee selectEmpDetailsById(int id) {
//        return cashierBL.selectEmpDetailsById(id);
//    }

//    public Employee selectEmpDetailsByEmployeeNum(int id) {
//        return cashierBL.selectEmpDetailsByEmployeeNum(id);
//    }


//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
