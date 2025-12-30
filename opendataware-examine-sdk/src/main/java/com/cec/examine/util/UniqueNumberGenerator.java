package com.cec.examine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UniqueNumberGenerator {
    private static Map<String, Integer> generatedNumbers = new HashMap<>();
    private static Random random = new Random();

    public static int generateUniqueNumber(String phoneNumber) {
        // 提取手机号的后八位
        String lastEightDigits = phoneNumber.substring(phoneNumber.length() - 8);

        // 检查是否已经生成了对应的唯一数字
        if (generatedNumbers.containsKey(lastEightDigits)) {
            return generatedNumbers.get(lastEightDigits);
        }

        // 生成唯一的8位数字
        int uniqueNumber = generateRandomUniqueNumber();
        
        // 存储映射关系，以便下次使用同一手机号时返回相同的数字
        generatedNumbers.put(lastEightDigits, uniqueNumber);
        
        return uniqueNumber;
    }

    private static int generateRandomUniqueNumber() {
        // 这里使用随机生成的方式，可以根据需求使用更复杂的生成算法
        return random.nextInt(90000000) + 10000000;
    }
}
