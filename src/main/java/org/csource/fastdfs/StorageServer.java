/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import java.io.IOException;
import java.util.Hashtable;
import org.csource.common.MyException;
import org.csource.fastdfs.pool.Connection;
import java.net.InetSocketAddress;

/**
 * Storage Server Info
 *
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class StorageServer extends TrackerServer {
    protected int store_path_index = 0;

    protected static Hashtable<String, InetSocketAddress> sockAddressCache = new Hashtable<String, InetSocketAddress>();

    /**
     * Constructor
     *
     * @param ip_addr    the ip address of storage server
     * @param port       the port of storage server
     * @param store_path the store path index on the storage server
     */
    public StorageServer(String ip_addr, int port, int store_path) throws IOException {
        super(new InetSocketAddress(ip_addr, port));
        this.store_path_index = store_path;
    }

    /**
     * Constructor
     *
     * @param ip_addr    the ip address of storage server
     * @param port       the port of storage server
     * @param store_path the store path index on the storage server
     */
    public StorageServer(String ip_addr, int port, byte store_path) throws IOException {
        super(new InetSocketAddress(ip_addr, port));
        if (store_path < 0) {
            this.store_path_index = 256 + store_path;
        } else {
            this.store_path_index = store_path;
        }
    }

    /**
     * @return the store path index on the storage server
     */
    public int getStorePathIndex() {
        return this.store_path_index;
    }

    public Connection getConnection() throws MyException, IOException {
        Connection connection;
        InetSocketAddress sockAddr;
        MyException myException = null;
        IOException ioException = null;

        if (!ClientGlobal.g_multi_storage_ips) {
            return super.getConnection();
        }

        if (ClientGlobal.g_connect_first_by == ClientGlobal.CONNECT_FIRST_BY_TRACKER) {
            try {
                if ((connection=super.getConnection()) != null) {
                    return connection;
                }
            } catch (MyException ex1) {
                myException = ex1;
            } catch (IOException ex2) {
                ioException = ex2;
            }

            sockAddr = ClientGlobal.g_storages_address_map.get(this.inetSockAddr);
            if (sockAddr != null) {
                return super.getConnection(sockAddr);
            } else if (myException != null) {
                throw myException;
            } else if (ioException != null) {
                throw ioException;
            }
        } else {
            String key = this.inetSockAddr.getAddress().getHostAddress() +
                "@" + this.inetSockAddr.getPort();
            sockAddr = sockAddressCache.get(key);
            try {
                if (sockAddr == null) {
                    sockAddr = this.inetSockAddr;
                    if ((connection=super.getConnection(sockAddr)) != null) {
                        sockAddressCache.put(key, sockAddr);
                        return connection;
                    }
                } else {
                    if ((connection=super.getConnection(sockAddr)) != null) {
                        return connection;
                    }
                }
            } catch (MyException ex1) {
                myException = ex1;
            } catch (IOException ex2) {
                ioException = ex2;
            }

            //retry another ip address
            if ((sockAddr=ClientGlobal.g_storages_address_map.get(sockAddr)) == null) {
                if (myException != null) {
                    throw myException;
                } else if (ioException != null) {
                    throw ioException;
                }
                return null;
            }

            if ((connection=super.getConnection(sockAddr)) != null) {
                sockAddressCache.put(key, sockAddr);
                return connection;
            }
        }

        return null;
    }
}
