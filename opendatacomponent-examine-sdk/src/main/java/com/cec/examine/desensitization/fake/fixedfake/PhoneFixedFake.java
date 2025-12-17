package com.cec.examine.desensitization.fake.fixedfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

public class PhoneFixedFake extends SensitivePatternRegex {


	public PhoneFixedFake() {

	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)";
		return pattern;
	}

	@Override
	public String target(String source) {
		String s1 = source.substring(0,3);
		String s2 = source.substring(3,11);
		if(Objects.isNull(this.configs)){
			String replace = SensitivePatternUtil.getEqLenNumStr(s2);
			StringBuilder sb = new StringBuilder();
			sb.append(s1).append(replace);
			return sb.toString();
		}else {
			Config config = this.configs.get(0);
			String replace = SensitivePatternUtil.getEqLenNumStr(source.substring(config.getStart(),config.getEnd()));
			StringBuilder sb = new StringBuilder(source.substring(0,config.getStart()));
			sb.append(replace);
			sb.append(source.substring(config.getEnd(),source.length()));
			return sb.toString();

		}
	}

}
