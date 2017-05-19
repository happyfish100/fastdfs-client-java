
# FastDFS java client SDK

FastDFS Java Client API may be copied only under the terms of the BSD license.

## 使用ant从源码构建

```
ant clean package
```

## 使用maven从源码安装

```
mvn clean install
```

## 使用maven从jar文件安装
```
mvn install:install-file -DgroupId=org.csource -DartifactId=fastdfs-client-java -Dversion=1.27-SNAPSHOT -Dpackaging=jar -Dfile=fastdfs-client-java-${version}.jar
```

## 在您的maven项目pom.xml中添加依赖

```xml
<dependency>
    <groupId>org.csource</groupId>
    <artifactId>fastdfs-client-java</artifactId>
    <version>1.27-SNAPSHOT</version>
</dependency>
```

## 创建配置文件fdfs_client.conf(或其它文件名xxx.conf)

    文件所在位置可以是项目classpath(或OS文件系统目录比如/opt/):
    /opt/fdfs_client.conf
    C:\Users\James\config\fdfs_client.conf
    
    优先按OS文件系统路径读取，没有找到才查找项目classpath，尤其针对linux环境下的相对路径比如：
    fdfs_client.conf
    config/fdfs_client.conf

```
connect_timeout = 2
network_timeout = 30
charset = UTF-8
http.tracker_http_port = 8080
http.anti_steal_token = no
http.secret_key = FastDFS1234567890

tracker_server = 10.0.11.247:22122
tracker_server = 10.0.11.248:22122
tracker_server = 10.0.11.249:22122
```

    注：tracker_server指向您自己IP地址和端口，1-n个

