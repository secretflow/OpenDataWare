package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternDict;
import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

/**
 */
public class NameMD5 extends SensitivePatternRegex {
	
//	public NameMD5(Integer dictID) {
//		super(dictID);
//		this.dicTag = this.DIC_TAG_O;//设置为原始词库
//
//	}

	@Override
	public String pattern() {
		String chineseNameRegex = "(?<=\\D|\\b)([\\u4E00-\\u9FA5A-Za-z\\s]+(·[\\u4E00-\\u9FA5A-Za-z]+)*$)(?=\\D|\\b)";
		return chineseNameRegex;
	}

	@Override
	public String target(String source) {
		return MD5Util.textToMD5L32(source);
	}
	
}
