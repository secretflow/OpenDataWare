package com.cec.examine.desensitization.mask;
import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.template.Config;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.Objects;
import java.util.regex.Matcher;
/**
 *IPv4遮盖
 **/
public class IPv4Mask extends SensitivePatternRegex {


	public IPv4Mask() {
	}


	@Override
	public String pattern() {
		String pattern = "(?<=(\\b|\\D))((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))";
		return pattern;
	}


	@Override
	public String target(String source) {

		if (Objects.isNull(this.configs)) {
			String[] texts = source.split("\\.");

			return texts[0]+"."+texts[1]+"."+"**"+"."+"**";
		}

		String result = source;

		for (Config config : this.configs) {

			result = SensitivePatternUtil.maskWithConfig(result, config);

		}

		return result;

	}

}
