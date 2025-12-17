package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 日期遮盖  默认全部遮盖 支持参数化配置
 */
public class DateMask extends SensitivePatternRegex {
    @Override
    public String pattern() {
        // 时间格式
//        String pattern = "^(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})\\s+(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2})$";
        String pattern = "\\d{4}(-\\d{2}){0,2}( \\d{2}:\\d{2}:\\d{2})?";
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
