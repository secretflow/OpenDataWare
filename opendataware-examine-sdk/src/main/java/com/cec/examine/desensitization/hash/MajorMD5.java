package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 主修专业哈希
 */
public class MajorMD5 extends SensitivePatternRegex {
    public MajorMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^[\\u4e00-\\u9fa5\\s()（）\\w&-]+$";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));

    }
}
