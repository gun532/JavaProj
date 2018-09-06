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

    public void addEmployee(String name,String pass, int id, String phone,int accountNum, int branchNumber, String profession) {
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
                switch (type)
                {
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
                        e= new Manager(rs.getInt("employeeCode"), rs.getString("fullName"), rs.getInt("ID"),
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

}





//    //לפי הגישה הזו אנו שולפים את המלאי בפעם הראשונה כשעושים לוגין לעובד
//    void addInventoryToDB(Inventory inventory, Product p) throws Exception {
//        inventory.addToInventory(p);
//        updateInventory(inventory);
//    }


//
//    public Product takeFromInventory(int productCode, int amount, Inventory inventory) throws Exception {
//
//        Product refProduct = inventory.getMyInventory().get(productCode);
//        if (refProduct != null) {
//            if (refProduct.getAmount() - amount >= 0) {
//                refProduct.setAmount(refProduct.getAmount() - amount);
//                //this.totalItems -= amount;
//                //this.totalValue -= refProduct.getPrice()*amount;
//                inventory.setTotalItems(inventory.getTotalProducts() - amount);
//                inventory.setTotalValue(inventory.getTotalValue() - (refProduct.getPrice() * amount));
//                Product product = new Product(refProduct);
//                product.setAmount(amount);
//                return product;
//            } else
//                throw new Exception("There's not enough items in the inventory.");
//        } else
//            throw new Exception("The product is not in the inventory.");
//    }
//}

//    public void addToCart(Product product) throws Exception {
//        ShoppingCart newCart = new ShoppingCart();
//        if (newCart.getCart().containsValue(product)) {
//            newCart.getCart().get(product.getProductCode()).incAmount(product.getAmount());
//        } else {
//            newCart.getCart().put(product.hashCode(), product);
//        }
//
//        newCart.setTotalItems(newCart.getTotalItems() + product.getAmount());
//        newCart.setTotalItems(newCart.getTotalPrice()+ product.getPrice() * product.getAmount());
//    }
//
//    public void removeFromCart(Product product) throws Exception {
//        Product refProduct = this.cart.get(product.getProductCode());
//        if(refProduct != null){
//            if(refProduct.getAmount() - product.getAmount() >= 0) {
//                refProduct.decAmount(product.getAmount());
//                if (refProduct.getAmount() == 0)
//                    this.cart.remove(product.getProductCode());
//
//                this.totalItems -= product.getAmount();
//                this.totalPrice -= product.getPrice()*product.getAmount();
//            }
//            else{
//                throw new Exception("You are trying to remove more items than you have in cart.");
//            }
//        }
//        else{
//            throw new Exception("The product is not in the shopping cart.");
//        }
//    }
//}
