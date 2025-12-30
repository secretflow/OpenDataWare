package com.cec.flink.connectors.delivery;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import java.util.Map;

/**
 * 元件交付算子的配置选项
 * @author koala
 */
@PublicEvolving
public class DeliveryConnectorOptions {
    public static final ConfigOption<String> DB_NAME = ConfigOptions.key("db.name").stringType().noDefaultValue().withDescription("内存数据库名称");
    public static final ConfigOption<String> DC_TYPE = ConfigOptions.key("dc.type").stringType().noDefaultValue().withDescription("元件类型：modal｜composed｜combinatorial");
    public static final ConfigOption<String> DC_ID = ConfigOptions.key("dc.id").stringType().noDefaultValue().withDescription("元件ID");
    public static final ConfigOption<String> DC_MAIN_KEY = ConfigOptions.key("dc.main.key").stringType().noDefaultValue().withDescription("元件主体标识");
    /**
     * 交付连接算子的配置选项
     * 配置方法举例：dc.delivery.load.properties='cache.enable=true;cache.size=1000;timeout=5000'
     */
    public static final ConfigOption<Map<String,String>> DC_DELIVERY_PROP = ConfigOptions.key("dc.delivery.properties").mapType().noDefaultValue().withDescription("元件交付自定义的加载项目");
    public static final ConfigOption<String> DC_DELIVERY_CLASS = ConfigOptions.key("dc.delivery.class").stringType().noDefaultValue().withDescription("元件交付加载数据的实现类");
    private DeliveryConnectorOptions() {
    }
}
