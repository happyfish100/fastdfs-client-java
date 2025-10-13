/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import org.csource.common.ConfigureMapper;
import org.csource.common.MyException;
import org.csource.fastdfs.pool.Connection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Tracker client for request to tracker server
 * Note: the instance of this class is NOT thread safe !!!
 *
 * @author Happy Fish / YuQing
 * @version Version 1.19
 */
public class TrackerClient {
    protected TrackerGroup tracker_group;
    protected byte errno;

    /**
     * constructor with global tracker group
     */
    public TrackerClient() {
        this.tracker_group = ClientGlobal.g_tracker_group;
    }

    /**
     * constructor with specified tracker group
     *
     * @param tracker_group the tracker group object
     */
    public TrackerClient(TrackerGroup tracker_group) {
        this.tracker_group = tracker_group;
    }

    /**
     * get the error code of last call
     *
     * @return the error code of last call
     */
    public byte getErrorCode() {
        return this.errno;
    }

    /**
     * get a connection to tracker server
     *
     * @return tracker server Socket object, return null if fail
     */
    public TrackerServer getTrackerServer() throws IOException {
        return this.tracker_group.getTrackerServer();
    }

    /**
     * query storage server to upload file
     *
     * @param trackerServer the tracker server
     * @return storage server Socket object, return null if fail
     */
    public StorageServer getStoreStorage(TrackerServer trackerServer) throws IOException, MyException {
        final String groupName = null;
        return this.getStoreStorage(trackerServer, groupName);
    }

    public Connection getConnection(TrackerServer trackerServer) throws IOException, MyException {
        Connection connection = null;
        int length = this.tracker_group.tracker_servers.length;
        boolean failOver = length > 1 && trackerServer == null;
        try {
            if (trackerServer == null) {
                trackerServer = getTrackerServer();
                if (trackerServer == null) {
                    throw new MyException("tracker server is empty!");
                }
            }
            connection = trackerServer.getConnection();
        } catch (IOException e) {
            if (failOver) {
                System.err.println("trackerServer get connection error, emsg:" + e.getMessage());
            } else {
                throw e;
            }
        } catch (MyException e) {
            if (failOver) {
                System.err.println("trackerServer get connection error, emsg:" + e.getMessage());
            } else {
                throw e;
            }
        }
        if (connection != null || !failOver) {
            return connection;
        }
        //do fail over
        int currentIndex = 0;
        if (trackerServer != null) {
            currentIndex = trackerServer.getIndex();
        }
        int failOverCount = 0;
        while (failOverCount < length - 1) {
            failOverCount++;
            currentIndex++;
            if (currentIndex >= length) {
                currentIndex = 0;
            }
            try {
                trackerServer = this.tracker_group.getTrackerServer(currentIndex);
                if (trackerServer == null) {
                    throw new MyException("tracker server is empty!");
                }
                return trackerServer.getConnection();
            } catch (IOException e) {
                System.err.println("fail over trackerServer get connection error, failOverCount:" + failOverCount + "," + e.getMessage());
                if (failOverCount == length - 1) {
                    throw e;
                }

            } catch (MyException e) {
                System.err.println("fail over trackerServer get connection error, failOverCount:" + failOverCount + ", " + e.getMessage());
                if (failOverCount == length - 1) {
                    throw e;
                }
            }


        }
        return null;
    }


