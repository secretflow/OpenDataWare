package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 医师职业证书哈希
 */
public class MedicalPracticeCertificateMD5 extends SensitivePatternRegex {
    public MedicalPracticeCertificateMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "^[A-Z0-9][0-9]{2}[A-Z0-9]{2}[0-9]{2}[0-9]{2}[0-9]{6}$";
        return pattern;
    }

    @Override
    public String target(String source) {

        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
