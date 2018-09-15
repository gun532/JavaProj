package DAL;

import BL.GlobalLogger;
import Entities.Employee.*;

import java.sql.*;
import java.util.logging.Level;

public class EmployeeDataAccess {


    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;
    private GlobalLogger log = new GlobalLogger("logs.log");

    public EmployeeDataAccess() {
        try {
            myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
            log.logger.setLevel(Level.INFO);
            log.logger.setLevel(Level.WARNING);
            log.logger.setLevel(Level.SEVERE);
        } catch (Exception e) {
            log.logger.severe(e.getMessage());

            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }


    public Employee selectEmpDetailsById(int id) {

        try {
            Class.forName(myDriver);
            String sql = "SELECT * from employee where ID = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("profession");
                Employee e;
                switch (type) {
                    case "SELLER":
                        e = new Seller(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with id: " + id + "succeeded");

                        return e;
                    case "CASHIER":
                        e = new Cashier(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with id: " + id + "succeeded");

                        return e;
                    case "MANAGER":
                        e = new Manager(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with id: " + id + "succeeded");

                        return e;
                }
            }

//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public Employee selectEmpDetailsByEmployeeNum(int empNum) {
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from employee where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, empNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("profession");
                Employee e;
                switch (type) {
                    case "SELLER":
                        e = new Seller(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with number: " + empNum + "succeeded");

                        return e;
                    case "CASHIER":
                        e = new Cashier(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with number: " + empNum + "succeeded");

                        return e;
                    case "MANAGER":
                        e = new Manager(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        log.logger.info("selecting employee with number: " + empNum + "succeeded");
                        return e;
                }

            }

//            myConn.close();
        }
        catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public String getPass(int id) {
        String code = "";
        try {
            Class.forName(myDriver);

            String sql = "SELECT password from employee where employee.ID= ?";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) code = rs.getString("password");
            log.logger.info("Password was successfully returned");
            return code;

        }
        catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


}
