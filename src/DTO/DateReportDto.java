package DTO;

import java.util.Date;

public class DateReportDto extends DtoBase {
    private Date date;
    private int BranchNumber;

    public DateReportDto(String func, Date date, int branchNumber) {
        super(func);
        this.date = date;
        BranchNumber = branchNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBranchNumber() {
        return BranchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        BranchNumber = branchNumber;
    }
}
