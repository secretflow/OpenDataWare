package com.cec.flink.connectors.http;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

/**
 * Http Source连接算子配置选项
 * @author koala
 */
public class HttpConnectorOptions {
    public static final String IDENTIFIER = "easy-rest";
    
    // 必需参数
    public static final ConfigOption<String> URL = 
        ConfigOptions.key("url")
            .stringType()
            .noDefaultValue()
            .withDescription("HTTP接口URL地址");
    
    // 可选参数
    public static final ConfigOption<String> METHOD = 
        ConfigOptions.key("method")
            .stringType()
            .defaultValue("GET")
            .withDescription("HTTP方法(GET/POST)");
    
    public static final ConfigOption<Long> INTERVAL = 
        ConfigOptions.key("interval")
            .longType()
            .defaultValue(5000L)
            .withDescription("轮询间隔(毫秒)");
    
    public static final ConfigOption<Integer> TIMEOUT = 
        ConfigOptions.key("timeout")
            .intType()
            .defaultValue(30000)
            .withDescription("请求超时时间(毫秒)");

    public static final ConfigOption<Boolean> LISTEN_OPEN =
            ConfigOptions.key("listen.open")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription("是否开启监听调用功能");
}