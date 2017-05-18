package org.csource.common;

/**
 * Created by James on 2017/5/16.
 */
public class IniFileReaderTests {

  public static void main(String[] args) throws Exception {
    String conf_filename = "fdfs_client.conf";
    IniFileReader iniFileReader = new IniFileReader(conf_filename);
    System.out.println("getConfFilename: " + iniFileReader.getConfFilename());
    System.out.println("connect_timeout: " + iniFileReader.getIntValue("connect_timeout", 3));
    System.out.println("network_timeout: " + iniFileReader.getIntValue("network_timeout", 45));
    System.out.println("charset: " + iniFileReader.getStrValue("charset"));
    System.out.println("http.tracker_http_port: " + iniFileReader.getIntValue("http.tracker_http_port", 8080));
    System.out.println("http.anti_steal_token: " + iniFileReader.getBoolValue("http.anti_steal_token", false));
    System.out.println("http.secret_key: " + iniFileReader.getStrValue("http.secret_key"));
    String[] tracker_servers = iniFileReader.getValues("tracker_server");
    if (tracker_servers != null) {
      System.out.println("tracker_servers.length: " + tracker_servers.length);
      for (int i = 0; i < tracker_servers.length; i++) {
        System.out.println(String.format("tracker_servers[%s]: %s", i, tracker_servers[i]));
      }
    }
  }

}
