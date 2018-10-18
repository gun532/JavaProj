package BL;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


//SERVER class - to run the project please run this first before you run the controller
//if you see - serverSocket starteed, it's ok to start the controller

public class Server{
    public static final int PORT_NUMBER = 9999;

    public static void main(String[] args) {
//        SocketServer.runSocket();


         SSLServerSocket sslServerSocket;
         SSLServerSocketFactory sslServerSocketfactory;
         SSLSocket sslSocket ;

        //System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
        //System.setProperty("javax.net.ssl.keyStorePassword","123456");
        //System.setProperty("javax.net.debug","all");
        System.out.println("SocketServer started");
        //ServerSocket server = null;
        try {
            //create the ssl connection on the server side
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
             sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
             sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(PORT_NUMBER);
            sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
            //server = new ServerSocket(PORT_NUMBER);
            while (true) {
                //*create a new {
                //      @link SocketServer
                // } object for each connection
                //*this will allow multiple client connections
                //new SocketServer(server.accept());

                sslSocket = (SSLSocket)sslServerSocket.accept();
                new SocketServer(sslSocket);

                //new SocketServer((SSLSocket) sslServerSocket.accept());
                //sslSocket = (SSLSocket)sslServerSocket.accept();

                //connectionArray.add(socket);
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server." + ex.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                    if (server != null)
//                        server.close();

//                if (sslServerSocket != null) {
//                    sslServerSocket.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
    }
}

