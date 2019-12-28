package org.csource.fastdfs;

import org.csource.common.NameValuePair;

import java.net.InetSocketAddress;

public class Test1 {
    public static void main(String args[]) {
        try {

            if (args.length < 1) {
                System.out.println("Usage: 2 parameters, one is config filename, "
                        + "the other is the local filename to upload");
                return;
            }

            System.out.println("java.version=" + System.getProperty("java.version"));

            String conf_filename = args[0];
            String local_filename;
            String ext_name;
            if (args.length > 1) {
                local_filename = args[1];
                ext_name = null;
            }
            else if (System.getProperty("os.name").equalsIgnoreCase("windows")) {
                local_filename = "c:/windows/system32/notepad.exe";
                ext_name = "exe";
            } else {
                local_filename = "/etc/hosts";
                ext_name = "";
            }

            ClientGlobal.init(conf_filename);
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            TrackerGroup tg = new TrackerGroup(new InetSocketAddress[]{new InetSocketAddress("47.95.221.159", 22122)});
            TrackerClient tc = new TrackerClient(tg);

            TrackerServer ts = tc.getTrackerServer();
            if (ts == null) {
                System.out.println("getTrackerServer return null");
                return;
            }

            StorageServer ss = tc.getStoreStorage(ts);
            if (ss == null) {
                System.out.println("getStoreStorage return null");
            }

            StorageClient1 sc1 = new StorageClient1(ts, ss);

            NameValuePair[] meta_list = null;  //new NameValuePair[0];
            String fileid = sc1.upload_file1(local_filename, ext_name, meta_list);
            System.out.println("Upload local file " + local_filename + " ok, fileid: " + fileid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
