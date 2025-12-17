package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description HUN姻状况遮盖
 * @createTime 2023/07/05
 */
public class MaritalStatusMask extends SensitivePatternRegex {

    private int maskStart;
    private int maskLength;
    private char maskTag;

    public MaritalStatusMask() {
        this(0,2,'*');
    }

    public MaritalStatusMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }



    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(未HUN|已HUN|离HUN|SANG偶)(?=\\D|\\b)";
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
