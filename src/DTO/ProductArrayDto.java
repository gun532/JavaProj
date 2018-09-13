package DTO;

import Entities.Employee.Employee;
import Entities.Product;

import java.util.ArrayList;

public class ProductArrayDto extends DtoBase {
    private ArrayList<Product> allProducts;

    public ProductArrayDto(String func, ArrayList<Product> allProducts) {
        super(func);
        this.allProducts = allProducts;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(ArrayList<Product> allProducts) {
        this.allProducts = allProducts;
    }
}
