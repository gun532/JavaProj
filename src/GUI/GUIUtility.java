package GUI;

import java.sql.*;

public class GUIUtility {
    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/test_db", "root", "12345");

    public GUIUtility() throws SQLException {
        try {

        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public String checkPass(int id) {
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
