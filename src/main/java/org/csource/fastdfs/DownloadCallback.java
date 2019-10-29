/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

/**
 * Download file callback interface
 *
 * @author Happy Fish / YuQing
 * @version Version 1.4
 */
public interface DownloadCallback {
  /**
   * recv file content callback function, may be called more than once when the file downloaded
   *
   * @param file_size file size
   * @param data      data buff
   * @param bytes     data bytes
   * @return 0 success, return none zero(errno) if fail
   */
  public int recv(long file_size, byte[] data, int bytes);
}
