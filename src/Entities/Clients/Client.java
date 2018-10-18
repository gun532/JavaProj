package Entities.Clients;

import Entities.Deal;
import Entities.ShoppingCart;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Client {
    private int id;
    private String fullName;
    private String phoneNumber;
    private ClientType type;
    protected double discountRate; // TODO: 01/09/2018 remove this method and the column in the DB.
    private Map <Integer, ShoppingCart> shoppingHistory;
    private int clientCode;

    public Client(){
        this.id = 0;
        this.clientCode = 0;
        this.fullName = null;
        this.phoneNumber = null;
        this.discountRate = 0;
        this.shoppingHistory = new LinkedHashMap<Integer, ShoppingCart>();
    }

    public Client(int in_id, String in_fullName, String in_phoneNumber, ClientType type, int clientCode) {
        this.id = in_id;
        this.fullName = in_fullName;
        this.phoneNumber = in_phoneNumber;
        this.type = type;
        this.discountRate = 0;
        this.shoppingHistory = new LinkedHashMap<Integer, ShoppingCart>();
        this.clientCode = clientCode;
    }

    public Client(Client client)
    {
        this.id = client.id;
        this.fullName = client.fullName;
        this.phoneNumber = client.phoneNumber;
        this.type = client.type;
        this.discountRate = client.discountRate;
        this.shoppingHistory = client.shoppingHistory;
        this.clientCode = client.clientCode;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ClientType getType() {
        return type;
    }

    public Map<Integer, ShoppingCart> getShoppingHistory() {
        return shoppingHistory;
    }

    public int getClientCode() { return clientCode; }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientCode(int clientCode) {
        this.clientCode = clientCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public void addNewCartToHistory(ShoppingCart newCart)
    {
        this.shoppingHistory.put(newCart.hashCode(),newCart);
    }


    @Override
    public String toString() {
        return "\nBL.Client Details:\n" + "BL.Client Code: " + this.hashCode() + "\nBL.Client id: " + this.id + "\nBL.Client full name: " + this.fullName + "\nBL.Client phone number: " + this.phoneNumber
        + "\nBL.Client Type: " + this.type + "\nDiscount Rate: " + this.discountRate + "\nShopping History:\n" + this.shoppingHistory;
    }

    @Override
    public boolean equals(Object obj) {

        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Client)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Client c = (Client) obj;

        // Compare the data members and return accordingly
        return this.id == c.id;
    }

    public static void main(String args[]){

    }
}


