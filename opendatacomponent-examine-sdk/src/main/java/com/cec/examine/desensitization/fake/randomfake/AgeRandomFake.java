package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.Random;
import java.util.regex.Matcher;

/**
 *年龄随机映射  最大年龄130
 **/
public class AgeRandomFake extends SensitivePatternRegex {


	public AgeRandomFake() {
	}

	@Override
	public String pattern() {
		String pattern = "\\b\\d{1,3}(?:岁)?\\b";
		return pattern;
	}

	@Override
	public String target(String target) {
		//将除去前六位的号码全部散列，然后按照Luhn生成校验码
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		sb.append(random.nextInt(Integer.parseInt(target)));
		if(target.contains("岁")){
			sb.append("岁");
		}
		return sb.toString();
	}
	
	

}
