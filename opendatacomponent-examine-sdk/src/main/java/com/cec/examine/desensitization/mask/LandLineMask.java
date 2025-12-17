package com.cec.examine.desensitization.mask;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;

public class LandLineMask extends SensitivePatternRegex {


	public LandLineMask() {

	}
	
	@Override
	public String pattern() {
		String pattern = "(\\d{3,4}-\\d{7,8})";
		return pattern;
	}
	
	@Override
	public String target(String source) {

	    // TODO group and group(number)
		String result = source;
		if (Objects.isNull(this.configs)) {
			String[] texts = source.split("\\-");
			result = texts[0]+"-"+texts[1].substring(0,4)+"****";
			return result;
		}

		for (Config config : this.configs) {

			result = SensitivePatternUtil.maskWithConfig(result, config);
		}

		return result;
	}

}
