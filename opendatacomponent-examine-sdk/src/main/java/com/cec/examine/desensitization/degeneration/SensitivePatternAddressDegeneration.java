package com.cec.examine.desensitization.degeneration;


import com.cec.examine.desensitization.SensitivePatternDegeneration;

/**
 * 字典人名遮蔽
 *  ->张*丰
 * 张三->张*
 * Sam Li->S*m L*
 * Sam->S*m
 * @author wenyiqiu
 *
 */
public class SensitivePatternAddressDegeneration extends SensitivePatternDegeneration {

	public SensitivePatternAddressDegeneration(Integer dictID) {
		super(dictID);
		this.dicTag = this.DIC_TAG_O;//设置为原始词库
	}

	public SensitivePatternAddressDegeneration() {

	}
	
	@Override
	public String target(String source) {
		return source;
	}

}
