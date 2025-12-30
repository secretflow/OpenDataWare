package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 服役状况哈希
 */
public class MilitaryServiceStatusMD5 extends SensitivePatternRegex {
    public MilitaryServiceStatusMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(未服兵役|服现役|预备役|退出现役)(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
