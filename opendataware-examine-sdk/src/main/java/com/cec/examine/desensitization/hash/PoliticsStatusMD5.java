package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 政治面貌哈希
 */
public class PoliticsStatusMD5 extends SensitivePatternRegex {
    public PoliticsStatusMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^(中共党员|中共预备党员|共青团员|民革党员|民盟盟员|民建会员|民进会员|农工党党员|致公党党员|九三学社社员|台盟盟员|无党派人士|群众)$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
