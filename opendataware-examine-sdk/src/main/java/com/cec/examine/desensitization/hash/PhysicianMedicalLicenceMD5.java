package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 医师资格证书哈希
 */
public class PhysicianMedicalLicenceMD5 extends SensitivePatternRegex {
    public PhysicianMedicalLicenceMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^\\d{4}[A-Z0-9]{2}\\d[A-Z0-9]{2}\\d{18}$";

        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
