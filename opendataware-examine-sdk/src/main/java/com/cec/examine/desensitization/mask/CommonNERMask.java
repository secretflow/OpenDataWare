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
 * @Description 通用NER遮蔽函数，任何NER类型都可以直接使用
 * @createTime 2023/07/12
 */
public class CommonNERMask extends SensitivePatternNER {

    //这个是目前安全系统支持的所有类型
    private int maskStart;
    private int maskLength;
    private char maskTag;

    public CommonNERMask() {
        String [] arrType = new String[]{BertNerTypeCode.ADDRESS,BertNerTypeCode.NAME, BertNerTypeCode.COMPANY,BertNerTypeCode.GOVERNMENT, BertNerTypeCode.ORGANIZATION};
        setRecognize(new RecognizeNERRuleBased());
    }

    public CommonNERMask(int maskStart, int maskLength, char maskTag) {
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
