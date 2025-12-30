package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 不动产权证哈希
 *
 */
public class RealEstateOwnershipCertificateMD5 extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "^\\d{4}\\d{4}$";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
