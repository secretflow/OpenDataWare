package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 个人荣誉遮盖
 * @createTime 2023/07/13
 */
public class PersonalHonorMask extends SensitivePatternRegex {
    @Override
    public String pattern() {
        String pattern = "(?:年度风云人物|突出贡献奖|先进个人代表|最佳业绩奖|字字珠玑奖|优秀共产党员|优秀党务工作者" +
                "|劳动模范|优秀员工|业务能手|先进个人|业务专家|团队核心" +
                "|销售冠军| 杰出骨干|卓越精英|业务标兵|销售明星|服务之星|技术能手|优秀技师|优秀干部)";
        return pattern;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.maskNormal(source, 0, source.length(), '*');
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
