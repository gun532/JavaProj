package DAL;

import Entities.Branch;
import Entities.Clients.Client;
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

    public boolean addEmployee(String name, String pass, int id, String phone, int accountNum, int branchNumber, String profession) {
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
            return true;
//            myConn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteClient(int clientID) {
        try {
            Class.forName(myDriver);
            String sql = "DELETE from clients where clientID = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, clientID);
            statement.execute();
            //myConn.close();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(int empNum) {
        try {
            Class.forName(myDriver);
            String sql = "DELETE from employee where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, empNum);
            statement.execute();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(String name, int id, String phone, int accountNumber,
                                  int branch, String profession, String pass, int employeeCode) {
        try {
            Class.forName(myDriver);
            String sql = "UPDATE employee set fullName = ?, ID = ?, phoneNum = ?, accountNumber = ?, branch = ?, profession = ?, password = ? where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.setString(3, phone);
            statement.setInt(4, accountNumber);
            statement.setInt(5, branch);
            statement.setString(6, profession);
            statement.setString(7, pass);
            statement.setInt(8, employeeCode);
            statement.executeUpdate();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
//            myConn.close();
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

    public boolean updateClient(int clientID, String name, String phone, String type, int clientCode) {
        try {
            Class.forName(myDriver);
            String sql = "UPDATE clients set clientID = ? , fullName = ? ,phone = ?,ClientType = ? where clientNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, clientID);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.setString(4, type);
            statement.setInt(5, clientCode);

            statement.executeUpdate();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private int selectNumOfEmployeesInBranch(int branch) {
        int numOfEmployees = 0;
        try {
            Class.forName(myDriver);
            String sql = "SELECT numOfEmployees from branch where branchNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branch);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                numOfEmployees = rs.getInt("numOfEmployees");
            }
            return numOfEmployees;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numOfEmployees;
    }

    public boolean increaseEmployeeInBranch(int branchNumber) {
        try {
            Class.forName(myDriver);
            int numOfEmployees = selectNumOfEmployeesInBranch(branchNumber) + 1;
            String sql = "UPDATE branch set numOfEmployees = ? where branchNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, numOfEmployees);
            statement.setInt(2, branchNumber);
            statement.executeUpdate();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean decreaseEmployeeInBranch(int branchNumber) {
        try {
            Class.forName(myDriver);
            int numOfEmployees = selectNumOfEmployeesInBranch(branchNumber) - 1;
            String sql = "UPDATE branch set numOfEmployees = ? where branchNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, numOfEmployees);
            statement.setInt(2, branchNumber);
            statement.executeUpdate();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Branch selectBranchDetails(int branchNumber) {
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from branch where branchNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branchNumber);
            ResultSet rs = statement.executeQuery();
            Branch branch;
            while (rs.next()) {
                branch = new Branch(rs.getString("location"), rs.getInt("numOfEmployees"),
                        rs.getString("phone"), rs.getInt("branchNumber"));
                return branch;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateBranchPhoneNumber(String phone, int branchNumber) {
        try {
            Class.forName(myDriver);
            String sql = "UPDATE branch set phone = ? where branchNumber = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, phone);
            statement.setInt(2, branchNumber);
            statement.executeUpdate();
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void closeConnection() {
        try {
            myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

