package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 未服兵役|服现役|预备役|退出现役
 * 兵役状况  默认全部遮盖
 */
public class MilitaryServiceStatusMask extends SensitivePatternRegex {
    public MilitaryServiceStatusMask() {
    }
    @Override
    public String pattern() {
        String pattern = "(?<=\\D|\\b)(未服兵役|服现役|预备役|退出现役)(?=\\D|\\b)";
        return pattern;
    }

    @Override
    public String target(String result) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.mask(result, 0, result.length(), '*');
        }

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }

}
