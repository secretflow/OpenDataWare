package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * ip地址哈希
 */
public class IPv4MD5 extends SensitivePatternRegex {

    public IPv4MD5() {

    }

    @Override
    public String pattern() {
        String pattern = "(?<=(\\b|\\D))((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }

}
