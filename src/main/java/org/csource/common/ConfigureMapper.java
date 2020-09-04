package org.csource.common;

import java.util.Properties;

/**
 * Description: 在特殊的内外网环境下，如果fdfs配置在内网，但是想通过外网操作fdfs上传文件，可以使用本配置方法来做IP映射。
 * 前提：外网映射的IP端口可达。
 * 配置格式：内网ip=外网IP:外网端口
 *
 * @author kyq
 * @version 1.0
 * @Date 2020/9/4 9:41
 */
public class ConfigureMapper {
    private static final String SYS_CONFIG_FILE = "fdfs_ip_mapper.properties";
    private static Properties sysProperties = new Properties();

    static {
        try {
            sysProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fdfs_ip_mapper.properties"));
        } catch (Exception var1) {
            System.out.println("fdfs_ip_mapper.properties not found!");
        }

    }

    public static String getProperty(String strKey) {
        String strValue = "";

        try {
            strValue = sysProperties.getProperty(strKey);
        } catch (Exception var3) {
            System.out.println("Property <" + strKey + "> not found!");
        }

        return replaceNull(strValue);
    }

    public static String replaceNull(Object obj) {
        return isEmpty(obj) ? "" : obj.toString();
    }

    public static boolean isEmpty(Object... objs) {
        if (objs == null) {
            return true;
        } else {
            Object[] var4 = objs;
            int var3 = objs.length;

            for(int var2 = 0; var2 < var3; ++var2) {
                Object obj = var4[var2];
                if (obj != null && !"".equals(obj)) {
                    return false;
                }
            }

            return true;
        }
    }
}
