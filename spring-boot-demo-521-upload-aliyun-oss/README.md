# Spring Boot入门样例-521-upload-aliyun-oss整合阿里云OSS上传图片

> 图片通过阿里云等云存储系统，可以让用户更快的显示图片。本demo演示如何演示如何将图片上传到阿里云OSS。

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
        <!-- 阿里云OSS -->
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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
aliyun:
  oss:
    address: http://static.sxxx.com
    end-point: oss-cn-shenzhen.aliyuncs.com
    access-key: LTAxxxxxxxxxxxxxx
    secret-key: z80tbxx
    bucket: funsonli-mini

```

### 代码解析

AliyunOssUtil.java 如下
```
@Slf4j
@Component
public class AliyunOssUtil {

    /** oss处访问图片的 url */
    @Value("${aliyun.oss.address}")
    private String address;

    /** oss的 endpoint */
    @Value("${aliyun.oss.end-point}")
    private String endPoint;

    @Value("${aliyun.oss.access-key}")
    private String accessKey;

    @Value("${aliyun.oss.secret-key}")
    private String secretKey;

    /** oss的储存名称 */
    @Value("${aliyun.oss.bucket}")
    private String bucket;

    /**
     * 文件流上传
     * @param file 文件名
     * @param key  文件名
     * @return
     */
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
    private AliyunOssUtil aliyunOssUtil;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public BaseResult upload(@RequestParam(required = false) MultipartFile file,
                             HttpServletRequest request) {

        String result = null;
        String fileName = aliyunOssUtil.rename(file.getOriginalFilename());
        try {
            InputStream inputStream = file.getInputStream();
            result = aliyunOssUtil.uploadInputStream(inputStream, fileName);
        } catch (Exception e) {
            log.error(e.toString());
            return BaseResult.error(e.toString());
        }

        return BaseResult.success(result);
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

在postman中通过如下方式上传

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-520-01.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)
- 阿里云官方SDK Java https://help.aliyun.com/document_detail/84781.html


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

