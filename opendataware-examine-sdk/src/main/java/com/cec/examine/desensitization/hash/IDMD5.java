package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class IDMD5 extends SensitivePatternRegex {

    public IDMD5() {

    }

    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));
    }

}
