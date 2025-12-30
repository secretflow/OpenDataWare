package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class VINMD5 extends SensitivePatternRegex {
    public VINMD5(){

    }
    @Override
    public String pattern() {
        //车架号正则
        String pattern = "^[A-HJ-NPR-Z0-9]{17}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