    /**
     * query storage server to upload file
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name to upload file to, can be empty
     * @return storage server object, return null if fail
     */
    public StorageServer getStoreStorage(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        byte[] header;
        String ip_addr;
        int port;
        byte cmd;
        int out_len;
        byte store_path;
        Connection connection = getConnection(trackerServer);
        OutputStream out = connection.getOutputStream();

        try {
            if (groupName == null || groupName.length() == 0) {
                cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
                out_len = 0;
            } else {
                cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
                out_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
            }
            header = ProtoCommon.packHeader(cmd, out_len, (byte) 0);
            out.write(header);

            if (groupName != null && groupName.length() > 0) {
                byte[] bGroupName;
                byte[] bs;
                int group_len;

                bs = groupName.getBytes(ClientGlobal.g_charset);
                bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];

                if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN) {
                    group_len = bs.length;
                } else {
                    group_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
                }
                Arrays.fill(bGroupName, (byte) 0);
                System.arraycopy(bs, 0, bGroupName, 0, group_len);
                out.write(bGroupName);
            }

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                return null;
            }

            if (pkgInfo.body.length < ProtoCommon.TRACKER_QUERY_STORAGE_STORE_IPV4_BODY_LEN) {
                this.errno = ProtoCommon.ERR_NO_EINVAL;
                throw new IOException("Invalid body length: " + pkgInfo.body.length);
            }

            int ip_size;
            if (pkgInfo.body.length == ProtoCommon.TRACKER_QUERY_STORAGE_STORE_IPV6_BODY_LEN) {
                ip_size = ProtoCommon.FDFS_IPV6_SIZE;
            } else if (pkgInfo.body.length == ProtoCommon.TRACKER_QUERY_STORAGE_STORE_IPV4_BODY_LEN) {
                ip_size = ProtoCommon.FDFS_IPV4_SIZE;
            } else {
                this.errno = ProtoCommon.ERR_NO_EINVAL;
                throw new IOException("Invalid body length: " + pkgInfo.body.length);
            }

            ip_addr = new String(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN, ip_size - 1).trim();
            port = (int) ProtoCommon.buff2long(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ip_size - 1);
            store_path = pkgInfo.body[pkgInfo.body.length - 1];

            return new StorageServer(ip_addr, port, store_path);
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            } finally {
                connection = null;
            }
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    /**
     * query storage servers to upload file
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name to upload file to, can be empty
     * @return storage servers, return null if fail
     */
    public StorageServer[] getStoreStorages(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        byte[] header;
        String ip_addr;
        int port;
        byte cmd;
        int out_len;
        Connection connection = getConnection(trackerServer);
        OutputStream out = connection.getOutputStream();

        try {
            if (groupName == null || groupName.length() == 0) {
                cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL;
                out_len = 0;
            } else {
                cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL;
                out_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
            }
            header = ProtoCommon.packHeader(cmd, out_len, (byte) 0);
            out.write(header);

            if (groupName != null && groupName.length() > 0) {
                byte[] bGroupName;
                byte[] bs;
                int group_len;

                bs = groupName.getBytes(ClientGlobal.g_charset);
                bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];

                if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN) {
                    group_len = bs.length;
                } else {
                    group_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
                }
                Arrays.fill(bGroupName, (byte) 0);
                System.arraycopy(bs, 0, bGroupName, 0, group_len);
                out.write(bGroupName);
            }

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                return null;
            }

            if (pkgInfo.body.length < ProtoCommon.TRACKER_QUERY_STORAGE_STORE_IPV4_BODY_LEN) {
                this.errno = ProtoCommon.ERR_NO_EINVAL;
                throw new IOException("Invalid body length: " + pkgInfo.body.length);
            }

            int ipPortLen = pkgInfo.body.length - (ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1);
            int recordLength = ProtoCommon.FDFS_IPV6_SIZE - 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
            int ip_size;
            if ((pkgInfo.body.length >= ProtoCommon.TRACKER_QUERY_STORAGE_STORE_IPV6_BODY_LEN) &&
                    ipPortLen % recordLength == 0)
            {
                ip_size = ProtoCommon.FDFS_IPV6_SIZE;
            } else {
                recordLength = ProtoCommon.FDFS_IPV4_SIZE - 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
                if (ipPortLen % recordLength  == 0) {
                    ip_size = ProtoCommon.FDFS_IPV4_SIZE;
                } else {
                    this.errno = ProtoCommon.ERR_NO_EINVAL;
                    throw new IOException("Invalid body length: " + pkgInfo.body.length);
                }
            }

            int serverCount = ipPortLen / recordLength;
            if (serverCount > 16) {
                this.errno = ProtoCommon.ERR_NO_ENOSPC;
                return null;
            }

            StorageServer[] results = new StorageServer[serverCount];
            byte store_path = pkgInfo.body[pkgInfo.body.length - 1];
            int offset = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;

            for (int i = 0; i < serverCount; i++) {
                ip_addr = new String(pkgInfo.body, offset, ip_size - 1).trim();
                offset += ip_size - 1;

                port = (int) ProtoCommon.buff2long(pkgInfo.body, offset);
                offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

                String mapper = ConfigureMapper.getProperty(ip_addr);
                if (!ConfigureMapper.isEmpty(new Object[]{mapper})) {
                    String[] mappingIp = mapper.split(":");
                    ip_addr = mappingIp[0];
                    port = Integer.parseInt(mappingIp[1]);
                }

                results[i] = new StorageServer(ip_addr, port, store_path);
            }

            return results;
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            } finally {
                connection = null;
            }
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    /**
     * query storage server to download file
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name of storage server
     * @param filename      filename on storage server
     * @return storage server Socket object, return null if fail
     */
    public StorageServer getFetchStorage(TrackerServer trackerServer,
                                         String groupName, String filename) throws IOException, MyException {
        ServerInfo[] servers = this.getStorages(trackerServer, ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE,
                groupName, filename);
        if (servers == null) {
            return null;
        } else {
            return new StorageServer(servers[0].getIpAddr(), servers[0].getPort(), 0);
        }
    }

    /**
     * query storage server to update file (delete file or set meta data)
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name of storage server
     * @param filename      filename on storage server
     * @return storage server Socket object, return null if fail
     */
    public StorageServer getUpdateStorage(TrackerServer trackerServer,
                                          String groupName, String filename) throws IOException, MyException {
        ServerInfo[] servers = this.getStorages(trackerServer, ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE,
                groupName, filename);
        if (servers == null) {
            return null;
        } else {
            return new StorageServer(servers[0].getIpAddr(), servers[0].getPort(), 0);
        }
    }

    /**
     * get storage servers to download file
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name of storage server
     * @param filename      filename on storage server
     * @return storage servers, return null if fail
     */
    public ServerInfo[] getFetchStorages(TrackerServer trackerServer,
                                         String groupName, String filename) throws IOException, MyException {
        return this.getStorages(trackerServer, ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL,
                groupName, filename);
    }

    /**
     * query storage server to download file
     *
     * @param trackerServer the tracker server
     * @param cmd           command code, ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE or
     *                      ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE
     * @param groupName     the group name of storage server
     * @param filename      filename on storage server
     * @return storage server Socket object, return null if fail
     */
    protected ServerInfo[] getStorages(TrackerServer trackerServer,
                                       byte cmd, String groupName, String filename) throws IOException, MyException {
        byte[] header;
        byte[] bFileName;
        byte[] bGroupName;
        byte[] bs;
        int len;
        String ip_addr;
        int port;
        Connection connection = getConnection(trackerServer);
        OutputStream out = connection.getOutputStream();

        try {
            bs = groupName.getBytes(ClientGlobal.g_charset);
            bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];
            bFileName = filename.getBytes(ClientGlobal.g_charset);

            if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN) {
                len = bs.length;
            } else {
                len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
            }
            Arrays.fill(bGroupName, (byte) 0);
            System.arraycopy(bs, 0, bGroupName, 0, len);

            header = ProtoCommon.packHeader(cmd, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + bFileName.length, (byte) 0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + bFileName.length];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            System.arraycopy(bFileName, 0, wholePkg, header.length + bGroupName.length, bFileName.length);
            out.write(wholePkg);

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                return null;
            }

            int server_count = 1;
            int ip_size;
            if (pkgInfo.body.length < ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV4_BODY_LEN) {
                this.errno = ProtoCommon.ERR_NO_EINVAL;
                throw new IOException("Invalid body length: " + pkgInfo.body.length);
            }

            if ((pkgInfo.body.length >= ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV6_BODY_LEN) &&
                    (pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV6_BODY_LEN) %
                    (ProtoCommon.FDFS_IPV6_SIZE - 1) == 0)
            {
                ip_size = ProtoCommon.FDFS_IPV6_SIZE;
                server_count += (pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV6_BODY_LEN) /
                    (ProtoCommon.FDFS_IPV6_SIZE - 1);
            } else if ((pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV4_BODY_LEN) %
                    (ProtoCommon.FDFS_IPV4_SIZE - 1) == 0)
            {
                ip_size = ProtoCommon.FDFS_IPV4_SIZE;
                server_count += (pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_IPV4_BODY_LEN) /
                    (ProtoCommon.FDFS_IPV4_SIZE - 1);
            } else {
                this.errno = ProtoCommon.ERR_NO_EINVAL;
                throw new IOException("Invalid body length: " + pkgInfo.body.length);
            }

            ip_addr = new String(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN, ip_size - 1).trim();
            int offset = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ip_size - 1;

            port = (int) ProtoCommon.buff2long(pkgInfo.body, offset);
            offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

            ServerInfo[] servers = new ServerInfo[server_count];
            servers[0] = new ServerInfo(ip_addr, port);
            for (int i = 1; i < server_count; i++) {
                servers[i] = new ServerInfo(new String(pkgInfo.body, offset, ip_size - 1).trim(), port);
                offset += ip_size - 1;
            }

            return servers;
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            } finally {
                connection = null;
            }

            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    /**
     * query storage server to download file
     *
     * @param trackerServer the tracker server
     * @param file_id       the file id(including group name and filename)
     * @return storage server Socket object, return null if fail
     */
    public StorageServer getFetchStorage1(TrackerServer trackerServer, String file_id) throws IOException, MyException {
        String[] parts = new String[2];
        this.errno = StorageClient1.split_file_id(file_id, parts);
        if (this.errno != 0) {
            return null;
        }

        return this.getFetchStorage(trackerServer, parts[0], parts[1]);
    }

    /**
     * get storage servers to download file
     *
     * @param trackerServer the tracker server
     * @param file_id       the file id(including group name and filename)
     * @return storage servers, return null if fail
     */
    public ServerInfo[] getFetchStorages1(TrackerServer trackerServer, String file_id) throws IOException, MyException {
        String[] parts = new String[2];
        this.errno = StorageClient1.split_file_id(file_id, parts);
        if (this.errno != 0) {
            return null;
        }

        return this.getFetchStorages(trackerServer, parts[0], parts[1]);
    }

    /**
     * list groups
     *
     * @param trackerServer the tracker server
     * @return group stat array, return null if fail
     */
    public StructGroupStat[] listGroups(TrackerServer trackerServer) throws IOException, MyException {
        byte[] header;
        String ip_addr;
        int port;
        byte cmd;
        int out_len;
        byte store_path;
        Connection connection = getConnection(trackerServer);
        OutputStream out = connection.getOutputStream();

        try {
            header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_SERVER_LIST_GROUP, 0, (byte) 0);
            out.write(header);

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                return null;
            }

            ProtoStructDecoder<StructGroupStat> decoder = new ProtoStructDecoder<StructGroupStat>();
            return decoder.decode(pkgInfo.body, StructGroupStat.class, StructGroupStat.getFieldsTotalSize());
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            } finally {
                connection = null;
            }
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.errno = ProtoCommon.ERR_NO_EINVAL;
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    /**
     * query storage server stat info of the group
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name of storage server
     * @return storage server stat array, return null if fail
     */
    public StructStorageStat[] listStorages(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        final String storageIpAddr = null;
        return this.listStorages(trackerServer, groupName, storageIpAddr);
    }

    private int getIpaddrLength(String ip, byte[] bIpAddr) {
        if (bIpAddr.length < ProtoCommon.FDFS_IPV4_SIZE) {
            return bIpAddr.length;
        } else if (ip.indexOf(':') >= 0) {  //IPv6 address
            if (bIpAddr.length < ProtoCommon.FDFS_IPV6_SIZE) {
                return bIpAddr.length;
            } else {
                return ProtoCommon.FDFS_IPV6_SIZE - 1;
            }
        } else {  //IPv4 address
            return ProtoCommon.FDFS_IPV4_SIZE - 1;
        }
    }

    /**
     * query storage server stat info of the group
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name of storage server
     * @param storageIpAddr the storage server ip address, can be null or empty
     * @return storage server stat array, return null if fail
     */
    public StructStorageStat[] listStorages(TrackerServer trackerServer, String groupName,
            String storageIpAddr) throws IOException, MyException {
        byte[] header;
        byte[] bGroupName;
        byte[] bs;
        int len;
        Connection connection = getConnection(trackerServer);
        OutputStream out = connection.getOutputStream();

        try {
            bs = groupName.getBytes(ClientGlobal.g_charset);
            bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];

            if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN) {
                len = bs.length;
            } else {
                len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
            }
            Arrays.fill(bGroupName, (byte) 0);
            System.arraycopy(bs, 0, bGroupName, 0, len);

            int ipAddrLen;
            byte[] bIpAddr;
            if (storageIpAddr != null && storageIpAddr.length() > 0) {
                bIpAddr = storageIpAddr.getBytes(ClientGlobal.g_charset);
                ipAddrLen = getIpaddrLength(storageIpAddr, bIpAddr);
            } else {
                bIpAddr = null;
                ipAddrLen = 0;
            }

            header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ipAddrLen, (byte) 0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + ipAddrLen];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            if (ipAddrLen > 0) {
                System.arraycopy(bIpAddr, 0, wholePkg, header.length + bGroupName.length, ipAddrLen);
            }
            out.write(wholePkg);

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                return null;
            }

            ProtoStructDecoder<StructStorageStat> decoder = new ProtoStructDecoder<StructStorageStat>();
            return decoder.decode(pkgInfo.body, StructStorageStat.class,
                    StructStorageStat.getFieldsTotalSize());
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }

            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.errno = ProtoCommon.ERR_NO_EINVAL;
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    /**
     * delete a storage server from the tracker server
     *
     * @param trackerServer the connected tracker server
     * @param groupName     the group name of storage server
     * @param storageIpAddr the storage server ip address
     * @return true for success, false for fail
     */
    private boolean deleteStorage(TrackerServer trackerServer,
                                  String groupName, String storageIpAddr) throws IOException, MyException {
        byte[] header;
        byte[] bGroupName;
        byte[] bs;
        int len;
        Connection connection;

        connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        try {
            bs = groupName.getBytes(ClientGlobal.g_charset);
            bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];

            if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN) {
                len = bs.length;
            } else {
                len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
            }
            Arrays.fill(bGroupName, (byte) 0);
            System.arraycopy(bs, 0, bGroupName, 0, len);

            byte[] bIpAddr = storageIpAddr.getBytes(ClientGlobal.g_charset);
            int ipAddrLen = getIpaddrLength(storageIpAddr, bIpAddr);
            header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ipAddrLen, (byte) 0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + ipAddrLen];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            System.arraycopy(bIpAddr, 0, wholePkg, header.length + bGroupName.length, ipAddrLen);
            out.write(wholePkg);

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(),
                    ProtoCommon.TRACKER_PROTO_CMD_RESP, 0);
            this.errno = pkgInfo.errno;
            return pkgInfo.errno == 0;
        } catch (IOException e) {
            try {
                connection.close();
            } finally {
                connection = null;
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.release();
            }
        }
    }

    /**
     * delete a storage server from the global FastDFS cluster
     *
     * @param groupName     the group name of storage server
     * @param storageIpAddr the storage server ip address
     * @return true for success, false for fail
     */
    public boolean deleteStorage(String groupName, String storageIpAddr) throws IOException, MyException {
        return this.deleteStorage(ClientGlobal.g_tracker_group, groupName, storageIpAddr);
    }

    /**
     * delete a storage server from the FastDFS cluster
     *
     * @param trackerGroup  the tracker server group
     * @param groupName     the group name of storage server
     * @param storageIpAddr the storage server ip address
     * @return true for success, false for fail
     */
    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName,
            String storageIpAddr) throws IOException, MyException
    {
        int serverIndex;
        int notFoundCount;
        TrackerServer trackerServer;

        notFoundCount = 0;
        for (serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; serverIndex++) {
            try {
                trackerServer = trackerGroup.getTrackerServer(serverIndex);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                this.errno = ProtoCommon.ECONNREFUSED;
                return false;
            }
            StructStorageStat[] storageStats = listStorages(trackerServer, groupName, storageIpAddr);
            if (storageStats == null) {
                if (this.errno == ProtoCommon.ERR_NO_ENOENT) {
                    notFoundCount++;
                } else {
                    return false;
                }
            } else if (storageStats.length == 0) {
                notFoundCount++;
            } else if (storageStats[0].getStatus() == ProtoCommon.FDFS_STORAGE_STATUS_ONLINE ||
                    storageStats[0].getStatus() == ProtoCommon.FDFS_STORAGE_STATUS_ACTIVE) {
                this.errno = ProtoCommon.ERR_NO_EBUSY;
                return false;
            }

        }

        if (notFoundCount == trackerGroup.tracker_servers.length) {
            this.errno = ProtoCommon.ERR_NO_ENOENT;
            return false;
        }

        notFoundCount = 0;
        for (serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; serverIndex++) {
            try {
                trackerServer = trackerGroup.getTrackerServer(serverIndex);
            } catch (IOException ex) {
                System.err.println("connect to server " + trackerGroup.tracker_servers[serverIndex].getAddress().getHostAddress() + ":" + trackerGroup.tracker_servers[serverIndex].getPort() + " fail");
                ex.printStackTrace(System.err);
                this.errno = ProtoCommon.ECONNREFUSED;
                return false;
            }
            if (!this.deleteStorage(trackerServer, groupName, storageIpAddr)) {
                if (this.errno != 0) {
                    if (this.errno == ProtoCommon.ERR_NO_ENOENT) {
                        notFoundCount++;
                    } else if (this.errno != ProtoCommon.ERR_NO_EALREADY) {
                        return false;
                    }
                }
            }

        }

        if (notFoundCount == trackerGroup.tracker_servers.length) {
            this.errno = ProtoCommon.ERR_NO_ENOENT;
            return false;
        }

        if (this.errno == ProtoCommon.ERR_NO_ENOENT) {
            this.errno = 0;
        }

        return this.errno == 0;
    }

    /**
     * query storage server to upload file
     *
     * @param trackerServer the tracker server
     * @param groupName     the group name to upload file to, can be empty
     * @return storage server object, return null if fail
     */
    public StringBuilder fetchStorageIds(boolean haveAllowEmptyField) throws IOException, MyException {
        byte[] header;
        int offset = 0;
        int length;
        int total_count;
        int current_count;

        Connection connection = getConnection(null);
        try {
            int reqBodyLength;
            OutputStream out = connection.getOutputStream();
            StringBuilder builder = new StringBuilder();

            if (haveAllowEmptyField) {
                reqBodyLength = 5;
            } else {
                reqBodyLength = 4;
            }
            header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_FETCH_STORAGE_IDS, reqBodyLength, (byte)0);
            byte[] wholePkg = new byte[header.length + reqBodyLength];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            if (haveAllowEmptyField) {
                wholePkg[wholePkg.length - 1] = 1;
            }
            do {
                byte[] bs = ProtoCommon.int2buff(offset);
                System.arraycopy(bs, 0, wholePkg, header.length, bs.length);
                out.write(wholePkg);

                ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(
                        connection.getInputStream(),
                        ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
                this.errno = pkgInfo.errno;
                if (pkgInfo.errno != 0) {
                    return null;
                }

                if (pkgInfo.body.length < 8) {
                    throw new MyException("invalid body length: " + pkgInfo.body.length);
                }

                total_count = ProtoCommon.buff2int(pkgInfo.body, 0);
                current_count = ProtoCommon.buff2int(pkgInfo.body, 4);
                if (current_count < 0) {
                    throw new MyException("invalid current count: " + current_count);
                }

                length = pkgInfo.body.length - 8;
                if (length == 0) {
                    break;
                }

                bs = new byte[length];
                System.arraycopy(pkgInfo.body, 8, bs, 0, length);
                builder.append(new String(bs, ClientGlobal.g_charset));

                offset += current_count;
            } while (offset < total_count);

            return builder;
        } catch (IOException e) {
            try {
                connection.close();
            } finally {
                connection = null;
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.release();
            }
        }
    }

}
