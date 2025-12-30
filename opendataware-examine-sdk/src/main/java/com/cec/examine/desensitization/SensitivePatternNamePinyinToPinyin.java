package com.cec.examine.desensitization;


/**
 * 英文名->英文假名: 
 *  Albert Sim->Orville Christ
 * 汉字名拼音->汉字假名拼音: 
 *  zhansanfeng->lixiaotian
 *  zhan.sanfeng->li.xiaotian
 * @author wenyiqiu
 *
 */
public class SensitivePatternNamePinyinToPinyin extends SensitivePatternDict {
	
	public SensitivePatternNamePinyinToPinyin(Integer dictID) {
		super(dictID);
		this.dicTag = this.DIC_TAG_P;//设置为拼音词库
	}
	
	@Override
	public String target(String source) {
		return pinyinToFakeNamePinyinMap.get(dictID).get(source).toLowerCase();
	}

}
