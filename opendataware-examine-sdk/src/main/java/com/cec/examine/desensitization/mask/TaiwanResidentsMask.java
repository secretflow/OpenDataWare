package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 台湾同胞通行证
 * 大写字母 T+8 位数字
 * @createTime 2023/07/05
 */
public class TaiwanResidentsMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public TaiwanResidentsMask() {
        this(1,9,'*');
    }

    public TaiwanResidentsMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        String pattern = "^T[0-9]{8}$";
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
