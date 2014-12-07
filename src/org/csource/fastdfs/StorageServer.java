/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package org.csource.fastdfs;

import java.io.*;
import java.util.*;
import java.net.*;
import org.csource.common.*;

/**
* Storage Server Info
* @author Happy Fish / YuQing
* @version Version 1.11
*/
public class StorageServer extends TrackerServer
{
	protected int store_path_index = 0;
	
/**
* Constructor
* @param ip_addr the ip address of storage server
* @param port the port of storage server
* @param store_path the store path index on the storage server
*/
	public StorageServer(String ip_addr, int port, int store_path) throws IOException
	{		
		super(ClientGlobal.getSocket(ip_addr, port), new InetSocketAddress(ip_addr, port));
		this.store_path_index = store_path;
	}

/**
* Constructor
* @param ip_addr the ip address of storage server
* @param port the port of storage server
* @param store_path the store path index on the storage server
*/
	public StorageServer(String ip_addr, int port, byte store_path) throws IOException
	{
		super(ClientGlobal.getSocket(ip_addr, port), new InetSocketAddress(ip_addr, port));
		if (store_path < 0)
		{
			this.store_path_index = 256 + store_path;
		}
		else
		{
			this.store_path_index = store_path;
		}
	}
	
/**
* @return the store path index on the storage server
*/
	public int getStorePathIndex()
	{
		return this.store_path_index;
	}
}
