package EmployeePackage;

import java.sql.*;

public class ManagerUtility {
    private final String myDriver = "org.gjt.mm.mysql.Driver";
    private final Connection myConn = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/test_db", "root", "12345");

    public void addEmployee(Employee emp)
    {
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

    public void deleteEmployee(int empNum)
    {
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
            String sql = "UPDATE employee set name = ?, phone = ?, accountNum = ?, BranchNum = ?, profession = ? where empNum = ?";
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
            String myDriver = "org.gjt.mm.mysql.Driver";
            Connection myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
            Class.forName(myDriver);
            String sql = "SELECT * from employee where empNum = ?";
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

    public ManagerUtility() throws SQLException {
        try{

        }
        catch (Exception e)
        {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
