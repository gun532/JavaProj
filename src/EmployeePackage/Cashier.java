package EmployeePackage;

public class Cashier extends Employee{
    public Cashier(String name, int id, String phone, int accountNum, int branchNumber)
    {
        super(name, id, phone, accountNum,branchNumber, Profession.CASHIER);
    }

//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
