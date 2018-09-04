package Entities;

import java.util.Vector;

public class Deal {
    private double discount;
    private Vector<Integer> gifts = new Vector<>();

    public Deal()
    {
        this.discount = 1;
    }

    public Deal(double discount)
    {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public Vector<Integer> getGifts() {
        return gifts;
    }

    public void addGiftToDeal(int productCode){
        this.gifts.add(productCode);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
