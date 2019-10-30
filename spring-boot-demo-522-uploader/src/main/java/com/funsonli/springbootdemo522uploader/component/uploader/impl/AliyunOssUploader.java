package com.funsonli.springbootdemo522uploader.component.uploader.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.funsonli.springbootdemo522uploader.component.uploader.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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