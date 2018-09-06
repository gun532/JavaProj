package Entities.Employee;

public class Seller extends Employee {
    public Seller(int employeeNumber, String name, int id, String phone, int accountNum, int branchNumber) {
        super(employeeNumber, name, id, phone, accountNum,branchNumber, Profession.SELLER);
    }

//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
