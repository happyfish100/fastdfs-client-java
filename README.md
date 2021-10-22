# FastDFS java client SDK

FastDFS Java Client API may be copied only under the terms of the BSD license.

## 使用 maven 仓库

已经发布到中央仓库，在 maven 项目的 `pom.xml` 中添加依赖：

```xml

<dependency>
  <!-- TODO 主仓库 groupId -->
  <groupId>io.github.rui8832</groupId>
  <artifactId>fastdfs-client-java</artifactId>
  <version>1.29-20211022</version>
</dependency>
```

如果需要要使用 SNAPSHOT 版，需要在 `pom.xml` 中添加：

```xml

<project>
  <dependencies>
    <dependency>
      <!-- TODO 主仓库 groupId -->
      <groupId>io.github.rui8832</groupId>
      <artifactId>fastdfs-client-java</artifactId>
      <version>1.29-SNAPSHOT</version>
    </dependency>
  </dependencies>

  <repositories>
    <repository>
      <id>ossrh-snapshot</id>
      <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
      <releases>
        <enabled>false</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </snapshots>
    </repository>
  </repositories>
</project>
```

注：

* 版本 `1.29-20211022` 是“正式”的 `1.29-SNAPSHOT` 版。
* 为了通过发布到中央仓库的验证流程，我使用我了 fork 的 `groupId` 进行发布，故中央仓库的 `groupId` 与作者不一致。
* 主仓库要发布到 maven 中央仓库需要维护者 happyfish100 注册 sonatype 帐号，生成 GPG，完成验证和发布，届时可以使用作者发布的版本。

### 构建并发布到中央仓库

维护者 happyfish100 注册 sonatype 帐号，生成 GPG，并在 `settings.xml` 中填入注册的帐号和 GPG 信息。

执行全局替换，将 `rui8832` 替换为 `happyfish100`。

然后执行下面的命令发布到中央仓库：

```
mvn -e -X -B -U -s settings.xml -P ossrh clean deploy
```

发布完成后一般 2~3 内同步到中央仓库。

## 使用源码构建

**使用 ant 从源码构建**

```
ant clean package
```

**使用 maven 从源码安装**

```
mvn clean install
```

**使用 maven 从 jar 文件安装**

```
mvn install:install-file -DgroupId=org.csource -DartifactId=fastdfs-client-java -Dversion=${version} -Dpackaging=jar -Dfile=fastdfs-client-java-${version}.jar
```

构建安装完成后，在您的 maven 项目的 `pom.xml` 中添加依赖：

```xml

<dependency>
  <groupId>org.csource</groupId>
  <artifactId>fastdfs-client-java</artifactId>
  <version>1.29-SNAPSHOT</version>
</dependency>
```

## `.conf` 配置文件、所在目录、加载优先顺序

配置文件名 `fdfs_client.conf` (或使用其它文件名 `xxx_yyy.conf`)。

文件所在位置可以是项目 `classpath` (或OS文件系统目录比如 `/opt/`):

```
/opt/fdfs_client.conf
C:\Users\James\config\fdfs_client.conf
```

优先按OS文件系统路径读取，没有找到才查找项目 `classpath`，尤其针对linux环境下的相对路径比如：

```
fdfs_client.conf
config/fdfs_client.conf
```

配置文件示例：

```
connect_timeout = 2
network_timeout = 30
charset = UTF-8
http.tracker_http_port = 80
http.anti_steal_token = no
http.secret_key = FastDFS1234567890

tracker_server = 10.0.11.247:22122
tracker_server = 10.0.11.248:22122
tracker_server = 10.0.11.249:22122

connection_pool.enabled = true
connection_pool.max_count_per_entry = 500
connection_pool.max_idle_time = 3600
connection_pool.max_wait_time_in_ms = 1000
```

注意事项：

* `tracker_server` 指向您自己IP地址和端口，`1-n` 个
* 除了 `tracker_server`，其它配置项都是可选的

## `.properties` 配置文件、所在目录、加载优先顺序

配置文件名 `fastdfs-client.properties`(或使用其它文件名 `xxx-yyy.properties`)。

文件所在位置可以是项目 `classpath`(或OS文件系统目录比如 `/opt/`):

```
/opt/fastdfs-client.properties
C:\Users\James\config\fastdfs-client.properties
```

优先按OS文件系统路径读取，没有找到才查找项目 `classpath`，尤其针对linux环境下的相对路径比如：

```
fastdfs-client.properties
config/fastdfs-client.properties
```

配置文件示例：

```
fastdfs.connect_timeout_in_seconds = 5
fastdfs.network_timeout_in_seconds = 30
fastdfs.charset = UTF-8
fastdfs.http_anti_steal_token = false
fastdfs.http_secret_key = FastDFS1234567890
fastdfs.http_tracker_http_port = 80

fastdfs.tracker_servers = 10.0.11.201:22122,10.0.11.202:22122,10.0.11.203:22122

fastdfs.connection_pool.enabled = true
fastdfs.connection_pool.max_count_per_entry = 500
fastdfs.connection_pool.max_idle_time = 3600
fastdfs.connection_pool.max_wait_time_in_ms = 1000
```

注意事项：

* `.properties` 配置文件中属性名跟 `conf` 配置文件不尽相同，并且统一加前缀"`fastdfs.`"，便于整合到用户项目配置文件
* `fastdfs.tracker_servers` 配置项不能重复属性名，多个 `tracker_server` 用半角逗号 "`,`" 隔开
* 除了 `fastdfs.tracker_servers` 配置项以外的其它配置项都是可选的

## 加载配置示例

加载原 `.conf` 格式文件配置：

```
    ClientGlobal.init("fdfs_client.conf");
    ClientGlobal.init("config/fdfs_client.conf");
    ClientGlobal.init("/opt/fdfs_client.conf");
    ClientGlobal.init("C:\\Users\\James\\config\\fdfs_client.conf");
```

加载 `.properties` 格式文件配置：

```
    ClientGlobal.initByProperties("fastdfs-client.properties");
    ClientGlobal.initByProperties("config/fastdfs-client.properties");
    ClientGlobal.initByProperties("/opt/fastdfs-client.properties");
    ClientGlobal.initByProperties("C:\\Users\\James\\config\\fastdfs-client.properties");
```

加载 `Properties` 对象配置：

```
    Properties props = new Properties();
    props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "10.0.11.101:22122,10.0.11.102:22122");
    ClientGlobal.initByProperties(props);
```

加载 `trackerServers` 字符串配置：

```
    String trackerServers = "10.0.11.101:22122,10.0.11.102:22122";
    ClientGlobal.initByTrackers(trackerServers);
```

## 检查加载配置结果

输出配置结果：

```
    System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
```

将会输出如下格式的配置信息：

```
ClientGlobal.configInfo(): {
  g_connect_timeout(ms) = 5000
  g_network_timeout(ms) = 30000
  g_charset = UTF-8
  g_anti_steal_token = false
  g_secret_key = FastDFS1234567890
  g_tracker_http_port = 80
  g_connection_pool_enabled = true
  g_connection_pool_max_count_per_entry = 500
  g_connection_pool_max_idle_time(ms) = 3600000
  g_connection_pool_max_wait_time_in_ms(ms) = 1000
  trackerServers = 10.0.11.101:22122,10.0.11.102:22122
}
```
