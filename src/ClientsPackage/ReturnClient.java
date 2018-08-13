package ClientsPackage;

public class ReturnClient extends Client {

    public ReturnClient(){
        super();
        this.setType(ClientType.RETURNCLIENT);
        this.setDiscountRate(10);
    }

    public ReturnClient(int in_id, String in_fullName, String in_phoneNumber){
        super(in_id, in_fullName, in_phoneNumber,ClientType.RETURNCLIENT,10);
    }

    public ReturnClient(ReturnClient client) {
        super(client);
    }
}
