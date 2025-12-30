package com.cec.examine.desensitization.ner;

import com.cec.examine.desensitization.SensitivePatternNER;
import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 住址遮盖
 * @createTime 2023/07/12
 */
public class AddressNER extends SensitivePatternNER {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public AddressNER() {
    }

    @Override
    public String target(String s) {
        return null;
    }

    public AddressNER(String text) {
        this(text.indexOf("市")+1,text.length(),'*');
    }

    public AddressNER(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }

    public String pattern() {
        String pattern = "([\\u4e00-\\u9fa5]{2,5}(?:省|自治区|市))([\\u4e00-\\u9fa5]{2,7}?(?:市|区|县|州)){0,1}([\\u4e00-\\u9fa5]{2,7}?(?:路|街|巷|道|弄|号)){0,1}";
        return pattern;
    }

    public String target(Matcher m) {
        String source = m.group(0);

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
