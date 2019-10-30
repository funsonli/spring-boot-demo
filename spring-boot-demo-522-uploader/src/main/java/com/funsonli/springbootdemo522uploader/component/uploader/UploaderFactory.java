package com.funsonli.springbootdemo522uploader.component.uploader;

import com.funsonli.springbootdemo522uploader.component.uploader.impl.AliyunOssUploader;
import com.funsonli.springbootdemo522uploader.component.uploader.impl.QiniuUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/10/30
 */
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
