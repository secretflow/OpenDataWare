package com.cec.examine.desensitization;

import com.cec.examine.dict.HIT;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则模式匹配抽象基类
 * @author wenyiqiu
 *
 */
public abstract class SensitivePatternRegex extends SensitivePattern {

	public SensitivePatternRegex(){

	}

	public abstract String pattern();//定义敏感模式正则

	public abstract String target(String source);//定义重新组装后的格式

	public String target(Matcher matcher){return  this.target(matcher.group());}//默认情况下，调用Matcher的效果就是直接返回正则命中的整体字符串

	/**
	 *
	 * @param text 长文本内容
	 * @param longText true代表开启扫描
	 * @return
	 */
	@Override
	public String desensitive(String text, boolean longText) {
		if (text == null || "".equals(text)) return "";
		if(longText) {
			this.text = text;//将原文传递给子类
			Pattern r = Pattern.compile(this.pattern());
			Matcher m = r.matcher(text);
			List<HIT> res = new ArrayList<HIT>();
			while (m.find()) {
				String source = m.group(0);
				//组装替换结果
				HIT hit = new HIT();
				hit.start = m.start();
				hit.end = m.end();
				hit.source = source;
				hit.target = this.target(m);
				res.add(hit);
			}
			return processHIT(res, text);
		} else {
			return this.target(text);
		}
	}

	public  List<HIT> getHit( String text) {
		if(text == null || "".equals(text)) return null;
		this.text = text;//将原文传递给子类
		Pattern r = Pattern.compile(pattern());
		Matcher m = r.matcher(text);
		List<HIT> res = new ArrayList<HIT>();
		while(m.find()) {
			String source = m.group(0);
			//组装替换结果
			HIT hit = new HIT();
			hit.start = m.start();
			hit.end = m.end();
			hit.source = source;
			hit.target = this.target(m.group(0));
			res.add(hit);
		}
		return res;
	}

}
