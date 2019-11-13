/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Server Info
 *
 * @author Happy Fish / YuQing
 * @version Version 1.23
 */
public class FileInfo {
  public static final short FILE_TYPE_NORMAL = 1;
  public static final short FILE_TYPE_APPENDER = 2;
  public static final short FILE_TYPE_SLAVE = 4;

  protected boolean fetch_from_server;
  protected short file_type;
  protected String source_ip_addr;
  protected long file_size;
  protected Date create_timestamp;
  protected int crc32;

  /**
   * Constructor
   *
   * @param fetch_from_server if fetch from server flag
   * @param file_type         the file type
   * @param file_size         the file size
   * @param create_timestamp  create timestamp in seconds
   * @param crc32             the crc32 signature
   * @param source_ip_addr    the source storage ip address
   */
  public FileInfo(boolean fetch_from_server, short file_type, long file_size,
          int create_timestamp, int crc32, String source_ip_addr)
  {
    this.fetch_from_server = fetch_from_server;
    this.file_type = file_type;
    this.file_size = file_size;
    this.create_timestamp = new Date(create_timestamp * 1000L);
    this.crc32 = crc32;
    this.source_ip_addr = source_ip_addr;
  }

  /**
   * get the fetch_from_server flag
   *
   * @return the fetch_from_server flag
   */
  public boolean getFetchFromServer() {
    return this.fetch_from_server;
  }

  /**
   * set the fetch_from_server flag
   *
   * @param fetch_from_server the fetch from server flag
   */
  public void setFetchFromServer(boolean fetch_from_server) {
    this.fetch_from_server = fetch_from_server;
  }

  /**
   * get the file type
   *
   * @return the file type
   */
  public short getFileType() {
    return this.file_type;
  }

  /**
   * set the file type
   *
   * @param file_type the file type
   */
  public void setFileType(short file_type) {
    this.file_type = file_type;
  }

  /**
   * get the source ip address of the file uploaded to
   *
   * @return the source ip address of the file uploaded to
   */
  public String getSourceIpAddr() {
    return this.source_ip_addr;
  }

  /**
   * set the source ip address of the file uploaded to
   *
   * @param source_ip_addr the source ip address
   */
  public void setSourceIpAddr(String source_ip_addr) {
    this.source_ip_addr = source_ip_addr;
  }

  /**
   * get the file size
   *
   * @return the file size
   */
  public long getFileSize() {
    return this.file_size;
  }

  /**
   * set the file size
   *
   * @param file_size the file size
   */
  public void setFileSize(long file_size) {
    this.file_size = file_size;
  }

  /**
   * get the create timestamp of the file
   *
   * @return the create timestamp of the file
   */
  public Date getCreateTimestamp() {
    return this.create_timestamp;
  }

  /**
   * set the create timestamp of the file
   *
   * @param create_timestamp create timestamp in seconds
   */
  public void setCreateTimestamp(int create_timestamp) {
    this.create_timestamp = new Date(create_timestamp * 1000L);
  }

  /**
   * get the file CRC32 signature
   *
   * @return the file CRC32 signature
   */
  public long getCrc32() {
    return this.crc32;
  }

  /**
   * set the create timestamp of the file
   *
   * @param crc32 the crc32 signature
   */
  public void setCrc32(int crc32) {
    this.crc32 = crc32;
  }

  /**
   * to string
   *
   * @return string
   */
  public String toString() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return "fetch_from_server = " + this.fetch_from_server + ", " +
      "file_type = " + this.file_type + ", " +
      "source_ip_addr = " + this.source_ip_addr + ", " +
      "file_size = " + this.file_size + ", " +
      "create_timestamp = " + df.format(this.create_timestamp) + ", " +
      "crc32 = " + this.crc32;
  }
}
