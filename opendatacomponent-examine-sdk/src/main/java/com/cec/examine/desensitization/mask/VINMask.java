package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 车架号遮盖
 * 由17位字符组成，所以俗称十七位码
 * @createTime 2023/07/05
 */
public class VINMask extends SensitivePatternRegex {
    @Override
    public String pattern() {
        //车架号正则
        String pattern = "^[A-HJ-NPR-Z0-9]{17}$";
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
