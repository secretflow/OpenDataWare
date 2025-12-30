package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 台胞通行证哈希
 */
public class TaiwanResidentsMD5 extends SensitivePatternRegex {
    public TaiwanResidentsMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^T[0-9]{8}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
