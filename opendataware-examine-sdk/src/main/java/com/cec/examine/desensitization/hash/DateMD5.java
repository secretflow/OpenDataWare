package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class DateMD5 extends SensitivePatternRegex {
    public DateMD5(){

    }
    @Override
    public String pattern() {
        // 时间格式
        String pattern = "^(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})\\s+(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2})$";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
