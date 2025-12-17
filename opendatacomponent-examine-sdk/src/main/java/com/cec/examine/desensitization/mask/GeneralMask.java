package com.cec.examine.desensitization.mask;
import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;
/**
 * @author koala
 * @version 1.0.0
 * @Description 通用遮蔽
 * @createTime 2025/12/05
 */
public class GeneralMask extends SensitivePatternRegex {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public GeneralMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
    }


    @Override
    public String pattern() {
        return "";
    }

    @Override
    public String target(String source) {
        if(source == null || "null".equals(source)){ return null;}
        return SensitivePatternUtil.mask(source, maskStart, maskLength, maskTag);
    }
}
