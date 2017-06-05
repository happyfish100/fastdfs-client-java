/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package org.csource.fastdfs;

import org.csource.fastdfs.*;

import java.text.SimpleDateFormat;

/**
 * load test class
 *
 * @author Happy Fish / YuQing
 * @version Version 1.20
 */
public class Monitor {
  private Monitor() {
  }

  /**
   * entry point
   *
   * @param args comand arguments
   *             <ul><li>args[0]: config filename</li></ul>
   */
  public static void main(String args[]) {
    if (args.length < 1) {
      System.out.println("Error: Must have 1 parameter: config filename");
      return;
    }

    System.out.println("java.version=" + System.getProperty("java.version"));

    try {
      ClientGlobal.init(args[0]);
      System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
      System.out.println("charset=" + ClientGlobal.g_charset);

      TrackerClient tracker = new TrackerClient();

  		/*
      System.out.println("delete storage return: " + tracker.deleteStorage("group1", "192.168.0.192"));
  		System.out.println("delete storage errno: " + tracker.getErrorCode());
  		*/

      TrackerServer trackerServer = tracker.getConnection();
      if (trackerServer == null) {
        return;
      }

      int count;
      StructGroupStat[] groupStats = tracker.listGroups(trackerServer);
      if (groupStats == null) {
        System.out.println("");
        System.out.println("ERROR! list groups error, error no: " + tracker.getErrorCode());
        System.out.println("");
        return;
      }

      System.out.println("group count: " + groupStats.length);

      count = 0;
      for (StructGroupStat groupStat : groupStats) {
        count++;
        System.out.println("Group " + count + ":");
        System.out.println("group name = " + groupStat.getGroupName());
        System.out.println("disk total space = " + groupStat.getTotalMB() + "MB");
        System.out.println("disk free space = " + groupStat.getFreeMB() + " MB");
        System.out.println("trunk free space = " + groupStat.getTrunkFreeMB() + " MB");
        System.out.println("storage server count = " + groupStat.getStorageCount());
        System.out.println("active server count = " + groupStat.getActiveCount());
        System.out.println("storage server port = " + groupStat.getStoragePort());
        System.out.println("storage HTTP port = " + groupStat.getStorageHttpPort());
        System.out.println("store path count = " + groupStat.getStorePathCount());
        System.out.println("subdir count per path = " + groupStat.getSubdirCountPerPath());
        System.out.println("current write server index = " + groupStat.getCurrentWriteServer());
        System.out.println("current trunk file id = " + groupStat.getCurrentTrunkFileId());

        StructStorageStat[] storageStats = tracker.listStorages(trackerServer, groupStat.getGroupName());
        if (storageStats == null) {
          System.out.println("");
          System.out.println("ERROR! list storage error, error no: " + tracker.getErrorCode());
          System.out.println("");
          break;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int stroageCount = 0;
        for (StructStorageStat storageStat : storageStats) {
          stroageCount++;
          System.out.println("\tStorage " + stroageCount + ":");
          System.out.println("\t\tstorage id = " + storageStat.getId());
          System.out.println("\t\tip_addr = " + storageStat.getIpAddr() + "  " + ProtoCommon.getStorageStatusCaption(storageStat.getStatus()));
          System.out.println("\t\thttp domain = " + storageStat.getDomainName());
          System.out.println("\t\tversion = " + storageStat.getVersion());
          System.out.println("\t\tjoin time = " + df.format(storageStat.getJoinTime()));
          System.out.println("\t\tup time = " + (storageStat.getUpTime().getTime() == 0 ? "" : df.format(storageStat.getUpTime())));
          System.out.println("\t\ttotal storage = " + storageStat.getTotalMB() + "MB");
          System.out.println("\t\tfree storage = " + storageStat.getFreeMB() + "MB");
          System.out.println("\t\tupload priority = " + storageStat.getUploadPriority());
          System.out.println("\t\tstore_path_count = " + storageStat.getStorePathCount());
          System.out.println("\t\tsubdir_count_per_path = " + storageStat.getSubdirCountPerPath());
          System.out.println("\t\tstorage_port = " + storageStat.getStoragePort());
          System.out.println("\t\tstorage_http_port = " + storageStat.getStorageHttpPort());
          System.out.println("\t\tcurrent_write_path = " + storageStat.getCurrentWritePath());
          System.out.println("\t\tsource ip_addr = " + storageStat.getSrcIpAddr());
          System.out.println("\t\tif_trunk_server = " + storageStat.isTrunkServer());
          System.out.println("\t\tconntion.alloc_count  = " + storageStat.getConnectionAllocCount());
          System.out.println("\t\tconntion.current_count  = " + storageStat.getConnectionCurrentCount());
          System.out.println("\t\tconntion.max_count  = " + storageStat.getConnectionMaxCount());
          System.out.println("\t\ttotal_upload_count = " + storageStat.getTotalUploadCount());
          System.out.println("\t\tsuccess_upload_count = " + storageStat.getSuccessUploadCount());
          System.out.println("\t\ttotal_append_count = " + storageStat.getTotalAppendCount());
          System.out.println("\t\tsuccess_append_count = " + storageStat.getSuccessAppendCount());
          System.out.println("\t\ttotal_modify_count = " + storageStat.getTotalModifyCount());
          System.out.println("\t\tsuccess_modify_count = " + storageStat.getSuccessModifyCount());
          System.out.println("\t\ttotal_truncate_count = " + storageStat.getTotalTruncateCount());
          System.out.println("\t\tsuccess_truncate_count = " + storageStat.getSuccessTruncateCount());
          System.out.println("\t\ttotal_set_meta_count = " + storageStat.getTotalSetMetaCount());
          System.out.println("\t\tsuccess_set_meta_count = " + storageStat.getSuccessSetMetaCount());
          System.out.println("\t\ttotal_delete_count = " + storageStat.getTotalDeleteCount());
          System.out.println("\t\tsuccess_delete_count = " + storageStat.getSuccessDeleteCount());
          System.out.println("\t\ttotal_download_count = " + storageStat.getTotalDownloadCount());
          System.out.println("\t\tsuccess_download_count = " + storageStat.getSuccessDownloadCount());
          System.out.println("\t\ttotal_get_meta_count = " + storageStat.getTotalGetMetaCount());
          System.out.println("\t\tsuccess_get_meta_count = " + storageStat.getSuccessGetMetaCount());
          System.out.println("\t\ttotal_create_link_count = " + storageStat.getTotalCreateLinkCount());
          System.out.println("\t\tsuccess_create_link_count = " + storageStat.getSuccessCreateLinkCount());
          System.out.println("\t\ttotal_delete_link_count = " + storageStat.getTotalDeleteLinkCount());
          System.out.println("\t\tsuccess_delete_link_count = " + storageStat.getSuccessDeleteLinkCount());
          System.out.println("\t\ttotal_upload_bytes = " + storageStat.getTotalUploadBytes());
          System.out.println("\t\tsuccess_upload_bytes = " + storageStat.getSuccessUploadBytes());
          System.out.println("\t\ttotal_append_bytes = " + storageStat.getTotalAppendBytes());
          System.out.println("\t\tsuccess_append_bytes = " + storageStat.getSuccessAppendBytes());
          System.out.println("\t\ttotal_modify_bytes = " + storageStat.getTotalModifyBytes());
          System.out.println("\t\tsuccess_modify_bytes = " + storageStat.getSuccessModifyBytes());
          System.out.println("\t\ttotal_download_bytes = " + storageStat.getTotalDownloadloadBytes());
          System.out.println("\t\tsuccess_download_bytes = " + storageStat.getSuccessDownloadloadBytes());
          System.out.println("\t\ttotal_sync_in_bytes = " + storageStat.getTotalSyncInBytes());
          System.out.println("\t\tsuccess_sync_in_bytes = " + storageStat.getSuccessSyncInBytes());
          System.out.println("\t\ttotal_sync_out_bytes = " + storageStat.getTotalSyncOutBytes());
          System.out.println("\t\tsuccess_sync_out_bytes = " + storageStat.getSuccessSyncOutBytes());
          System.out.println("\t\ttotal_file_open_count = " + storageStat.getTotalFileOpenCount());
          System.out.println("\t\tsuccess_file_open_count = " + storageStat.getSuccessFileOpenCount());
          System.out.println("\t\ttotal_file_read_count = " + storageStat.getTotalFileReadCount());
          System.out.println("\t\tsuccess_file_read_count = " + storageStat.getSuccessFileReadCount());
          System.out.println("\t\ttotal_file_write_count = " + storageStat.getTotalFileWriteCount());
          System.out.println("\t\tsuccess_file_write_count = " + storageStat.getSuccessFileWriteCount());
          System.out.println("\t\tlast_heart_beat_time = " + df.format(storageStat.getLastHeartBeatTime()));
          System.out.println("\t\tlast_source_update = " + df.format(storageStat.getLastSourceUpdate()));
          System.out.println("\t\tlast_sync_update = " + df.format(storageStat.getLastSyncUpdate()));
          System.out.println("\t\tlast_synced_timestamp = " + df.format(storageStat.getLastSyncedTimestamp()) + getSyncedDelayString(storageStats, storageStat));
        }
      }

      trackerServer.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected static String getSyncedDelayString(StructStorageStat[] storageStats, StructStorageStat currentStorageStat) {
    long maxLastSourceUpdate = 0;
    for (StructStorageStat storageStat : storageStats) {
      if (storageStat != currentStorageStat && storageStat.getLastSourceUpdate().getTime() > maxLastSourceUpdate) {
        maxLastSourceUpdate = storageStat.getLastSourceUpdate().getTime();
      }
    }

    if (maxLastSourceUpdate == 0) {
      return "";
    }

    if (currentStorageStat.getLastSyncedTimestamp().getTime() == 0) {
      return " (never synced)";
    }

    int delaySeconds = (int) ((maxLastSourceUpdate - currentStorageStat.getLastSyncedTimestamp().getTime()) / 1000);
    int day = delaySeconds / (24 * 3600);
    int remainSeconds = delaySeconds % (24 * 3600);
    int hour = remainSeconds / 3600;
    remainSeconds %= 3600;
    int minute = remainSeconds / 60;
    int second = remainSeconds % 60;
    String delayTimeStr;
    if (day != 0) {
      delayTimeStr = String.format("%1$d days %2$02dh:%3$02dm:%4$02ds", day, hour, minute, second);
    } else if (hour != 0) {
      delayTimeStr = String.format("%1$02dh:%2$02dm:%3$02ds", hour, minute, second);
    } else if (minute != 0) {
      delayTimeStr = String.format("%1$02dm:%2$02ds", minute, second);
    } else {
      delayTimeStr = String.format("%1$ds", second);
    }

    return " (" + delayTimeStr + " delay)";
  }
}
