package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class AddresMD5 extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "^[\\u4e00-\\u9fa5省市市区县]+[\\u4e00-\\u9fa5a-zA-Z0-9]+[路街巷道弄号]?[\\u4e00-\\u9fa5a-zA-Z0-9]*号?$";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
