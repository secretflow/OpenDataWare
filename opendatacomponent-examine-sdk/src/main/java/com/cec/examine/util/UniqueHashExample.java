package com.cec.examine.util;

import java.util.zip.CRC32;

public class UniqueHashExample {
    public static void main(String[] args) {
        long originalValue = 123456789123456789L; // 替换为你的原始数值

        // 使用CRC32哈希函数生成哈希值
        long hashValue = customHash(originalValue);

        System.out.println("原始值: " + originalValue);
        System.out.println("哈希值: " + hashValue);
    }

    // 使用CRC32哈希函数生成哈希值
    private static long customHash(long value) {
        CRC32 crc32 = new CRC32();
        crc32.update((int) value); // 使用低32位
        return crc32.getValue();
    }
}
