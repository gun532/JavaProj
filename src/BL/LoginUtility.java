package BL;

import DAL.EmployeeDataAccess;
import Entities.Employee.Employee;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginUtility {
    private EmployeeDataAccess employeeDataAccess = new EmployeeDataAccess();
    //private ArrayList<Employee> connectedEmployees = new ArrayList<>();

    private GlobalLogger log = new GlobalLogger("logs.log");

    public LoginUtility() {
    }

    public String getEncryptedPass(String pass) {

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            log.logger.info("Password was encrypted");
            return sb.toString().toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            log.logger.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //TODO - create a log of function to delete from the array list

    public Employee login(int id, String password) {
        String hashedPass = getEncryptedPass(password);
        String DBPass = employeeDataAccess.getPass(id);
        if (hashedPass == null) {

            log.logger.warning("login for employee with ID: " + id + "wasn't successful");
            return null;
        }
        if (hashedPass.equals(DBPass)) {
            Employee emp = employeeDataAccess.selectEmpDetailsById(id);
            //connectedEmployees.add(emp);
            //Employee emp = employeeDataAccess.selectEmpDetailsById(id);
            //AuthService.getInstance().setCurrentEmployee(emp);
            //return true;
            log.logger.info("login for employee with ID: " + id + "was successful");
            return emp;
        }
        return null;
    }

//    public ArrayList<Employee> getConnectedEmployees() {
//        return connectedEmployees;
//    }
}
