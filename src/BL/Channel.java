package BL;

import DTO.PeerDto;
import Entities.Employee.Cashier;
import Entities.Employee.Employee;
import Entities.Employee.Manager;
import Entities.Employee.Seller;
import GUI.ChatMessagePage;
import GUI.ChatPage;
import GUI.Controller;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.*;


public class Channel implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private InetAddress peerSocketAddress;
    private int peerPort;
    private Employee peerToTalk;
    private ChatPage chatPage;
    private ChatMessagePage chatMessagePage;
    private GlobalLogger log = new GlobalLogger("logs.log");

    public void bind(int port, ChatPage chatPage) throws SocketException {
        socket = new DatagramSocket(port);
        this.chatPage = chatPage;
    }

    public void start() {
        running = true;
        Thread thread = new Thread(this);
        thread.start();

    }

    public void stop() {
        running = false;
        socket.close();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        Boolean isEmpty = true;
        //running = true;
        while (running) {
            try {
                socket.receive(packet);

                // open text box
                peerSocketAddress = packet.getAddress();
                peerPort = packet.getPort();
                peerToTalk = searchPeerLocation(peerSocketAddress, peerPort);

                if (peerToTalk != null) {
                    if (chatPage.getChatFramesMap().get(peerToTalk.getEmployeeNumber()) != null) {
                        String msg = new String(buffer, 0, packet.getLength());
                        chatPage.getChatFramesMap().get(peerToTalk.getEmployeeNumber()).getTextArea().append(msg + "\n");

                        System.out.println(msg);

                    } else {
                        if (chatPage.getChatFramesMap().get(peerToTalk.getEmployeeNumber()) == null) {
                            chatMessagePage = new ChatMessagePage(chatPage.getController(), peerToTalk, this);
                            chatPage.getChatFramesMap().put(peerToTalk.getEmployeeNumber(), chatMessagePage);
                        }

                        chatPage.getChatFramesMap().get(peerToTalk.getEmployeeNumber()).setVisible(true);
                        String msg = new String(buffer, 0, packet.getLength());
                        chatMessagePage.getTextArea().append(msg + "\n");

                        System.out.println(msg);
                        log.logger.info(msg);
                    }
                }

        } catch(IOException e){
                for(int i = 0 ; i<buffer.length;i++)
                {
                    if(buffer[i] != 0)  isEmpty = false;
                }
                if(isEmpty) System.out.println("The buffer was empty");
                else e.printStackTrace();
            break;
        }

    }

}

    public void sendTo(SocketAddress address, String msg) throws IOException {
        byte[] buffer = msg.getBytes();


        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        packet.setSocketAddress(address);

        socket.send(packet);
        log.logger.info("message sent");
    }

    private Employee searchPeerLocation(InetAddress peerAddress, int peerPort) {
        Employee employee = null;
        try {
            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
            Gson gson = new Gson();

            PeerDto peerDto = new PeerDto("getPeerByPornAndAddress", peerAddress, peerPort);
            out.println(gson.toJson(peerDto));

            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
            String response = in.readLine();
//            response = response.substring(1,response.length()-1);
            if (response.equals("null")) {
//                JOptionPane.showMessageDialog(new JFrame(), "Unsuccessful login, please try again.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            } else {
                String profession = new JSONObject(response).getString("jobPos");
                switch (profession) {
                    case "SELLER":
                        employee = gson.fromJson(response, Seller.class);
                        break;
                    case "CASHIER":
                        employee = gson.fromJson(response, Cashier.class);
                        break;
                    case "MANAGER":
                        employee = gson.fromJson(response, Manager.class);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return employee;
    }
}


//    public DatagramSocket getSocket() {
//        return socket;
//    }
//
//    public void actionPerformed(ActionEvent evt) {
//        // Respond when the user clicks one of the buttons
//        // or presses return in the message input box.
//        Object source = evt.getSource();
//        if (source == )
//            transcript.setText("");
//        else if (source == sendButton || source == messageInput)
//            doSend();
//        else if (source == closeButton)
//            doClose();
//    }

//}
