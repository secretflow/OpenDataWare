package com.cec.examine.desensitization.fake.fixedfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.regex.Matcher;

/**
 *IPv4假名化 固定映射
 **/
public class IPv4FixedFake extends SensitivePatternRegex {

	public IPv4FixedFake() {

	}

	@Override
	public String pattern() {
		String pattern = "(?<=(\\b|\\D))(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))";
		//ipv6脱敏正则
				/*
				String pattern_ipv6 = "(?<=(\\b|\\D))(([0-9a-fA-F]{1,4})\\:){7}(([0-9a-fA-F]){1,4})|" +
						"(([0-9a-fA-F]{1,4}))?\\::(([0-9a-fA-F]{1,4})\\:){5}(([0-9a-fA-F]{1,4}))|" +
						"(([0-9a-fA-F]{1,4}){0,2})\\::(([0-9a-fA-F]{1,4})\\:){4}(([0-9a-fA-F]{1,4}))|" +
						"(([0-9a-fA-F]{1,4}){0,3})\\::(([0-9a-fA-F]{1,4})\\:){3}(([0-9a-fA-F]{1,4}))|" +
						"(([0-9a-fA-F]{1,4}){0,4})\\::(([0-9a-fA-F]{1,4})\\:){2}(([0-9a-fA-F]{1,4}))|" +
						"(([0-9a-fA-F]{1,4}){0,5})\\::(([0-9a-fA-F]{1,4})\\:)(([0-9a-fA-F]{1,4}))|" +
						"(([0-9a-fA-F]{1,4}){0,6})\\::(([0-9a-fA-F]{1,4}))|" +
						"(([:]{2}))";
				*/
		return pattern;
	}
	@Override
	public String target(String source) {
		String [] s = source.split(".",-1);
		return ipFake(s[0]) + "." + ipFake(s[1]) + "." + ipFake(s[2]) + "." + ipFake(s[3]);
	}
	
	private String ipFake(String text) {
		return String.valueOf( Math.abs(text.hashCode()) % 255);
	}

}
