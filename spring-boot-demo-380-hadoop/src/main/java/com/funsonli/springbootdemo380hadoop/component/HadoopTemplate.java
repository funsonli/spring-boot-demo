package com.funsonli.springbootdemo380hadoop.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/16
 */
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