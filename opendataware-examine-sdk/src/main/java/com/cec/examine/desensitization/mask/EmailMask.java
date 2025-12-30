package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

public class EmailMask extends SensitivePatternRegex {

	private char maskTag;
	
	public EmailMask() {
		this('*');

	}
	
	public EmailMask(char maskTag) {

		this.maskTag = maskTag;
	}
	
	@Override
	public String pattern() {
		String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
		return pattern;
	}
	@Override
	public String target(String result) {
		String [] s = result.split("@",-1);
		if(s.length < 2) return result;//如果不符合email格式，则直接返回原值
		if (Objects.isNull(this.configs)) {

			String bf = SensitivePatternUtil.maskUntil(s[0], 1, -2, '*');
			String af = SensitivePatternUtil.maskUntil(s[1], 1, -2, '*');

			return bf + "@" + af;
		}

		for (Config config : this.configs) {

			result = SensitivePatternUtil.maskWithConfig(result, config);
		}

		return result;
	}

}
