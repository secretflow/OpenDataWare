package com.cec.examine.desensitization.hash;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.encryption.MD5Util;

import java.util.regex.Matcher;


public class PhoneMD5 extends SensitivePatternRegex {
	
	public PhoneMD5() {

	}
	
	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)";
		return pattern;
	}
	@Override
	public String target(String source) {
		return MD5Util.textToMD5L32(getSaltedValue(source));
	}

}
