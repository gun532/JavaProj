package Entities.Employee;

public abstract class Employee {
    private String name;
    private int id;
    private int employeeNumber;
    private String phone;
    private int accountNum;
    private int branchNumber;
    private Profession jobPos;

    public Employee(){    }

    public Employee(String name, int id, String phone, int accountNum, int branchNumber, Profession profession)
    {
        //Random rand = new Random();
        this.name = name;
        this.id = id;
        //this.employeeNumber = counter++;
        this.phone = phone;
        this.accountNum = accountNum;
        this.branchNumber = branchNumber;
        jobPos = profession;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getPhone() {
        return phone;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public Profession getJobPos() {
        return jobPos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    public void setJobPos(Profession jobPos) {
        this.jobPos = jobPos;
    }

    @Override
    public String toString() {
        //System.out.println("This is the details:");
        return "Employee name: " + this.name + ", Employee number: " +this.employeeNumber+
                ", Employee ID:" + this.id + ", Employee phone:" + this.phone+ ", Employee Account Number:" +this.accountNum
                +", Employee Branch:" +this.branchNumber + ", Employee's Profession: " + this.jobPos;
    }





    public static void main(String [] args)
    {
        //Manager m1 = new Manager("Guy",1243,"050-112211",234532,2);
//        Cashier m1 = new Cashier("Roy",342436,"050-253265",4646,2);
//        try
//        {
//            //change it later to something more secure
//            String myDriver = "org.gjt.mm.mysql.Driver";
//            Connection myConn = DriverManager.getConnection
//                    ("jdbc:mysql://localhost:3306/test_db","root","12345");
//            Class.forName(myDriver);
//
//            String sql = "INSERT INTO employee (name,id,phone,accountNum,branchNum,profession) values (?,?,?,?,?,?)";
//            PreparedStatement statement = myConn.prepareStatement(sql);
//            statement.setString(1,m1.getName());
//            statement.setInt(2,m1.getId());
//            statement.setString(3,m1.getPhone());
//            statement.setInt(4,m1.getAccountNum());
//            statement.setInt(5,m1.getBranchNumber());
//            statement.setString(6,m1.getJobPos().name());
//
//            statement.executeUpdate();
//
//            myConn.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
