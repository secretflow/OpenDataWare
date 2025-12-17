package com.cec.examine.desensitization.fake.randomfake;

import com.cec.examine.desensitization.SensitivePatternDict;
import com.cec.examine.util.name.FamilyNames;
import com.cec.examine.util.SensitivePatternUtil;

/**
 * 字典人名假名化
 *  ->  
 * @author wenyiqiu
 *
 */
public class NameFake extends SensitivePatternDict {
	
	public NameFake(Integer dictID) {
		super(dictID);
		this.dicTag = this.DIC_TAG_O;//设置为原始词库
	}
	
	@Override
	public String target(String source) {
		String target = source;
		if(SensitivePatternUtil.isHanName(source)) {
			switch(source.length()) {
			case 2:
				target = FamilyNames.getLikelyName(source);
				break;
			case 3:
			case 4:
			case 5:
				target = FamilyNames.getLikelyFullName(source);
				break;
			}
		} else if(SensitivePatternUtil.isSingleEnglishName(source)) {
			//说明不包含空格
			target = FamilyNames.getEnglishSingleName(source);
		} else if(SensitivePatternUtil.isEnglishName(source) ) {
			//说明包含空格
			target = FamilyNames.getEnglishSpaceName(source);
		} else {
			//其余的都返回三个汉字
			target = FamilyNames.getLikelyFullName(source);
		}
		return target;
	}

}
