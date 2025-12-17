package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 医师资格证遮盖
 * 1-4位是取得证书的年度代码 5-6位是省 自治区 直辖市的代码 第7位是执业医师级别代码
 * 8-9位是执业医师的类别代码 10-24(27)位是身份证代码
 * @createTime 2023/07/04
 */
public class PhysicianMedicalLicenceMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;
    public PhysicianMedicalLicenceMask() {
        this(8,19,'*');
    }

    public PhysicianMedicalLicenceMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    @Override
    public String pattern() {
//       String pattern =  "^\\d{4}[0-9A-Z]{2}\\d[0-9A-Z]{2}\\d{15}(\\d{3})?$";
       String pattern = "^\\d{4}[A-Z0-9]{2}\\d[A-Z0-9]{2}\\d{18}$";


        return pattern;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.maskNormal(source, maskStart, maskLength, maskTag);
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
