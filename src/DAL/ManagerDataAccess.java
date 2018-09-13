package DAL;

import BL.GlobalLogger;
import Entities.Branch;
import Entities.Employee.Cashier;
import Entities.Employee.Employee;
import Entities.Employee.Manager;
import Entities.Employee.Seller;
import Entities.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class ManagerDataAccess {
    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;
    private GlobalLogger log = new GlobalLogger("logs.log");

    public ManagerDataAccess() throws IllegalStateException {
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
            log.logger.info("adding employee was successful");
            return true;
//            myConn.close();

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
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
            log.logger.severe("deleting client with id: " + clientID + " was successful");
            return true;
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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
            log.logger.severe("deleting employee with employee number: " + empNum + " was successful");

            return true;
//            myConn.close();

        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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
            log.logger.info("employee was updated successfully");

            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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

            }
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        log.logger.info("all employees was selected");

        return employeeArrayList;
    }

//    public int getTotalAmountInBranch(int branchNumber) {
//        try {
//            int total = 0;
//            Class.forName(myDriver);
//            createViewForTotalPurchasesForBranch(branchNumber);
//            String sql = "SELECT sum(total) as totalPurchases from purchasesbybranch";
//            PreparedStatement statement = myConn.prepareStatement(sql);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                total = rs.getInt("totalPurchases");
//            }
//            log.logger.info("total amount was calculated");
//            return total;
//        } catch (ClassNotFoundException e) {
//            log.logger.severe(e.getMessage());
//            e.printStackTrace();
//        } catch (SQLException e) {
//            log.logger.severe(e.getMessage());
//            e.printStackTrace();
//        }
//        return 0;
//    }


