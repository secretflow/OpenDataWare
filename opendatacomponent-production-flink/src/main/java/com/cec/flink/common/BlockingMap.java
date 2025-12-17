package com.cec.flink.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个阻塞的HashMap实现，用于监听多路数据到达
 * @param <K> 键
 * @param <V> 值
 * @author koala
 */
public class BlockingMap<K, V> {
    private final ConcurrentMap<K, V> map;
    private final ConcurrentMap<K, Condition> conditions;
    private final ReentrantLock lock;

    /**
     * 阻塞的HashMap构造函数
     */
    public BlockingMap() {
        this.map = new ConcurrentHashMap<>();
        this.conditions = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
    }

    /**
     * 阻塞的监听Key对应的值是否到达
     * @param key 键
     * @return 值
     * @throws InterruptedException 进程被取消
     */
    public V get(K key) throws InterruptedException {
        lock.lock();
        try {
            V value;
            while ((value = map.get(key)) == null) {
                Condition condition = conditions.computeIfAbsent(key, k -> lock.newCondition());
                condition.await();
            }
            return value;
        } finally {
            lock.unlock();
        }
    }
    /**
     * 阻塞的监听Key对应的值是否到达
     * @param key 键
     * @param timeout 设置超时时间
     * @return 值
     * @throws InterruptedException 进程被取消
     */
    public V get(K key, long timeout) throws InterruptedException {
        lock.lock();
        try {
            V value;
            long nanos = timeout * 1_000_000;
            while ((value = map.get(key)) == null) {
                Condition condition = conditions.computeIfAbsent(key, k -> lock.newCondition());
                if (nanos <= 0L) {
                    return null;
                }
                nanos = condition.awaitNanos(nanos);
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 写入值
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value) {
        lock.lock();
        try {
            map.put(key, value);
            Condition condition = conditions.get(key);
            if (condition != null) {
                condition.signalAll();
                conditions.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 是否包含值
     * @param key 键
     * @return 是否包含
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public V remove(K key) {
        lock.lock();
        try {
            conditions.remove(key);
            return map.remove(key);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 返回大小
     * @return 大小
     */
    public int size() {
        return map.size();
    }

    /**
     * 是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 清空map
     */
    public void clear() {
        lock.lock();
        try {
            conditions.values().forEach(Condition::signalAll);
            conditions.clear();
            map.clear();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingMap<String, String> blockingMap = new BlockingMap<>();

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(2000);
                blockingMap.put("key1", "Hello World");
                System.out.println("Produced: key1 -> Hello World");
                
                Thread.sleep(1000);
                blockingMap.put("key2", "BlockingMap Test");
                System.out.println("Produced: key2 -> BlockingMap Test");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer1 = new Thread(() -> {
            try {
                System.out.println("Consumer1 waiting for key1...");
                String value = blockingMap.get("key1");
                System.out.println("Consumer1 received: " + value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer2 = new Thread(() -> {
            try {
                System.out.println("Consumer2 waiting for key2...");
                String value = blockingMap.get("key2", 5000);
                if (value != null) {
                    System.out.println("Consumer2 received: " + value);
                } else {
                    System.out.println("Consumer2 timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer1.start();
        consumer2.start();

        producer.join();
        consumer1.join();
        consumer2.join();
    }
}