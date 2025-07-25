/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 **/

package org.csource.fastdfs;

import org.csource.common.IniFileReader;
import org.csource.common.MyException;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Global variables
 *
 * @author Happy Fish / YuQing
 * @version Version 1.33
 */
public class ClientGlobal {

  public static final String CONF_KEY_CONNECT_TIMEOUT = "connect_timeout";
  public static final String CONF_KEY_NETWORK_TIMEOUT = "network_timeout";
  public static final String CONF_KEY_CHARSET = "charset";
  public static final String CONF_KEY_HTTP_ANTI_STEAL_TOKEN = "http.anti_steal_token";
  public static final String CONF_KEY_HTTP_SECRET_KEY = "http.secret_key";
  public static final String CONF_KEY_HTTP_TRACKER_HTTP_PORT = "http.tracker_http_port";
  public static final String CONF_KEY_TRACKER_SERVER = "tracker_server";
  public static final String PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS = "fastdfs.connect_timeout_in_seconds";
  public static final String PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS = "fastdfs.network_timeout_in_seconds";
  public static final String PROP_KEY_CHARSET = "fastdfs.charset";
  public static final String PROP_KEY_HTTP_ANTI_STEAL_TOKEN = "fastdfs.http_anti_steal_token";
  public static final String PROP_KEY_HTTP_SECRET_KEY = "fastdfs.http_secret_key";
  public static final String PROP_KEY_HTTP_TRACKER_HTTP_PORT = "fastdfs.http_tracker_http_port";
  public static final String PROP_KEY_TRACKER_SERVERS = "fastdfs.tracker_servers";
  public static final String PROP_KEY_CONNECT_FIRST_BY = "fastdfs.connect_first_by";

  public static final String PROP_KEY_CONNECTION_POOL_ENABLED = "fastdfs.connection_pool.enabled";
  public static final String PROP_KEY_CONNECTION_POOL_MAX_COUNT_PER_ENTRY = "fastdfs.connection_pool.max_count_per_entry";
  public static final String PROP_KEY_CONNECTION_POOL_MAX_IDLE_TIME = "fastdfs.connection_pool.max_idle_time";
  public static final String PROP_KEY_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS = "fastdfs.connection_pool.max_wait_time_in_ms";

  public static final String PROP_KEY_HAVE_ALLOW_EMPTY_FIELD = "fastdfs.fetch_storage_ids.have_allow_empty_field";

  public static final int DEFAULT_CONNECT_TIMEOUT = 5; //second
  public static final int DEFAULT_NETWORK_TIMEOUT = 30; //second
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final boolean DEFAULT_HTTP_ANTI_STEAL_TOKEN = false;
  public static final String DEFAULT_HTTP_SECRET_KEY = "FastDFS1234567890";
  public static final int DEFAULT_HTTP_TRACKER_HTTP_PORT = 80;

  public static final int CONNECT_FIRST_BY_TRACKER = 0;
  public static final int CONNECT_FIRST_BY_LAST_CONNECTED = 1;

  public static final boolean DEFAULT_CONNECTION_POOL_ENABLED = true;
  public static final int DEFAULT_CONNECTION_POOL_MAX_COUNT_PER_ENTRY = 100;
  public static final int DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME = 3600 ;//second
  public static final int DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS = 1000 ;//millisecond

  public static int g_connect_timeout = DEFAULT_CONNECT_TIMEOUT * 1000; //millisecond
  public static int g_network_timeout = DEFAULT_NETWORK_TIMEOUT * 1000; //millisecond
  public static String g_charset = DEFAULT_CHARSET;
  public static boolean g_anti_steal_token = DEFAULT_HTTP_ANTI_STEAL_TOKEN; //if anti-steal token
  public static String g_secret_key = DEFAULT_HTTP_SECRET_KEY; //generage token secret key
  public static int g_tracker_http_port = DEFAULT_HTTP_TRACKER_HTTP_PORT;
  public static int g_connect_first_by = CONNECT_FIRST_BY_TRACKER;
  public static boolean g_multi_storage_ips = false;
  public static StorageAddressMap g_storages_address_map;

