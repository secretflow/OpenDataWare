package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.regex.Matcher;

/**
 * MAC地址遮盖 全部遮盖
 */
public class MACMask extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$|^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        return pattern;
    }

    @Override
    public String target(String source) {
//        String source = m.group(0);
        return "**:**:**:**:**:**";
    }
}
