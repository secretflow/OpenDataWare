package com.cec.examine.template;

import java.util.List;

/**
 * 支持从字典文件或者SDK资源文件获取安全识别、质检的配置表
 */
public class ConfigFactory<T extends AbstractConfigLoader> {

    public static String REMOTE = "remote";
    public static String RESOURCE_FILE = "resource_file";

    public List loadConfigs(String type, Class<T> clazz) {
        T configLoader = null;
        try {
            configLoader = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(REMOTE.equals(type)) {
            return configLoader.loadFromRemote();
        } else if(RESOURCE_FILE.equals(type)) {
            return configLoader.loadFromResourceFile();
        } else
            return null;
    }
}
