package Entities.Clients;

public class NewClient extends Client {

    public NewClient(int id, String fullName, String phoneNumber, int clientCode) {
        super(id, fullName, phoneNumber, ClientType.NEWCLIENT,clientCode);
        this.discountRate = 0;
    }

    public NewClient(NewClient client) {
        super(client);
    }
}
