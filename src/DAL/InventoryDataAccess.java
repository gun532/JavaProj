package DAL;

import BL.GlobalLogger;
import Entities.Inventory;
import Entities.Product;

import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryDataAccess {

    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;
    private GlobalLogger log = new GlobalLogger("logs.log");

    public InventoryDataAccess() {
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

    public void updateInventory(Inventory inventory) {

        try {
            Class.forName(myDriver);
            String sql = "UPDATE inventory set  numberOfItems = ? where productCode = ? and inventory_code = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(3, inventory.getInventoryNumber());
            for (Map.Entry<Integer, Product> entry : inventory.getMyInventory().entrySet()) {
                statement.setInt(1, entry.getValue().getAmount());
                statement.setInt(2, entry.getKey());
                statement.executeUpdate();
            }
            //myConn.close();
            log.logger.info("inventory update was successful" );

        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    //    public void removeFromInventory(Product product, int branchNumber) throws Exception {
//
//        try {
//            Class.forName(myDriver);
//            String sql = "DELETE from inventory where inventoryCode = ? and productCode = ?";
//            PreparedStatement statement = myConn.prepareStatement(sql);
//            statement.setInt(1, product.getProductCode());
//            statement.setInt(2, branchNumber);
//            statement.execute();
//            myConn.close();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void addToInventory(Product product, int branchNumber) throws Exception {
//        int code;
//        Class.forName(myDriver);
//
//        String sql = "SELECT productCode from inventory where productCode=?";
//        PreparedStatement statement = myConn.prepareStatement(sql);
//
//        statement.setInt(1, product.getProductCode());
//        ResultSet rs = statement.executeQuery();
//        if(rs.next()) {
//            code = rs.getInt("productCode");
//        }
//        else
//        {
//            throw new Exception();
//        }
//        //Check if inventory already has this product.
//        if (code == 0) {
//            throw new Exception("The product is already in the store's inventory.");
//            //possible to ask if admin wants to update
//        }
//        //else add a new product to inventory
//
//        sql = "INSERT INTO inventory (inventory_code,productCode,numberOfItems) values (?,?,?)";
//        statement = myConn.prepareStatement(sql);
//
//        statement.setInt(1, branchNumber);
//        statement.setInt(2, product.getProductCode());
//        statement.setInt(3, product.getAmount());
//
//        statement.executeUpdate();
//
//        myConn.close();
//    }


    public Inventory selectFromInventory(int branchNum) {
        Inventory inventory = new Inventory();
        try {
            inventory.setInventoryNumber(branchNum);
            Class.forName(myDriver);
            String sql = "SELECT inventory_code, p.productCode, numberOfItems, name, price from inventory  join product p on inventory.productCode = p.productCode where inventory_code = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, branchNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setAmount(rs.getInt("numberOfItems"));
                p.setProductCode(rs.getInt("productCode"));
                inventory.addToInventory(p);
            }
            log.logger.info("select from inventory in branch " +branchNum+ " was successful" );
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

        return inventory;
    }


}
