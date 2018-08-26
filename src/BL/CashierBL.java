package BL;

import DAL.EmployeeDataAccess;
import DAL.InventoryDataAccess;
import Entities.Employee.Employee;

public class CashierBL {
    private EmployeeDataAccess employeeDataAccess;
    private InventoryDataAccess inventoryDataAccess;

    public CashierBL(EmployeeDataAccess employeeDataAccess, InventoryDataAccess inventoryDataAccess) {
        this.employeeDataAccess = employeeDataAccess;
        this.inventoryDataAccess = inventoryDataAccess;
    }

    public Employee selectEmpDetailsById(int id) {
        return employeeDataAccess.selectEmpDetailsById(id);
    }

    public Employee selectEmpDetailsByEmployeeNum(int id) {
        return employeeDataAccess.selectEmpDetailsByEmployeeNum(id);
    }
}
