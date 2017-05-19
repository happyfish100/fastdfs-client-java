/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package org.csource.fastdfs;

import java.util.Date;

/**
* C struct body decoder
* @author Happy Fish / YuQing
* @version Version 1.25
*/
public class StructStorageStat extends StructBase
{
	protected static final int FIELD_INDEX_STATUS                   = 0;
	protected static final int FIELD_INDEX_ID                       = 1;
	protected static final int FIELD_INDEX_IP_ADDR                  = 2;
	protected static final int FIELD_INDEX_DOMAIN_NAME              = 3;
	protected static final int FIELD_INDEX_SRC_IP_ADDR              = 4;
	protected static final int FIELD_INDEX_VERSION                  = 5;
	protected static final int FIELD_INDEX_JOIN_TIME                = 6;
	protected static final int FIELD_INDEX_UP_TIME                  = 7;
	protected static final int FIELD_INDEX_TOTAL_MB                 = 8;
	protected static final int FIELD_INDEX_FREE_MB                  = 9;
	protected static final int FIELD_INDEX_UPLOAD_PRIORITY          = 10;
	protected static final int FIELD_INDEX_STORE_PATH_COUNT         = 11;
	protected static final int FIELD_INDEX_SUBDIR_COUNT_PER_PATH    = 12;
	protected static final int FIELD_INDEX_CURRENT_WRITE_PATH       = 13;
	protected static final int FIELD_INDEX_STORAGE_PORT             = 14;
	protected static final int FIELD_INDEX_STORAGE_HTTP_PORT        = 15;

	protected static final int FIELD_INDEX_CONNECTION_ALLOC_COUNT   = 16;
	protected static final int FIELD_INDEX_CONNECTION_CURRENT_COUNT = 17;
	protected static final int FIELD_INDEX_CONNECTION_MAX_COUNT     = 18;

	protected static final int FIELD_INDEX_TOTAL_UPLOAD_COUNT       = 19;
	protected static final int FIELD_INDEX_SUCCESS_UPLOAD_COUNT     = 20;
	protected static final int FIELD_INDEX_TOTAL_APPEND_COUNT       = 21;
	protected static final int FIELD_INDEX_SUCCESS_APPEND_COUNT     = 22;
	protected static final int FIELD_INDEX_TOTAL_MODIFY_COUNT       = 23;
	protected static final int FIELD_INDEX_SUCCESS_MODIFY_COUNT     = 24;
	protected static final int FIELD_INDEX_TOTAL_TRUNCATE_COUNT     = 25;
	protected static final int FIELD_INDEX_SUCCESS_TRUNCATE_COUNT   = 26;
	protected static final int FIELD_INDEX_TOTAL_SET_META_COUNT     = 27;
	protected static final int FIELD_INDEX_SUCCESS_SET_META_COUNT   = 28;
	protected static final int FIELD_INDEX_TOTAL_DELETE_COUNT       = 29;
	protected static final int FIELD_INDEX_SUCCESS_DELETE_COUNT     = 30;
	protected static final int FIELD_INDEX_TOTAL_DOWNLOAD_COUNT     = 31;
	protected static final int FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT   = 32;
	protected static final int FIELD_INDEX_TOTAL_GET_META_COUNT     = 33;
	protected static final int FIELD_INDEX_SUCCESS_GET_META_COUNT   = 34;
	protected static final int FIELD_INDEX_TOTAL_CREATE_LINK_COUNT  = 35;
	protected static final int FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT= 36;
	protected static final int FIELD_INDEX_TOTAL_DELETE_LINK_COUNT  = 37;
	protected static final int FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT= 38;
	protected static final int FIELD_INDEX_TOTAL_UPLOAD_BYTES       = 39;
	protected static final int FIELD_INDEX_SUCCESS_UPLOAD_BYTES     = 40;
	protected static final int FIELD_INDEX_TOTAL_APPEND_BYTES       = 41;
	protected static final int FIELD_INDEX_SUCCESS_APPEND_BYTES     = 42;
	protected static final int FIELD_INDEX_TOTAL_MODIFY_BYTES       = 43;
	protected static final int FIELD_INDEX_SUCCESS_MODIFY_BYTES     = 44;
	protected static final int FIELD_INDEX_TOTAL_DOWNLOAD_BYTES     = 45;
	protected static final int FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES   = 46;
	protected static final int FIELD_INDEX_TOTAL_SYNC_IN_BYTES      = 47;
	protected static final int FIELD_INDEX_SUCCESS_SYNC_IN_BYTES    = 48;
	protected static final int FIELD_INDEX_TOTAL_SYNC_OUT_BYTES     = 49;
	protected static final int FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES   = 50;
	protected static final int FIELD_INDEX_TOTAL_FILE_OPEN_COUNT    = 51;
	protected static final int FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT  = 52;
	protected static final int FIELD_INDEX_TOTAL_FILE_READ_COUNT    = 53;
	protected static final int FIELD_INDEX_SUCCESS_FILE_READ_COUNT  = 54;
	protected static final int FIELD_INDEX_TOTAL_FILE_WRITE_COUNT   = 55;
	protected static final int FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT = 56;
	protected static final int FIELD_INDEX_LAST_SOURCE_UPDATE       = 57;
	protected static final int FIELD_INDEX_LAST_SYNC_UPDATE         = 58;
	protected static final int FIELD_INDEX_LAST_SYNCED_TIMESTAMP    = 59;
	protected static final int FIELD_INDEX_LAST_HEART_BEAT_TIME     = 60;
	protected static final int FIELD_INDEX_IF_TRUNK_FILE            = 61;
	
