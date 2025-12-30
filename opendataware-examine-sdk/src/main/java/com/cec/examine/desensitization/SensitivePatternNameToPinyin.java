package com.cec.examine.desensitization;

import com.cec.examine.desensitization.fake.randomfake.NameFake;
import com.cec.examine.util.pinyin.PinyinFormat;
import com.cec.examine.util.pinyin.PinyinHelper;


/**
 * 英文名->英文假名：Albert Sim->Orville Christ
 * 汉字名->汉字拼音： ->lixiaotian
 * @author wenyiqiu
 *
 */
public class SensitivePatternNameToPinyin extends SensitivePatternDict {
	
	public SensitivePatternNameToPinyin(Integer dictID) {
		super(dictID);
		this.dicTag = this.DIC_TAG_O;//设置为原始词库
	}
	
	@Override
	public String target(String source) {
		String fakeName = new NameFake(dictID).target(source);
		return PinyinHelper.convertToPinyinString(fakeName, "", PinyinFormat.WITHOUT_TONE);
	}

}
