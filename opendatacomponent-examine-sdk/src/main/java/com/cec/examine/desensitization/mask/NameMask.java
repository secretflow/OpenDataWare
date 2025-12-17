package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 姓名遮盖
 * @createTime 2023/07/25
 */
public class NameMask extends SensitivePatternRegex {

    private int maskStart;
    private int maskLength;
    private char maskTag;
    public NameMask() {
        this('*');
    }

    public NameMask(char maskTag) {
        this.maskTag = maskTag;

    }

    @Override
    public String pattern() {
        String chineseNameRegex = "(?<=\\D|\\b)([\\u4E00-\\u9FA5A-Za-z\\s]+(·[\\u4E00-\\u9FA5A-Za-z]+)*$)(?=\\D|\\b)";
        return chineseNameRegex;
    }

    @Override
    public String target(String source) {

        if (Objects.isNull(this.configs)) {
            return SensitivePatternUtil.maskNormal(source, 1, source.length(), '*');
        }

        String result = source;

        for (Config config : this.configs) {

            result = SensitivePatternUtil.maskWithConfig(result, config);
        }

        return result;
    }
}
