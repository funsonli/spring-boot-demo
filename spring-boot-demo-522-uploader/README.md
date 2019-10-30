# Spring Boot入门样例-521-uploader整合七牛云和阿里云OSS上传图片

> 如果项目使用多个云存储，我们可以在配置文件中配置多个。本demo演示使用工厂模式无缝切换。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.aliyun.oss</groupId>
            <artifactId>aliyun-sdk-oss</artifactId>
            <version>3.4.2</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
        <dependency>
            <groupId>com.qiniu</groupId>
            <artifactId>qiniu-java-sdk</artifactId>
            <version>[7.2.0, 7.2.99]</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>4.6.4</version>
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
uploader:
  type: 2
  qiniu:
    accessKey: 0u4ZFxeExxxx
    secretKey: 3bqtpy8xxxx
    bucket: jiajiayoupin
    domain: http://img.xxx.com
    region: -1
  aliyun:
    address: http://static.xxxx.com
    end-point: oss-cn-shenzhen.aliyuncs.com
    access-key: LTAIxxxx
    secret-key: z80txxx
    bucket: haixxxx

```

### 代码解析

UploaderFactory.java 如下 工厂模式

```
@Slf4j
@Component
public class UploaderFactory {
    @Value("${uploader.type}")
    private Integer type;

    @Autowired
    private QiniuUploader qiniuUploader;

    @Autowired
    private AliyunOssUploader aliyunOssUploader;

    /**
     * 
     * @author Funsonli
     * @date 2019/10/30
     * @return
     */
    public Uploader getUploader() {
        if (type == 1) {
            return qiniuUploader;
        } else if (type == 2) {
            return aliyunOssUploader;
        } else{
            throw new RuntimeException("暂不支持该配置，请检查");
        }
    }

    /**
     *
     * @author Funsonli
     * @date 2019/10/30
     * @return
     */
    public Uploader getUploader(Integer type) {
        this.type = type;
        return getUploader();
    }
}
```

UploaderFactory.java 如下 工厂模式

```
public interface Uploader {
    /**
     *
     * @author Funsonli
     * @date 2019/10/30
     * @param file 文件流
     * @param key
     * @return
     */
    String uploadInputStream(InputStream file, String key);

}
```

AliyunOssUploader.java 如下
```
@Slf4j
@Component
public class AliyunOssUploader implements Uploader {

    /** oss处访问图片的 url */
    @Value("${uploader.aliyun.address}")
    private String address;

    /** oss的 endpoint */
    @Value("${uploader.aliyun.end-point}")
    private String endPoint;

    @Value("${uploader.aliyun.access-key}")
    private String accessKey;

    @Value("${uploader.aliyun.secret-key}")
    private String secretKey;

    /** oss的储存名称 */
    @Value("${uploader.aliyun.bucket}")
    private String bucket;

    /**
     * 文件流上传
     * @param file 文件名
     * @param key  文件名
     * @return
     */
    @Override
    public String uploadInputStream(InputStream file, String key) {

        OSSClient ossClient = new OSSClient(endPoint, new DefaultCredentialProvider(accessKey, secretKey), null);
        try {
            ossClient.putObject(bucket, key, file);
            ossClient.shutdown();
            return address + "/" + key;
        } catch (Exception ex) {
            throw new RuntimeException("上传文件出错，请检查配置" + ex.toString());
        }
    }

}
```

QiniuUploader.java 如下
``` 
@Slf4j
@Component
public class QiniuUploader implements Uploader {

    /**
     * 生成上传凭证，然后准备上传
     */
    @Value("${uploader.qiniu.accessKey}")
    private String accessKey;

    @Value("${uploader.qiniu.secretKey}")
    private String secretKey;

    @Value("${uploader.qiniu.bucket}")
    private String bucket;

    @Value("${uploader.qiniu.domain}")
    private String domain;

    @Value("${uploader.qiniu.region}")
    private Integer region;

    /**
     * 华东	Region.region0(), Region.huadong()
     * 华北	Region.region1(), Region.huabei()
     * 华南	Region.region2(), Region.huanan()
     * 北美	Region.regionNa0(), Region.beimei()
     * 东南亚	Region.regionAs0(), Region.xinjiapo()
     */
    public Configuration getConfiguration(){

        Configuration cfg = null;
        switch (region) {
            case 0:
                cfg = new Configuration(Region.region0());
                break;
            case 1:
                cfg = new Configuration(Region.region1());
                break;
            case 2:
                cfg = new Configuration(Region.region2());
                break;
            case 3:
                cfg = new Configuration(Region.regionNa0());
                break;
            case 4:
                cfg = new Configuration(Region.regionAs0());
                break;
            default:
                cfg = new Configuration(Region.autoRegion());
        }

        return cfg;
    }

    /**
     * 文件流上传
     * @param file 文件名
     * @param key  文件名
     * @return
     */
    @Override
    public String uploadInputStream(InputStream file, String key) {

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            UploadManager uploadManager = new UploadManager(getConfiguration());
            Response response = uploadManager.put(file, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return domain + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new RuntimeException("上传文件出错，请检查七牛云配置，" + r.toString());
        }
    }

    /**
     * 以UUID重命名
     *
     * @param fileName
     * @return
     */
    public String rename(String fileName) {
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extName;
    }
}
```

CommonController.java 如下 upload直接上传文件
``` 
@Slf4j
@RestController
@RequestMapping("/bootan/common")
public class CommonController {
    @Autowired
    private UploaderFactory uploaderFactory;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public BaseResult upload(@RequestParam(required = false) MultipartFile file,
                             HttpServletRequest request) {

        String result = null;
        String fileName = rename(file.getOriginalFilename());
        try {
            InputStream inputStream = file.getInputStream();
            result = uploaderFactory.getUploader().uploadInputStream(inputStream, fileName);
        } catch (Exception e) {
            log.error(e.toString());
            return BaseResult.error(e.toString());
        }

        return BaseResult.success(result);
    }

    /**
     * 以UUID重命名
     *
     * @param fileName
     * @return
     */
    public String rename(String fileName) {
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extName;
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

在postman中通过如下方式上传

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-520-01.png?raw=true)

修改配置文件中的type，再次上传，到了其他云存储。


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- 七牛官方SDK Java https://developer.qiniu.com/kodo/sdk/1239/java#upload-config
- 阿里云官方SDK Java https://help.aliyun.com/document_detail/84781.html


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

