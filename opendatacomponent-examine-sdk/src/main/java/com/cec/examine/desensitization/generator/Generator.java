package com.cec.examine.desensitization.generator;
import com.cec.examine.util.encryption.MD5Util;

import java.util.*;


public abstract class Generator {

    /**
     * 内置的计数器
     */
    protected int counter = 0;

    public abstract String generateFixed(String seed);

    /**
     * 通过种子生成固定长度随机数字
     * @param seed
     * @param length
     * @return
     */
    public String generateFixedLengthNumber(String seed, int length) {
        String randomNum = MD5Util.textToMD5L32(seed);
        String r1 = randomNum.substring(0,8);
        String r2 = randomNum.substring(8,16);
        String r3 = randomNum.substring(16,24);
        String r4 = randomNum.substring(24,32);
        String l1 = String.valueOf(Long.parseLong(r1, 16));
        String l2 = String.valueOf(Long.parseLong(r2, 16));
        String l3 = String.valueOf(Long.parseLong(r3, 16));
        String l4 = String.valueOf(Long.parseLong(r4, 16));
        String total = l1 + l2 + l3 + l4;
        return  total.substring(0, length);
    }

    /**
     * 通过给定枚举值，生成固定的随机词
     * @param seed
     * @param enumWords
     * @return
     */
    public String generateFixedEnumWord(String seed, List<String> enumWords) {
        String randomNum = MD5Util.textToMD5L32(seed);
        String r1 = randomNum.substring(0,8);
        Long seedL = Long.parseLong(r1, 16);
        Random random = new Random(seedL);
        Integer index = random.nextInt(enumWords.size());
        return enumWords.get(index);
    }

    public String generateFixedEnumWord(String seed, String [] enumWords) {
        String randomNum = MD5Util.textToMD5L32(seed);
        String r1 = randomNum.substring(0,8);
        Long seedL = Long.parseLong(r1, 16);
        Random random = new Random(seedL);
        Integer index = random.nextInt(enumWords.length);
        return enumWords[index];
    }

    /**
     * 通过给定范围生成数字
     * @param seed
     * @param from
     * @param to
     * @return
     */
    public String generateFixedRangeNumber(String seed, int from, int to) {
        String randomNum = MD5Util.textToMD5L32(seed);
        String r1 = randomNum.substring(0,8);
        Long seedL = Long.parseLong(r1, 16);
        Random random = new Random(seedL);
        Integer index = random.nextInt(to - from);
        return String.valueOf(index + from);
    }

    /**
     * 概率区间生成器
     * @param seed 种子
     * @param probabilitySet 对应的分位值*100
     * @return 区间名称
     */
    public static Integer generateProbabilityIntervalLable(String seed, TreeSet<Integer> probabilitySet) {
        String randomNum = MD5Util.textToMD5L32(seed);
        String r1 = randomNum.substring(0,8);
        long seedL = Long.parseLong(r1, 16);
        Random random = new Random(seedL);
        Integer index = random.nextInt(100);
        return probabilitySet.higher(index);
    }
}
