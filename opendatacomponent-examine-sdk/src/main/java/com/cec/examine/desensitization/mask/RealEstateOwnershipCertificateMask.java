package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 不动产权证书遮盖
 * 8位数字构成  前边四位是年份 后边四位是流水号
 * 默认全部遮盖  支持参数化配置
 */
public class RealEstateOwnershipCertificateMask extends SensitivePatternRegex {
    public RealEstateOwnershipCertificateMask(){

    }
    @Override
    public String pattern() {
        String pattern = "^\\d{4}\\d{4}$";
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
