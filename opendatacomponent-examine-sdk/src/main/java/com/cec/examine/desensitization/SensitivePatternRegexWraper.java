package com.cec.examine.desensitization;

import com.cec.examine.recognize.RecognizeRegularExpression;
import com.cec.examine.template.Rule;

import java.util.regex.Matcher;

/**
 * 正则模式匹配模板类，可以支持设置识别对象和设置脱敏方法
 * @author wenyiqiu
 *
 */
public class SensitivePatternRegexWraper extends SensitivePatternRegex {

	private Rule<RecognizeRegularExpression> rule;

	private SensitivePatternRegex sensitivePatternRegex;

	public SensitivePatternRegexWraper(Rule rule){
		this.rule = rule;

		String funcClassName = rule.getDenseFunctionClass();
		Class<?> aClass = null;
		try {
			aClass = Class.forName(funcClassName);
			this.sensitivePatternRegex = (SensitivePatternRegex)aClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		sensitivePatternRegex.setConfigs(rule.getConfigs());
	}


	@Override
	public String pattern() {
		// 优先获取规则中设置的pattern，若规则未设置pattern则使用函数内置pattern。
		String pattern = rule.getRecognize().getPattern();
		if(pattern == null)
			pattern = sensitivePatternRegex.pattern();
		return pattern;
	}


	@Override
	public String target(String source) {
		return sensitivePatternRegex.target(source);
	}

}
