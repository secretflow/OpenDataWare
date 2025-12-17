package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 港澳通行证哈希
 */
public class HongKongAndMacauTravelPermitMD5 extends SensitivePatternRegex {
    public HongKongAndMacauTravelPermitMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^[A-Z]{1}[0-9]{8}[0-9]{2}$";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
