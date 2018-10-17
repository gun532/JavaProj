package BL;

import Entities.Employee.Employee;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SSLSocketData {
    private SSLSocket sslSocket;
    private DataInputStream inputStream;
    private PrintStream outputStream;
    private String clientAddress;
    private Employee employee;

    public SSLSocketData(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
        try
        {
            inputStream = new DataInputStream(sslSocket.getInputStream());
            outputStream = new PrintStream(sslSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientAddress = sslSocket.getLocalAddress().getHostAddress();// + ":" + sslSocket.getPort();
    }

    public SSLSocket getSslSocket() {
        return sslSocket;
    }

    public void setSslSocket(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public PrintStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
