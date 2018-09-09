package BL;

import DAL.EmployeeDataAccess;
import Entities.Employee.Employee;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class LoginUtility {
    private EmployeeDataAccess employeeDataAccess = new EmployeeDataAccess();
    private ArrayList<Employee> connectedEmployees = new ArrayList<>();


    public String getEncryptedPass(String pass) {

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO - create a log of function to delete from the array list

    public Employee login(int id, String password) {
        String hasedPass = getEncryptedPass(password);
        //String hasedPass = String.valueOf(hash);
        String DBPass = employeeDataAccess.getPass(id);
        if (hasedPass == null) {
            //TODO: write to logger
//            return false;
            return null;
        }
        if (hasedPass.equals(DBPass)) {
            Employee emp = employeeDataAccess.selectEmpDetailsById(id);
            //connectedEmployees.add(emp);
            //Employee emp = employeeDataAccess.selectEmpDetailsById(id);
            //AuthService.getInstance().setCurrentEmployee(emp);
            //return true;
            return emp;
        }
//        return false;
        return null;
    }

    public ArrayList<Employee> getConnectedEmployees() {
        return connectedEmployees;
    }
}
