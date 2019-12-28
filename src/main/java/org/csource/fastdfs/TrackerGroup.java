/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.csource.fastdfs;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Tracker server group
 *
 * @author Happy Fish / YuQing
 * @version Version 1.17
 */
public class TrackerGroup {
  public int tracker_server_index;
  public InetSocketAddress[] tracker_servers;
  protected Integer lock;

  /**
   * Constructor
   *
   * @param tracker_servers tracker servers
   */
  public TrackerGroup(InetSocketAddress[] tracker_servers) {
    this.tracker_servers = tracker_servers;
    this.lock = new Integer(0);
    this.tracker_server_index = 0;
  }

  /**
   * return connected tracker server
   *
   * @return connected tracker server, null for fail
   */
  public TrackerServer getTrackerServer(int serverIndex) throws IOException {
    return new TrackerServer(this.tracker_servers[serverIndex]);
  }

  /**
   * return connected tracker server
   *
   * @return connected tracker server, null for fail
   */
  public TrackerServer getTrackerServer() throws IOException {
    int current_index;

    synchronized (this.lock) {
      this.tracker_server_index++;
      if (this.tracker_server_index >= this.tracker_servers.length) {
        this.tracker_server_index = 0;
      }

      current_index = this.tracker_server_index;
    }

    try {
      return this.getTrackerServer(current_index);
    } catch (IOException ex) {
      System.err.println("connect to server " + this.tracker_servers[current_index].getAddress().getHostAddress() + ":" + this.tracker_servers[current_index].getPort() + " fail");
      ex.printStackTrace(System.err);
    }

    for (int i = 0; i < this.tracker_servers.length; i++) {
      if (i == current_index) {
        continue;
      }

      try {
        TrackerServer trackerServer = this.getTrackerServer(i);

        synchronized (this.lock) {
          if (this.tracker_server_index == current_index) {
            this.tracker_server_index = i;
          }
        }

        return trackerServer;
      } catch (IOException ex) {
        System.err.println("connect to server " + this.tracker_servers[i].getAddress().getHostAddress() + ":" + this.tracker_servers[i].getPort() + " fail");
        ex.printStackTrace(System.err);
      }
    }

    return null;
  }

  public Object clone() {
    InetSocketAddress[] trackerServers = new InetSocketAddress[this.tracker_servers.length];
    for (int i = 0; i < trackerServers.length; i++) {
      trackerServers[i] = new InetSocketAddress(this.tracker_servers[i].getAddress().getHostAddress(), this.tracker_servers[i].getPort());
    }

    return new TrackerGroup(trackerServers);
  }
}
