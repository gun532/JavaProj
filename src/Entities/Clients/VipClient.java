package Entities.Clients;

import Entities.ShoppingCart;

public class VipClient extends Client {

//    public VipClient(){
//        super();
//        this.setType(ClientType.VIPCLIENT);
//        this.discountRate = 30;
//    }

    public VipClient(int in_id, String in_fullName, String in_phoneNumber, int clientCode){
        super(in_id, in_fullName, in_phoneNumber,ClientType.VIPCLIENT, clientCode);
        this.discountRate = (30);
    }

//    public  VipClient(Client client)
//    {
//        super(client);
//        this.setType(ClientType.VIPCLIENT);
//        this.discountRate = (30);
//    }

//    @Override
//    public void getClientDeal(ShoppingCart shoppingCart) {
//        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice()*discountRate);
//        shoppingCart.addToCart();
//    }
}
