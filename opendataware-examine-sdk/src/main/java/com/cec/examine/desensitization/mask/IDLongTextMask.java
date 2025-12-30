package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 *身份证号识别脱敏 身份证长文本识别
 * 六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码
 **/
public class IDLongTextMask extends SensitivePatternRegex {

    private int maskStart;
    private int maskLength;
    private char maskTag;

    public IDLongTextMask() {
        this(6, 8, '*');
    }

    public IDLongTextMask(int maskStart, int maskLength, char maskTag) {
        this.maskLength = maskLength;
        this.maskStart = maskStart;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        //长文本识别
        String pattern = "(?<=\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(?=\\D|\\b)";
//        String pattern = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}(\\d|X|x)$";
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
