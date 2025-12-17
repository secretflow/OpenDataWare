package com.cec.flink.connectors.http;


import org.apache.flink.table.data.RowData;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.LookupFunction;
import java.util.Collection;
import java.util.Collections;

/**
 * Http lookup 连接实现类
 * @author koala
 */
public class HttpLookupFunction extends LookupFunction {

    private final String baseUrl;
    private final int[] lookupKeyIndices;

    public HttpLookupFunction(String baseUrl, int[] lookupKeyIndices) {
        this.baseUrl = baseUrl;
        this.lookupKeyIndices = lookupKeyIndices;
    }

    @Override
    public void open(FunctionContext context) throws Exception {
        super.open(context);
    }

    @Override
    public Collection<RowData> lookup(RowData keyRow) {
        try {
            // 从keyRow中提取查找键值
            Object[] keys = new Object[lookupKeyIndices.length];
            for (int i = 0; i < lookupKeyIndices.length; i++) {
                keys[i] = keyRow.getString(lookupKeyIndices[i]);
            }

            String requestUrl = buildRequestUrl(keys);
            System.out.println("lookup url: " + requestUrl);
            String jsonResponse = executeHttpRequest(requestUrl);
            RowData resultRow = parseJsonResponse(jsonResponse);
            if (resultRow != null) {
                return Collections.singletonList(resultRow);
            }
            return Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("HTTP lookup failed", e);
        }
    }

    private String buildRequestUrl(Object... keys) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (baseUrl.contains("?")) {
            urlBuilder.append("&");
        } else {
            urlBuilder.append("?");
        }

        for (int i = 0; i < keys.length; i++) {
            if (i > 0) {
                urlBuilder.append("&");
            }
            urlBuilder.append("key").append(i).append("=").append(keys[i]);
        }

        return urlBuilder.toString();
    }

    private String executeHttpRequest(String requestUrl) throws Exception {
        if (!Httpj.isValidUrl(requestUrl)) {
            return "Error: Invalid or unsafe URL provided.";
        }
        return Httpj.sendGet(requestUrl,10);
    }

    private RowData parseJsonResponse(String jsonResponse) {
        // 简化实现，实际使用时需要根据具体API响应格式解析
        // 这里返回一个示例RowData
        return null; // 实际应返回解析后的RowData
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
