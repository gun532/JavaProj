package ClientsPackage;
//MAY NOT BE NEEDED!
public class NewClient extends Client {

    public NewClient(){
        super();
        this.setType(ClientType.NEWCLIENT);
    }

    public NewClient(int in_id, String in_fullName, String in_phoneNumber){
        super(in_id, in_fullName, in_phoneNumber);
        this.setType(ClientType.NEWCLIENT);
    }
}
