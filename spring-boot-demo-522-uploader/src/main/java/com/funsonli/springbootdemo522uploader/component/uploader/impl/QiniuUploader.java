package com.funsonli.springbootdemo522uploader.component.uploader.impl;

import com.funsonli.springbootdemo522uploader.component.uploader.Uploader;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
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