package ClientsPackage;

import EmployeePackage.Profession;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Client {
    private int id;
    private String fullName;
    private String phoneNumber;
    private ClientType type;
    protected double discountRate;
    private Map <Integer, ShoppingCart> shoppingHistory;
    //private int clientCode;

    public  Client(){
        this.id = 0;
        //this.clientCode = 0;
        this.fullName = null;
        this.phoneNumber = null;
        this.type = ClientType.NEWCLIENT;
        this.discountRate = 0;
        this.shoppingHistory = new LinkedHashMap<Integer, ShoppingCart>();
    }

    public Client(int in_id, String in_fullName, String in_phoneNumber, ClientType type) {
        this.id = in_id;
        this.fullName = in_fullName;
        this.phoneNumber = in_phoneNumber;
        this.type = type;
        this.discountRate = 0;
        this.shoppingHistory = new LinkedHashMap<Integer, ShoppingCart>();
        //this.clientCode = this.hashCode();
    }

    public Client(Client client)
    {
        this.id = client.id;
        this.fullName = client.fullName;
        this.phoneNumber = client.phoneNumber;
        this.type = client.type;
        this.discountRate = client.discountRate;
        this.shoppingHistory = client.shoppingHistory;
        //this.clientCode = this.hashCode();
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

    public double getDiscountRate() {
        return discountRate;
    }

    public Map<Integer, ShoppingCart> getShoppingHistory() {
        return shoppingHistory;
    }

    //public int getClientCode() { return clientCode; }

    public int getClientCode() { return this.hashCode(); }

    public void setId(int id) {
        this.id = id;
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
        return "\nClient Details:\n" + "Client Code: " + this.hashCode() + "\nClient id: " + this.id + "\nClient full name: " + this.fullName + "\nClient phone number: " + this.phoneNumber
        + "\nClient Type: " + this.type + "\nDiscount Rate: " + this.discountRate + "\nShopping History:\n" + this.shoppingHistory;
    }

    @Override
    public int hashCode() {
        return (int) (this.id / 47);
    }

    public static void main(String args[]){
        Client cl1 = new Client(304989171,"Roy Bar","0506797973", ClientType.NEWCLIENT) {};
        //cl1 = new VipClient(cl1);
        System.out.println(cl1);
    }
}


