package com.cec.examine.desensitization.fake.fixedfake;

import com.cec.examine.desensitization.SensitivePatternRegex;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.regex.Matcher;

public class BankCardFixedFake extends SensitivePatternRegex {


	public BankCardFixedFake() {

	}

	@Override
	public String pattern() {
		String pattern = "(?<=\\D|\\b)(\\d{16,19})(?=\\D|\\b)";
		return pattern;
	}

	@Override
	public String target(String target) {
		//将除去前六位的号码全部散列，然后按照Luhn生成校验码
		//String target = m.group(0);
		StringBuilder sb = new StringBuilder(target.substring(0, 6));
		if(SensitivePatternUtil.checkLuhn(target)) {
			//6-(len-1)
			String userInfo = target.substring(6, target.length()-1);
			sb.append(SensitivePatternUtil.getEqLenNumStr(userInfo));
			sb.append(SensitivePatternUtil.getLuhnCode(target.substring(0, target.length()-1)));
			target = sb.toString();
		}
		return target;
	}
	
	

}
