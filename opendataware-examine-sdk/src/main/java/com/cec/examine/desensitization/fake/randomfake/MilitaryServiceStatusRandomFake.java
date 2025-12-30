package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitiveMilitaryServiceStatusFake.java
 * @Description 兵役状况假名化
 * @createTime 2023/07/05
 */
public class MilitaryServiceStatusRandomFake extends SensitivePatternRegex {
    public MilitaryServiceStatusRandomFake(){

    }
    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(未服兵役|服现役|预备役|退出现役)(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String source) {
        String militaryServiceStatus = generateMilitaryServiceStatus(source);
        return militaryServiceStatus;
    }

    private static String generateMilitaryServiceStatus(String source) {
        String militaryStatus[] = {"未服兵役","服现役","预备役","退出现役"};
        Random random = new Random();
//        int index = random.nextInt(4);
//        while (source.equals(militaryStatus[index])){
//            generateMilitaryServiceStatus(source);
//        }
        List<String> newArr = randomReplaceValue(Arrays.stream(militaryStatus).collect(Collectors.toList()),source);
        return newArr.get(random.nextInt(newArr.size()));
    }

    public static List<String> randomReplaceValue(List<String> arr, String value) {
        List<String> newArr = new ArrayList<>();

        // 遍历数组中的每个元素
        for (String item : arr) {
            if (item.equals(value)) {
                // 如果元素等于给定的值，随机选择数组中的其他值来替代它
                String replacement = getRandomReplacement(arr, value);
                newArr.add(replacement);
            } else {
                // 如果元素不等于给定的值，保持不变
                newArr.add(item);
            }
        }

        return newArr;
    }

    public static String getRandomReplacement(List<String> arr, String value) {
        Random random = new Random();
        String replacement;

        do {
            // 从数组中随机选择一个元素
            replacement = arr.get(random.nextInt(arr.size()));
        } while (replacement.equals(value));

        return replacement;
    }
}
