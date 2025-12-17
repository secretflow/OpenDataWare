package com.cec.examine.desensitization.fake.fixedfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.regex.Matcher;
/**
 *座机号假名化
 **/
public class LandLineFixedFake extends SensitivePatternRegex {

	public LandLineFixedFake() {

	}

	@Override
	public String pattern() {
		return "(\\d{3,4}-\\d{7,8})";
	}
	@Override
	public String target(String source) {
		String[] split = source.split("-");

		return SensitivePatternUtil.getEqLenNumStr(split[0]) + "-" + SensitivePatternUtil.getEqLenNumStr(split[1]);
	}

}
