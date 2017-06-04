/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 */

package org.csource.fastdfs;

/**
 * C struct body decoder
 *
 * @author Happy Fish / YuQing
 * @version Version 1.18
 */
public class StructGroupStat extends StructBase {
  protected static final int FIELD_INDEX_GROUP_NAME = 0;
  protected static final int FIELD_INDEX_TOTAL_MB = 1;
  protected static final int FIELD_INDEX_FREE_MB = 2;
  protected static final int FIELD_INDEX_TRUNK_FREE_MB = 3;
  protected static final int FIELD_INDEX_STORAGE_COUNT = 4;
  protected static final int FIELD_INDEX_STORAGE_PORT = 5;
  protected static final int FIELD_INDEX_STORAGE_HTTP_PORT = 6;
  protected static final int FIELD_INDEX_ACTIVE_COUNT = 7;
  protected static final int FIELD_INDEX_CURRENT_WRITE_SERVER = 8;
  protected static final int FIELD_INDEX_STORE_PATH_COUNT = 9;
  protected static final int FIELD_INDEX_SUBDIR_COUNT_PER_PATH = 10;
  protected static final int FIELD_INDEX_CURRENT_TRUNK_FILE_ID = 11;

  protected static int fieldsTotalSize;
  protected static StructBase.FieldInfo[] fieldsArray = new StructBase.FieldInfo[12];

  static {
    int offset = 0;
    fieldsArray[FIELD_INDEX_GROUP_NAME] = new StructBase.FieldInfo("groupName", offset, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1);
    offset += ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1;

    fieldsArray[FIELD_INDEX_TOTAL_MB] = new StructBase.FieldInfo("totalMB", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_FREE_MB] = new StructBase.FieldInfo("freeMB", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_TRUNK_FREE_MB] = new StructBase.FieldInfo("trunkFreeMB", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_STORAGE_COUNT] = new StructBase.FieldInfo("storageCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_STORAGE_PORT] = new StructBase.FieldInfo("storagePort", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_STORAGE_HTTP_PORT] = new StructBase.FieldInfo("storageHttpPort", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_ACTIVE_COUNT] = new StructBase.FieldInfo("activeCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_CURRENT_WRITE_SERVER] = new StructBase.FieldInfo("currentWriteServer", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_STORE_PATH_COUNT] = new StructBase.FieldInfo("storePathCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_SUBDIR_COUNT_PER_PATH] = new StructBase.FieldInfo("subdirCountPerPath", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsArray[FIELD_INDEX_CURRENT_TRUNK_FILE_ID] = new StructBase.FieldInfo("currentTrunkFileId", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
    offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

    fieldsTotalSize = offset;
  }

  protected String groupName;  //name of this group
  protected long totalMB;      //total disk storage in MB
  protected long freeMB;       //free disk space in MB
  protected long trunkFreeMB;  //trunk free space in MB
  protected int storageCount;  //storage server count
  protected int storagePort;   //storage server port
  protected int storageHttpPort; //storage server HTTP port
  protected int activeCount;     //active storage server count
  protected int currentWriteServer; //current storage server index to upload file
  protected int storePathCount;     //store base path count of each storage server
  protected int subdirCountPerPath; //sub dir count per store path
  protected int currentTrunkFileId; //current trunk file id

  /**
   * get fields total size
   *
   * @return fields total size
   */
  public static int getFieldsTotalSize() {
    return fieldsTotalSize;
  }

  /**
   * get group name
   *
   * @return group name
   */
  public String getGroupName() {
    return this.groupName;
  }

  /**
   * get total disk space in MB
   *
   * @return total disk space in MB
   */
  public long getTotalMB() {
    return this.totalMB;
  }

  /**
   * get free disk space in MB
   *
   * @return free disk space in MB
   */
  public long getFreeMB() {
    return this.freeMB;
  }

  /**
   * get trunk free space in MB
   *
   * @return trunk free space in MB
   */
  public long getTrunkFreeMB() {
    return this.trunkFreeMB;
  }

  /**
   * get storage server count in this group
   *
   * @return storage server count in this group
   */
  public int getStorageCount() {
    return this.storageCount;
  }

  /**
   * get active storage server count in this group
   *
   * @return active storage server count in this group
   */
  public int getActiveCount() {
    return this.activeCount;
  }

  /**
   * get storage server port
   *
   * @return storage server port
   */
  public int getStoragePort() {
    return this.storagePort;
  }

  /**
   * get storage server HTTP port
   *
   * @return storage server HTTP port
   */
  public int getStorageHttpPort() {
    return this.storageHttpPort;
  }

  /**
   * get current storage server index to upload file
   *
   * @return current storage server index to upload file
   */
  public int getCurrentWriteServer() {
    return this.currentWriteServer;
  }

  /**
   * get store base path count of each storage server
   *
   * @return store base path count of each storage server
   */
  public int getStorePathCount() {
    return this.storePathCount;
  }

  /**
   * get sub dir count per store path
   *
   * @return sub dir count per store path
   */
  public int getSubdirCountPerPath() {
    return this.subdirCountPerPath;
  }

  /**
   * get current trunk file id
   *
   * @return current trunk file id
   */
  public int getCurrentTrunkFileId() {
    return this.currentTrunkFileId;
  }

  /**
   * set fields
   *
   * @param bs     byte array
   * @param offset start offset
   */
  public void setFields(byte[] bs, int offset) {
    this.groupName = stringValue(bs, offset, fieldsArray[FIELD_INDEX_GROUP_NAME]);
    this.totalMB = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_MB]);
    this.freeMB = longValue(bs, offset, fieldsArray[FIELD_INDEX_FREE_MB]);
    this.trunkFreeMB = longValue(bs, offset, fieldsArray[FIELD_INDEX_TRUNK_FREE_MB]);
    this.storageCount = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORAGE_COUNT]);
    this.storagePort = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORAGE_PORT]);
    this.storageHttpPort = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORAGE_HTTP_PORT]);
    this.activeCount = intValue(bs, offset, fieldsArray[FIELD_INDEX_ACTIVE_COUNT]);
    this.currentWriteServer = intValue(bs, offset, fieldsArray[FIELD_INDEX_CURRENT_WRITE_SERVER]);
    this.storePathCount = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORE_PATH_COUNT]);
    this.subdirCountPerPath = intValue(bs, offset, fieldsArray[FIELD_INDEX_SUBDIR_COUNT_PER_PATH]);
    this.currentTrunkFileId = intValue(bs, offset, fieldsArray[FIELD_INDEX_CURRENT_TRUNK_FILE_ID]);
  }
}
