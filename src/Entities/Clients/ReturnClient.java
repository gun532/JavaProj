package Entities.Clients;

public class ReturnClient extends Client {

    public ReturnClient(){
        super();
        this.setType(ClientType.RETURNCLIENT);
        this.discountRate = (10);
    }

    public ReturnClient(int in_id, String in_fullName, String in_phoneNumber, int clientCode){
        super(in_id, in_fullName, in_phoneNumber, ClientType.RETURNCLIENT,clientCode);
        this.discountRate = (10);
    }

    public ReturnClient(Client client) {
        super(client);
        this.setType(ClientType.RETURNCLIENT);
        this.discountRate = (10);
    }
}
