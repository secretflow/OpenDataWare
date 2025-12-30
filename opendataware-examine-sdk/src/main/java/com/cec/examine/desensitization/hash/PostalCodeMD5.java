package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 邮编哈希
 */
public class PostalCodeMD5 extends SensitivePatternRegex {
    public PostalCodeMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^[0-9]\\d{5}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
