package BL;

import DAL.ManagerDataAccess;
import Entities.Employee.Employee;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.json.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
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

    public void addEmployee(String name, String pass, int id, String phone, int accountNum, int branchNumber, String profession) {
        managerDataAccess.addEmployee(name, pass, id, phone, accountNum, branchNumber, profession);
    }

    public CashierBL getCashierBL() {
        return cashierBL;
    }

    public String getEncryptedPass(String pass) {
        return loginUtility.getEncryptedPass(pass);
    }

    public ArrayList<Employee> selectAllEmployees() {
        return managerDataAccess.selectAllEmployees();
    }

    public JSONArray createReportTotalPurchasesInBranch(int branchNumber) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            int i = 0;
            ResultSet rs = managerDataAccess.totalClientPurchasesInBranch(branchNumber);
            ResultSetMetaData resultSetMetaData = managerDataAccess.getResultSetMetaData(rs);
            while (rs.next()) {
                int numColumns = resultSetMetaData.getColumnCount();
                jsonObject = new JSONObject();
                for ( i = 1; i < numColumns + 1; i++) {
                    String column_name = resultSetMetaData.getColumnName(i);
                    if (resultSetMetaData.getColumnType(i) == Types.VARCHAR) {
                        jsonObject.put(column_name, rs.getString(column_name));
                    } else if (resultSetMetaData.getColumnType(i) == Types.DOUBLE) {
                        jsonObject.put(column_name, rs.getDouble(column_name));
                    }
                }
                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put("Total Amount:",managerDataAccess.getTotalAmountInBranch(branchNumber));
            jsonArray.put(jsonObject);
            //jsonArray.put(i-1,managerDataAccess.getTotalAmountInBranch(branchNumber));
            //jsonArray.put(jsonObject.put("Total Amount:", managerDataAccess.getTotalAmountInBranch(branchNumber)));
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeJSONToFile(JSONArray jsonArray) {
        try (FileWriter file = new FileWriter("/Users/IBM_ADMIN/Desktop/file1.json")) {
            file.write(jsonArray.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            //System.out.println("\nJSON Object: " + obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
