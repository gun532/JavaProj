package EmployeePackage;

import java.sql.*;


public class Manager extends Employee {
    private ManagerUtility managerUtility;
    {
        try {
            managerUtility = new ManagerUtility();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Manager(String name, int id, String phone, int accountNum, int branchNumber) {
        super(name, id, phone, accountNum,branchNumber, Profession.MANAGER);
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

//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " + this.getJobPos() + ", Employee's branch number: " + this.getBranchNumber();
//        return str;
//    }


    Employee selectDetails(int empNum) {
        return managerUtility.selectDetails(empNum);
    }

    public static void main(String[] args) {
        Manager m = new Manager("Guy", 1243, "050-112211", 234532, 2);
        //Employee e = new Seller("Joseph Stalin",44354366,"052-423576822",9984848,m.getBranchNumber());
        //m.addEmployee(e);
        Employee e2 = m.selectDetails(10);
        System.out.println(e2.toString());
        e2.setBranchNumber(2);
        e2.setJobPos(Profession.CASHIER);
        m.updateEmployee(e2);
        System.out.println("After change:");
        System.out.println(e2.toString());
        m.deleteEmployee(e2.getEmployeeNumber());
        //System.out.print("DONE");
    }
}
