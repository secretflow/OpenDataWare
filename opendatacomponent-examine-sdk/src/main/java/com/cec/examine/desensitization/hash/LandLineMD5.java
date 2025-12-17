package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class LandLineMD5 extends SensitivePatternRegex {


    public LandLineMD5() {

    }

    @Override
    public String pattern() {
        return "(\\d{3,4}-\\d{7,8})";
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }

}
