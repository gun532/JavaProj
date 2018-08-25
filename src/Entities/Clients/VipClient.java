package Entities.Clients;

public class VipClient extends Client {

    public VipClient(){
        super();
        this.setType(ClientType.VIPCLIENT);
        this.discountRate = 30;
    }

    public VipClient(int in_id, String in_fullName, String in_phoneNumber){
        super(in_id, in_fullName, in_phoneNumber,ClientType.VIPCLIENT);
        this.discountRate = (30);
    }

    public  VipClient(Client client)
    {
        super(client);
        this.setType(ClientType.VIPCLIENT);
        this.discountRate = (30);
    }
}
