package DAL;

import BL.GlobalLogger;
import Entities.Clients.*;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import Entities.Product;
import Entities.ShoppingCart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class ClientsDataAccess {

    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;
    private GlobalLogger log = new GlobalLogger("logs.log");

    public ClientsDataAccess() {

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

    public ArrayList<Client> selectAllClients() {
        ArrayList<Client> clientArrayList = new ArrayList<>();
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from clients";
            PreparedStatement statement = myConn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("ClientType");
                Client c;
                switch (type) {
                    case "NEWCLIENT":
                        c = new NewClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone"), rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                    case "RETURNCLIENT":
                        c = new ReturnClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone"), rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                    case "VIPCLIENT":
                        c = new VipClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone"), rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                }

            }
            log.logger.info("All Clients was selected");
//            myConn.close();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            //e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            // e.printStackTrace();
        }
        return clientArrayList;
    }


    public boolean addNewClient(int ID, String fullName, String phoneNum, String clientType) {
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            Class.forName(myDriver);
            String sql = "INSERT INTO clients (clientID, fullName, phone, ClientType, discountRate) values (?,?,?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setInt(1, ID);
            statement.setString(2, fullName);
            statement.setString(3, phoneNum);
            statement.setString(4, clientType);
            switch (clientType) {
                case "NEWCLIENT":
                    statement.setInt(5, 0);
                    break;
                case "RETURNCLIENT":
                    statement.setInt(5, 30);
                    break;
                case "VIPCLIENT":
                    statement.setInt(5, 50);
                    break;
            }

            statement.executeUpdate();
            log.logger.info("BL.Client with id " + ID + " was added");
            return true;

        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            //e.printStackTrace();
        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            // e.printStackTrace();
        }
        return false;
    }


    public void createNewOrder(ShoppingCart shoppingCart, int clientNum, double total) {
        try {
            Class.forName(myDriver);

            insertShoppingCart(shoppingCart);
            insertCartDetails(shoppingCart, total);
            insertToShoppingHistory(clientNum, shoppingCart.getCartID());

            log.logger.info("new order for client " + clientNum + "was successfully committed");

//            myConn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.logger.severe(e.getMessage());
            //e.printStackTrace();
        }
    }


    private void insertToShoppingHistory(int clientNum, int cartID) {
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            String sql = "INSERT INTO shopping_history (clientNumber, cartID) values (?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setInt(1, clientNum);
            statement.setInt(2, cartID);

            statement.executeUpdate();

            log.logger.info("shopping history for " + clientNum + " with cart number: " +cartID+ " succeed");


        } catch (SQLException e) {
            //e.printStackTrace();
            log.logger.severe(e.getMessage());
        }
    }

    private void insertShoppingCart(ShoppingCart shoppingCart) {

        //change it later to something more secure

        //ResultSet rs;
        //ResultSetMetaData metaData = rs.getMetaData();
        //int cols = metaData.getColumnCount();
        try {
            String sql = "INSERT INTO shoppingcart (cartCode, product_code, numOfItems) values (?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);
            for (Map.Entry<Integer, Product> entry : shoppingCart.getCart().entrySet()) {
                statement.setInt(1, shoppingCart.getCartID());
                statement.setInt(2, entry.getKey());
                statement.setInt(3, entry.getValue().getAmount());
                statement.executeUpdate();
            }
            log.logger.info("shopping cart was successfully added");
        } catch (SQLException e) {
            //e.printStackTrace();
            log.logger.severe(e.getMessage());
        }
    }


    private void insertCartDetails(ShoppingCart shoppingCart, double total) {
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            java.sql.Date sqlDate = new java.sql.Date(shoppingCart.getCartDate().getTime());
            String sql = "INSERT INTO cart_details (cartID, total, branch_Number, employeeNum, date) values (?,?,?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setInt(1, shoppingCart.getCartID());
            statement.setDouble(2, total);
            statement.setInt(3, shoppingCart.getBranchCode());
            statement.setInt(4, shoppingCart.getEmployeeCode());
            statement.setDate(5, sqlDate);

            statement.executeUpdate();

            log.logger.info("cart details successfully added");

        } catch (SQLException e) {
            log.logger.severe(e.getMessage());
            //e.printStackTrace();
        }
    }

    public Client selectClientByID(int id) {
        try {
            Class.forName(myDriver);
            String sql = "SELECT * from clients where clientID = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("ClientType");
                Client c;
                switch (type) {
                    case "NEWCLIENT":
                        c = new NewClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone"), rs.getInt("clientNumber"));
                        log.logger.info("selecting client with id: " + id + "succeeded");
                        return c;
                    case "RETURNCLIENT":
                        c = new ReturnClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone"), rs.getInt("clientNumber"));
                        log.logger.info("selecting client with id: " + id + "succeeded");
                        return c;
                    case "VIPCLIENT":
                        c = new VipClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone"), rs.getInt("clientNumber"));
                        log.logger.info("selecting client with id: " + id + "succeeded");
                        return c;
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
}
