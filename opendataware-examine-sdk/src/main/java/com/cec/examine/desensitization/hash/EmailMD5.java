package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class EmailMD5 extends SensitivePatternRegex {


    public EmailMD5() {

    }

    @Override
    public String pattern() {
        String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
//        String pattern = "";
        return pattern;
    }

    @Override
    public String target(String source) {
        String [] s = source.split("@",-1);
        if(s.length<2) return  source;
        String replace = MD5Util.textToMD5L32(getSaltedValue(s[0]));

        return new StringBuilder()
                .append(replace)
                .append("@")
                .append(s[1])
                .toString();
    }

}
