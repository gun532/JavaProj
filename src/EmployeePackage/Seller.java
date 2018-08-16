package EmployeePackage;

public class Seller extends Employee {
    public Seller(String name, int id, String phone, int accountNum, int branchNumber) {
        super(name, id, phone, accountNum,branchNumber, Profession.SELLER);
        setBranchNumber(branchNumber);
    }

//    @Override
//    public String toString() {
//        String str = super.toString();
//        str = str + ", Employee's position: " +this.getJobPos()+ ", Employee's branch number: " +this.getBranchNumber();
//        return str;
//    }
}
