# Spring Boot入门样例-380-hadoop整合hadoop大数据系统

> hadoop一个大数据开源框架,允许使用简单的编程模型在跨计算机集群的分布式环境中存储和处理大数据。本demo演示如何演示如何上传文件到hadoop中，大数据的map reduce样例请参考hadoop-demo。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> memcached 安装请参考 https://www.runoob.com/memcached/window-install-memcached.html

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>2.7.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-hdfs</artifactId>
            <version>2.7.3</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
hadoop:
  name-node: hdfs://127.0.0.1:8000
  namespace: /demo
```

### 代码解析

HadoopConfig.java 如下 
``` 
@Configuration
@ConditionalOnProperty(name="hadoop.name-node")
@Slf4j
public class HadoopConfig {

    @Value("${hadoop.name-node}")
    private String nameNode;

    @Bean("fileSystem")
    public FileSystem createFs(){
        //读取配置文件
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("dfs.replication", "1");
        // 文件系统
        FileSystem fs = null;
        // 返回指定的文件系统,如果在本地测试，需要使用此种方法获取文件系统
        try {
            URI uri = new URI(nameNode.trim());
            fs = FileSystem.get(uri, conf);
        } catch (Exception e) {
            log.error("", e);
        }
        return  fs;
    }
}
```

HadoopTemplate.java 如下 封装上传下载文件
```
@Component
@Slf4j
public class HadoopTemplate {

    @Autowired
    private FileSystem fileSystem;

    @Value("${hadoop.name-node}")
    private String nameNode;

    @Value("${hadoop.namespace}")
    private String nameSpace;

    @PostConstruct
    public void init(){
        existDir(nameSpace,true);
    }

    public void uploadFile(String srcFile){
        System.out.println(nameNode);
        System.out.println(nameSpace);
        copyFileToHDFS(false,true,srcFile,nameSpace);
    }

    public void uploadFile(String srcFile,String destPath){
        copyFileToHDFS(false,true,srcFile,destPath);
    }

    public void delFile(String fileName){
        rmdir(nameSpace,fileName) ;
    }

    public void delDir(String path){
        nameSpace = nameSpace + "/" +path;
        rmdir(path,null) ;
    }

    public void download(String fileName,String savePath){
        getFile(nameSpace+"/"+fileName,savePath);
    }


    /**
     * 创建目录
     * @param filePath
     * @param create
     * @return
     */
    public boolean existDir(String filePath, boolean create){
        boolean flag = false;
        if (StringUtils.isEmpty(filePath)){
            throw new IllegalArgumentException("filePath不能为空");
        }
        try{
            Path path = new Path(filePath);
            if (create){
                if (!fileSystem.exists(path)){
                    fileSystem.mkdirs(path);
                }
            }
            if (fileSystem.isDirectory(path)){
                flag = true;
            }
        }catch (Exception e){
            log.error("", e);
        }
        return flag;
    }