  public static boolean g_connection_pool_enabled = DEFAULT_CONNECTION_POOL_ENABLED;
  public static int g_connection_pool_max_count_per_entry = DEFAULT_CONNECTION_POOL_MAX_COUNT_PER_ENTRY;
  public static int g_connection_pool_max_idle_time = DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME * 1000; //millisecond
  public static int g_connection_pool_max_wait_time_in_ms = DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS; //millisecond

  public static final boolean DEFAULT_HAVE_ALLOW_EMPTY_FIELD = true;
  public static boolean g_have_allow_empty_field = DEFAULT_HAVE_ALLOW_EMPTY_FIELD;

  public static TrackerGroup g_tracker_group;

  private ClientGlobal() {
  }

  private static void loadStorageServersFromTracker() throws IOException, MyException {
      TrackerClient tracker = new TrackerClient();
      StringBuilder builder = tracker.fetchStorageIds(g_have_allow_empty_field);
      if (builder == null && g_have_allow_empty_field) {
          builder = tracker.fetchStorageIds(false);
      }

      if (builder == null || builder.length() == 0) {
          return;
      }

      boolean without_port = true;
      int count = 0;
      String[] lines = builder.toString().split("\n");
      String[] ipAddresses = new String[lines.length];
      for (String line : lines) {
          String[] cols = line.split(" ");
          if (cols.length != 3) {
              throw new MyException("invalid line: " + line);
          }

          String ipAddrs = cols[2];
          if (ipAddrs.indexOf(',') > 0) {
              ipAddresses[count++] = ipAddrs;
          }
      }

      if (count == 0) {
          return;
      }

      int startIndex;
      if (ipAddresses[0].charAt(0) == '[') {  //IPv6
          if ((startIndex=ipAddresses[0].indexOf(']')) < 0) {
              throw new MyException("invalid IPv6 address: " + ipAddresses[0]);
          }
      } else {
          startIndex = 0;
      }
      if (ipAddresses[0].indexOf(':', startIndex) > 0) {
          without_port = false;
      }

      g_multi_storage_ips = true;
      g_storages_address_map = new StorageAddressMap(without_port);
      if (without_port) {
          for (String ipAddr: ipAddresses) {
              if (ipAddr.charAt(0) == '[') {  //IPv6
                  ipAddr = ipAddr.substring(1, ipAddr.length() - 1);
              }
              String[] cols = ipAddr.split(",");
              g_storages_address_map.puts(cols[0], cols[1]);
          }
      } else {
          for (String ipPort: ipAddresses) {
              int colonIndex = ipPort.lastIndexOf(':');
              if (colonIndex < 0) {
                  throw new MyException("invalid ip and port: " + ipPort);
              }

              String ipAddr = ipPort.substring(0, colonIndex);
              int port = Integer.parseInt(ipPort.substring(colonIndex + 1));

              if (ipAddr.charAt(0) == '[') {  //IPv6
                  ipAddr = ipAddr.substring(1, ipAddr.length() - 1);
              }
              String[] cols = ipAddr.split(",");
              g_storages_address_map.puts(cols[0], cols[1], port);
          }
      }
  }

