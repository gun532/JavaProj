package DTO;

public class LoginDetailsDto extends DtoBase {

    private int employeeId;
    private String password;

    public LoginDetailsDto(String func, int employeeId, String password) {
        super(func);
        this.employeeId = employeeId;
        this.password = password;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
