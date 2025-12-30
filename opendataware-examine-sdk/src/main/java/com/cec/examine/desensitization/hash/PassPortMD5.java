package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class PassPortMD5 extends SensitivePatternRegex {
    public PassPortMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)([a-zA-z]|[0-9]){5,17}(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
