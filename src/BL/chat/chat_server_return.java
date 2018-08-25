package BL.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class chat_server_return implements Runnable{
    //globals

    Socket SOCK;
    private Scanner input;
    private PrintWriter out;
    String msg = "";
    //------------------

    public chat_server_return(Socket socket){
        this.SOCK = socket;
    }

    public void checkConn() throws IOException {
        if(!SOCK.isConnected())
        {
            for(int i = 1; i<= ChatServer.connectionArray.size(); i++)
            {
                if(ChatServer.connectionArray.get(i) == SOCK)
                {
                    ChatServer.connectionArray.remove(i);
                }
            }
            for(int i = 1; i<= ChatServer.connectionArray.size();i++)
            {
                Socket tempSocket = (Socket)ChatServer.connectionArray.get(i-1);
                PrintWriter temp_out = new PrintWriter(tempSocket.getOutputStream());
                temp_out.println(tempSocket.getLocalAddress().getHostName() + " disconnected!");
                out.flush();
                //show disconnection on server
                System.out.println(tempSocket.getLocalAddress().getHostName() + " disconnected!");
            }
        }
    }

    public void run(){
        try{
            try{
                input = new Scanner(SOCK.getInputStream());
                out = new PrintWriter(SOCK.getOutputStream());

                while(true){
                    checkConn();

                    if(!input.hasNext()){
                        return;
                    }
                    msg = input.nextLine();

                    System.out.println("Client Said: " +msg);

                    for(int i = 1; i<= ChatServer.connectionArray.size();i++)
                    {
                        Socket tempSocket = (Socket)ChatServer.connectionArray.get(i-1);
                        PrintWriter temp_out = new PrintWriter(tempSocket.getOutputStream());
                        temp_out.println(msg);
                        temp_out.flush();
                        //show disconnection on server
                        System.out.println("Sent to: " + tempSocket.getLocalAddress().getHostName());
                    }
                }
            }
            finally {
                SOCK.close();
            }
        }
        catch(Exception e){System.out.println(e);}
    }

}