	protected static int fieldsTotalSize;
	protected static StructBase.FieldInfo[] fieldsArray = new StructBase.FieldInfo[62];
	
	static
	{
		int offset = 0;
		
		fieldsArray[FIELD_INDEX_STATUS] = new StructBase.FieldInfo("status", offset, 1);
		offset += 1;
		
		fieldsArray[FIELD_INDEX_ID] = new StructBase.FieldInfo("id", offset, ProtoCommon.FDFS_STORAGE_ID_MAX_SIZE);
		offset += ProtoCommon.FDFS_STORAGE_ID_MAX_SIZE;
		
		fieldsArray[FIELD_INDEX_IP_ADDR] = new StructBase.FieldInfo("ipAddr", offset, ProtoCommon.FDFS_IPADDR_SIZE);
		offset += ProtoCommon.FDFS_IPADDR_SIZE;
		
		fieldsArray[FIELD_INDEX_DOMAIN_NAME] = new StructBase.FieldInfo("domainName", offset, ProtoCommon.FDFS_DOMAIN_NAME_MAX_SIZE);
		offset += ProtoCommon.FDFS_DOMAIN_NAME_MAX_SIZE;

		fieldsArray[FIELD_INDEX_SRC_IP_ADDR] = new StructBase.FieldInfo("srcIpAddr", offset, ProtoCommon.FDFS_IPADDR_SIZE);
		offset += ProtoCommon.FDFS_IPADDR_SIZE;
				
		fieldsArray[FIELD_INDEX_VERSION] = new StructBase.FieldInfo("version", offset, ProtoCommon.FDFS_VERSION_SIZE);
		offset += ProtoCommon.FDFS_VERSION_SIZE;
		
		fieldsArray[FIELD_INDEX_JOIN_TIME] = new StructBase.FieldInfo("joinTime", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_UP_TIME] = new StructBase.FieldInfo("upTime", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_MB] = new StructBase.FieldInfo("totalMB", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_FREE_MB] = new StructBase.FieldInfo("freeMB", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_UPLOAD_PRIORITY] = new StructBase.FieldInfo("uploadPriority", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_STORE_PATH_COUNT] = new StructBase.FieldInfo("storePathCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUBDIR_COUNT_PER_PATH] = new StructBase.FieldInfo("subdirCountPerPath", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_CURRENT_WRITE_PATH] = new StructBase.FieldInfo("currentWritePath", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_STORAGE_PORT] = new StructBase.FieldInfo("storagePort", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_STORAGE_HTTP_PORT] = new StructBase.FieldInfo("storageHttpPort", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_CONNECTION_ALLOC_COUNT] = new StructBase.FieldInfo("connectionAllocCount", offset, 4);
		offset += 4;

		fieldsArray[FIELD_INDEX_CONNECTION_CURRENT_COUNT] = new StructBase.FieldInfo("connectionCurrentCount", offset, 4);
		offset += 4;

		fieldsArray[FIELD_INDEX_CONNECTION_MAX_COUNT] = new StructBase.FieldInfo("connectionMaxCount", offset, 4);
		offset += 4;

		fieldsArray[FIELD_INDEX_TOTAL_UPLOAD_COUNT] = new StructBase.FieldInfo("totalUploadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_SUCCESS_UPLOAD_COUNT] = new StructBase.FieldInfo("successUploadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_APPEND_COUNT] = new StructBase.FieldInfo("totalAppendCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_SUCCESS_APPEND_COUNT] = new StructBase.FieldInfo("successAppendCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_MODIFY_COUNT] = new StructBase.FieldInfo("totalModifyCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_SUCCESS_MODIFY_COUNT] = new StructBase.FieldInfo("successModifyCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_TRUNCATE_COUNT] = new StructBase.FieldInfo("totalTruncateCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_SUCCESS_TRUNCATE_COUNT] = new StructBase.FieldInfo("successTruncateCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_SET_META_COUNT] = new StructBase.FieldInfo("totalSetMetaCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_SET_META_COUNT] = new StructBase.FieldInfo("successSetMetaCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_DELETE_COUNT] = new StructBase.FieldInfo("totalDeleteCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_DELETE_COUNT] = new StructBase.FieldInfo("successDeleteCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_DOWNLOAD_COUNT] = new StructBase.FieldInfo("totalDownloadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT] = new StructBase.FieldInfo("successDownloadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_GET_META_COUNT] = new StructBase.FieldInfo("totalGetMetaCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_GET_META_COUNT] = new StructBase.FieldInfo("successGetMetaCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_CREATE_LINK_COUNT] = new StructBase.FieldInfo("totalCreateLinkCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT] = new StructBase.FieldInfo("successCreateLinkCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_DELETE_LINK_COUNT] = new StructBase.FieldInfo("totalDeleteLinkCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT] = new StructBase.FieldInfo("successDeleteLinkCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
				
		fieldsArray[FIELD_INDEX_TOTAL_UPLOAD_BYTES] = new StructBase.FieldInfo("totalUploadBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_UPLOAD_BYTES] = new StructBase.FieldInfo("successUploadBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_APPEND_BYTES] = new StructBase.FieldInfo("totalAppendBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_APPEND_BYTES] = new StructBase.FieldInfo("successAppendBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_MODIFY_BYTES] = new StructBase.FieldInfo("totalModifyBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_MODIFY_BYTES] = new StructBase.FieldInfo("successModifyBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_DOWNLOAD_BYTES] = new StructBase.FieldInfo("totalDownloadloadBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES] = new StructBase.FieldInfo("successDownloadloadBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_SYNC_IN_BYTES] = new StructBase.FieldInfo("totalSyncInBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_SYNC_IN_BYTES] = new StructBase.FieldInfo("successSyncInBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_SYNC_OUT_BYTES] = new StructBase.FieldInfo("totalSyncOutBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES] = new StructBase.FieldInfo("successSyncOutBytes", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_FILE_OPEN_COUNT] = new StructBase.FieldInfo("totalFileOpenCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT] = new StructBase.FieldInfo("successFileOpenCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;

		fieldsArray[FIELD_INDEX_TOTAL_FILE_READ_COUNT] = new StructBase.FieldInfo("totalFileReadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_FILE_READ_COUNT] = new StructBase.FieldInfo("successFileReadCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_TOTAL_FILE_WRITE_COUNT] = new StructBase.FieldInfo("totalFileWriteCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT] = new StructBase.FieldInfo("successFileWriteCount", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
        
		fieldsArray[FIELD_INDEX_LAST_SOURCE_UPDATE] = new StructBase.FieldInfo("lastSourceUpdate", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_LAST_SYNC_UPDATE] = new StructBase.FieldInfo("lastSyncUpdate", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_LAST_SYNCED_TIMESTAMP] = new StructBase.FieldInfo("lastSyncedTimestamp", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_LAST_HEART_BEAT_TIME] = new StructBase.FieldInfo("lastHeartBeatTime", offset, ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE);
		offset += ProtoCommon.FDFS_PROTO_PKG_LEN_SIZE;
		
		fieldsArray[FIELD_INDEX_IF_TRUNK_FILE] = new StructBase.FieldInfo("ifTrunkServer", offset, 1);
		offset += 1;
		
		fieldsTotalSize = offset;
	}
	
	protected byte status;
	protected String id;
	protected String ipAddr;
	protected String srcIpAddr;
	protected String domainName; //http domain name
	protected String version;
	protected long totalMB; //total disk storage in MB
	protected long freeMB;  //free disk storage in MB
	protected int uploadPriority;  //upload priority
	protected Date joinTime; //storage join timestamp (create timestamp)
	protected Date upTime;   //storage service started timestamp
	protected int storePathCount;  //store base path count of each storage server
	protected int subdirCountPerPath;
	protected int storagePort;
	protected int storageHttpPort; //storage http server port
	protected int currentWritePath; //current write path index
	protected int connectionAllocCount;
	protected int connectionCurrentCount;
	protected int connectionMaxCount;
	protected long totalUploadCount;
	protected long successUploadCount;
	protected long totalAppendCount;
	protected long successAppendCount;
	protected long totalModifyCount;
	protected long successModifyCount;
	protected long totalTruncateCount;
	protected long successTruncateCount;
	protected long totalSetMetaCount;
	protected long successSetMetaCount;
	protected long totalDeleteCount;
	protected long successDeleteCount;
	protected long totalDownloadCount;
	protected long successDownloadCount;
	protected long totalGetMetaCount;
	protected long successGetMetaCount;
	protected long totalCreateLinkCount;
	protected long successCreateLinkCount;
	protected long totalDeleteLinkCount;
	protected long successDeleteLinkCount;
	protected long totalUploadBytes;
	protected long successUploadBytes;
	protected long totalAppendBytes;
	protected long successAppendBytes;
	protected long totalModifyBytes;
	protected long successModifyBytes;
	protected long totalDownloadloadBytes;
	protected long successDownloadloadBytes;
	protected long totalSyncInBytes;
	protected long successSyncInBytes;
	protected long totalSyncOutBytes;
	protected long successSyncOutBytes;
	protected long totalFileOpenCount;
	protected long successFileOpenCount;
	protected long totalFileReadCount;
	protected long successFileReadCount;
	protected long totalFileWriteCount;
	protected long successFileWriteCount;
	protected Date lastSourceUpdate;
	protected Date lastSyncUpdate;
	protected Date lastSyncedTimestamp;
	protected Date lastHeartBeatTime;
	protected boolean ifTrunkServer;
  
/**
* get storage status
* @return storage status
*/
  public byte getStatus()
  {
  	return this.status;
  }
  
/**
* get storage server id
* @return storage server id
*/
  public String getId()
  {
  	return this.id;
  }

/**
* get storage server ip address
* @return storage server ip address
*/
  public String getIpAddr()
  {
  	return this.ipAddr;
  }
  
/**
* get source storage ip address
* @return source storage ip address
*/
  public String getSrcIpAddr()
  {
  	return this.srcIpAddr;
  }
  
/**
* get the domain name of the storage server
* @return the domain name of the storage server
*/
  public String getDomainName()
  {
  	return this.domainName;
  }

/**
* get storage version
* @return storage version
*/
  public String getVersion()
  {
  	return this.version;
  }
  
/**
* get total disk space in MB
* @return total disk space in MB
*/
  public long getTotalMB()
  {
  	return this.totalMB;
  }
  
/**
* get free disk space in MB
* @return free disk space in MB
*/
  public long getFreeMB()
  {
  	return this.freeMB;
  }
	
/**
* get storage server upload priority
* @return storage server upload priority
*/
  public int getUploadPriority()
  {
  	return this.uploadPriority;
  }
  
/**
* get storage server join time
* @return storage server join time
*/
  public Date getJoinTime()
  {
  	return this.joinTime;
  }
  
/**
* get storage server up time
* @return storage server up time
*/
  public Date getUpTime()
  {
  	return this.upTime;
  }

/**
* get store base path count of each storage server
* @return store base path count of each storage server
*/
  public int getStorePathCount()
  {
  	return this.storePathCount;
  }
  
/**
* get sub dir count per store path
* @return sub dir count per store path
*/
  public int getSubdirCountPerPath()
  {
  	return this.subdirCountPerPath;
  }
  
/**
* get storage server port
* @return storage server port
*/
  public int getStoragePort()
  {
  	return this.storagePort;
  }
  
/**
* get storage server HTTP port
* @return storage server HTTP port
*/
  public int getStorageHttpPort()
  {
  	return this.storageHttpPort;
  }

/**
* get current write path index
* @return current write path index
*/
  public int getCurrentWritePath()
  {
  	return this.currentWritePath;
  }

/**
* get total upload file count
* @return total upload file count
*/
  public long getTotalUploadCount()
  {
  	return this.totalUploadCount;
  }

/**
* get success upload file count
* @return success upload file count
*/
  public long getSuccessUploadCount()
  {
  	return this.successUploadCount;
  }

/**
* get total append count
* @return total append count
*/
  public long getTotalAppendCount()
  {
  	return this.totalAppendCount;
  }

/**
* get success append count
* @return success append count
*/
  public long getSuccessAppendCount()
  {
  	return this.successAppendCount;
  }
  
/**
* get total modify count
* @return total modify count
*/
  public long getTotalModifyCount()
  {
  	return this.totalModifyCount;
  }

/**
* get success modify count
* @return success modify count
*/
  public long getSuccessModifyCount()
  {
  	return this.successModifyCount;
  }
  
/**
* get total truncate count
* @return total truncate count
*/
  public long getTotalTruncateCount()
  {
  	return this.totalTruncateCount;
  }

/**
* get success truncate count
* @return success truncate count
*/
  public long getSuccessTruncateCount()
  {
  	return this.successTruncateCount;
  }
  
/**
* get total set meta data count
* @return total set meta data count
*/
  public long getTotalSetMetaCount()
  {
  	return this.totalSetMetaCount;
  }
  
/**
* get success set meta data count
* @return success set meta data count
*/
  public long getSuccessSetMetaCount()
  {
  	return this.successSetMetaCount;
  }
  
/**
* get total delete file count
* @return total delete file count
*/
  public long getTotalDeleteCount()
  {
  	return this.totalDeleteCount;
  }

/**
* get success delete file count
* @return success delete file count
*/
  public long getSuccessDeleteCount()
  {
  	return this.successDeleteCount;
  }
  
/**
* get total download file count
* @return total download file count
*/
  public long getTotalDownloadCount()
  {
  	return this.totalDownloadCount;
  }

/**
* get success download file count
* @return success download file count
*/
  public long getSuccessDownloadCount()
  {
  	return this.successDownloadCount;
  }

/**
* get total get metadata count
* @return total get metadata count
*/
  public long getTotalGetMetaCount()
  {
  	return this.totalGetMetaCount;
  }

/**
* get success get metadata count
* @return success get metadata count
*/
  public long getSuccessGetMetaCount()
  {
  	return this.successGetMetaCount;
  }

/**
* get total create linke count
* @return total create linke count
*/
  public long getTotalCreateLinkCount()
  {
  	return this.totalCreateLinkCount;
  }

/**
* get success create linke count
* @return success create linke count
*/
  public long getSuccessCreateLinkCount()
  {
  	return this.successCreateLinkCount;
  }
  
/**
* get total delete link count
* @return total delete link count
*/
  public long getTotalDeleteLinkCount()
  {
  	return this.totalDeleteLinkCount;
  }
  
/**
* get success delete link count
* @return success delete link count
*/
  public long getSuccessDeleteLinkCount()
  {
  	return this.successDeleteLinkCount;
  }

/**
* get total upload file bytes
* @return total upload file bytes
*/
  public long getTotalUploadBytes()
  {
  	return this.totalUploadBytes;
  }
  
/**
* get success upload file bytes
* @return success upload file bytes
*/
  public long getSuccessUploadBytes()
  {
  	return this.successUploadBytes;
  }

/**
* get total append bytes
* @return total append bytes
*/
  public long getTotalAppendBytes()
  {
  	return this.totalAppendBytes;
  }
  
/**
* get success append bytes
* @return success append bytes
*/
  public long getSuccessAppendBytes()
  {
  	return this.successAppendBytes;
  }

/**
* get total modify bytes
* @return total modify bytes
*/
  public long getTotalModifyBytes()
  {
  	return this.totalModifyBytes;
  }
  
/**
* get success modify bytes
* @return success modify bytes
*/
  public long getSuccessModifyBytes()
  {
  	return this.successModifyBytes;
  }
  
/**
* get total download file bytes
* @return total download file bytes
*/
  public long getTotalDownloadloadBytes()
  {
  	return this.totalDownloadloadBytes;
  }
  
/**
* get success download file bytes
* @return success download file bytes
*/
  public long getSuccessDownloadloadBytes()
  {
  	return this.successDownloadloadBytes;
  }

/**
* get total sync in bytes
* @return total sync in bytes
*/
  public long getTotalSyncInBytes()
  {
  	return this.totalSyncInBytes;
  }
  
/**
* get success sync in bytes
* @return success sync in bytes
*/
  public long getSuccessSyncInBytes()
  {
  	return this.successSyncInBytes;
  }

/**
* get total sync out bytes
* @return total sync out bytes
*/
  public long getTotalSyncOutBytes()
  {
  	return this.totalSyncOutBytes;
  }
  
/**
* get success sync out bytes
* @return success sync out bytes
*/
  public long getSuccessSyncOutBytes()
  {
  	return this.successSyncOutBytes;
  }

/**
* get total file opened count
* @return total file opened bytes
*/
  public long getTotalFileOpenCount()
  {
  	return this.totalFileOpenCount;
  }
  
/**
* get success file opened count
* @return success file opened count
*/
  public long getSuccessFileOpenCount()
  {
  	return this.successFileOpenCount;
  }

/**
* get total file read count
* @return total file read bytes
*/
  public long getTotalFileReadCount()
  {
  	return this.totalFileReadCount;
  }
  
/**
* get success file read count
* @return success file read count
*/
  public long getSuccessFileReadCount()
  {
  	return this.successFileReadCount;
  }

/**
* get total file write count
* @return total file write bytes
*/
  public long getTotalFileWriteCount()
  {
  	return this.totalFileWriteCount;
  }
  
/**
* get success file write count
* @return success file write count
*/
  public long getSuccessFileWriteCount()
  {
  	return this.successFileWriteCount;
  }
  
/**
* get last source update timestamp
* @return last source update timestamp
*/
  public Date getLastSourceUpdate()
  {
  	return this.lastSourceUpdate;
  }

/**
* get last synced update timestamp
* @return last synced update timestamp
*/
  public Date getLastSyncUpdate()
  {
  	return this.lastSyncUpdate;
  }
  
/**
* get last synced timestamp
* @return last synced timestamp
*/
  public Date getLastSyncedTimestamp()
  {
  	return this.lastSyncedTimestamp;
  }

/**
* get last heart beat timestamp
* @return last heart beat timestamp
*/
  public Date getLastHeartBeatTime()
  {
  	return this.lastHeartBeatTime;
  }

/**
* if the trunk server
* @return true for the trunk server, otherwise false
*/
  public boolean isTrunkServer()
  {
  	return this.ifTrunkServer;
  }
  
/**
* get connection alloc count
* @return connection alloc count
*/
  public int getConnectionAllocCount()
  {
  	return this.connectionAllocCount;
  }

/**
* get connection current count
* @return connection current count
*/
  public int getConnectionCurrentCount()
  {
  	return this.connectionCurrentCount;
  }

/**
* get connection max count
* @return connection max count
*/
  public int getConnectionMaxCount()
  {
  	return this.connectionMaxCount;
  }

/**
* set fields
* @param bs byte array
* @param offset start offset
*/
	public void setFields(byte[] bs, int offset)
	{
		this.status = byteValue(bs, offset, fieldsArray[FIELD_INDEX_STATUS]);
		this.id = stringValue(bs, offset, fieldsArray[FIELD_INDEX_ID]);
		this.ipAddr = stringValue(bs, offset, fieldsArray[FIELD_INDEX_IP_ADDR]);
		this.srcIpAddr = stringValue(bs, offset, fieldsArray[FIELD_INDEX_SRC_IP_ADDR]);
		this.domainName = stringValue(bs, offset, fieldsArray[FIELD_INDEX_DOMAIN_NAME]);
		this.version = stringValue(bs, offset, fieldsArray[FIELD_INDEX_VERSION]);
		this.totalMB = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_MB]);
		this.freeMB = longValue(bs, offset, fieldsArray[FIELD_INDEX_FREE_MB]);
		this.uploadPriority = intValue(bs, offset, fieldsArray[FIELD_INDEX_UPLOAD_PRIORITY]);
		this.joinTime = dateValue(bs, offset, fieldsArray[FIELD_INDEX_JOIN_TIME]);
		this.upTime = dateValue(bs, offset, fieldsArray[FIELD_INDEX_UP_TIME]);
		this.storePathCount = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORE_PATH_COUNT]);
		this.subdirCountPerPath = intValue(bs, offset, fieldsArray[FIELD_INDEX_SUBDIR_COUNT_PER_PATH]);
		this.storagePort = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORAGE_PORT]);
		this.storageHttpPort = intValue(bs, offset, fieldsArray[FIELD_INDEX_STORAGE_HTTP_PORT]);
		this.currentWritePath = intValue(bs, offset, fieldsArray[FIELD_INDEX_CURRENT_WRITE_PATH]);

		this.connectionAllocCount = int32Value(bs, offset, fieldsArray[FIELD_INDEX_CONNECTION_ALLOC_COUNT]);
		this.connectionCurrentCount = int32Value(bs, offset, fieldsArray[FIELD_INDEX_CONNECTION_CURRENT_COUNT]);
		this.connectionMaxCount = int32Value(bs, offset, fieldsArray[FIELD_INDEX_CONNECTION_MAX_COUNT]);

		this.totalUploadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_UPLOAD_COUNT]);
		this.successUploadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_UPLOAD_COUNT]);
		this.totalAppendCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_APPEND_COUNT]);
		this.successAppendCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_APPEND_COUNT]);
		this.totalModifyCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_MODIFY_COUNT]);
		this.successModifyCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_MODIFY_COUNT]);
		this.totalTruncateCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_TRUNCATE_COUNT]);
		this.successTruncateCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_TRUNCATE_COUNT]);
		this.totalSetMetaCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_SET_META_COUNT]);
		this.successSetMetaCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_SET_META_COUNT]);
		this.totalDeleteCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_DELETE_COUNT]);
		this.successDeleteCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_DELETE_COUNT]);
		this.totalDownloadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_DOWNLOAD_COUNT]);
		this.successDownloadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT]);
		this.totalGetMetaCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_GET_META_COUNT]);
		this.successGetMetaCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_GET_META_COUNT]);
		this.totalCreateLinkCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_CREATE_LINK_COUNT]);
		this.successCreateLinkCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT]);
		this.totalDeleteLinkCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_DELETE_LINK_COUNT]);
		this.successDeleteLinkCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT]);
		this.totalUploadBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_UPLOAD_BYTES]);
		this.successUploadBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_UPLOAD_BYTES]);
		this.totalAppendBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_APPEND_BYTES]);
		this.successAppendBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_APPEND_BYTES]);
		this.totalModifyBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_MODIFY_BYTES]);
		this.successModifyBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_MODIFY_BYTES]);
		this.totalDownloadloadBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_DOWNLOAD_BYTES]);
		this.successDownloadloadBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES]);
		this.totalSyncInBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_SYNC_IN_BYTES]);
		this.successSyncInBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_SYNC_IN_BYTES]);
		this.totalSyncOutBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_SYNC_OUT_BYTES]);
		this.successSyncOutBytes = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES]);
		this.totalFileOpenCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_FILE_OPEN_COUNT]);
		this.successFileOpenCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT]);
		this.totalFileReadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_FILE_READ_COUNT]);
		this.successFileReadCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_FILE_READ_COUNT]);
		this.totalFileWriteCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_TOTAL_FILE_WRITE_COUNT]);
		this.successFileWriteCount = longValue(bs, offset, fieldsArray[FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT]);
		this.lastSourceUpdate = dateValue(bs, offset, fieldsArray[FIELD_INDEX_LAST_SOURCE_UPDATE]);
		this.lastSyncUpdate = dateValue(bs, offset, fieldsArray[FIELD_INDEX_LAST_SYNC_UPDATE]);
		this.lastSyncedTimestamp = dateValue(bs, offset, fieldsArray[FIELD_INDEX_LAST_SYNCED_TIMESTAMP]);
		this.lastHeartBeatTime = dateValue(bs, offset, fieldsArray[FIELD_INDEX_LAST_HEART_BEAT_TIME]);
		this.ifTrunkServer = booleanValue(bs, offset, fieldsArray[FIELD_INDEX_IF_TRUNK_FILE]);
	}

/**
* get fields total size
* @return fields total size
*/
	public static int getFieldsTotalSize()
	{
		return fieldsTotalSize;
	}
}
