package com.cec.examine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomReplaceValue {
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

    public static void main(String[] args) {
        List<String> myArray = new ArrayList<>();
        myArray.add("apple");
        myArray.add("banana");
        myArray.add("cherry");
        myArray.add("date");
        myArray.add("fig");

        String givenValue = "cherry";
        List<String> newArray = randomReplaceValue(myArray, givenValue);

        System.out.println(newArray);
    }
}
