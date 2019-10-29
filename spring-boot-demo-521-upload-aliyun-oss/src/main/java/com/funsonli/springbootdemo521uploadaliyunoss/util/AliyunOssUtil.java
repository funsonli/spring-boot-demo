package com.funsonli.springbootdemo521uploadaliyunoss.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * https://developer.qiniu.com/kodo/sdk/1239/java
 *
 * @author Funsonli
 * @date 2019/10/29
 */
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