package com.cec.examine.desensitization;
import com.cec.examine.recognize.RecognizeDictionary;
import com.cec.examine.recognize.RecognizeRegularExpression;
import com.cec.examine.template.Rule;

/**
 * 正则模式匹配模板类，可以支持设置识别对象和设置脱敏方法
 * @author wenyiqiu
 *
 */
public class SensitiveAdaptor {

	private Rule<?> rule;


	public SensitiveAdaptor(Rule rule){
		this.rule = rule;
	}

	public String desensitive(String text) {
		String result = text;
		if(this.rule.getRecognize() instanceof RecognizeRegularExpression) {
			//正则识别
			SensitivePatternRegexWraper sensitivePatternRegexWraper = new SensitivePatternRegexWraper(rule);
			result = sensitivePatternRegexWraper.desensitive(text);
		} else if(this.rule.getRecognize() instanceof RecognizeDictionary){
			//字典识别

		}
		return result;
	}

}
