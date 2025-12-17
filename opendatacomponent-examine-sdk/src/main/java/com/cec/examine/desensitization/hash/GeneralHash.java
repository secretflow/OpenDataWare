package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

public class GeneralHash extends SensitivePatternRegex {
    @Override
    public String pattern() {
        return null;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(source);
    }
}
