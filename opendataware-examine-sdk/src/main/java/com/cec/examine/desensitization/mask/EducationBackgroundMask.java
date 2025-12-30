package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName SensitiveEducationBackgroundMask.java
 * @Description 学历遮盖
 * @createTime 2023/07/03
 */
public class EducationBackgroundMask extends SensitivePatternRegex {
    public EducationBackgroundMask() {

    }
    @Override
    public String pattern() {
        String pattern = "(?:学士|硕士|博士|本科|研究生|大专|专科|高中|初中|小学|博士后|博士研究生|硕士研究生)";
        return pattern;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.maskNormal(source,0,source.length(),'*');
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
