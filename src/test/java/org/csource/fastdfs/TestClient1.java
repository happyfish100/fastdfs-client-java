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
 * @version Version 1.16
 */
public class TestClient1 {
  private TestClient1() {
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
    String group_name;

    try {
      ClientGlobal.init(conf_filename);
      System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
      System.out.println("charset=" + ClientGlobal.g_charset);

      String file_id;

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
      StorageClient1 client = new StorageClient1(trackerServer, storageServer);
      byte[] file_buff;
      NameValuePair[] meta_list;
      String master_file_id;
      String prefix_name;
      String file_ext_name;
      String slave_file_id;
      String generated_slave_file_id;
      int errno;

      group_name = "group1";
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

      meta_list = new NameValuePair[4];
      meta_list[0] = new NameValuePair("width", "800");
      meta_list[1] = new NameValuePair("heigth", "600");
      meta_list[2] = new NameValuePair("bgcolor", "#FFFFFF");
      meta_list[3] = new NameValuePair("author", "Mike");

      file_buff = "this is a test".getBytes(ClientGlobal.g_charset);
      System.out.println("file length: " + file_buff.length);

      file_id = client.upload_file1(file_buff, "txt", meta_list);
  		/*
  		group_name = "group1";
  		file_id = client.upload_file1(group_name, file_buff, "txt", meta_list);
  		*/
      if (file_id == null) {
        System.err.println("upload file fail, error code: " + client.getErrorCode());
        return;
      } else {
        System.err.println("file_id: " + file_id);
        System.err.println(client.get_file_info1(file_id));

        ServerInfo[] servers = tracker.getFetchStorages1(trackerServer, file_id);
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

        if ((errno = client.set_metadata1(file_id, meta_list, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE)) == 0) {
          System.err.println("set_metadata success");
        } else {
          System.err.println("set_metadata fail, error no: " + errno);
        }

        meta_list = client.get_metadata1(file_id);
        if (meta_list != null) {
          for (int i = 0; i < meta_list.length; i++) {
            System.out.println(meta_list[i].getName() + " " + meta_list[i].getValue());
          }
        }

        //Thread.sleep(30000);

        file_buff = client.download_file1(file_id);
        if (file_buff != null) {
          System.out.println("file length:" + file_buff.length);
          System.out.println((new String(file_buff)));
        }

        master_file_id = file_id;
        prefix_name = "-part1";
        file_ext_name = "txt";
        file_buff = "this is a slave buff.".getBytes(ClientGlobal.g_charset);
        slave_file_id = client.upload_file1(master_file_id, prefix_name, file_buff, file_ext_name, meta_list);
        if (slave_file_id != null) {
          System.err.println("slave file_id: " + slave_file_id);
          System.err.println(client.get_file_info1(slave_file_id));

          generated_slave_file_id = ProtoCommon.genSlaveFilename(master_file_id, prefix_name, file_ext_name);
          if (!generated_slave_file_id.equals(slave_file_id)) {
            System.err.println("generated slave file: " + generated_slave_file_id + "\n != returned slave file: " + slave_file_id);
          }
        }

        //Thread.sleep(10000);
        if ((errno = client.delete_file1(file_id)) == 0) {
          System.err.println("Delete file success");
        } else {
          System.err.println("Delete file fail, error no: " + errno);
        }
      }

      if ((file_id = client.upload_file1(local_filename, null, meta_list)) != null) {
        int ts;
        String token;
        String file_url;
        InetSocketAddress inetSockAddr;

        System.err.println("file_id: " + file_id);
        System.err.println(client.get_file_info1(file_id));

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
        System.err.println("file url: " + file_url);

        errno = client.download_file1(file_id, 0, 100, "c:\\" + file_id.replaceAll("/", "_"));
        if (errno == 0) {
          System.err.println("Download file success");
        } else {
          System.err.println("Download file fail, error no: " + errno);
        }

        errno = client.download_file1(file_id, new DownloadFileWriter("c:\\" + file_id.replaceAll("/", "-")));
        if (errno == 0) {
          System.err.println("Download file success");
        } else {
          System.err.println("Download file fail, error no: " + errno);
        }

        master_file_id = file_id;
        prefix_name = "-part2";
        file_ext_name = null;
        slave_file_id = client.upload_file1(master_file_id, prefix_name, local_filename, file_ext_name, meta_list);
        if (slave_file_id != null) {
          System.err.println("slave file_id: " + slave_file_id);
          System.err.println(client.get_file_info1(slave_file_id));

          generated_slave_file_id = ProtoCommon.genSlaveFilename(master_file_id, prefix_name, file_ext_name);
          if (!generated_slave_file_id.equals(slave_file_id)) {
            System.err.println("generated slave file: " + generated_slave_file_id + "\n != returned slave file: " + slave_file_id);
          }
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

      file_id = client.upload_file1(null, f.length(), new UploadLocalFileSender(local_filename), file_ext_name, meta_list);
      if (file_id != null) {
        System.out.println("file id: " + file_id);
        System.out.println(client.get_file_info1(file_id));
        master_file_id = file_id;
        prefix_name = "-part3";
        slave_file_id = client.upload_file1(master_file_id, prefix_name, f.length(), new UploadLocalFileSender(local_filename), file_ext_name, meta_list);
        if (slave_file_id != null) {
          System.err.println("slave file_id: " + slave_file_id);
          generated_slave_file_id = ProtoCommon.genSlaveFilename(master_file_id, prefix_name, file_ext_name);
          if (!generated_slave_file_id.equals(slave_file_id)) {
            System.err.println("generated slave file: " + generated_slave_file_id + "\n != returned slave file: " + slave_file_id);
          }
        }
      } else {
        System.err.println("Upload file fail, error no: " + errno);
      }

      storageServer = tracker.getFetchStorage1(trackerServer, file_id);
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
