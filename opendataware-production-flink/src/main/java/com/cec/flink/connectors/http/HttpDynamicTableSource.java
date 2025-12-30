package com.cec.flink.connectors.http;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.formats.common.TimestampFormat;
import org.apache.flink.formats.json.JsonRowDataDeserializationSchema;
import org.apache.flink.legacy.table.connector.source.SourceFunctionProvider;
import org.apache.flink.streaming.api.functions.source.legacy.SourceFunction;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.connector.source.LookupTableSource;
import org.apache.flink.table.connector.source.ScanTableSource;
import org.apache.flink.table.connector.source.abilities.SupportsFilterPushDown;
import org.apache.flink.table.connector.source.lookup.LookupFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.expressions.CallExpression;
import org.apache.flink.table.expressions.FieldReferenceExpression;
import org.apache.flink.table.expressions.ResolvedExpression;
import org.apache.flink.table.expressions.ValueLiteralExpression;
import org.apache.flink.table.factories.DynamicTableFactory;
import org.apache.flink.table.functions.LookupFunction;
import org.apache.flink.table.types.logical.RowType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http源连接算子类
 * @author koala
 */
public class HttpDynamicTableSource implements ScanTableSource, LookupTableSource, SupportsFilterPushDown {

    private final ReadableConfig config;
    private final DecodingFormat<DeserializationSchema<RowData>> decodingFormat;
    private final RowType producedDataType;
    private List<ResolvedExpression> pushedFilters = new ArrayList<>();
    private final DynamicTableFactory.Context context;

    public HttpDynamicTableSource(
            ReadableConfig config,
            DecodingFormat<DeserializationSchema<RowData>> decodingFormat,
            RowType producedDataType, DynamicTableFactory.Context context) {
        this.config = config;
        this.decodingFormat = decodingFormat;
        this.producedDataType = producedDataType;
        this.context = context;
    }

    @Override
    public ChangelogMode getChangelogMode() {
        // 返回仅插入模式，因为我们从HTTP接口读取的是追加数据
        return ChangelogMode.insertOnly();
    }

    @Override
    public ScanRuntimeProvider getScanRuntimeProvider(ScanContext runtimeProviderContext) {
        // 创建反序列化器
        final JsonRowDataDeserializationSchema deserializer = new JsonRowDataDeserializationSchema(producedDataType, TypeInformation.of(RowData.class), false, true, TimestampFormat.SQL);
        // 创建SourceFunction
        // 先把pushedFilters变为可序列化的对象
        List<Map<String,String>> serializablePushedFilters = new ArrayList<>();
        for (ResolvedExpression filter : pushedFilters) {
            //可序列化的Filter对象，有三个字段，字段、值、函数名
            Map<String,String> serializablePushedFilter = new HashMap<>();
            if (filter instanceof CallExpression call) {
                String functionName = call.getFunctionName();
                serializablePushedFilter.put("f", functionName);
                // 解析表达式，获取字段名和值
                List<ResolvedExpression> children = call.getResolvedChildren();
                if (children.size() == 2) {
                    String fieldName = extractFieldName(children.get(0));
                    serializablePushedFilter.put("n", fieldName);
                    String value = extractValue(children.get(1));
                    serializablePushedFilter.put("v", value);
                }
            }
            serializablePushedFilters.add(serializablePushedFilter);
        }
        final SourceFunction<RowData> sourceFunction = new HttpSourceFunction(config, deserializer, serializablePushedFilters, context.getObjectIdentifier().getObjectName());
        // 返回SourceFunctionProvider，false表示不是有界数据
        return SourceFunctionProvider.of(sourceFunction, false);
    }

    @Override
    public DynamicTableSource copy() {
        return new HttpDynamicTableSource(config, decodingFormat, producedDataType, context);
    }

    @Override
    public String asSummaryString() {
        return "HTTP Table Source";
    }

    @Override
    public LookupRuntimeProvider getLookupRuntimeProvider(LookupContext context) {
        System.out.println("xxxxxx + lookup");
        int[][] keys = context.getKeys();
        if (keys.length == 0) {
            throw new IllegalArgumentException("Lookup keys cannot be empty");
        }
        LookupFunction lookupFunction = new HttpLookupFunction(config.get(HttpConnectorOptions.URL), keys[0]);
        return LookupFunctionProvider.of(lookupFunction);
    }

    @Override
    public Result applyFilters(List<ResolvedExpression> filters) {
        List<ResolvedExpression> acceptedFilters = new ArrayList<>();
        List<ResolvedExpression> remainingFilters = new ArrayList<>();

        for (ResolvedExpression filter : filters) {
            if (canPushDown(filter)) {
                acceptedFilters.add(filter);
            } else {
                remainingFilters.add(filter);
            }
        }

        this.pushedFilters = acceptedFilters;
        // 返回接受的过滤器和剩余的过滤器
        return Result.of(acceptedFilters, remainingFilters);
    }

    private boolean canPushDown(ResolvedExpression filter) {
        // 判断哪些过滤器可以下推到 HTTP 源
        // 例如：简单的等值比较、范围查询等
        if (filter instanceof CallExpression call) {
            String functionName = call.getFunctionName().toUpperCase();
            // 支持的操作符：=, >, <, >=, <=, LIKE 等
            return functionName.equals("EQUALS") ||
                    functionName.equals("GREATER_THAN") ||
                    functionName.equals("LESS_THAN") ||
                    functionName.equals("GREATER_THAN_OR_EQUAL") ||
                    functionName.equals("LESS_THAN_OR_EQUAL") ||
                    functionName.equals("LIKE");
        }
        return false;
    }

    private String extractFieldName(ResolvedExpression expr) {
        if (expr instanceof FieldReferenceExpression fieldRef) {
            return fieldRef.getName();
        } else {
            return expr.toString();
        }
    }

    private String extractValue(ResolvedExpression expr) {
        if (expr instanceof ValueLiteralExpression valueExpr) {
            try {
                // 获取字面量值
                return valueExpr.getValueAs(String.class).orElse(null);
            } catch (Exception e) {
                return null;
            }
        } else {
            return expr.toString();
        }
    }
}