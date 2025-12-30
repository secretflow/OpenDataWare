package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.regex.Matcher;

/**
 *银行卡随机映射  保留前6位 后13位随机映射
 **/
public class BankCardRandomFake extends SensitivePatternRegex {


	public BankCardRandomFake() {
	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)";
		return pattern;
	}

	@Override
	public String target(String target) {
		//将除去前六位的号码全部散列，然后按照Luhn生成校验码
		StringBuilder sb = new StringBuilder(target.substring(0, 6));
		sb.append(SensitivePatternUtil.generateRandomNumber(target.substring(6,target.length()).length()));
		return sb.toString();
	}
	
	

}
