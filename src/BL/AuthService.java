package BL;

import Entities.Employee.Employee;
import DAL.EmployeeDataAccess;

public class AuthService {

    private static AuthService instance;
    private Employee currentEmployee;

    private AuthService() {
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
}
