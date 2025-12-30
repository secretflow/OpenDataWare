package com.cec.flink.connectors.http;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

/**
 * 简单的Http客户端实现类
 * @author koala
 */
public class Httpj {

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    private String server;
    private Integer port;

    public Httpj(String server, Integer port) {
        this.server = server;
        this.port = port;
    }

    public static String post(String url) {
        return post(url, "", null);
    }

    public static String post(String url, String json) {
        return post(url, json, 10);
    }

    public static String execute(String method, String url, String jsonBody, int timeoutMillis, Map<String, String> headers, String requestId) {
        if (!isValidUrl(url)) {
            return "Error: Invalid or unsafe URL provided.";
        }
        if("get".equals(method.toLowerCase())) {
            return sendGet(url, timeoutMillis, headers);
        } else if("post".equals(method.toLowerCase())) {
            return post(url, jsonBody, timeoutMillis, headers);
        } else if("$request".equals(method.toLowerCase())) {
            JSONObject requestJson = new JSONObject();
            requestJson.put("requestId", requestId);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            requestJson.put("processTime", formattedNow);
            return requestJson.toJSONString();
        } else {
            return null;
        }
    }

    public static String post(String url, String json, Map<String, String> headers) {
        return post(url, json, 10, headers);
    }

    public static String post(String url, String json, int timeoutMillis) {
        return post(url, json, timeoutMillis, null);
    }

    public static String post(String url, String json, int timeoutSeconds, Map<String, String> headers) {
        if (!isValidUrl(url)) {
            return "Error: Invalid or unsafe URL provided.";
        }
        /*
        String result = "";
        HttpClient client = HttpClient.newHttpClient();
        java.net.http.HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .header("connection", "Keep-Alive")*/
         //       .header("accept", "*/*");
        /*
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        builder.timeout(java.time.Duration.ofSeconds(timeoutSeconds));
        HttpRequest request = builder.build();
        HttpResponse<String> response = null;
        try {
            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();*/
        return "";
    }

    public static String sendGet(String url, int timeoutSeconds) {
        return sendGet(url, timeoutSeconds, null);
    }

    public static String sendGet(String url, int timeoutSeconds, Map<String, String> headers){
        if (!isValidUrl(url)) {
            return "Error: Invalid or unsafe URL provided.";
        }
        /*
        String result = "";
        HttpClient client = HttpClient.newHttpClient();
        java.net.http.HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .header("connection", "Keep-Alive")
         */
        //        .header("accept", "*/*");
        /*
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        builder.timeout(java.time.Duration.ofSeconds(timeoutSeconds));
        HttpRequest request = builder.build();
        HttpResponse<String> response = null;
        try {
            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
        */
        return "";
    }


    public static String sendGet(String url, Map<String, String> headers) throws IOException, InterruptedException {
        return sendGet(url, 10, headers);
    }

    private static final String[] PRIVATE_IP_PREFIXES = {"10.", "172.16.", "172.17.", "172.18.", "172.19.", "172.20.", "172.21.", "172.22.", "172.23.", "172.24.", "172.25.", "172.26.", "172.27.", "172.28.", "172.29.", "172.30.", "172.31.", "192.168.", "127.0.0.1", "0.0.0.0", "169.254."};

    /**
     * 校验URL是否安全，防止SSRF攻击
     * @param urlString 待校验的URL字符串
     * @return 安全则返回true，否则返回false
     */
    public static boolean isValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);

            // 1. 限制协议：只允许HTTP/HTTPS [3,6](@ref)
            if (!"http".equalsIgnoreCase(url.getProtocol()) && !"https".equalsIgnoreCase(url.getProtocol())) {
                return false;
            }

            // 2. 解析主机名并获取IP地址
            String host = url.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            String ip = inetAddress.getHostAddress();

            // 3. 内网IP黑名单校验 [3,6](@ref)
            for (String prefix : PRIVATE_IP_PREFIXES) {
                if (ip.startsWith(prefix)) {
                    return false;
                }
            }

            // 4. （可选但推荐）域名白名单校验
            // Set<String> allowedDomains = Set.of("api.trusted.com", "cdn.safe.org");
            // if (!allowedDomains.contains(host)) {
            //    return false;
            // }

            return true;
        } catch (Exception e) {
            // 记录日志，但对外返回校验失败
            return false;
        }
    }
}
