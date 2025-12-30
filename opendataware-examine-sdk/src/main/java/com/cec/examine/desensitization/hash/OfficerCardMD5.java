package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 军官证哈希
 */
public class OfficerCardMD5 extends SensitivePatternRegex {
    public OfficerCardMD5(){

    }
    @Override
    public String pattern() {
        String REGEX_OFFICER_CARD = "^[\\u4E00-\\u9FA5]{1,3}(字第)\\d+号$";
        return REGEX_OFFICER_CARD;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