  /**
   * load global variables
   *
   * @param conf_filename config filename
   */
  public static void init(String conf_filename) throws IOException, MyException {
    IniFileReader iniReader;
    String[] szTrackerServers;
    String[] parts;

    iniReader = new IniFileReader(conf_filename);

    g_connect_timeout = iniReader.getIntValue("connect_timeout", DEFAULT_CONNECT_TIMEOUT);
    if (g_connect_timeout < 0) {
      g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
    }
    g_connect_timeout *= 1000; //millisecond

    g_network_timeout = iniReader.getIntValue("network_timeout", DEFAULT_NETWORK_TIMEOUT);
    if (g_network_timeout < 0) {
      g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
    }
    g_network_timeout *= 1000; //millisecond

    g_charset = iniReader.getStrValue("charset");
    if (g_charset == null || g_charset.length() == 0) {
      g_charset = "ISO8859-1";
    }

    szTrackerServers = iniReader.getValues("tracker_server");
    if (szTrackerServers == null) {
      throw new MyException("item \"tracker_server\" in " + conf_filename + " not found");
    }

    InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
    for (int i = 0; i < szTrackerServers.length; i++) {
      if (szTrackerServers[i].contains("[")) {
        parts = new String[2];
        parts[0] = szTrackerServers[i].substring(1, szTrackerServers[i].indexOf("]"));
        parts[1] = szTrackerServers[i].substring(szTrackerServers[i].lastIndexOf(":") + 1);
      } else {
        parts = szTrackerServers[i].split("\\:", 2);
      }

      if (parts.length != 2) {
        throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
      }

      tracker_servers[i] = new InetSocketAddress(InetAddress.getByName(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }
    g_tracker_group = new TrackerGroup(tracker_servers);

    String connect_first_by = iniReader.getStrValue("connect_first_by");
    if (connect_first_by != null && connect_first_by.equalsIgnoreCase("last-connected")) {
        g_connect_first_by = CONNECT_FIRST_BY_LAST_CONNECTED;
    }

    g_tracker_http_port = iniReader.getIntValue("http.tracker_http_port", 80);
    g_anti_steal_token = iniReader.getBoolValue("http.anti_steal_token", false);
    if (g_anti_steal_token) {
      g_secret_key = iniReader.getStrValue("http.secret_key");
    }
    g_connection_pool_enabled = iniReader.getBoolValue("connection_pool.enabled", DEFAULT_CONNECTION_POOL_ENABLED);
    g_connection_pool_max_count_per_entry = iniReader.getIntValue("connection_pool.max_count_per_entry", DEFAULT_CONNECTION_POOL_MAX_COUNT_PER_ENTRY);
    g_connection_pool_max_idle_time = iniReader.getIntValue("connection_pool.max_idle_time", DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME);
    if (g_connection_pool_max_idle_time < 0) {
      g_connection_pool_max_idle_time = DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME;
    }
    g_connection_pool_max_idle_time *= 1000;
    g_connection_pool_max_wait_time_in_ms = iniReader.getIntValue("connection_pool.max_wait_time_in_ms", DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS);
    if (g_connection_pool_max_wait_time_in_ms < 0) {
      g_connection_pool_max_wait_time_in_ms = DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS;
    }

    g_have_allow_empty_field = iniReader.getBoolValue("fetch_storage_ids.have_allow_empty_field", DEFAULT_HAVE_ALLOW_EMPTY_FIELD);
    loadStorageServersFromTracker();
  }

  /**
   * load from properties file
   *
   * @param propsFilePath properties file path, eg:
   *                      "fastdfs-client.properties"
   *                      "config/fastdfs-client.properties"
   *                      "/opt/fastdfs-client.properties"
   *                      "C:\\Users\\James\\config\\fastdfs-client.properties"
   *                      properties文件至少包含一个配置项 fastdfs.tracker_servers 例如：
   *                      fastdfs.tracker_servers = 10.0.11.245:22122,10.0.11.246:22122
   *                      server的IP和端口用冒号':'分隔
   *                      server之间用逗号','分隔
   */
  public static void initByProperties(String propsFilePath) throws IOException, MyException {
    Properties props = new Properties();
    InputStream in = IniFileReader.loadFromOsFileSystemOrClasspathAsStream(propsFilePath);
    if (in != null) {
      props.load(in);
    }
    initByProperties(props);
  }

  public static void initByProperties(Properties props) throws IOException, MyException {
    String trackerServersConf = props.getProperty(PROP_KEY_TRACKER_SERVERS);
    if (trackerServersConf == null || trackerServersConf.trim().length() == 0) {
      throw new MyException(String.format("configure item %s is required", PROP_KEY_TRACKER_SERVERS));
    }
    initByTrackers(trackerServersConf.trim());

    String connectTimeoutInSecondsConf = props.getProperty(PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS);
    String networkTimeoutInSecondsConf = props.getProperty(PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS);
    String charsetConf = props.getProperty(PROP_KEY_CHARSET);
    String httpAntiStealTokenConf = props.getProperty(PROP_KEY_HTTP_ANTI_STEAL_TOKEN);
    String httpSecretKeyConf = props.getProperty(PROP_KEY_HTTP_SECRET_KEY);
    String httpTrackerHttpPortConf = props.getProperty(PROP_KEY_HTTP_TRACKER_HTTP_PORT);

    String connectFirstBy = props.getProperty(PROP_KEY_CONNECT_FIRST_BY);
    String poolEnabled = props.getProperty(PROP_KEY_CONNECTION_POOL_ENABLED);
    String poolMaxCountPerEntry = props.getProperty(PROP_KEY_CONNECTION_POOL_MAX_COUNT_PER_ENTRY);
    String poolMaxIdleTime  = props.getProperty(PROP_KEY_CONNECTION_POOL_MAX_IDLE_TIME);
    String poolMaxWaitTimeInMS = props.getProperty(PROP_KEY_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS);
    if (connectTimeoutInSecondsConf != null && connectTimeoutInSecondsConf.trim().length() != 0) {
      g_connect_timeout = Integer.parseInt(connectTimeoutInSecondsConf.trim()) * 1000;
    }
    if (networkTimeoutInSecondsConf != null && networkTimeoutInSecondsConf.trim().length() != 0) {
      g_network_timeout = Integer.parseInt(networkTimeoutInSecondsConf.trim()) * 1000;
    }
    if (charsetConf != null && charsetConf.trim().length() != 0) {
      g_charset = charsetConf.trim();
    }
    if (httpAntiStealTokenConf != null && httpAntiStealTokenConf.trim().length() != 0) {
      g_anti_steal_token = Boolean.parseBoolean(httpAntiStealTokenConf);
    }
    if (httpSecretKeyConf != null && httpSecretKeyConf.trim().length() != 0) {
      g_secret_key = httpSecretKeyConf.trim();
    }
    if (httpTrackerHttpPortConf != null && httpTrackerHttpPortConf.trim().length() != 0) {
      g_tracker_http_port = Integer.parseInt(httpTrackerHttpPortConf);
    }

    if (connectFirstBy != null && connectFirstBy.equalsIgnoreCase("last-connected")) {
        g_connect_first_by = CONNECT_FIRST_BY_LAST_CONNECTED;
    }
    if (poolEnabled != null && poolEnabled.trim().length() != 0) {
      g_connection_pool_enabled = Boolean.parseBoolean(poolEnabled);
    }
    if (poolMaxCountPerEntry != null && poolMaxCountPerEntry.trim().length() != 0 ) {
      g_connection_pool_max_count_per_entry = Integer.parseInt(poolMaxCountPerEntry);
    }
    if (poolMaxIdleTime != null && poolMaxIdleTime.trim().length() != 0) {
      g_connection_pool_max_idle_time = Integer.parseInt(poolMaxIdleTime) * 1000;
    }
    if (poolMaxWaitTimeInMS != null && poolMaxWaitTimeInMS.trim().length() != 0) {
      g_connection_pool_max_wait_time_in_ms = Integer.parseInt(poolMaxWaitTimeInMS);
    }

    String haveAllowEmptyField = props.getProperty(PROP_KEY_HAVE_ALLOW_EMPTY_FIELD);
    if (haveAllowEmptyField != null && haveAllowEmptyField.trim().length() != 0) {
      g_have_allow_empty_field = Boolean.parseBoolean(haveAllowEmptyField);
    }

    loadStorageServersFromTracker();
  }

  /**
   * load from properties file
   *
   * @param trackerServers 例如："10.0.11.245:22122,10.0.11.246:22122"
   *                       server的IP和端口用冒号':'分隔
   *                       server之间用逗号','分隔
   */
  public static void initByTrackers(String trackerServers) throws IOException, MyException {
    List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
    String spr1 = ",";
    String spr2 = ":";
    String[] arr1 = trackerServers.trim().split(spr1);
    for (String addrStr : arr1) {
      if(addrStr.contains("[")) {
        String host = addrStr.substring(1, addrStr.indexOf("]"));
        int port = Integer.parseInt(addrStr.substring(addrStr.lastIndexOf(":") + 1));
        list.add(new InetSocketAddress(InetAddress.getByName(host), port));
      } else {
        String[] arr2 = addrStr.trim().split(spr2);
        String host = arr2[0].trim();
        int port = Integer.parseInt(arr2[1].trim());
        list.add(new InetSocketAddress(InetAddress.getByName(host), port));
      }
    }
    InetSocketAddress[] trackerAddresses = list.toArray(new InetSocketAddress[list.size()]);
    initByTrackers(trackerAddresses);
  }

  public static void initByTrackers(InetSocketAddress[] trackerAddresses) throws IOException, MyException {
    g_tracker_group = new TrackerGroup(trackerAddresses);
  }

  /**
   * construct Socket object
   *
   * @param ip_addr ip address or hostname
   * @param port    port number
   * @return connected Socket object
   */
  public static Socket getSocket(String ip_addr, int port) throws IOException {
    Socket sock = new Socket();
    sock.setSoTimeout(ClientGlobal.g_network_timeout);
    sock.connect(new InetSocketAddress(InetAddress.getByName(ip_addr), port), ClientGlobal.g_connect_timeout);
    return sock;
  }

  /**
   * construct Socket object
   *
   * @param addr InetSocketAddress object, including ip address and port
   * @return connected Socket object
   */
  public static Socket getSocket(InetSocketAddress addr) throws IOException {
    Socket sock = new Socket();
    sock.setReuseAddress(true);
    sock.setSoTimeout(ClientGlobal.g_network_timeout);
    sock.connect(addr, ClientGlobal.g_connect_timeout);
    return sock;
  }

  public static int getG_connect_timeout() {
    return g_connect_timeout;
  }

  public static void setG_connect_timeout(int connect_timeout) {
    ClientGlobal.g_connect_timeout = connect_timeout;
  }

  public static int getG_network_timeout() {
    return g_network_timeout;
  }

  public static void setG_network_timeout(int network_timeout) {
    ClientGlobal.g_network_timeout = network_timeout;
  }

  public static String getG_charset() {
    return g_charset;
  }

  public static void setG_charset(String charset) {
    ClientGlobal.g_charset = charset;
  }

  public static int getG_tracker_http_port() {
    return g_tracker_http_port;
  }

  public static void setG_tracker_http_port(int tracker_http_port) {
    ClientGlobal.g_tracker_http_port = tracker_http_port;
  }

  public static boolean getG_anti_steal_token() {
    return g_anti_steal_token;
  }

  public static boolean isG_anti_steal_token() {
    return g_anti_steal_token;
  }

  public static void setG_anti_steal_token(boolean anti_steal_token) {
    ClientGlobal.g_anti_steal_token = anti_steal_token;
  }

  public static String getG_secret_key() {
    return g_secret_key;
  }

  public static void setG_secret_key(String secret_key) {
    ClientGlobal.g_secret_key = secret_key;
  }

  public static TrackerGroup getG_tracker_group() {
    return g_tracker_group;
  }

  public static void setG_tracker_group(TrackerGroup tracker_group) {
    ClientGlobal.g_tracker_group = tracker_group;
  }

  public static boolean isG_connection_pool_enabled() {
    return g_connection_pool_enabled;
  }

  public static String configInfo() {
    String trackerServers = "";
    if (g_tracker_group != null) {
      InetSocketAddress[] trackerAddresses = g_tracker_group.tracker_servers;
      for (InetSocketAddress inetSocketAddress : trackerAddresses) {
        if(trackerServers.length() > 0) trackerServers += ",";
        String address = inetSocketAddress.toString();
        trackerServers += address.startsWith("/") ? address.substring(1) : address;
      }
    }
    return "{"
      + "\n  g_connect_timeout(ms) = " + g_connect_timeout
      + "\n  g_network_timeout(ms) = " + g_network_timeout
      + "\n  g_charset = " + g_charset
      + "\n  g_anti_steal_token = " + g_anti_steal_token
      + "\n  g_secret_key = " + g_secret_key
      + "\n  g_tracker_http_port = " + g_tracker_http_port
      + "\n  g_multi_storage_ips = " + g_multi_storage_ips
      + "\n  g_connect_first_by = " + (g_connect_first_by == CONNECT_FIRST_BY_TRACKER ? "tracker" : "last-connected")
      + "\n  g_connection_pool_enabled = " + g_connection_pool_enabled
      + "\n  g_connection_pool_max_count_per_entry = " + g_connection_pool_max_count_per_entry
      + "\n  g_connection_pool_max_idle_time(ms) = " + g_connection_pool_max_idle_time
      + "\n  g_connection_pool_max_wait_time_in_ms(ms) = " + g_connection_pool_max_wait_time_in_ms
      + "\n  g_have_allow_empty_field = " + g_have_allow_empty_field
      + "\n  trackerServers = " + trackerServers
      + "\n}";
  }

}
