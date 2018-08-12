package EmployeePackage;

import java.sql.*;
import java.util.Scanner;

public class Manager extends Employee {
    public Manager(String name, int id, String phone, int accountNum, int branchNumber) {
        super(name, id, phone, accountNum);
        setBranchNumber(branchNumber);
        setJobPos(Profession.MANAGER);
    }

    public void addEmploee() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Employee Type:");
        String emp = scan.next();
        Employee m1 = new Employee() {
        };
        switch (emp) {
            case "Manager":
                m1 = new Manager("Guy", 1243, "050-112211", 234532, this.getBranchNumber());
                break;
            case "Cashier":
                m1 = new Cashier("Roy", 342436, "050-253265", 4646, this.getBranchNumber());
                break;
            case "Seller":
                //m1 = new Seller("Ben",32,"050-2524542",424,this.getBranchNumber());
                m1 = new Seller("Joan", 323, "052-27904542", 423, this.getBranchNumber());

                break;
        }
        try {

            //change it later to something more secure

            //ResultSet rs;
            //ResultSetMetaData metaData = rs.getMetaData();
            //int cols = metaData.getColumnCount();
            String myDriver = "org.gjt.mm.mysql.Driver";

            Connection myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
            Class.forName(myDriver);

            String sql = "INSERT INTO employee (name,id,phone,accountNum,branchNum,profession) values (?,?,?,?,?,?)";
            PreparedStatement statement = myConn.prepareStatement(sql);

            statement.setString(1, m1.getName());
            statement.setInt(2, m1.getId());
            statement.setString(3, m1.getPhone());
            statement.setInt(4, m1.getAccountNum());
            statement.setInt(5, m1.getBranchNumber());
            statement.setString(6, m1.getJobPos().name());

            statement.executeUpdate();

            myConn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void deleteEmployee() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Employee id to update:");
        int empID = scan.nextInt();
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            Connection myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
            Class.forName(myDriver);
            String sql = "DELETE from employee where empNum = ?";
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, empID);
            statement.execute();
            myConn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateEpmloyee(Employee e1) {
        //employee to update - e1
        //when button is clicked create an object with type employee from textboxes
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            Connection myConn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/test_db", "root", "12345");
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

    @Override
    public String toString() {
        String str = super.toString();
        str = str + ", Employee's position: " + this.getJobPos() + ", Employee's branch number: " + this.getBranchNumber();
        return str;
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

    public static void main(String[] args) {
        Manager m = new Manager("Guy", 1243, "050-112211", 234532, 2);
        //Employee e2 = new Seller("Joan",323,"052-44444444",423,m.getBranchNumber());
        //m.addEmploee();
        Employee e2 = m.selectDetails(9);
        e2.setBranchNumber(2);
        e2.setJobPos(Profession.CASHIER);
        m.updateEpmloyee(e2);
        //m.deleteEmployee();
        //me trying to see if I can push from intellij
        System.out.print("DONE");
    }
}
