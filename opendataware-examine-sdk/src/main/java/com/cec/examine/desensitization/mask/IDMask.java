package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;
/**
 *身份证号识别脱敏 身份证严格匹配
 * 六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码
 **/
public class IDMask extends SensitivePatternRegex {

    private int maskStart;
    private int maskLength;
    private char maskTag;

    public IDMask() {
        this(6, 8, '*');
    }

    public IDMask(int maskStart, int maskLength, char maskTag) {
        this.maskLength = maskLength;
        this.maskStart = maskStart;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
        //身份证号码严格匹配

        String pattern = "(?<=\\D|\\b|\\d)(110|120|130|140|150|210|220|230|310|320|330|340|350|360|370|410|420|430|440|450|460|500|510|520|530|540|610|620|630|640|650|810|820|830)(\\d{3})(19|20)(\\d{2})(01|02|03|04|05|06|07|08|09|10|11|12)(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)(\\d{3}[0-9Xx])(?=\\D|\\b|\\d)";
//        String pattern = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}(\\d|X|x)$";

        return "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|(10|20|30|31))\\d{3}[0-9Xx]";
    }


    @Override
    public String target(String source ) {

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
