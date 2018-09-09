package DTO;

import Entities.Clients.Client;
import Entities.Clients.ClientType;
import Entities.ShoppingCart;

import java.util.ArrayList;
import java.util.Map;

public class ClientsArrayDto extends DtoBase{

    private ArrayList<Client> allClients;

    public ClientsArrayDto(String func, ArrayList<Client> allClients) {
        super(func);
        this.allClients = allClients;
    }

    public ArrayList<Client> getAllClients() {
        return allClients;
    }

    public void setAllClients(ArrayList<Client> allClients) {
        this.allClients = allClients;
    }
}
