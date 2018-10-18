package DTO;

import Entities.Employee.Employee;

import java.util.ArrayList;

public class EmployeeArrayDto extends DtoBase {
    private ArrayList<Employee> allEmployees;
    int branch;

    public EmployeeArrayDto(String func, ArrayList<Employee> allEmployees, int branch) {
        super(func);
        this.allEmployees = allEmployees;
        this.branch = branch;
    }

    public ArrayList<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(ArrayList<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

    public int getBranch() {
        return branch;
    }
}
