package org.csource.fastdfs.pool;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {

    private Socket sock;
    private InetSocketAddress inetSockAddr;
    private Long lastAccessTime = System.currentTimeMillis();

    private boolean needActiveTest = false;

    public Connection(Socket sock, InetSocketAddress inetSockAddr) {
        this.sock = sock;
        this.inetSockAddr = inetSockAddr;
    }

    /**
     * get the server info
     *
     * @return the server info
     */
    public InetSocketAddress getInetSocketAddress() {
        return this.inetSockAddr;
    }

    public OutputStream getOutputStream() throws IOException {
        return this.sock.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return this.sock.getInputStream();
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     *
     * @throws IOException
     */
    public void close() throws IOException {
        //if connection enabled get from connection pool
        if (ClientGlobal.g_connection_pool_enabled) {
            ConnectionPool.closeConnection(this);
        } else {
            this.closeDirectly();
        }
    }

    public void release() throws IOException {
        if (ClientGlobal.g_connection_pool_enabled) {
            ConnectionPool.releaseConnection(this);
        } else {
            this.closeDirectly();
        }
    }

    /**
     * force close socket,
     */
    public void closeDirectly() throws IOException {
        if (this.sock != null) {
            try {
                ProtoCommon.closeSocket(this.sock);
            } finally {
                this.sock = null;
            }
        }
    }

    public boolean activeTest() throws IOException {
        if (this.sock == null) {
            return false;
        }
        return ProtoCommon.activeTest(this.sock);
    }

    public boolean isConnected() {
        boolean isConnected = false;
        if (sock != null) {
            if (sock.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }
    public boolean isAvaliable() {
        if (isConnected()) {
            if (sock.getPort() == 0) {
                return false;
            }
            if (sock.getInetAddress() == null) {
                return false;
            }
            if (sock.getRemoteSocketAddress() == null) {
                return false;
            }
            if (sock.isInputShutdown()) {
                return false;
            }
            if (sock.isOutputShutdown()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isNeedActiveTest() {
        return needActiveTest;
    }

    public void setNeedActiveTest(boolean needActiveTest) {
        this.needActiveTest = needActiveTest;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "sock=" + sock +
                ", inetSockAddr=" + inetSockAddr +
                ", lastAccessTime=" + lastAccessTime +
                ", needActiveTest=" + needActiveTest +
                '}';
    }
}
