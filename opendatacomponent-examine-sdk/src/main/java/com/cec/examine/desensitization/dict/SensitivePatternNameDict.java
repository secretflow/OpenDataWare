package com.cec.examine.desensitization.dict;

import com.cec.examine.desensitization.SensitivePatternDict;
import com.cec.examine.util.SensitivePatternUtil;

/**
 * 字典人名遮蔽
 *  ->张*丰
 * 张三->张*
 * Sam Li->S*m L*
 * Sam->S*m
 * @author wenyiqiu
 *
 */
public class SensitivePatternNameDict extends SensitivePatternDict {
	
	public SensitivePatternNameDict(Integer dictID) {
		super(dictID);
		this.dicTag = this.DIC_TAG_O;//设置为原始词库
	}

	public SensitivePatternNameDict() {

	}
	
	@Override
	public String target(String source) {
		return SensitivePatternUtil.maskMiddle(source, '*', 2);
	}

}
