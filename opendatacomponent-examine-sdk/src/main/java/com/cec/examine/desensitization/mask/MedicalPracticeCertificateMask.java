package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 医师执业证书遮盖
 * @createTime 2023/07/05
 */
public class MedicalPracticeCertificateMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public MedicalPracticeCertificateMask() {
        this(1,8,'*');
    }

    public MedicalPracticeCertificateMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
//        String pattern = "^[0-9]{1}[0-9A-Z]{2}[0-9A-Z]{2}[0-9A-Z]{2}[0-9A-Z]{2}[0-9A-Z]{2}[0-9A-Z]{2}$";
        String pattern = "^[A-Z0-9][0-9]{2}[A-Z0-9]{2}[0-9]{2}[0-9]{2}[0-9]{6}$";
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
