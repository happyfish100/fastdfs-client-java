/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 */

package org.csource.fastdfs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Tracker client
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
  public TrackerServer getConnection() throws IOException {
    return this.tracker_group.getConnection();
  }

  /**
   * query storage server to upload file
   *
   * @param trackerServer the tracker server
   * @return storage server Socket object, return null if fail
   */
  public StorageServer getStoreStorage(TrackerServer trackerServer) throws IOException {
    final String groupName = null;
    return this.getStoreStorage(trackerServer, groupName);
  }

  /**
   * query storage server to upload file
   *
   * @param trackerServer the tracker server
   * @param groupName     the group name to upload file to, can be empty
   * @return storage server object, return null if fail
   */
  public StorageServer getStoreStorage(TrackerServer trackerServer, String groupName) throws IOException {
    byte[] header;
    String ip_addr;
    int port;
    byte cmd;
    int out_len;
    boolean bNewConnection;
    byte store_path;
    Socket trackerSocket;

    if (trackerServer == null) {
      trackerServer = getConnection();
      if (trackerServer == null) {
        return null;
      }
      bNewConnection = true;
    } else {
      bNewConnection = false;
    }

    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

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

      ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
        ProtoCommon.TRACKER_PROTO_CMD_RESP,
        ProtoCommon.TRACKER_QUERY_STORAGE_STORE_BODY_LEN);
      this.errno = pkgInfo.errno;
      if (pkgInfo.errno != 0) {
        return null;
      }

      ip_addr = new String(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN, ProtoCommon.FDFS_IPADDR_SIZE - 1).trim();

      port = (int) ProtoCommon.buff2long(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN
        + ProtoCommon.FDFS_IPADDR_SIZE - 1);
      store_path = pkgInfo.body[ProtoCommon.TRACKER_QUERY_STORAGE_STORE_BODY_LEN - 1];

      return new StorageServer(ip_addr, port, store_path);
    } catch (IOException ex) {
      if (!bNewConnection) {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }

      throw ex;
    } finally {
      if (bNewConnection) {
        try {
          trackerServer.close();
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
  public StorageServer[] getStoreStorages(TrackerServer trackerServer, String groupName) throws IOException {
    byte[] header;
    String ip_addr;
    int port;
    byte cmd;
    int out_len;
    boolean bNewConnection;
    Socket trackerSocket;

    if (trackerServer == null) {
      trackerServer = getConnection();
      if (trackerServer == null) {
        return null;
      }
      bNewConnection = true;
    } else {
      bNewConnection = false;
    }

    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

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

      ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
        ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
      this.errno = pkgInfo.errno;
      if (pkgInfo.errno != 0) {
        return null;
      }

      if (pkgInfo.body.length < ProtoCommon.TRACKER_QUERY_STORAGE_STORE_BODY_LEN) {
        this.errno = ProtoCommon.ERR_NO_EINVAL;
        return null;
      }

      int ipPortLen = pkgInfo.body.length - (ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1);
      final int recordLength = ProtoCommon.FDFS_IPADDR_SIZE - 1 + ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

      if (ipPortLen % recordLength != 0) {
        this.errno = ProtoCommon.ERR_NO_EINVAL;
        return null;
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
        ip_addr = new String(pkgInfo.body, offset, ProtoCommon.FDFS_IPADDR_SIZE - 1).trim();
        offset += ProtoCommon.FDFS_IPADDR_SIZE - 1;

        port = (int) ProtoCommon.buff2long(pkgInfo.body, offset);
        offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

        results[i] = new StorageServer(ip_addr, port, store_path);
      }

      return results;
    } catch (IOException ex) {
      if (!bNewConnection) {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }

      throw ex;
    } finally {
      if (bNewConnection) {
        try {
          trackerServer.close();
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
                                       String groupName, String filename) throws IOException {
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
                                        String groupName, String filename) throws IOException {
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
                                       String groupName, String filename) throws IOException {
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
                                     byte cmd, String groupName, String filename) throws IOException {
    byte[] header;
    byte[] bFileName;
    byte[] bGroupName;
    byte[] bs;
    int len;
    String ip_addr;
    int port;
    boolean bNewConnection;
    Socket trackerSocket;

    if (trackerServer == null) {
      trackerServer = getConnection();
      if (trackerServer == null) {
        return null;
      }
      bNewConnection = true;
    } else {
      bNewConnection = false;
    }
    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

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

      ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
        ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
      this.errno = pkgInfo.errno;
      if (pkgInfo.errno != 0) {
        return null;
      }

      if (pkgInfo.body.length < ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) {
        throw new IOException("Invalid body length: " + pkgInfo.body.length);
      }

      if ((pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) % (ProtoCommon.FDFS_IPADDR_SIZE - 1) != 0) {
        throw new IOException("Invalid body length: " + pkgInfo.body.length);
      }

      int server_count = 1 + (pkgInfo.body.length - ProtoCommon.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) / (ProtoCommon.FDFS_IPADDR_SIZE - 1);

      ip_addr = new String(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN, ProtoCommon.FDFS_IPADDR_SIZE - 1).trim();
      int offset = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ProtoCommon.FDFS_IPADDR_SIZE - 1;

      port = (int) ProtoCommon.buff2long(pkgInfo.body, offset);
      offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

      ServerInfo[] servers = new ServerInfo[server_count];
      servers[0] = new ServerInfo(ip_addr, port);
      for (int i = 1; i < server_count; i++) {
        servers[i] = new ServerInfo(new String(pkgInfo.body, offset, ProtoCommon.FDFS_IPADDR_SIZE - 1).trim(), port);
        offset += ProtoCommon.FDFS_IPADDR_SIZE - 1;
      }

      return servers;
    } catch (IOException ex) {
      if (!bNewConnection) {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }

      throw ex;
    } finally {
      if (bNewConnection) {
        try {
          trackerServer.close();
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
  public StorageServer getFetchStorage1(TrackerServer trackerServer, String file_id) throws IOException {
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
  public ServerInfo[] getFetchStorages1(TrackerServer trackerServer, String file_id) throws IOException {
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
  public StructGroupStat[] listGroups(TrackerServer trackerServer) throws IOException {
    byte[] header;
    String ip_addr;
    int port;
    byte cmd;
    int out_len;
    boolean bNewConnection;
    byte store_path;
    Socket trackerSocket;

    if (trackerServer == null) {
      trackerServer = getConnection();
      if (trackerServer == null) {
        return null;
      }
      bNewConnection = true;
    } else {
      bNewConnection = false;
    }

    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

    try {
      header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_SERVER_LIST_GROUP, 0, (byte) 0);
      out.write(header);

      ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
        ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
      this.errno = pkgInfo.errno;
      if (pkgInfo.errno != 0) {
        return null;
      }

      ProtoStructDecoder<StructGroupStat> decoder = new ProtoStructDecoder<StructGroupStat>();
      return decoder.decode(pkgInfo.body, StructGroupStat.class, StructGroupStat.getFieldsTotalSize());
    } catch (IOException ex) {
      if (!bNewConnection) {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }

      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace();
      this.errno = ProtoCommon.ERR_NO_EINVAL;
      return null;
    } finally {
      if (bNewConnection) {
        try {
          trackerServer.close();
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
  public StructStorageStat[] listStorages(TrackerServer trackerServer, String groupName) throws IOException {
    final String storageIpAddr = null;
    return this.listStorages(trackerServer, groupName, storageIpAddr);
  }

  /**
   * query storage server stat info of the group
   *
   * @param trackerServer the tracker server
   * @param groupName     the group name of storage server
   * @param storageIpAddr the storage server ip address, can be null or empty
   * @return storage server stat array, return null if fail
   */
  public StructStorageStat[] listStorages(TrackerServer trackerServer,
                                          String groupName, String storageIpAddr) throws IOException {
    byte[] header;
    byte[] bGroupName;
    byte[] bs;
    int len;
    boolean bNewConnection;
    Socket trackerSocket;

    if (trackerServer == null) {
      trackerServer = getConnection();
      if (trackerServer == null) {
        return null;
      }
      bNewConnection = true;
    } else {
      bNewConnection = false;
    }
    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

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
        if (bIpAddr.length < ProtoCommon.FDFS_IPADDR_SIZE) {
          ipAddrLen = bIpAddr.length;
        } else {
          ipAddrLen = ProtoCommon.FDFS_IPADDR_SIZE - 1;
        }
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

      ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
        ProtoCommon.TRACKER_PROTO_CMD_RESP, -1);
      this.errno = pkgInfo.errno;
      if (pkgInfo.errno != 0) {
        return null;
      }

      ProtoStructDecoder<StructStorageStat> decoder = new ProtoStructDecoder<StructStorageStat>();
      return decoder.decode(pkgInfo.body, StructStorageStat.class, StructStorageStat.getFieldsTotalSize());
    } catch (IOException ex) {
      if (!bNewConnection) {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }

      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace();
      this.errno = ProtoCommon.ERR_NO_EINVAL;
      return null;
    } finally {
      if (bNewConnection) {
        try {
          trackerServer.close();
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
                                String groupName, String storageIpAddr) throws IOException {
    byte[] header;
    byte[] bGroupName;
    byte[] bs;
    int len;
    Socket trackerSocket;

    trackerSocket = trackerServer.getSocket();
    OutputStream out = trackerSocket.getOutputStream();

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
    byte[] bIpAddr = storageIpAddr.getBytes(ClientGlobal.g_charset);
    if (bIpAddr.length < ProtoCommon.FDFS_IPADDR_SIZE) {
      ipAddrLen = bIpAddr.length;
    } else {
      ipAddrLen = ProtoCommon.FDFS_IPADDR_SIZE - 1;
    }

    header = ProtoCommon.packHeader(ProtoCommon.TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + ipAddrLen, (byte) 0);
    byte[] wholePkg = new byte[header.length + bGroupName.length + ipAddrLen];
    System.arraycopy(header, 0, wholePkg, 0, header.length);
    System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
    System.arraycopy(bIpAddr, 0, wholePkg, header.length + bGroupName.length, ipAddrLen);
    out.write(wholePkg);

    ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(),
      ProtoCommon.TRACKER_PROTO_CMD_RESP, 0);
    this.errno = pkgInfo.errno;
    return pkgInfo.errno == 0;
  }

  /**
   * delete a storage server from the global FastDFS cluster
   *
   * @param groupName     the group name of storage server
   * @param storageIpAddr the storage server ip address
   * @return true for success, false for fail
   */
  public boolean deleteStorage(String groupName, String storageIpAddr) throws IOException {
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
  public boolean deleteStorage(TrackerGroup trackerGroup,
                               String groupName, String storageIpAddr) throws IOException {
    int serverIndex;
    int notFoundCount;
    TrackerServer trackerServer;

    notFoundCount = 0;
    for (serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; serverIndex++) {
      try {
        trackerServer = trackerGroup.getConnection(serverIndex);
      } catch (IOException ex) {
        ex.printStackTrace(System.err);
        this.errno = ProtoCommon.ECONNREFUSED;
        return false;
      }

      try {
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
      } finally {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
        }
      }
    }

    if (notFoundCount == trackerGroup.tracker_servers.length) {
      this.errno = ProtoCommon.ERR_NO_ENOENT;
      return false;
    }

    notFoundCount = 0;
    for (serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; serverIndex++) {
      try {
        trackerServer = trackerGroup.getConnection(serverIndex);
      } catch (IOException ex) {
        System.err.println("connect to server " + trackerGroup.tracker_servers[serverIndex].getAddress().getHostAddress() + ":" + trackerGroup.tracker_servers[serverIndex].getPort() + " fail");
        ex.printStackTrace(System.err);
        this.errno = ProtoCommon.ECONNREFUSED;
        return false;
      }

      try {
        if (!this.deleteStorage(trackerServer, groupName, storageIpAddr)) {
          if (this.errno != 0) {
            if (this.errno == ProtoCommon.ERR_NO_ENOENT) {
              notFoundCount++;
            } else if (this.errno != ProtoCommon.ERR_NO_EALREADY) {
              return false;
            }
          }
        }
      } finally {
        try {
          trackerServer.close();
        } catch (IOException ex1) {
          ex1.printStackTrace();
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
}
