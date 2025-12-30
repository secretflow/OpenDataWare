package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

public class PhoneMask extends SensitivePatternRegex {


	private int maskStart;
	private int maskLength;
	private char maskTag;
	
	public PhoneMask() {
		this(3, 4, '*');
	}

	public PhoneMask(int maskStart, int maskLength, char maskTag) {
		this.maskStart = maskStart;
		this.maskLength = maskLength;
		this.maskTag = maskTag;

	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(?=\\D|\\b)";
		return pattern;
	}

	@Override
	public String target(String source) {
//		return SensitivePatternUtil.mask(m.group(0), maskStart, maskLength, maskTag);

		if (Objects.isNull(this.configs)) {

			return SensitivePatternUtil.mask(source, maskStart, maskLength, maskTag);
		}

		String result = source;

		for (Config config : this.configs) {

			result = SensitivePatternUtil.maskWithConfig(result, config);
		}

		return result;

	}

}
