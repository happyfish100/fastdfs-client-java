package org.csource.fastdfs.pool;

import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {
    /**
     * key is ip:port, value is ConnectionManager
     */
    private final static ConcurrentHashMap<String, ConnectionManager> CP = new ConcurrentHashMap<String, ConnectionManager>();

    public static synchronized ConnectionInfo getConnection(InetSocketAddress socketAddress) throws IOException {
        if (socketAddress == null) {
            return null;
        }
        String key = getKey(socketAddress);
        ConnectionManager connectionManager = CP.get(key);
        if (connectionManager == null) {
            connectionManager = new ConnectionManager(key);
            CP.put(key, connectionManager);
        }
        return connectionManager.getConnection();
    }

    /**
     * release connection
     */
    public static void closeConnection(TrackerServer trackerServer) throws IOException {
        if (trackerServer == null || trackerServer.getInetSocketAddress() == null) {
            return;
        }
        String key = getKey(trackerServer.getInetSocketAddress());
        if (key != null) {
            ConnectionManager connectionManager = CP.get(key);
            connectionManager.closeConnection(new ConnectionInfo(trackerServer.getSocket(), trackerServer.getInetSocketAddress(),trackerServer.getLastAccessTime(),true));
        } else {
            trackerServer.closeDirect();
        }
    }

    public static void freeConnection(TrackerServer trackerServer) throws IOException {
        if (trackerServer == null || trackerServer.getInetSocketAddress() == null) {
            return;
        }
        String key = getKey(trackerServer.getInetSocketAddress());
        if (key != null) {
            ConnectionManager connectionManager = CP.get(key);
            connectionManager.freeConnection(trackerServer);
        } else {
            trackerServer.closeDirect();
        }
    }

    private static String getKey(InetSocketAddress socketAddress) {
        if (socketAddress == null) {
            return null;
        }
        return String.format("%s:%s", socketAddress.getHostName(), socketAddress.getPort());
    }
    @Override
    public String toString() {
        if (!CP.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, ConnectionManager> managerEntry : CP.entrySet()) {
                builder.append("key:" + managerEntry.getKey() + " -------- entry:" + managerEntry.getValue() + "\n");
            }
            return builder.toString();
        }
        return null;
    }
}