    public String[] listFile(String path) {
        String[] files = new String[0];

        if (!existDir(path, false)) {
            return files;
        }
        if(!StringUtils.isEmpty(nameNode)){
            path = nameNode + path;
        }

        FileStatus[] statuses;
        try {
            statuses = fileSystem.listStatus(new Path(path));
            Path[] listedPaths = FileUtil.stat2Paths(statuses);
            if (listedPaths != null && listedPaths.length > 0){
                files = new String[listedPaths.length];
                for (int i = 0; i < files.length; i++){
                    files[i] = listedPaths[i].toString();
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return files;
    }


    /**
     * 文件上传至 HDFS
     * @param delSrc       指是否删除源文件，true为删除，默认为false
     * @param overwrite
     * @param srcFile      源文件，上传文件路径
     * @param destPath     hdfs的目的路径
     */
    public  void copyFileToHDFS(boolean delSrc, boolean overwrite,String srcFile,String destPath) {
        // 源文件路径是Linux下的路径，如果在 windows 下测试，需要改写为Windows下的路径，比如D://hadoop/djt/weibo.txt
        Path srcPath = new Path(srcFile);

        // 目的路径
        if(!StringUtils.isEmpty(nameNode)){
            destPath = nameNode + destPath;
        }
        Path dstPath = new Path(destPath);
        // 实现文件上传
        try {
            // 获取FileSystem对象
            fileSystem.copyFromLocalFile(srcPath, dstPath);
            fileSystem.copyFromLocalFile(delSrc,overwrite,srcPath, dstPath);
            //释放资源
            //    fileSystem.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }


    /**
     * 删除文件或者文件目录
     *
     * @param path
     */
    public void rmdir(String path,String fileName) {
        try {
            System.out.println(nameNode);
            // 返回FileSystem对象
            if(!StringUtils.isEmpty(nameNode)){
                path = nameNode + path;
            }
            if(!StringUtils.isEmpty(fileName)){
                path =  path + "/" +fileName;
            }
            // 删除文件或者文件目录  delete(Path f) 此方法已经弃用
            fileSystem.delete(new Path(path),true);
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
    }

    /**
     * 从 HDFS 下载文件
     *
     * @param hdfsFile
     * @param destPath 文件下载后,存放地址
     */
    public void getFile(String hdfsFile,String destPath) {
        // 源文件路径
        if (!StringUtils.isEmpty(nameNode)) {
            hdfsFile = nameNode + hdfsFile;
        }
        Path hdfsPath = new Path(hdfsFile);
        Path dstPath = new Path(destPath);
        try {
            // 下载hdfs上的文件
            fileSystem.copyToLocalFile(hdfsPath, dstPath);
            // 释放资源
            // fs.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

}

```

SiteController.java 如下 view中存储数据，index方法中获取数据
``` 
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    HadoopTemplate hadoopTemplate;

    @GetMapping({"", "/", "index"})
    public String index() {
        StringBuilder sb = new StringBuilder();
        String[] list = hadoopTemplate.listFile("/");
        if (list.length > 0) {
            for (String str : list) {
                sb.append(str + "<br>");
            }
        } else {
            sb.append("nothing");
        }

        return sb.toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        hadoopTemplate.uploadFile("D:\\funson.txt");
        hadoopTemplate.download("/funson.txt", "D:\\temp\\");
        return "ok";
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/index 列出hadoop根目录下的文件或目录
hdfs://192.168.0.5:8000/demo
hdfs://192.168.0.5:8000/demo1
hdfs://192.168.0.5:8000/input
hdfs://192.168.0.5:8000/tmp
hdfs://192.168.0.5:8000/user

浏览器访问 http://localhost:8080/student/add/funson/35
将D:\funson.txt文件上传到hadoop服务器，再下载到本地d:\temp\funson.txt

在hadoop服务器上用可以看到funson.txt
命令行下执行  bin\hadoop.cmd -ls /demo

```

### 错误解决

1.没有连接
控制台报错
```
2019-10-16 15:12:56.600  INFO 1304 --- [           main] org.apache.hadoop.ipc.Client             : Retrying connect to server: 192.168.0.5/192.168.0.5:9000. Already tried 0 time(s); maxRetries=45
2019-10-16 15:13:16.601  INFO 1304 --- [           main] org.apache.hadoop.ipc.Client             : Retrying connect to server: 192.168.0.5/192.168.0.5:9000. Already tried 1 time(s); maxRetries=45
java.net.ConnectException: Call From DESKTOP-7EV5L7F/192.168.0.15 to 192.168.0.5:9000 failed on connection exception: java.net.ConnectException: Connection refused: no further information; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
```
无法telnet服务器9000端口

修改hosts文件，将127.0.0.1 localhost 修改为 0.0.0.0 localhost
```
# 127.0.0.1 localhost
0.0.0.0 localhost
```

2.没有权限
控制提示
```
org.apache.hadoop.security.AccessControlException: Permission denied: user=i, access=WRITE, inode="/":funson:supergroup:drwxr-xr-x
```
在 hdfs-site.xml 中添加参数 后重启
```
<property>
    <name>dfs.permissions</name>
    <value>false</value>
</property>
```

3.客户端少文件
```
Could not locate executable D:\hadoop-3.2.1\bin\winutils.exe in the Hadoop binaries.
```
没有winutils.exe这个东西。https://github.com/srccodes/hadoop-common-2.2.0-bin/blob/master/bin/winutils.exe下载放到D:\hadoop-3.2.1\bin\winutils.exe下


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

