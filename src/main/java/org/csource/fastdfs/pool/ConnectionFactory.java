package org.csource.fastdfs.pool;

import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
public class ConnectionFactory {
    /**
     * create from InetSocketAddress
     * @param socketAddress
     * @return
     * @throws IOException
     */
    public static Connection create(InetSocketAddress socketAddress) throws IOException {
        Socket sock = new Socket();
        sock.setReuseAddress(true);
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(socketAddress, ClientGlobal.g_connect_timeout);
        return new Connection(sock, socketAddress);
    }
}
