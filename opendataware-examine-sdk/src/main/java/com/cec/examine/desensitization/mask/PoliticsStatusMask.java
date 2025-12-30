package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 政治面貌遮盖
 * @createTime 2023/07/05
 */
public class PoliticsStatusMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public PoliticsStatusMask() {
    }

    public PoliticsStatusMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String pattern = "^(中共党员|中共预备党员|共青团员|民革党员|民盟盟员|民建会员|民进会员|农工党党员|致公党党员|九三学社社员|台盟盟员|无党派人士|群众)$";
        return pattern;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.mask(source, 0, source.length(), '*');
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
