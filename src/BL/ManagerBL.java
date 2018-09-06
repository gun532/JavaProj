package BL;

import DAL.ManagerDataAccess;
import Entities.Employee.Employee;
import Entities.Inventory;

import java.util.ArrayList;

public class ManagerBL {
    private CashierBL cashierBL;
    private ManagerDataAccess managerDataAccess;
    private LoginUtility loginUtility;

    public ManagerBL(CashierBL cashierBL) {
        this.cashierBL = cashierBL;
        this.loginUtility = new LoginUtility();
    }

    public ManagerBL(ManagerDataAccess managerDataAccess) {
        this.managerDataAccess = managerDataAccess;
        this.loginUtility = new LoginUtility();
    }

    public void addEmployee(String name,String pass, int id,String phone,int accountNum, int branchNumber, String profession) {
        managerDataAccess.addEmployee(name, pass, id, phone, accountNum, branchNumber,profession);
    }

    public CashierBL getCashierBL() {
        return cashierBL;
    }

    public String getEncryptedPass(String pass)
    {
        return loginUtility.getEncryptedPass(pass);
    }

    public ArrayList<Employee> selectAllEmployees(){
        return managerDataAccess.selectAllEmployees();
    }
}
