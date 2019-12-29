package org.csource.fastdfs.pool;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {
    /**
     * ip:port is key
     */
    private String key;

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
    private volatile ConcurrentLinkedQueue<Connection> freeConnections = new ConcurrentLinkedQueue<Connection>();

    private ConnectionManager() {

    }

    public ConnectionManager(String key) {
        this.key = key;
    }

    private  Connection newConnection() throws IOException {
        try {
            Connection connection = ConnectionFactory.create(this.key);
            return connection;
        } catch (IOException e) {
            throw e;
        }
    }


    public  Connection getConnection() throws MyException, IOException {
        lock.lock();
        try {
            Connection connection = null;
            while (true) {
                if (freeCount.get() > 0) {
                    freeCount.decrementAndGet();
                    connection = freeConnections.poll();
                    if (!connection.isAvaliable() || (System.currentTimeMillis() - connection.getLastAccessTime()) > ClientGlobal.getG_connection_pool_max_idle_time()) {
                        closeConnection(connection);
                        continue;
                    }
                } else if (ClientGlobal.getG_connection_pool_max_count_per_entry() == 0 || totalCount.get() < ClientGlobal.getG_connection_pool_max_count_per_entry()) {
                    connection = newConnection();
                    if (connection != null) {
                        totalCount.incrementAndGet();
                    }
                } else {
                    try {
                        if (condition.await(ClientGlobal.getG_connection_pool_max_wait_time_in_ms(), TimeUnit.MILLISECONDS)) {
                            //wait single success
                            continue;
                        }
                        throw new MyException("get connection fail,  wait_time greater than " + ClientGlobal.g_connection_pool_max_wait_time_in_ms + "ms");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new MyException("get connection fail, emsg:" + e.getMessage());
                    }
                }
                return connection;
            }
        } catch (IOException e) {
            System.err.println("get connection ERROR , emsg:" + e.getMessage());
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public  void releaseConnection(Connection connection) throws IOException {
        if (connection == null) {
            return;
        }
        if ((System.currentTimeMillis() - connection.getLastAccessTime()) < ClientGlobal.g_connection_pool_max_idle_time) {
            try {
                lock.lock();
                freeConnections.add(connection);
                freeCount.incrementAndGet();
                condition.signal();
            } finally {
                lock.unlock();
            }
        } else {
            closeConnection(connection);
        }

    }

    public void closeConnection(Connection connection) throws IOException {
        try {
            if (connection != null) {
                totalCount.decrementAndGet();
                connection.close();
            }
        } catch (IOException e) {
            System.err.println("close socket error , msg:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConnectionManager{" +
                "key='" + key + '\'' +
                ", totalCount=" + totalCount +
                ", freeCount=" + freeCount +
                ", linkedQueueCP=" + freeConnections +
                '}';
    }
}
