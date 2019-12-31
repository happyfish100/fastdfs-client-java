package org.csource.fastdfs.pool;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {

    private InetSocketAddress inetSocketAddress;

    /**
     * total create connection pool
     */
    private AtomicInteger totalCount = new AtomicInteger();

    /**
     * free connection count
     */
    private AtomicInteger freeCount = new AtomicInteger();

    /**
     * lock
     */
    private ReentrantLock lock = new ReentrantLock(true);

    private Condition condition = lock.newCondition();

    /**
     * free connections
     */
    private LinkedList<Connection> freeConnections = new LinkedList<Connection>();

    private ConnectionManager() {

    }

    public ConnectionManager(InetSocketAddress socketAddress) {
        this.inetSocketAddress = socketAddress;
    }

    public Connection getConnection() throws MyException {
        lock.lock();
        try {
            Connection connection = null;
            while (true) {
                if (freeCount.get() > 0) {
                    freeCount.decrementAndGet();
                    connection = freeConnections.poll();
                    if (!connection.isAvaliable() || (System.currentTimeMillis() - connection.getLastAccessTime()) > ClientGlobal.g_connection_pool_max_idle_time) {
                        closeConnection(connection);
                        continue;
                    }
                    if (connection.isNeedActiveTest()) {
                        boolean isActive = false;
                        try {
                            isActive = connection.activeTest();
                        } catch (IOException e) {
                            System.err.println("send to server[" + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() + "] active test error ,emsg:" + e.getMessage());
                            isActive = false;
                        }
                        if (!isActive) {
                            closeConnection(connection);
                            continue;
                        } else {
                            connection.setNeedActiveTest(false);
                        }
                    }
                } else if (ClientGlobal.g_connection_pool_max_count_per_entry == 0 || totalCount.get() < ClientGlobal.g_connection_pool_max_count_per_entry) {
                    connection = ConnectionFactory.create(this.inetSocketAddress);
                    totalCount.incrementAndGet();
                } else {
                    try {
                        if (condition.await(ClientGlobal.g_connection_pool_max_wait_time_in_ms, TimeUnit.MILLISECONDS)) {
                            //wait single success
                            continue;
                        }
                        throw new MyException("connect to server " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() + " fail, wait_time > " + ClientGlobal.g_connection_pool_max_wait_time_in_ms + "ms");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new MyException("connect to server " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() + " fail, emsg:" + e.getMessage());
                    }
                }
                return connection;
            }
        } finally {
            lock.unlock();
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        lock.lock();
        try {
            connection.setLastAccessTime(System.currentTimeMillis());
            freeConnections.add(connection);
            freeCount.incrementAndGet();
            condition.signal();
        } finally {
            lock.unlock();
        }

    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                totalCount.decrementAndGet();
                connection.closeDirectly();
            }
        } catch (IOException e) {
            System.err.println("close socket[" + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() + "] error ,emsg:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setActiveTestFlag() {
        if (freeCount.get() > 0) {
            lock.lock();
            try {
                for (Connection freeConnection : freeConnections) {
                    freeConnection.setNeedActiveTest(true);
                }
            } finally {
                lock.unlock();
            }
        }
    }


    @Override
    public String toString() {
        return "ConnectionManager{" +
                "ip:port='" + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() +
                ", totalCount=" + totalCount +
                ", freeCount=" + freeCount +
                ", freeConnections =" + freeConnections +
                '}';
    }
}
