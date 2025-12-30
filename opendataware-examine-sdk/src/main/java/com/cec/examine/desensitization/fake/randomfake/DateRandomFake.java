package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SeneitivePatternDateFake.java
 * @Description 日期 保留年份 其他部分随机映射
 * @createTime 2023/07/12
 */
public class DateRandomFake extends SensitivePatternRegex {
    public DateRandomFake(){

    }

    @Override
    public String pattern() {
        // 时间格式
         String pattern = "^(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})\\s+(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2})$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return randomizeDateTime(source);
    }

    private static String randomizeDateTime(String dateTime) {
        String[] parts = dateTime.split(" ");
        String datePart = parts[0];
        String timePart = parts[1];

        String[] dateParts = datePart.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        String[] timeParts = timePart.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);

        Random random = new Random();
        month = random.nextInt(12) + 1; // 随机月份，1到12之间
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = random.nextInt(31)+1;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = random.nextInt(30)+1;
                break;
            default:
                if (isYear(year)) {// 闰年二月有二十九天
                    day = random.nextInt(29)+1;
                } else {
                    day = random.nextInt(28)+1;
                }
        }
        hour = random.nextInt(12)+1;
        minute = random.nextInt(60); // 随机分钟，0到59之间
        second = random.nextInt(60);


        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    /**
     *平年 闰年
     **/
    public static boolean isYear(int y) {
        // 进行判断
        if (y % 4 == 0 && y % 100 != 0 || y % 200 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
