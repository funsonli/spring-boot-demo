package com.funsonli.springbootdemo522uploader.component.uploader;

import java.io.InputStream;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/10/30
 */
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
