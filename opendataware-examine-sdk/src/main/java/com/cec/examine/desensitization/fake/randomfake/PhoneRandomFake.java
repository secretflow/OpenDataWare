package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.regex.Matcher;

/**
* @Author: Zgh
* @Date: 2023/9/20
* @description: 手机号后八位固定映射
*/
public class PhoneRandomFake extends SensitivePatternRegex {


	public PhoneRandomFake() {
	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)";
		return pattern;
	}

	@Override
	public String target(String source) {
		StringBuilder sb = new StringBuilder();
		String s1 = source.substring(0,3);
		String s2 = source.substring(3,11);
		sb.append(s1).append(SensitivePatternUtil.generateRandomNumber(s2.length()));
		return sb.toString();
	}

}
