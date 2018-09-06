package DAL;

import Entities.Employee.*;

import java.sql.*;

public class EmployeeDataAccess {


    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;

    public EmployeeDataAccess() {
        try {
            myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");

        } catch (Exception e) {
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
                switch (type)
                {
                    case "SELLER":
                        e = new Seller(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                    case "CASHIER":
                        e = new Cashier(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                    case "MANAGER":
                        e= new Manager(rs.getInt("employeeCode"), rs.getString("fullName"), id,
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                }
//                e1.setEmployeeNumber(rs.getInt("employeeCode"));
//                e1.setName(rs.getString("fullName"));
//                e1.setId(id);
//                e1.setPhone(rs.getString("phoneNum"));
//                e1.setAccountNum(rs.getInt("accountNumber"));
//                e1.setBranchNumber(rs.getInt("branch"));
//                e1.setJobPos(Profession.valueOf(rs.getString("profession")));
            }

            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
                switch (type)
                {
                    case "SELLER":
                        e = new Seller(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                    case "CASHIER":
                        e = new Cashier(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                    case "MANAGER":
                        e= new Manager(empNum, rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        return e;
                }
//                e.setEmployeeNumber(empNum);
//                e.setName(rs.getString("fullName"));
//                e.setId(rs.getInt("ID"));
//                e.setPhone(rs.getString("phoneNum"));
//                e.setAccountNum(rs.getInt("accountNumber"));
//                e.setBranchNumber(rs.getInt("branch"));
//                e.setJobPos(Profession.valueOf(rs.getString("profession")));
            }

            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
            if(rs.next()) code = rs.getString("password");
            return code;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }





}
