/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package org.csource.fastdfs.test;

import java.io.*;
import java.util.*;
import java.net.*;
import org.csource.fastdfs.*;

/**
* upload file callback class, local file sender
* @author Happy Fish / YuQing
* @version Version 1.0
*/
public class UploadLocalFileSender implements UploadCallback
{
	private String local_filename;
	
	public UploadLocalFileSender(String szLocalFilename)
	{
		this.local_filename = szLocalFilename;
	}
	
	/**
	* send file content callback function, be called only once when the file uploaded
	* @param out output stream for writing file content
	* @return 0 success, return none zero(errno) if fail
	*/
	public int send(OutputStream out) throws IOException
	{
			FileInputStream fis;
			int readBytes;
			byte[] buff = new byte[256 * 1024];
			
			fis = new FileInputStream(this.local_filename);
			try
			{
				while ((readBytes=fis.read(buff)) >= 0)
				{
					if (readBytes == 0)
					{
						continue;
					}
					
					out.write(buff, 0, readBytes);
				}
			}
			finally
			{
				fis.close();
			}
			
			return 0;
	}
}
