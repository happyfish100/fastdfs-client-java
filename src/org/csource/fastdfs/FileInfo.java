/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package org.csource.fastdfs;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
* Server Info
* @author Happy Fish / YuQing
* @version Version 1.23
*/
public class FileInfo
{
	protected String source_ip_addr;
	protected long file_size;
	protected Date create_timestamp;
	protected int crc32;

/**
* Constructor
* @param file_size the file size
* @param create_timestamp create timestamp in seconds
* @param crc32 the crc32 signature
* @param source_ip_addr the source storage ip address
*/
	public FileInfo(long file_size, int create_timestamp, int crc32, String source_ip_addr)
	{
		this.file_size = file_size;
		this.create_timestamp = new Date(create_timestamp * 1000L);
		this.crc32 = crc32;
		this.source_ip_addr = source_ip_addr;
	}

/**
* set the source ip address of the file uploaded to
* @param source_ip_addr the source ip address
*/
	public void setSourceIpAddr(String source_ip_addr)
	{
		this.source_ip_addr = source_ip_addr;
	}
	
/**
* get the source ip address of the file uploaded to
* @return the source ip address of the file uploaded to
*/
	public String getSourceIpAddr()
	{
		return this.source_ip_addr;
	}
	
/**
* set the file size
* @param file_size the file size
*/
	public void setFileSize(long file_size)
	{
		this.file_size = file_size;
	}
	
/**
* get the file size
* @return the file size
*/
	public long getFileSize()
	{
		return this.file_size;
	}

/**
* set the create timestamp of the file
* @param create_timestamp create timestamp in seconds
*/
	public void setCreateTimestamp(int create_timestamp)
	{
		this.create_timestamp = new Date(create_timestamp * 1000L);
	}
	
/**
* get the create timestamp of the file
* @return the create timestamp of the file
*/
	public Date getCreateTimestamp()
	{
		return this.create_timestamp;
	}

/**
* set the create timestamp of the file
* @param crc32 the crc32 signature
*/
	public void setCrc32(int crc32)
	{
		this.crc32 = crc32;
	}
	
/**
* get the file CRC32 signature
* @return the file CRC32 signature
*/
	public long getCrc32()
	{
		return this.crc32;
	}
	
/**
* to string
* @return string
*/
	public String toString()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return 	"source_ip_addr = " + this.source_ip_addr + ", " + 
		        "file_size = " + this.file_size + ", " +
		        "create_timestamp = " + df.format(this.create_timestamp) + ", " +
		        "crc32 = " + this.crc32;
	}
}
