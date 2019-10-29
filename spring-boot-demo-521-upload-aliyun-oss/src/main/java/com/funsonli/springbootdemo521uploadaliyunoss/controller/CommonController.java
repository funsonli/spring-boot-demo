package com.funsonli.springbootdemo521uploadaliyunoss.controller;

import com.funsonli.springbootdemo521uploadaliyunoss.base.BaseResult;
import com.funsonli.springbootdemo521uploadaliyunoss.util.AliyunOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

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
