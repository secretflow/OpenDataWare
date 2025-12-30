package com.cec.examine.desensitization.fake.fixedfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitivePostalCodeFake.java
 * @Description 邮编固定
 * @createTime 2023/07/12
 */
public class PostalCodeFixedFake extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "^[0-9]\\d{5}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        String replace = SensitivePatternUtil.getEqLenNumStr(source);

        return replace;
    }

    private String postalCodeFake(String text) {

        Random random = new Random();
        int zipCode = random.nextInt(Integer.valueOf(text))+100000;
        return String.valueOf(zipCode);
    }

}
