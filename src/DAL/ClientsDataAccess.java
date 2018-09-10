package DAL;

import Entities.Clients.*;
import Entities.Employee.Employee;
import Entities.Employee.Profession;
import Entities.Product;
import Entities.ShoppingCart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class ClientsDataAccess {

    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn;

    public ClientsDataAccess() {

        try {
            myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
        } catch (Exception e) {
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
                switch (type)
                {
                    case "NEWCLIENT":
                        c = new NewClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone") ,rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                    case "RETURNCLIENT":
                        c = new ReturnClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone") ,rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                    case "VIPCLIENT":
                        c = new VipClient(rs.getInt("clientID"), rs.getString("fullName"), rs.getString("phone") ,rs.getInt("clientNumber"));
                        clientArrayList.add(c);
                        break;
                }

            }

//            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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
            statement.setInt(5, 0);

            statement.executeUpdate();
            return  true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void createNewOrder(ShoppingCart shoppingCart, int clientNum, double total) {
        try {
            Class.forName(myDriver);

            insertShoppingCart(shoppingCart);
            insertCartDetails(shoppingCart, total);
            insertToShoppingHistory(clientNum, shoppingCart.getCartID());

//            myConn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void insertToShoppingHistory(int clientNum, int cartID) {
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


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertShoppingCart(ShoppingCart shoppingCart) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertCartDetails(ShoppingCart shoppingCart, double total) {
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

        } catch (SQLException e) {
            e.printStackTrace();
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
                switch (type)
                {
                    case "NEWCLIENT":
                        c = new NewClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone") ,rs.getInt("clientNumber"));
                        return c;
                    case "RETURNCLIENT":
                        c = new ReturnClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone") ,rs.getInt("clientNumber"));
                        return c;
                    case "VIPCLIENT":
                        c = new VipClient(rs.getInt("clientID"), rs.getString("fullName"),
                                rs.getString("phone") ,rs.getInt("clientNumber"));
                        return c;
                }
//                c.setClientCode(rs.getInt("clientNumber"));
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
        return null;
    }
}
