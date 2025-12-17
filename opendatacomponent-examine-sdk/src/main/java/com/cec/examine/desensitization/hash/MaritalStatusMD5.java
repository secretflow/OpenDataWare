package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * HUN姻状况哈希
 */
public class MaritalStatusMD5 extends SensitivePatternRegex {
    public MaritalStatusMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(未HUN|已HUN|离HUN|SANG偶)(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
