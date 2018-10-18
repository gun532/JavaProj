package DTO;

import Entities.Product;

public class ProductReportDto extends DtoBase{
    private Product product;
    private int BranchNumber;

    public ProductReportDto(String func, Product product, int branchNumber) {
        super(func);
        this.product = product;
        BranchNumber = branchNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getBranchNumber() {
        return BranchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        BranchNumber = branchNumber;
    }
}
