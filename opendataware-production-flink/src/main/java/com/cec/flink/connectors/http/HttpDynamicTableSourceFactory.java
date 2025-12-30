package com.cec.flink.connectors.http;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.DeserializationFormatFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.types.logical.RowType;

import java.util.HashSet;
import java.util.Set;

/**
 * Http源连接算子工厂类
 * @author koala
 */
public class HttpDynamicTableSourceFactory implements DynamicTableSourceFactory {
    
    @Override
    public String factoryIdentifier() {
        return HttpConnectorOptions.IDENTIFIER;
    }
    
    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(HttpConnectorOptions.URL);
        options.add(FactoryUtil.FORMAT); // 必须指定数据格式
        return options;
    }
    
    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(HttpConnectorOptions.METHOD);
        options.add(HttpConnectorOptions.INTERVAL);
        options.add(HttpConnectorOptions.TIMEOUT);
        options.add(HttpConnectorOptions.LISTEN_OPEN);
        return options;
    }
    
    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        FactoryUtil.TableFactoryHelper helper = 
            FactoryUtil.createTableFactoryHelper(this, context);
        // 验证参数
        helper.validate();
        // 获取配置
        ReadableConfig config = helper.getOptions();
        // 发现解码格式
        final DecodingFormat<DeserializationSchema<RowData>> decodingFormat =
            helper.discoverDecodingFormat(DeserializationFormatFactory.class, FactoryUtil.FORMAT);
        // 获取表结构
        final RowType producedDataType =
                (RowType) context.getCatalogTable().getResolvedSchema().toPhysicalRowDataType().getLogicalType();
        return new HttpDynamicTableSource(config, decodingFormat, producedDataType, context);
    }
}