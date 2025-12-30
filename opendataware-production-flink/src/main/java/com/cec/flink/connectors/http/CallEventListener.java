package com.cec.flink.connectors.http;

import com.cec.flink.common.BlockingMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 监听阻塞Map中数据到达的类
 * @author koala
 */
public class CallEventListener {

    public static HashMap<String, BlockingQueue<Message>> queueMap = new HashMap<>();
    //返回结果的BlockingMap
    public static BlockingMap<String,String> resultCacheMap = new BlockingMap<>();

    /**
     * 通过requestId获取Flink返回的结果
     * @param requestId 请求唯一ID
     * @return 结果值
     * @throws InterruptedException 取消进程
     */
    public static String getResultJSONString(String requestId) throws InterruptedException {
        return resultCacheMap.get(requestId);
    }

    /**
     * 把通过requestId和对应的返回结果写入
     * @param requestId 请求唯一ID
     * @param result 结果值
     * @throws InterruptedException 取消进程
     */
    public static void putResultJSONString(String requestId, String result) throws InterruptedException {
        resultCacheMap.put(requestId, result);
    }

    /**
     * 清空所有的数据
     */
    public static void clearResultJSONStrings() {
        resultCacheMap.clear();
    }

    /**
     * 阻塞的获获取消息
     * @param topic 主题
     * @return 消息
     * @throws InterruptedException 取消进程
     */
    public static Message take(String topic) throws InterruptedException {
        BlockingQueue<Message> queue = queueMap.get(topic);
        if(queue == null) {
            queue = new LinkedBlockingQueue<>();
            queueMap.put(topic, queue);
        }
        return queue.take();
    }

    /**
     * 阻塞的写入消息
     * @param topic 主题
     * @param data 消息
     * @throws InterruptedException 取消进程
     */
    public static void put(String topic, Message data) throws InterruptedException {
        BlockingQueue<Message> queue = queueMap.get(topic);
        if(queue == null){
            queue = new LinkedBlockingQueue<>();
        }
        queue.put(data);
        queueMap.putIfAbsent(topic, queue);
    }

    /**
     * 消息的数据结构
     */
    public static class Message {
        /**
         * 获取请求的唯一ID
         * @return 请求的唯一ID
         */
        public String getRequestId() {
            return requestId;
        }

        /**
         * 设置请求的唯一ID
         * @param requestId 请求的唯一ID
         */
        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        /**
         * 获取值
         * @param key 值对应的主题
         * @return 值
         */
        public Object getValue(String key) {
            return value.get(key);
        }

        /**
         * 请求的唯一ID
         */
        public String requestId;
        /**
         * 值
         */
        public Map<String, Object> value;

        /**
         * 消息的构造函数
         * @param requestId 请求的唯一ID
         * @param value 值
         */
        public Message(String requestId, Map<String, Object> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }
}
