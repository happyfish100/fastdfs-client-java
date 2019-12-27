package org.csource.fastdfs.pool;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionInfo {
    private Socket socket;
    protected InetSocketAddress inetSockAddr;
    private Long lastAccessTime;
    private boolean needActiveCheck = false;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InetSocketAddress getInetSockAddr() {
        return inetSockAddr;
    }

    public void setInetSockAddr(InetSocketAddress inetSockAddr) {
        this.inetSockAddr = inetSockAddr;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean isNeedActiveCheck() {
        return needActiveCheck;
    }

    public void setNeedActiveCheck(boolean needActiveCheck) {
        this.needActiveCheck = needActiveCheck;
    }

    public ConnectionInfo(Socket socket, InetSocketAddress inetSockAddr, Long lastAccessTime, boolean needActiveCheck) {
        this.socket = socket;
        this.inetSockAddr = inetSockAddr;
        this.lastAccessTime = lastAccessTime;
        this.needActiveCheck = needActiveCheck;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "socket=" + socket +
                ", inetSockAddr=" + inetSockAddr +
                ", lastAccessTime=" + lastAccessTime +
                ", needActiveCheck=" + needActiveCheck +
                '}';
    }
}
