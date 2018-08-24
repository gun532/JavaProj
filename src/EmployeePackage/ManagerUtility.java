package EmployeePackage;

import ClientsPackage.*;

import java.sql.*;
import java.util.Map;

public class ManagerUtility {
    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/test_db", "root", "12345");

    public ManagerUtility() throws SQLException {
        try {

        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void addEmployee(Employee emp) {
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            Class.forName(myDriver);
            String sql = "INSERT INTO employee (name,id,phone,accountNum,branchNum,profession) values (?,?,?,?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setString(1, emp.getName());
            statement.setInt(2, emp.getId());
            statement.setString(3, emp.getPhone());
            statement.setInt(4, emp.getAccountNum());
            statement.setInt(5, emp.getBranchNumber());
            statement.setString(6, emp.getJobPos().name());

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

    void updateEmployee(Employee e1) {
        //employee to update - e1
        //when button is clicked create an object with type employee from textboxes
        try {
            Class.forName(myDriver);
            String sql = "UPDATE employee set name = ?, phone = ?, accountNum = ?, BranchNum = ?, profession = ? where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setString(1, e1.getName());
            statement.setString(2, e1.getPhone());
            statement.setInt(3, e1.getAccountNum());
            statement.setInt(4, e1.getBranchNumber());
            statement.setString(5, e1.getJobPos().name());
            statement.setInt(6, e1.getEmployeeNumber());
            statement.executeUpdate();
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Employee selectDetails(int empNum) {
        Employee e1 = new Employee() {
        };
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from employee where employeeCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, empNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                e1.setEmployeeNumber(empNum);
                e1.setName(rs.getString("name"));
                e1.setId(rs.getInt("ID"));
                e1.setPhone(rs.getString("phone"));
                e1.setAccountNum(rs.getInt("accountNum"));
                e1.setBranchNumber(rs.getInt("BranchNum"));
                e1.setJobPos(Profession.valueOf(rs.getString("profession")));
            }

            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return e1;
    }


    Inventory selectFromInventory(int branchNum) {
        Inventory inventory = new Inventory();
        Product p = new Product();
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from inventory join product p on inventory.productCode = p.productCode where inventoryCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branchNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setAmount(rs.getInt("numberOfItems"));
                this.addToInventory(p, branchNum);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }


    void updateInventory(Inventory inventory) {

        try {
            Class.forName(myDriver);
            String sql = "UPDATE inventory set productCode = ?, numberOfItems = ? where inventoryCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            for (Map.Entry<Integer, Product> entry : inventory.getMyInventory().entrySet()) {
                statement.setInt(1, entry.getKey());
                statement.setInt(2, entry.getValue().getAmount());

            }
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToInventory(Product product, int branchNumber) throws Exception {
        Class.forName(myDriver);

        String sql = "SELECT productCode from inventory where productCode=?";
        PreparedStatement statement = myConn.prepareStatement(sql);

        statement.setInt(1, product.getProductCode());
        ResultSet rs = statement.executeQuery();
        int code = rs.getInt("productCode");

        //Check if inventory already has this product.
        if (code == 0) {
            throw new Exception("The product is already in the store's inventory.");
            //possible to ask if admin wants to update
        }
        //else add a new product to inventory

        sql = "INSERT INTO inventory (inventoryCode,productCode,numberOfItems) values (?,?,?)";
        statement = myConn.prepareStatement(sql);

        statement.setInt(1, branchNumber);
        statement.setInt(2, product.getProductCode());
        statement.setInt(3, product.getAmount());

        statement.executeUpdate();

        myConn.close();
    }

    public void removeFromInventory(Product product, int branchNumber) throws Exception {

        try {
            Class.forName(myDriver);
            String sql = "DELETE from inventory where inventoryCode = ? and productCode = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, product.getProductCode());
            statement.setInt(2, branchNumber);
            statement.execute();
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //we will wrap this function with an on click changed event to make it run few times
    //product code and amount will be generated from a textbox. when the gui is ready we will take the
    //values from the textbox inside the function
    void newOrder(int branchName, int productCode, int amount) throws Exception {
        Inventory inventory = this.selectFromInventory(branchName);
        Product p = inventory.takeFromInventory(productCode, amount);
        ShoppingCart cart = new ShoppingCart();
        cart.addToCart(p);
        this .updateInventory(inventory);
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
