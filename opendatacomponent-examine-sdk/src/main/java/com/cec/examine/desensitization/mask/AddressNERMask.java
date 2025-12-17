package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternNER;
import com.cec.examine.ner.BertNerTypeCode;
import com.cec.examine.recognize.RecognizeNERRuleBased;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;

/**
 * @author ZGH
 * @version 1.0.0
 * @Description 住址遮盖
 * @createTime 2023/07/12
 */
public class AddressNERMask extends SensitivePatternNER {
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public AddressNERMask() {
        String [] arrType = new String[]{BertNerTypeCode.ADDRESS};
        setRecognize(new RecognizeNERRuleBased());
    }

    public AddressNERMask(int maskStart, int maskLength, char maskTag) {
        this.maskStart = maskStart;
        this.maskLength = maskLength;
        this.maskTag = maskTag;
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
