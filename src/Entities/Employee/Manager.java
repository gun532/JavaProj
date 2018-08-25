package Entities.Employee;

import DAL.ManagerDataAccess;

import java.util.logging.Logger;


public class Manager extends Employee {

    private static final Logger LOGGER = Logger.getLogger(Manager.class.getName());

    private ManagerDataAccess managerDataAccess;


    public Manager(String name, int id, String phone, int accountNum, int branchNumber) {
        super(name, id, phone, accountNum,branchNumber, Profession.MANAGER);
    }

//    public void addEmployee(Employee emp) {
//        managerDataAccess.addEmployee(emp);
//        LOGGER.info("A ");
//    }


//    public void deleteEmployee(int empID) {
//        managerDataAccess.deleteEmployee(empID);
//    }
//
//    public void updateEmployee(Employee e1) {
//        managerDataAccess.updateEmployee(e1);
//     }

//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " + this.getJobPos() + ", Employee's branch number: " + this.getBranchNumber();
//        return str;
//    }


//    Employee selectDetails(int empNum) {
//        return managerDataAccess.selectDetails(empNum);
//    }


//    void createNewOrder(int branchName, int productCode, int amount) throws Exception {
//        managerDataAccess.createNewOrder(branchName,productCode,amount);
//    }




//    public static void main(String[] args) {
//        Manager m = new Manager("Guy", 1243, "050-112211", 234532, 2);
//        //Employee e = new Seller("Joseph Stalin",44354366,"052-423576822",9984848,m.getBranchNumber());
//        //m.addEmployee(e);
//        Employee e2 = m.selectDetails(10);
//        System.out.println(e2.toString());
//        e2.setBranchNumber(2);
//        e2.setJobPos(Profession.CASHIER);
//        m.updateEmployee(e2);
//        System.out.println("After change:");
//        System.out.println(e2.toString());
//        m.deleteEmployee(e2.getEmployeeNumber());
//        //System.out.print("DONE");
//    }
}
