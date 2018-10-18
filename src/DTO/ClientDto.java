package DTO;

import Entities.Clients.ClientType;
import Entities.ShoppingCart;

import java.util.Map;

public class ClientDto extends DtoBase{
    private int id;
    private String fullName;
    private String phoneNumber;
    private ClientType type;
    private int clientCode;

    public ClientDto(String func, int id, String fullName, String phoneNumber, ClientType type, int clientCode) {
        super(func);
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.clientCode = clientCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public int getClientCode() {
        return clientCode;
    }

    public void setClientCode(int clientCode) {
        this.clientCode = clientCode;
    }
}
