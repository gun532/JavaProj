package DTO;

import Entities.Employee.Profession;

public class EmployeeDto extends DtoBase{
    private String name;
    private int id;
    private int employeeNumber;
    private String phone;
    private int accountNum;
    private int branchNumber;
    private Profession jobPos;
    private String pass;

    public EmployeeDto(String func, String name, int id, int employeeNumber, String phone,
                       int accountNum, int branchNumber, Profession jobPos, String pass) {
        super(func);
        this.name = name;
        this.id = id;
        this.employeeNumber = employeeNumber;
        this.phone = phone;
        this.accountNum = accountNum;
        this.branchNumber = branchNumber;
        this.jobPos = jobPos;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public Profession getJobPos() {
        return jobPos;
    }

    public void setJobPos(Profession jobPos) {
        this.jobPos = jobPos;
    }
}
