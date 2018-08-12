package EmployeePackage;

public class Branch {
    private int branchNumber;
    private String location;
    private int numberOfEmployees;
    private String phone;

    public int getBranchNumber() {
        return branchNumber;
    }

    public String getLocation() {
        return location;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public String getPhone() {
        return phone;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
