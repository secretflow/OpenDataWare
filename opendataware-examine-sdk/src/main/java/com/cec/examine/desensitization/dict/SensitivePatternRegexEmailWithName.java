package com.cec.examine.desensitization.dict;

import java.util.regex.Matcher;

import com.cec.examine.desensitization.SensitivePatternNamePinyinToPinyin;
import com.cec.examine.desensitization.SensitivePatternRegex;

/**
 * 若邮箱前缀本身是人名或者汉语人名的拼音，那么则可以按照名字的方式脱敏
 * @author wenyiqiu
 */
public class SensitivePatternRegexEmailWithName extends SensitivePatternRegex {

	private  int dictID;

	public SensitivePatternRegexEmailWithName() {
	}

	public SensitivePatternRegexEmailWithName(int dictID) {

		this.dictID = dictID;
	}
	
	@Override
	public String pattern() {
		String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
		return pattern;
	}
	@Override
	public String target(String source) {
		String [] s = source.split("@",-1);
		StringBuilder sb = new StringBuilder();
		String replace = new SensitivePatternNamePinyinToPinyin(dictID).desensitive(s[0]) ;//英文->英文 && 拼音->拼音
		sb.append(replace).append("@").append(s[1]);
		return sb.toString();
	}

}
