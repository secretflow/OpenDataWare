package com.cec.utils;
import java.io.InputStream;

/**
 * 为了方便演示的工具类，可以支持加载内置的元件表模型、元件生产模型等
 * @author koala
 */
public class ResourceFile {

    /**
     * 资源文件的根路径
     */
    private final String rootPath;

    /**
     * 资源文件构造函数
     * @param rootPath 资源文件的根路径
     */
    public ResourceFile(String rootPath){
        this.rootPath = rootPath;
    }

    /**
     * 获取资源文件的根路径
     * @return 资源文件的根路径
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * 获取资源文件的内容
     * @param fileName 文件名称
     * @return 资源文件的内容
     */
    public String getContent(String fileName) {
        InputStream inputStream = ResourceFile.class.getResourceAsStream(this.rootPath + "/" + fileName);
        FileUtilityUtil fileUtilityUtil = FileUtilityUtil.getFileReader(inputStream);
        return fileUtilityUtil.getAll();
    }

}
