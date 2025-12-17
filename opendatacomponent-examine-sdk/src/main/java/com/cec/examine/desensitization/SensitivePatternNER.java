package com.cec.examine.desensitization;


import com.cec.examine.dict.HIT;
import com.cec.examine.ner.BertNerTypeCode;
import com.cec.examine.recognize.Recognize;

import java.util.*;
/**
 * NER模式识别抽象基类
 * 支持汉字名拆解+硬词匹配 词库/汉字拼音拆解+英文名词库
 * @author wenyiqiu
 */
public abstract class SensitivePatternNER extends SensitivePattern {

	private String [] arrType = new String[]{BertNerTypeCode.ADDRESS,BertNerTypeCode.NAME, BertNerTypeCode.COMPANY,BertNerTypeCode.GOVERNMENT, BertNerTypeCode.ORGANIZATION};
	private Recognize recognize;
	public SensitivePatternNER() {
	}

	public void setRecognize(Recognize recognize) {
		this.recognize = recognize;
	}

	@Override
	public String desensitive(String text, boolean longText) {
		if(text == null || "".equals(text)) return "";
		if(longText) {
			List<HIT> res = recognize.getHitResult(text);
			for (HIT h : res) {
				h.target = this.target(h.source);
			}
			return processHIT(res, text);
		} else {
			return this.target(text);
		}
	}

	public abstract String target(String s);//定义重新组装后的格式
}
