package com.cec.flink.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockingMapTest {

    @Test
    public void test() throws InterruptedException {

        BlockingMap<String, String> blockingMap = new BlockingMap<String, String>();

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(200);
                blockingMap.put("key1", "Hello World");
                Thread.sleep(100);
                blockingMap.put("key2", "BlockingMap Test");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer1 = new Thread(() -> {
            try {
                String value = blockingMap.get("key1");
                assertEquals("Hello World", value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer2 = new Thread(() -> {
            try {
                String value = blockingMap.get("key2", 500);
                if (value != null) {
                    assertEquals("BlockingMap Test", value);
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