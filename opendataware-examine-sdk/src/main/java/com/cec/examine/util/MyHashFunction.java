package com.cec.examine.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHashFunction {
    public static String hashString(String input) throws NoSuchAlgorithmException {
        // 创建SHA-256哈希算法的实例
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
        // 将输入字符串编码为字节数组
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        
        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        
        // 返回哈希值的十六进制表示
        return hexString.toString();
    }

    public static void main(String[] args) {
        try {
            String input = "Hello, World!";
            String hash = hashString(input);
            System.out.println("Hash of \"" + input + "\": " + hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
