/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 */

package org.csource.fastdfs;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * C struct body decoder
 *
 * @author Happy Fish / YuQing
 * @version Version 1.17
 */
public class ProtoStructDecoder<T extends StructBase> {
  /**
   * Constructor
   */
  public ProtoStructDecoder() {
  }

  /**
   * decode byte buffer
   */
  public T[] decode(byte[] bs, Class<T> clazz, int fieldsTotalSize) throws Exception {
    if (bs.length % fieldsTotalSize != 0) {
      throw new IOException("byte array length: " + bs.length + " is invalid!");
    }

    int count = bs.length / fieldsTotalSize;
    int offset;
    T[] results = (T[]) Array.newInstance(clazz, count);

    offset = 0;
    for (int i = 0; i < results.length; i++) {
      results[i] = clazz.newInstance();
      results[i].setFields(bs, offset);
      offset += fieldsTotalSize;
    }

    return results;
  }
}
