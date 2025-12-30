package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 住址遮盖
 * @createTime 2023/07/12
 */
public class AddressMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public AddressMask() {
    }

    public AddressMask(String text) {
        this(text.indexOf("市")+1,text.length(),'*');
    }

    public AddressMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String pattern = "^[\\u4e00-\\u9fa5省市市区县]+[\\u4e00-\\u9fa5a-zA-Z0-9]+[路街巷道弄号]?[\\u4e00-\\u9fa5a-zA-Z0-9]*号?$";
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
