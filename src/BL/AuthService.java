package BL;

import Entities.Employee.Employee;
import DAL.EmployeeDataAccess;

public class AuthService {

    private LoginUtility loginUtility = new LoginUtility();
    private static AuthService instance;
    private Employee currentEmployee;
    private EmployeeDataAccess employeeDataAccess = new EmployeeDataAccess();
    private AuthService()  {}

    public static AuthService getInstance()  {
        if(instance == null){
            instance = new AuthService();
        }
        return instance;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public boolean login(int id, String password) {
        String hasedPass = loginUtility.getEncryptedPass(password);


        //String hasedPass = String.valueOf(hash);
        String DBPass = employeeDataAccess.getPass(id);
        if (hasedPass == null) {
            //TODO: write to logger
            return false;
        }
        if (hasedPass.equals(DBPass)) {
            currentEmployee = employeeDataAccess.selectEmpDetailsById(id);
            return true;
        }
        return false;
    }
}
