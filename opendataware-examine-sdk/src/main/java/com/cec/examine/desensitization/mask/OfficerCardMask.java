package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 军官证遮盖
 * 军区简称+顺序号
 * @createTime 2023/07/05
 */
public class OfficerCardMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public OfficerCardMask() {
    }

    public OfficerCardMask(String text) {
        this(text,text.indexOf("第")+1,text.indexOf("号"),'*');
    }

    public OfficerCardMask(String text, int maskStart, int maskLength, char maskTag) {
        this.text = text;
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String REGEX_OFFICER_CARD = "^[\\u4E00-\\u9FA5]{1,3}(字第)\\d+号$";
        return REGEX_OFFICER_CARD;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.maskNormal(source, maskStart, maskLength, maskTag);
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
