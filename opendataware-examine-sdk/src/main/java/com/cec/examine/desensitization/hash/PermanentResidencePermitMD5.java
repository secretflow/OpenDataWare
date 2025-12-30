package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 永久居住证哈希
 */
public class PermanentResidencePermitMD5 extends SensitivePatternRegex {
    public PermanentResidencePermitMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^[A-Z]{3}[0-9]{12}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
