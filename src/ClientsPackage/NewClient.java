package ClientsPackage;
//MAY NOT BE NEEDED!
public class NewClient extends Client {

    public NewClient(int id, String fullName, String phoneNumber) {
        super(id, fullName, phoneNumber, ClientType.NEWCLIENT, 0);
    }

    public NewClient(NewClient client) {
        super(client);
    }
}
