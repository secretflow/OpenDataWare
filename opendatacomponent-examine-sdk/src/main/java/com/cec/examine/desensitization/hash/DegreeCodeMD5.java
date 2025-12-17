package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 学位代码哈希
 */
public class DegreeCodeMD5 extends SensitivePatternRegex {
    @Override
    public String pattern() {
        /**
         *学位正则  3位有效数字
         **/

        String pattern = "^[0-9]{3}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
