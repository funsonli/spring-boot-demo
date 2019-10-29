# Spring Boot入门样例-520-upload-qiniu整合七牛云上传图片

> 图片通过七牛云等云存储系统，可以让用户更快的。本demo演示如何演示如何使用@CachePut 和 @Cacheable缓存数据。

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
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
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
qiniu:
  accessKey: 0u4ZXXXXXXXXXXXXXXXXXXX
  secretKey: 3bqtpyXXXXXXXXXXXXXXXXXXXXXXX
  bucket: jiajiayoupin
  domain: img.jiajiayoupin.com
  region: -1

```

### 代码解析

QiniuUtil.java StudentDao继承MongoRepository<Student, String>
```
@Slf4j
@Component
public class QiniuUtil {

    /**
     * 生成上传凭证，然后准备上传
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    @Value("${qiniu.region}")
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

StudentController.java 如下 upload直接上传文件
``` 
@Slf4j
@RestController
@RequestMapping("/bootan/common")
public class CommonController {
    @Autowired
    private QiniuUtil qiniuUtil;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public BaseResult upload(@RequestParam(required = false) MultipartFile file,
                             HttpServletRequest request) {

        String result = null;
        String fileName = qiniuUtil.rename(file.getOriginalFilename());
        try {
            InputStream inputStream = file.getInputStream();
            result = qiniuUtil.uploadInputStream(inputStream,fileName);
        } catch (Exception e) {
            log.error(e.toString());
            return BaseResult.error(e.toString());
        }

        return BaseResult.success("http://" + result);
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

在postman中通过如下方式上传

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-520-01.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

