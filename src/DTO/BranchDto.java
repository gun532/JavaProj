package DTO;

public class BranchDto extends DtoBase {
    private int branchNumber;
    private String location;
    private int numberOfEmployees;
    private String phone;

    public BranchDto(String func, int branchNumber, String location, int numberOfEmployees, String phone) {
        super(func);
        this.branchNumber = branchNumber;
        this.location = location;
        this.numberOfEmployees = numberOfEmployees;
        this.phone = phone;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
