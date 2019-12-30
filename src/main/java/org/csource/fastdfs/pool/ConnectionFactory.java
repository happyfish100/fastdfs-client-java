package org.csource.fastdfs.pool;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionFactory {
    /**
     * create from InetSocketAddress
     *
     * @param socketAddress
     * @return
     * @throws IOException
     */
    public static Connection create(InetSocketAddress socketAddress) throws MyException {
        try {
            Socket sock = new Socket();
            sock.setReuseAddress(true);
            sock.setSoTimeout(ClientGlobal.g_network_timeout);
            sock.connect(socketAddress, ClientGlobal.g_connect_timeout);
            return new Connection(sock, socketAddress);
        } catch (Exception e) {
            throw new MyException("get connection error, ip:host = " + socketAddress.getHostName() + ":" + socketAddress.getPort() + ", emsg:" + e.getMessage());
        }
    }
}
