/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package org.csource.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * client test
 *
 * @author Happy Fish / YuQing
 * @version Version 1.20
 */
public class TestAppender {
  private TestAppender() {
  }

  /**
   * entry point
   *
   * @param args comand arguments
   *             <ul><li>args[0]: config filename</li></ul>
   *             <ul><li>args[1]: local filename to upload</li></ul>
   */
  public static void main(String args[]) {
    if (args.length < 2) {
      System.out.println("Error: Must have 2 parameters, one is config filename, "
        + "the other is the local filename to upload");
      return;
    }

    System.out.println("java.version=" + System.getProperty("java.version"));

    String conf_filename = args[0];
    String local_filename = args[1];

    try {
      ClientGlobal.init(conf_filename);
      System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
      System.out.println("charset=" + ClientGlobal.g_charset);

      long startTime;
      String group_name;
      String remote_filename;
      ServerInfo[] servers;
      TrackerClient tracker = new TrackerClient();
      TrackerServer trackerServer = tracker.getConnection();

      StorageServer storageServer = null;

  		/*
      storageServer = tracker.getStoreStorage(trackerServer);
  		if (storageServer == null)
  		{
  			System.out.println("getStoreStorage fail, error code: " + tracker.getErrorCode());
  			return;
  		}
  		*/

      StorageClient client = new StorageClient(trackerServer, storageServer);
      byte[] file_buff;
      NameValuePair[] meta_list;
      String[] results;
      String appender_filename;
      String file_ext_name;
      int errno;

      meta_list = new NameValuePair[4];
      meta_list[0] = new NameValuePair("width", "800");
      meta_list[1] = new NameValuePair("heigth", "600");
      meta_list[2] = new NameValuePair("bgcolor", "#FFFFFF");
      meta_list[3] = new NameValuePair("author", "Mike");

      file_buff = "this is a test".getBytes(ClientGlobal.g_charset);
      System.out.println("file length: " + file_buff.length);

      group_name = null;
      StorageServer[] storageServers = tracker.getStoreStorages(trackerServer, group_name);
      if (storageServers == null) {
        System.err.println("get store storage servers fail, error code: " + tracker.getErrorCode());
      } else {
        System.err.println("store storage servers count: " + storageServers.length);
        for (int k = 0; k < storageServers.length; k++) {
          System.err.println((k + 1) + ". " + storageServers[k].getInetSocketAddress().getAddress().getHostAddress() + ":" + storageServers[k].getInetSocketAddress().getPort());
        }
        System.err.println("");
      }

      startTime = System.currentTimeMillis();
      results = client.upload_appender_file(file_buff, "txt", meta_list);
      System.out.println("upload_appender_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

  		/*
  		group_name = "";
  		results = client.upload_appender_file(group_name, file_buff, "txt", meta_list);
  		*/
      if (results == null) {
        System.err.println("upload file fail, error code: " + client.getErrorCode());
        return;
      } else {
        group_name = results[0];
        remote_filename = results[1];
        System.err.println("group_name: " + group_name + ", remote_filename: " + remote_filename);
        System.err.println(client.get_file_info(group_name, remote_filename));

        servers = tracker.getFetchStorages(trackerServer, group_name, remote_filename);
        if (servers == null) {
          System.err.println("get storage servers fail, error code: " + tracker.getErrorCode());
        } else {
          System.err.println("storage servers count: " + servers.length);
          for (int k = 0; k < servers.length; k++) {
            System.err.println((k + 1) + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());
          }
          System.err.println("");
        }

        meta_list = new NameValuePair[4];
        meta_list[0] = new NameValuePair("width", "1024");
        meta_list[1] = new NameValuePair("heigth", "768");
        meta_list[2] = new NameValuePair("bgcolor", "#000000");
        meta_list[3] = new NameValuePair("title", "Untitle");

        startTime = System.currentTimeMillis();
        errno = client.set_metadata(group_name, remote_filename, meta_list, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
        System.out.println("set_metadata time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println("set_metadata success");
        } else {
          System.err.println("set_metadata fail, error no: " + errno);
        }

        meta_list = client.get_metadata(group_name, remote_filename);
        if (meta_list != null) {
          for (int i = 0; i < meta_list.length; i++) {
            System.out.println(meta_list[i].getName() + " " + meta_list[i].getValue());
          }
        }

        //Thread.sleep(30000);

        startTime = System.currentTimeMillis();
        file_buff = client.download_file(group_name, remote_filename);
        System.out.println("download_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

        if (file_buff != null) {
          System.out.println("file length:" + file_buff.length);
          System.out.println((new String(file_buff)));
        }

        file_buff = "this is a slave buff".getBytes(ClientGlobal.g_charset);
        appender_filename = remote_filename;
        file_ext_name = "txt";
        startTime = System.currentTimeMillis();
        errno = client.append_file(group_name, appender_filename, file_buff);
        System.out.println("append_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println(client.get_file_info(group_name, appender_filename));
        } else {
          System.err.println("append file fail, error no: " + errno);
        }

        startTime = System.currentTimeMillis();
        errno = client.delete_file(group_name, remote_filename);
        System.out.println("delete_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println("Delete file success");
        } else {
          System.err.println("Delete file fail, error no: " + errno);
        }
      }

      results = client.upload_appender_file(local_filename, null, meta_list);
      if (results != null) {
        String file_id;
        int ts;
        String token;
        String file_url;
        InetSocketAddress inetSockAddr;

        group_name = results[0];
        remote_filename = results[1];
        file_id = group_name + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + remote_filename;

        inetSockAddr = trackerServer.getInetSocketAddress();
        file_url = "http://" + inetSockAddr.getAddress().getHostAddress();
        if (ClientGlobal.g_tracker_http_port != 80) {
          file_url += ":" + ClientGlobal.g_tracker_http_port;
        }
        file_url += "/" + file_id;
        if (ClientGlobal.g_anti_steal_token) {
          ts = (int) (System.currentTimeMillis() / 1000);
          token = ProtoCommon.getToken(file_id, ts, ClientGlobal.g_secret_key);
          file_url += "?token=" + token + "&ts=" + ts;
        }

        System.err.println("group_name: " + group_name + ", remote_filename: " + remote_filename);
        System.err.println(client.get_file_info(group_name, remote_filename));
        System.err.println("file url: " + file_url);

        errno = client.download_file(group_name, remote_filename, 0, 0, "c:\\" + remote_filename.replaceAll("/", "_"));
        if (errno == 0) {
          System.err.println("Download file success");
        } else {
          System.err.println("Download file fail, error no: " + errno);
        }

        errno = client.download_file(group_name, remote_filename, 0, 0, new DownloadFileWriter("c:\\" + remote_filename.replaceAll("/", "-")));
        if (errno == 0) {
          System.err.println("Download file success");
        } else {
          System.err.println("Download file fail, error no: " + errno);
        }

        appender_filename = remote_filename;
        file_ext_name = null;
        startTime = System.currentTimeMillis();
        errno = client.append_file(group_name, appender_filename, local_filename);
        System.out.println("append_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println(client.get_file_info(group_name, appender_filename));
        } else {
          System.err.println("append file fail, error no: " + errno);
        }
      }

      File f;
      f = new File(local_filename);
      int nPos = local_filename.lastIndexOf('.');
      if (nPos > 0 && local_filename.length() - nPos <= ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 1) {
        file_ext_name = local_filename.substring(nPos + 1);
      } else {
        file_ext_name = null;
      }

      results = client.upload_appender_file(null, f.length(),
        new UploadLocalFileSender(local_filename), file_ext_name, meta_list);
      if (results != null) {
        group_name = results[0];
        remote_filename = results[1];

        System.out.println("group name: " + group_name + ", remote filename: " + remote_filename);
        System.out.println(client.get_file_info(group_name, remote_filename));

        appender_filename = remote_filename;
        startTime = System.currentTimeMillis();
        errno = client.append_file(group_name, appender_filename, f.length(), new UploadLocalFileSender(local_filename));
        System.out.println("append_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println(client.get_file_info(group_name, appender_filename));
        } else {
          System.err.println("append file fail, error no: " + errno);
        }

        startTime = System.currentTimeMillis();
        errno = client.modify_file(group_name, appender_filename, 0, f.length(), new UploadLocalFileSender(local_filename));
        System.out.println("modify_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println(client.get_file_info(group_name, appender_filename));
        } else {
          System.err.println("modify file fail, error no: " + errno);
        }

        startTime = System.currentTimeMillis();
        errno = client.truncate_file(group_name, appender_filename);
        System.out.println("truncate_file time used: " + (System.currentTimeMillis() - startTime) + " ms");
        if (errno == 0) {
          System.err.println(client.get_file_info(group_name, appender_filename));
        } else {
          System.err.println("truncate file fail, error no: " + errno);
        }
      } else {
        System.err.println("Upload file fail, error no: " + errno);
      }

      storageServer = tracker.getFetchStorage(trackerServer, group_name, remote_filename);
      if (storageServer == null) {
        System.out.println("getFetchStorage fail, errno code: " + tracker.getErrorCode());
        return;
      }
  		/* for test only */
      System.out.println("active test to storage server: " + ProtoCommon.activeTest(storageServer.getSocket()));

      storageServer.close();

  		/* for test only */
      System.out.println("active test to tracker server: " + ProtoCommon.activeTest(trackerServer.getSocket()));

      trackerServer.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
