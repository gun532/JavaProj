package DTO;

import Entities.Employee.Employee;

import java.util.ArrayList;

public class EmployeeArrayDto extends DtoBase {
    private ArrayList<Employee> allEmployees;

    public EmployeeArrayDto(String func, ArrayList<Employee> allEmployees) {
        super(func);
        this.allEmployees = allEmployees;
    }

    public ArrayList<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(ArrayList<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }
}
