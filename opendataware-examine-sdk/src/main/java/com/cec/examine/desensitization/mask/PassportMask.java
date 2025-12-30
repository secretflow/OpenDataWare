package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitivePassportMask.java
 * @Description 护照脱敏
 * @createTime 2023/07/04
 */

public class PassportMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;


    public PassportMask() {
        this(1,8,'*');
    }

    public PassportMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)([a-zA-z]|[0-9]){5,17}(?=\\D|\\b)";
        return pattern;
    }


    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {

            return SensitivePatternUtil.mask(source, maskStart, maskLength, maskTag);
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
