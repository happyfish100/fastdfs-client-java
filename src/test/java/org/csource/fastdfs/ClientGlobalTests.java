package org.csource.fastdfs;

import java.util.Properties;

/**
 * Created by James on 2017/5/19.
 */
public class ClientGlobalTests {

  public static void main(String[] args) throws Exception {
    String trackerServers = "192.168.108.50:22122";
    ClientGlobal.initByTrackers(trackerServers);
    System.out.println("ClientGlobal.configInfo() : " + ClientGlobal.configInfo());

    String propFilePath = "fastdfs-client.properties";
    ClientGlobal.initByProperties(propFilePath);
    System.out.println("ClientGlobal.configInfo() : " + ClientGlobal.configInfo());

    Properties props = new Properties();
    props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "192.168.108.50:22122");
    ClientGlobal.initByProperties(props);
    System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());

  }

}
