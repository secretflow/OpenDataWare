package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 学历哈希
 */
public class EducationBackgroundMD5 extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "(?:学士|硕士|博士|本科|研究生|大专|专科|高中|初中|小学|博士后|博士研究生|硕士研究生)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
