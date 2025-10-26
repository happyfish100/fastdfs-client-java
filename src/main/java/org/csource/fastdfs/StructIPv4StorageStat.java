/**
 * Copyright (C) 2025 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import java.util.Date;

/**
 * C struct body decoder
 *
 * @author Happy Fish / YuQing
 * @version Version 1.36
 */
public class StructIPv4StorageStat extends StructStorageStat {
  protected static int fieldsTotalSize;
  protected static StructBase.FieldInfo[] fieldsArray =
      new StructBase.FieldInfo[StructStorageStat.FIELD_COUNT];

  static {
      fieldsTotalSize = StructStorageStat.initFieldsArray(
              fieldsArray, ProtoCommon.FDFS_IPV4_SIZE);
  }

  /**
   * get fields total size
   *
   * @return fields total size
   */
  public static int getFieldsTotalSize() {
    return fieldsTotalSize;
  }

  /**
   *
   * @param bs     byte array
   * @param offset start offset
   */
  public void setFields(byte[] bs, int offset) {
      super.setFields(fieldsArray, bs, offset);
  }
}
