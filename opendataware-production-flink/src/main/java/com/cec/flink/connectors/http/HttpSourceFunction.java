package com.cec.flink.connectors.http;

import org.apache.flink.streaming.api.functions.source.legacy.RichSourceFunction;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.data.StringData;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http数据源处理逻辑类
 * @author koala
 */
public class HttpSourceFunction extends RichSourceFunction<RowData> {
    private final ReadableConfig config;
    private final DeserializationSchema<RowData> deserializer;
    private volatile boolean isRunning = true;
    private final String tableName;

    private final List<Map<String,String>> serializablePushedFilters;

    public HttpSourceFunction(ReadableConfig config, DeserializationSchema<RowData> deserializer, List<Map<String,String>> serializablePushedFilters, String tableName) {
        this.config = config;
        this.deserializer = deserializer;
        this.serializablePushedFilters = serializablePushedFilters;
        this.tableName = tableName;
    }
    
    @Override
    public void run(SourceContext<RowData> ctx) throws Exception {
        String url = config.get(HttpConnectorOptions.URL);
        String method = config.get(HttpConnectorOptions.METHOD);
        long interval = config.get(HttpConnectorOptions.INTERVAL);
        int timeout = config.get(HttpConnectorOptions.TIMEOUT);
        boolean listenOpen = config.get(HttpConnectorOptions.LISTEN_OPEN);
        String listenTopic = tableName;
        while (isRunning) {
            try {
                Map<String, String> queryParams = null;
                String body = null;
                Map<String, String> headers = null;
                String requestId = null;
                if(listenOpen) {
                    CallEventListener.Message message = CallEventListener.take(listenTopic);
                    requestId = message.getRequestId();
                    RestParamters restParamters = parseHttpParameters(message);
                    queryParams = restParamters.getQueryStrings();
                    body = restParamters.getBody();
                    headers = restParamters.getHeaders();
                } else {
                    queryParams = buildQueryParamsFromFilters();
                }
                // 将查询参数添加到 URL
                String fullUrl = buildUrlWithParams(url, queryParams);
                String responseData = Httpj.execute(method, fullUrl, body, timeout, headers, requestId);
                if (responseData != null && !responseData.trim().isEmpty()) {
                    // 使用反序列化器将数据转换为RowData
                    deserializer.open(null);
                    RowData rowData = deserializer.deserialize(responseData.getBytes(StandardCharsets.UTF_8));
                    if (rowData != null) {
                        ctx.collect(rowData);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // 发生错误时等待一段时间后重试
            } finally {
                if(!listenOpen) {
                    if (interval > 0) {
                        Thread.sleep(interval);
                    } else break;
                }
            }
        }
    }
    
    @Override
    public void cancel() {
        isRunning = false;
    }

    /**
     * 提取Http源的参数，包括queryString、Body、header
     * @param message
     * @return
     */
    private RestParamters parseHttpParameters(CallEventListener.Message message) {
        RestParamters restParamters = new RestParamters();
        Map<String, String> queryParams = (Map<String, String>)message.getValue("$q");
        if( queryParams == null) {queryParams = new  HashMap<>();}
        String body = String.valueOf(message.getValue("$b"));
        Map<String, String> headers = (Map<String, String>) message.getValue("$h");
        for (Map<String, String> filter : serializablePushedFilters) {
            String functionName = filter.get("f");
            String fieldName = filter.get("n");
            String value = filter.get("v");
            if (fieldName != null && value != null) {
                // 根据操作符构建查询参数
                switch (functionName.toUpperCase()) {
                    case "EQUALS":
                        //替换变量值
                        if(value.indexOf("$") == 0) {
                            String v = String.valueOf(message.getValue(value));
                            if (v != null) {
                                queryParams.put(fieldName, v);
                            }
                        }
                        break;
                    //其他where条件预留
                    case "GREATER_THAN":
                    case "LESS_THAN":
                    case "LIKE":
                        break;
                }
            }
        }
        restParamters.setQueryStrings(queryParams);
        restParamters.setBody(body);
        restParamters.setHeaders(headers);
        return restParamters;
    }

    private Map<String, String> buildQueryParamsFromFilters() {
        Map<String, String> queryParams = new HashMap<>();
        for (Map<String, String> filter : serializablePushedFilters) {
            String functionName = filter.get("f");
            String fieldName = filter.get("n");
            String value = filter.get("v");
            if (fieldName != null && value != null) {
                // 根据操作符构建查询参数
                switch (functionName.toUpperCase()) {
                    case "EQUALS":
                        queryParams.put(fieldName, value);
                        break;
                        //其他where条件预留
                    case "GREATER_THAN":
                    case "LESS_THAN":
                    case "LIKE":
                        break;
                }
            }
        }
        return queryParams;
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        if (params.isEmpty()) {
            return baseUrl;
        }

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append(baseUrl.contains("?") ? "&" : "?");

        List<String> paramPairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramPairs.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        urlBuilder.append(String.join("&", paramPairs));
        return urlBuilder.toString();
    }

    /**
     * 给现有的RowData增加一列
     * @param originalRow
     * @param requestId
     * @return
     */
    public static GenericRowData addRequestIdColumn(RowData originalRow, String requestId) {
        // 计算新RowData的字段数量（原字段数 + 1）
        int newArity = originalRow.getArity() + 1;
        GenericRowData newRow = new GenericRowData(newArity);
        // 设置新的requestId字段
        String actualRequestId = (requestId != null) ? requestId : "-";
        newRow.setField(originalRow.getArity(), StringData.fromString(actualRequestId));
        // 保持原RowData的行类型
        newRow.setRowKind(originalRow.getRowKind());
        return newRow;
    }

    /**
     * Restful 参数
     */
    public static class RestParamters {
        private Map<String, String> queryStrings;

        public Map<String, String> getQueryStrings() {
            return queryStrings;
        }

        public void setQueryStrings(Map<String, String> queryStrings) {
            this.queryStrings = queryStrings;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        private String body;
        private Map<String, String> headers;
    }
}