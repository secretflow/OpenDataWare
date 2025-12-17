package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitivePatternMACFake.java
 * @Description MAC地址假名化
 * @createTime 2023/07/12
 */
public class MACRandomFake extends SensitivePatternRegex {
    public MACRandomFake(){

    }
    private static final String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
    };
    @Override
    public String pattern() {
//        String pattern = "/((([a-f0-9]{2}:){5})|(([a-f0-9]{2}-){5}))[a-f0-9]{2}/gi";
//        String pattern2 = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$|^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        String pattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return generateRandomMACAddress(source);
    }
    private static String generateRandomMACAddress(String text) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(HEX_DIGITS[random.nextInt(16)]);
            sb.append(HEX_DIGITS[random.nextInt(16)]);

            if (i != 5) {
                sb.append(":");
            }
        }

        return sb.toString();
    }
}
