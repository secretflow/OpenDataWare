package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 港澳通行证遮盖
 * @createTime 2023/07/04
 */
public class HongKongAndMacauTravelPermitMask extends SensitivePatternRegex {


    private int maskStart;
    private int maskLength;
    private char maskTag;

    public HongKongAndMacauTravelPermitMask() {
        this(1, 8, '*');
    }

    public HongKongAndMacauTravelPermitMask(int maskStart, int maskLength, char maskTag) {
        this.maskLength = maskLength;
        this.maskStart = maskStart;
        this.maskTag = maskTag;

    }
    @Override
    public String pattern() {
        String pattern = "^[A-Z]{1}[0-9]{8}[0-9]{2}$";
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
