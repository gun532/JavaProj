package DTO;

import java.net.InetAddress;
import java.net.SocketAddress;

public class PeerDto extends DtoBase{

    private InetAddress peerSocketAddress;
    private int peerPort;

    public PeerDto(String func, InetAddress peerSocketAddress, int peerPort) {
        super(func);
        this.peerSocketAddress = peerSocketAddress;
        this.peerPort = peerPort;
    }

    public InetAddress getPeerAddress() {
        return peerSocketAddress;
    }

    public void setAddress(InetAddress peerSocketAddress) {
        this.peerSocketAddress = peerSocketAddress;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
    }
}
