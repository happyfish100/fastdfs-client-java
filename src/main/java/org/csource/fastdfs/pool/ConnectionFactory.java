package org.csource.fastdfs.pool;

import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
public class ConnectionFactory {
    public static Connection create(String key) throws IOException {
        if (key == null) {
            System.err.printf("ip:port entry conn't be null");
            return null;
        }
        String[] keyPortString = key.split(":");
        if (keyPortString.length != 2) {
            System.err.printf("ip:port entry is invalid! key:{}", keyPortString);
            return null;
        }
        String ip = keyPortString[0];
        Integer port = Integer.parseInt(keyPortString[1]);
        Socket sock = new Socket();
        sock.setReuseAddress(true);
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        sock.connect(inetSocketAddress, ClientGlobal.g_connect_timeout);
        return new Connection(sock, inetSocketAddress);

    }

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
