package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;

/**
 * 个人荣誉哈希
 */
public class PersonalHonorMD5 extends SensitivePatternRegex {
    public PersonalHonorMD5(){

    }
    @Override
    public String pattern() {
        String pattern = "(?:年度风云人物|突出贡献奖|先进个人代表|最佳业绩奖|字字珠玑奖|优秀共产党员|优秀党务工作者" +
                "|劳动模范|优秀员工|业务能手|先进个人|业务专家|团队核心" +
                "|销售冠军| 杰出骨干|卓越精英|业务标兵|销售明星|服务之星|技术能手|优秀技师|优秀干部)";
        return pattern;
    }

    @Override
    public String target(String source) {
        return MD5Util.textToMD5L32(getSaltedValue(source));
    }
}
