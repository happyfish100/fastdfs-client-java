/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package org.csource.fastdfs;

import org.csource.fastdfs.*;

/**
 * load test class
 *
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class TestLoad {
  public static java.util.concurrent.ConcurrentLinkedQueue file_ids;
  public static int total_download_count = 0;
  public static int success_download_count = 0;
  public static int fail_download_count = 0;
  public static int total_upload_count = 0;
  public static int success_upload_count = 0;
  public static int upload_thread_count = 0;

  private TestLoad() {
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

      file_ids = new java.util.concurrent.ConcurrentLinkedQueue();

      for (int i = 0; i < 10; i++) {
        (new UploadThread(i)).start();
      }

      for (int i = 0; i < 20; i++) {
        (new DownloadThread(i)).start();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * discard file content callback class when download file
   *
   * @author Happy Fish / YuQing
   * @version Version 1.0
   */
  public static class DownloadFileDiscard implements DownloadCallback {
    public DownloadFileDiscard() {
    }

    public int recv(long file_size, byte[] data, int bytes) {
      return 0;
    }
  }

  /**
   * file uploader
   *
   * @author Happy Fish / YuQing
   * @version Version 1.0
   */
  public static class Uploader {
    public TrackerClient tracker;
    public TrackerServer trackerServer;

    public Uploader() throws Exception {
      this.tracker = new TrackerClient();
      this.trackerServer = tracker.getConnection();
    }

    public int uploadFile() throws Exception {
      StorageServer storageServer = null;
      StorageClient1 client = new StorageClient1(trackerServer, storageServer);
      byte[] file_buff;
      String file_id;

      file_buff = new byte[2 * 1024];
      java.util.Arrays.fill(file_buff, (byte) 65);

      try {
        file_id = client.upload_file1(file_buff, "txt", null);
        if (file_id == null) {
          System.out.println("upload file fail, error code: " + client.getErrorCode());
          return -1;
        }

        TestLoad.file_ids.offer(file_id);
        return 0;
      } catch (Exception ex) {
        System.out.println("upload file fail, error mesg: " + ex.getMessage());
        return -1;
      }
    }
  }

  /**
   * file downloader
   *
   * @author Happy Fish / YuQing
   * @version Version 1.0
   */
  public static class Downloader {
    public TrackerClient tracker;
    public TrackerServer trackerServer;
    public DownloadFileDiscard callback;

    public Downloader() throws Exception {
      this.tracker = new TrackerClient();
      this.trackerServer = tracker.getConnection();
      this.callback = new DownloadFileDiscard();
    }

    public int downloadFile(String file_id) throws Exception {
      int errno;
      StorageServer storageServer = null;
      StorageClient1 client = new StorageClient1(trackerServer, storageServer);

      try {
        errno = client.download_file1(file_id, this.callback);
        if (errno != 0) {
          System.out.println("Download file fail, file_id: " + file_id + ", error no: " + errno);
        }
        return errno;
      } catch (Exception ex) {
        System.out.println("Download file fail, error mesg: " + ex.getMessage());
        return -1;
      }
    }
  }

  /**
   * upload file thread
   *
   * @author Happy Fish / YuQing
   * @version Version 1.0
   */
  public static class UploadThread extends Thread {
    private int thread_index;

    public UploadThread(int index) {
      this.thread_index = index;
    }

    public void run() {
      try {
        TestLoad.upload_thread_count++;
        Uploader uploader = new Uploader();

        System.out.println("upload thread " + this.thread_index + " start");

        for (int i = 0; i < 50000; i++) {
          TestLoad.total_upload_count++;
          if (uploader.uploadFile() == 0) {
            TestLoad.success_upload_count++;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        TestLoad.upload_thread_count--;
      }

      System.out.println("upload thread " + this.thread_index
        + " exit, total_upload_count: " + TestLoad.total_upload_count
        + ", success_upload_count: " + TestLoad.success_upload_count
        + ", total_download_count: " + TestLoad.total_download_count
        + ", success_download_count: " + TestLoad.success_download_count);
    }
  }

  /**
   * download file thread
   *
   * @author Happy Fish / YuQing
   * @version Version 1.0
   */
  public static class DownloadThread extends Thread {
    private static Integer counter_lock = new Integer(0);
    private int thread_index;

    public DownloadThread(int index) {
      this.thread_index = index;
    }

    public void run() {
      try {
        String file_id;
        Downloader downloader = new Downloader();

        System.out.println("download thread " + this.thread_index + " start");

        file_id = "";
        while (TestLoad.upload_thread_count != 0 || file_id != null) {
          file_id = (String) TestLoad.file_ids.poll();
          if (file_id == null) {
            Thread.sleep(10);
            continue;
          }

          synchronized (this.counter_lock) {
            TestLoad.total_download_count++;
          }
          if (downloader.downloadFile(file_id) == 0) {
            synchronized (this.counter_lock) {
              TestLoad.success_download_count++;
            }
          } else {
            TestLoad.fail_download_count++;
          }
        }

        for (int i = 0; i < 3 && TestLoad.total_download_count < TestLoad.total_upload_count; i++) {
          file_id = (String) TestLoad.file_ids.poll();
          if (file_id == null) {
            Thread.sleep(10);
            continue;
          }

          synchronized (this.counter_lock) {
            TestLoad.total_download_count++;
          }
          if (downloader.downloadFile(file_id) == 0) {
            synchronized (this.counter_lock) {
              TestLoad.success_download_count++;
            }
          } else {
            TestLoad.fail_download_count++;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      System.out.println("download thread " + this.thread_index
        + " exit, total_download_count: " + TestLoad.total_download_count
        + ", success_download_count: " + TestLoad.success_download_count
        + ", fail_download_count: " + TestLoad.fail_download_count);
    }
  }
}
