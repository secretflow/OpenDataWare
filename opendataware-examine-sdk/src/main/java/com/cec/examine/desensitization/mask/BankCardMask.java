package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;
/**
 *银行卡遮盖
 **/
public class BankCardMask extends SensitivePatternRegex {

    private int maskStart;
    private char maskTag;

    public BankCardMask() {

        this(6, '*');
    }

    public BankCardMask(int maskStart, char maskTag) {
        this.maskStart = maskStart;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String result) {

        if (SensitivePatternUtil.checkLuhn(result)) {

            if (Objects.isNull(this.configs)) {
                return SensitivePatternUtil.mask(result, maskStart, result.length() - 10, maskTag);
            }

            for (Config config : this.configs) {

                result = SensitivePatternUtil.maskWithConfig(result, config);
            }

            return result;

        }
        return result;
    }

}
