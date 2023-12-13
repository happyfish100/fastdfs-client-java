/**
 * Copyright (C) 2023 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import java.util.HashMap;
import java.net.InetSocketAddress;

/**
 * Storage Server Address Map
 *
 * @author Happy Fish / YuQing
 * @version Version 1.31
 */
public class StorageAddressMap {

    protected boolean without_port;
    protected HashMap<String, InetSocketAddress> storages;

    public StorageAddressMap(boolean without_port) {
        this.without_port = without_port;
        this.storages = new HashMap<String, InetSocketAddress>();
    }

    protected String getKey(String ipAddr, int port) {
        return ipAddr + "@" + port;
    }

    public void puts(String srcIpAddr, String destIpAddr, int port) {
        storages.put(this.getKey(srcIpAddr, port),
                new InetSocketAddress(destIpAddr, port));
        storages.put(this.getKey(destIpAddr, port),
                new InetSocketAddress(srcIpAddr, port));
    }

    public void puts(String srcIpAddr, String destIpAddr) {
        storages.put(srcIpAddr, new InetSocketAddress(destIpAddr, 0));
        storages.put(destIpAddr, new InetSocketAddress(srcIpAddr, 0));
    }

    public InetSocketAddress get(String ipAddr, int port) {
        if (this.without_port) {
            InetSocketAddress sockAddr;
            if ((sockAddr=storages.get(ipAddr)) == null) {
                return null;
            }

            return new InetSocketAddress(sockAddr.getAddress(), port);
        } else {
            return storages.get(this.getKey(ipAddr, port));
        }
    }

    public InetSocketAddress get(InetSocketAddress sockAddr) {
        return this.get(sockAddr.getAddress().getHostAddress(), sockAddr.getPort());
    }
}
