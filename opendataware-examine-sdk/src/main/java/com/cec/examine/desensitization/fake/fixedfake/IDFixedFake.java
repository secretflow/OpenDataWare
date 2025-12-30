package com.cec.examine.desensitization.fake.fixedfake;
import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.regex.Matcher;

public class IDFixedFake extends SensitivePatternRegex {

	public IDFixedFake() {

	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(?=\\D|\\b)";
		return pattern;
	}
	@Override
	public String target(String source) {
		StringBuilder sb = new StringBuilder();
		String s1 = source.substring(0,14);
		String s2 = source.substring(14,18);
		sb.append(s1).append(SensitivePatternUtil.getEqLenNumStr(s2));
		return sb.toString();
	}

}
