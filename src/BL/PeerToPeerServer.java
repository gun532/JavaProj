//package BL;
//
//import java.io.*;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class PeerToPeerServer extends Thread {
//    private static final int PORT_NUMBER = 26666;
//
//    protected Socket socket;
//
//    public PeerToPeerServer(){}
//    private PeerToPeerServer(Socket socket) {
//        this.socket = socket;
//        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
//        start();
//    }
//
//    public void run() {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            in = socket.getInputStream();
//            out = socket.getOutputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String request;
//            while ((request = br.readLine()) != null) {
//                System.out.println("Message received:" + request);
//                request += '\n';
//                out.write(request.getBytes());
//            }
//
//        } catch (IOException ex) {
//            System.out.println("Unable to get streams from client");
//        } finally {
//            try {
//                System.out.println("Closed connection \n");
//                in.close();
//                out.close();
//                socket.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public void startServer()
//    {
//        System.out.println("BL.Client is now the started");
//        ServerSocket server = null;
//        try {
//            server = new ServerSocket(PORT_NUMBER);
//            while (true) {
//                new PeerToPeerServer(server.accept());
//            }
//        } catch (IOException ex) {
//            System.out.println("Unable to start server."+ex.getMessage());
//        } finally {
//            try {
//                if (server != null)
//                    server.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
////    public static void main(String[] args) {
////        System.out.println("SocketServer Example");
////        ServerSocket server = null;
////        try {
////            server = new ServerSocket(PORT_NUMBER);
////            while (true) {
////                new PeerToPeerServer(server.accept());
////            }
////        } catch (IOException ex) {
////            System.out.println("Unable to start server."+ex.getMessage());
////        } finally {
////            try {
////                if (server != null)
////                    server.close();
////            } catch (IOException ex) {
////                ex.printStackTrace();
////            }
////        }
////    }
//}