//    private void createViewForTotalPurchasesForBranch(int branchNumber) {
//        try {
//            Class.forName(myDriver);
//
//            String sql = "create view PurchasesByBranch  as (select distinct clients.fullName, total, clients.ClientType from clients join shopping_history join cart_details on clients.clientNumber = shopping_history.clientNumber and shopping_history.cartID = cart_details.cartID where branch_Number = ?)";
//            PreparedStatement statement = myConn.prepareStatement(sql);
//            statement.setInt(1, branchNumber);
//            statement.executeUpdate();
//            log.logger.info("view in the db was created");
//
//        } catch (ClassNotFoundException e) {
//            log.logger.severe(e.getMessage());
//            e.printStackTrace();
//        } catch (SQLException e) {
//            log.logger.severe(e.getMessage());
//            System.out.println("Table already exists!");
//        }
//    }
//
//    public ResultSetMetaData getResultSetMetaData(ResultSet rs) {
//        try {
//            return rs.getMetaData();
//        } catch (SQLException e) {
//            log.logger.severe(e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public ResultSet totalClientPurchasesInBranch(int branchNumber) {
//        try {
//            // @TODO: 06/09/2018 figure out how to keep the connection working if the view exists
//            Class.forName(myDriver);
//            createViewForTotalPurchasesForBranch(branchNumber);
//
//            String sql = "select fullName, sum(total) as totalBuying, clientType from PurchasesByBranch group by fullName";
//            PreparedStatement statement = myConn.prepareStatement(sql);
//            ResultSet rs = statement.executeQuery();
////            while (rs.next()) {
////            }
////                c.setClientCode(rs.getInt("clientNumber"));
////                c.setId(rs.getInt("clientID"));
////                c.setFullName(rs.getString("fullName"));
////                c.setPhoneNumber(rs.getString("phone"));
////                c.setType(ClientType.valueOf(rs.getString("ClientType")));
////                c.setDiscountRate(rs.getInt("discountRate"));
//
//            return rs;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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
            log.logger.info("Client was updated");
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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
            log.logger.info("number of employees in branch " + branch + " was returned");
            return numOfEmployees;
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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
                log.logger.info("Branch details were returned");
                return branch;
            }
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
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
            log.logger.info("phone number in branch " + branchNumber + "was updated");
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean addNewProduct(String name, double price) {
        try {
            Class.forName(myDriver);
            String sql = "INSERT INTO product (name, price) values (?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setString(1, name);
            statement.setDouble(2, price);

            statement.executeUpdate();
            log.logger.info("adding product was successful");
            return true;
//            myConn.close();

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int addProductAmountToInventory(int inventoryCode, int productAmount, String productName) {
        try {
            int productCode = selectProductCodeByName(productName);
            Class.forName(myDriver);
            String sql = "INSERT INTO inventory (inventory_code, productCode, numberOfItems) values (?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setInt(1, inventoryCode);
            statement.setInt(2, productCode);
            statement.setInt(3, productAmount);

            statement.executeUpdate();
            log.logger.info("adding product to inventory was successful");
            return productCode;
//            myConn.close();

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int selectProductCodeByName(String productName) {
        try {
            Class.forName(myDriver);
            String sql = "SELECT productCode from product where name = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, productName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int productCode = rs.getInt("productCode");
                log.logger.info("Branch details were returned");
                return productCode;
            }
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateProduct(String name, double price, int productCode) {
        try {
            Class.forName(myDriver);
            String sql = "UPDATE product set name = ?, price = ? where productCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, productCode);
            statement.executeUpdate();
            log.logger.info("product with code" + productCode + "was updated");
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public boolean updateProductAmountInInventory(int amount, int inventoryCode, int productCode) {
        try {
            Class.forName(myDriver);
            String sql = "UPDATE inventory set numberOfItems = ? where productCode = ? and inventory_code = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, amount);
            statement.setInt(2, productCode);
            statement.setInt(3, inventoryCode);
            statement.executeUpdate();
            log.logger.info("product amount with code" + productCode + "was updated");
            return true;
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeProductFromInventory(int productCode, int inventoryCode) {
        try {
            Class.forName(myDriver);
            String sql = "DELETE from inventory where productCode = ? and inventory_code = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, productCode);
            statement.setInt(2, inventoryCode);
            statement.execute();
            //myConn.close();
            log.logger.severe("deleting product with code: " + productCode + " from" +
                    "branch number: " + inventoryCode + " was successful");
            return true;
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<Product> selectAllProducts() {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from product";
            PreparedStatement statement = myConn.prepareStatement(sql);
            //statement.setInt(1, empNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Product p = new Product(rs.getInt("productCode"),rs.getString("name"),
                        rs.getDouble("price"));
                productArrayList.add(p);
                }
            //            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        log.logger.info("all employees was selected");

        return productArrayList;
    }

    public boolean createReport_totalPurchasesInBranchByDate(int branchNumber, java.util.Date date) {
        try {

            Class.forName(myDriver);
            createViewForPurchasesDetailsInBranch(branchNumber);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String sql = "select fullName, ClientType, date, name, price, numOfItems from PurchasesDetails where date = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setDate(1, sqlDate);
            ResultSet rs = statement.executeQuery();

            //creating word document
            String fileName = ".idea/dataSources/wordReportsFiles/" +"dateReport" + sqlDate + ".docx";
            XWPFDocument docx = new XWPFDocument();
            XWPFParagraph para1 = docx.createParagraph();
            para1.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = para1.createRun();
            run.setBold(true);
            run.setFontSize(30);
            run.setText("Report for date:" + sqlDate);
            XWPFTable tab = docx.createTable();
            XWPFTableRow row = tab.getRow(0);
            row.getCell(0).setText("fullName");
            row.addNewTableCell().setText("ClientType");
            row.addNewTableCell().setText("Date Of Purchase");
            row.addNewTableCell().setText("Product Name");
            row.addNewTableCell().setText("Product Price");
            row.addNewTableCell().setText("number of Items");
            while((rs!=null) && (rs.next()))
            {
                row = tab.createRow();
                row.getCell(0).setText(rs.getString("fullName"));
                row.getCell(1).setText(rs.getString("ClientType"));
                row.getCell(2).setText(rs.getString("date"));
                row.getCell(3).setText(rs.getString("name"));
                row.getCell(4).setText(rs.getString("price"));
                row.getCell(5).setText(rs.getString("numOfItems"));
            }
            docx.createParagraph().createRun().addBreak();
            XWPFParagraph para2 = docx.createParagraph();
            XWPFRun run2 = para2.createRun();
            run2.setText("Generated Automatically from the store system");
            docx.write(new FileOutputStream(fileName));
            //docx.write(new FileOutputStream(fileName));


            log.logger.severe("Word document was created!");
            System.out.println("Word document was created!");
            return true;

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }  catch (IOException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void createViewForPurchasesDetailsInBranch(int branchNumber) {
        try {
            Class.forName(myDriver);

            String sql = "create view PurchasesDetails as (select distinct clients.fullName, clients.ClientType, date, product_code, product.name, product.price ,numOfItems, total from clients join product join shoppingcart join shopping_history join cart_details on clients.clientNumber = shopping_history.clientNumber and shopping_history.cartID = cart_details.cartID \n" +
                    "and shoppingcart.cartCode = cart_details.cartID and product.productCode = shoppingcart.product_code where branch_Number = ?)";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branchNumber);
            statement.executeUpdate();
            log.logger.info("view in the db was created");

        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            System.out.println("Table already exists!");
        }
    }

//    public ResultSetMetaData getResultSetMetaData(ResultSet rs) {
//        try {
//            return rs.getMetaData();
//        } catch (SQLException e) {
//            log.logger.severe(e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }



    public void closeConnection() {
        try {
            myConn.close();
            log.logger.info("connection to db was terminated");

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }
}

