package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

public class BankCardMD5 extends SensitivePatternRegex {

    public BankCardMD5() {

    }

    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String target) {

        // 如果符合银行卡的校验规则
        if (SensitivePatternUtil.checkLuhn(target)) {

            String encryptValue = getSaltedValue(target);

            return MD5Util.textToMD5L32(encryptValue);
        }
        return target;
    }

}
