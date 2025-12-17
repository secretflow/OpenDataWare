package com.cec.examine.desensitization;

import java.util.regex.Matcher;

public class SensitivePatternJsonFromTemplate extends SensitivePatternRegex {
	

	private Template tpl;
	public SensitivePatternJsonFromTemplate(Template tpl) {
		this.tpl = tpl;
	}


	@Override
	public String pattern() {
		String pattern = "(\")(.*?)([^\\\\](\"))";
		return pattern;
	}

	@Override
	public String target(String source) {
		return source;
	}

	@Override
	public String target(Matcher m) {
		String target = m.group(0);
		char c = getNextNonBlankChar(m.end());
		if('\0' != c && ':' != c) {
			target = "\"" + tpl.target(target.substring(1, target.length()-1)) + "\"";
		}
		return target;
	}

	/**
	 * 模板处理的子类，需要重新desensitive方法，因为模板只能是走长文本处理
	 * @param text 短文本，默认text就是脱敏对象本身
	 * @return
	 */
	@Override
	public String desensitive(String text) {
		return desensitive(text, true);
	}


	
	//取下一个非空字符
	public char getNextNonBlankChar(int start) {
		int i = start;
		for(; i < text.length(); i++) {
			if(text.charAt(i) == '\f' || 
				text.charAt(i) == '\n' || 
				text.charAt(i) == '\r' || 
				text.charAt(i) == '\t' || 
				text.charAt(i) == '\b' || 
				text.charAt(i) == 32) {
				continue;
			} else {
				break;
			}
		}
		char c = i == text.length() ? '\0' : text.charAt(i);
		return c;
	}
}
