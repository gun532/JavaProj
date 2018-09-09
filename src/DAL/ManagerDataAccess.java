package DAL;

import Entities.Employee.*;

import java.sql.*;
import java.util.ArrayList;

public class ManagerDataAccess {
    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;

    private InventoryDataAccess inventoryDataAccess;

    public ManagerDataAccess() throws IllegalStateException {
        try {
            myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
            inventoryDataAccess = new InventoryDataAccess();

        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void addEmployee(String name, String pass, int id, String phone, int accountNum, int branchNumber, String profession) {
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            Class.forName(myDriver);
            String sql = "INSERT INTO employee (fullName,password,ID,phoneNum,accountNumber,branch,profession) values (?,?,?,?,?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, pass);
            statement.setInt(3, id);
            statement.setString(4, phone);
            statement.setInt(5, accountNum);
            statement.setInt(6, branchNumber);
            statement.setString(7, profession);

            statement.executeUpdate();

            myConn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int empNum) {
        try {
            Class.forName(myDriver);
            String sql = "DELETE from employee where empNum = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, empNum);
            statement.execute();
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        //employee to update - e1
        //when button is clicked create an object with type employee from textboxes
        try {
            Class.forName(myDriver);
            String sql = "UPDATE employee set name = ?, phone = ?, accountNum = ?, BranchNum = ?, profession = ? where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPhone());
            statement.setInt(3, employee.getAccountNum());
            statement.setInt(4, employee.getBranchNumber());
            statement.setString(5, employee.getJobPos().name());
            statement.setInt(6, employee.getEmployeeNumber());
            statement.executeUpdate();
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> selectAllEmployees() {
        ArrayList<Employee> employeeArrayList = new ArrayList<>();
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from employee";
            PreparedStatement statement = myConn.prepareStatement(sql);
            //statement.setInt(1, empNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("profession");
                Employee e;
                switch (type) {
                    case "SELLER":
                        e = new Seller(rs.getInt("employeeCode"), rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        employeeArrayList.add(e);
                        break;
                    case "CASHIER":
                        e = new Cashier(rs.getInt("employeeCode"), rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        employeeArrayList.add(e);
                        break;
                    case "MANAGER":
                        e = new Manager(rs.getInt("employeeCode"), rs.getString("fullName"), rs.getInt("ID"),
                                rs.getString("phoneNum"), rs.getInt("accountNumber"),
                                rs.getInt("branch"));
                        employeeArrayList.add(e);
                        break;
                }
//                c.setClientCode(rs.getInt("clientNumber"));
//                c.setId(rs.getInt("clientID"));
//                c.setFullName(rs.getString("fullName"));
//                c.setPhoneNumber(rs.getString("phone"));
//                c.setType(ClientType.valueOf(rs.getString("ClientType")));
//                c.setDiscountRate(rs.getInt("discountRate"));
            }
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeArrayList;
    }

    public int getTotalAmountInBranch(int branchNumber) {
        try {
            int total = 0;
            Class.forName(myDriver);
            createViewForTotalPurchasesForBranch(branchNumber);
            String sql = "SELECT sum(total) as totalPurchases from purchasesbybranch";
            PreparedStatement statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                total = rs.getInt("totalPurchases");
            }
            return total;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    private void createViewForTotalPurchasesForBranch(int branchNumber) {
        try {
            Class.forName(myDriver);

            String sql = "create view PurchasesByBranch  as (select distinct clients.fullName, total, clients.ClientType from clients join shopping_history join cart_details on clients.clientNumber = shopping_history.clientNumber and shopping_history.cartID = cart_details.cartID where branch_Number = ?)";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branchNumber);
            statement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Table already exists!");
        }
    }

    public ResultSetMetaData getResultSetMetaData(ResultSet rs) {
        try {
            return rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet totalClientPurchasesInBranch(int branchNumber) {
        try {
            // @TODO: 06/09/2018 figure out how to keep the connection working if the view exists
            Class.forName(myDriver);
            createViewForTotalPurchasesForBranch(branchNumber);

            String sql = "select fullName, sum(total) as totalBuying, clientType from PurchasesByBranch group by fullName";
            PreparedStatement statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//            }
//                c.setClientCode(rs.getInt("clientNumber"));
//                c.setId(rs.getInt("clientID"));
//                c.setFullName(rs.getString("fullName"));
//                c.setPhoneNumber(rs.getString("phone"));
//                c.setType(ClientType.valueOf(rs.getString("ClientType")));
//                c.setDiscountRate(rs.getInt("discountRate"));

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

