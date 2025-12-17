package com.cec.examine.desensitization;


import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.dict.HIT;
import com.cec.examine.util.name.FamilyNames;
import com.cec.examine.util.SensitivePatternUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字典树模式识别抽象基类
 * 支持汉字名拆解+硬词匹配 词库/汉字拼音拆解+英文名词库
 * @author wenyiqiu
 */
public abstract class SensitivePatternDict extends SensitivePattern {

	protected String dicTag;//<o非拼音匹配，p拼音匹配
	protected String DIC_TAG_O = "o";//<硬匹配词库
	protected String DIC_TAG_P = "p";//<全拼音+英文词库
	protected Integer dictID;//<子类指定字典ID


	// 设置
	public void setDictID(Integer dictID) {
		this.dictID = dictID;
	}

	protected SensitivePatternDict(){}

	public SensitivePatternDict(Integer dictID) {
		this.dictID = dictID;
//		DaemonSyncHandler.init();
	}

	//全局字典树
	private static ConcurrentHashMap<String, AhoCorasickDoubleArrayTrie<String>> acDATMap = new ConcurrentHashMap<>();
	//拼音与汉字名假名拼音的映射表
	protected static ConcurrentHashMap<Integer, Map<String, String>> pinyinToFakeNamePinyinMap = new ConcurrentHashMap<>();
	
	/**
	 * 加载字典到ID编号的字典树
	 * @param dictID 字典编号
	 * @param names 名字列表
	 */
	public static void loadNames(Integer dictID, List<String> names) {
		if(names.size() == 0) {
			return;
		}

		//原始词库
		AhoCorasickDoubleArrayTrie<String> oacdat = new AhoCorasickDoubleArrayTrie<>();
		//拼音词库
		AhoCorasickDoubleArrayTrie<String> pacdat = new AhoCorasickDoubleArrayTrie<>();
		//原始词库
		TreeMap<String, String> omap = new TreeMap<>();
		//拼音词库
		TreeMap<String, String> pmap = new TreeMap<>();
		
		Map<String, String> subpinyinToFakeNamePinyinMap = new HashMap<>();

        for(String name : names) {
        	//获取名字对应的假名
			//假名规则转化
			String fakeName = targetFunction.apply(name);
        	if(SensitivePatternUtil.isHanName(name)) {
        			String lastName = name.substring(name.length()-2, name.length());//名
        			String firstName = name.substring(0, name.length()-2);//名
        			String lastNamePinyin = SensitivePatternUtil.HanToPinyin(lastName);
        			String firstNamePinyin = SensitivePatternUtil.HanToPinyin(firstName);

        			omap.put(lastName, lastName);
        			//汉字拼音需要加入到拼音词库里面
        			pmap.put(lastNamePinyin, lastNamePinyin);
        			pmap.put(firstNamePinyin, firstNamePinyin);
        			//汉字的情况：将拼音与假名拼音的映射表加入进去
        			String lastFakeName = fakeName.substring(fakeName.length()-2, fakeName.length());//拆名
        			String firstFakeName = fakeName.substring(0, fakeName.length()-2);//拆名
        			subpinyinToFakeNamePinyinMap.put(lastNamePinyin, SensitivePatternUtil.HanToPinyin(lastFakeName));
        			subpinyinToFakeNamePinyinMap.put(firstNamePinyin, SensitivePatternUtil.HanToPinyin(firstFakeName));
        	} else if (SensitivePatternUtil.isEnglishName(name)) {
        		//英文需要加入到拼音词库里面
        		pmap.put(name.toLowerCase(), name.toLowerCase());
        		//将英文名加入到拼音映射词库中
        		subpinyinToFakeNamePinyinMap.put(name.toLowerCase(), fakeName);
        	} else {
        		//其余的名字全部拼音化后加入映射
				String hanToPinyin = SensitivePatternUtil.HanToPinyin(name);
				pmap.put(hanToPinyin, hanToPinyin);
        		subpinyinToFakeNamePinyinMap.put(hanToPinyin, SensitivePatternUtil.HanToPinyin(fakeName));
        	}
            omap.put(name.toLowerCase(), name.toLowerCase());
        }
        oacdat.build(omap);
		acDATMap.put("o" + dictID, oacdat);
        pacdat.build(pmap);
		acDATMap.put("p" + dictID, pacdat);
		pinyinToFakeNamePinyinMap.put(dictID, subpinyinToFakeNamePinyinMap);
		//System.out.println(pmap);
	}
	

	public static void removeDict(int dictId) {

		pinyinToFakeNamePinyinMap.remove(dictId);
		acDATMap.remove("o" + dictId);
		acDATMap.remove("p" + dictId);
	}

	/**
	 * 定义转换目标抽象函数
	 * @param source 匹配上的原子串source
	 * @return 返回转换后的子串
	 */
	public abstract String target(String source);

	/**
	 * Trie树字典匹配逻辑
	 */
	@Override
	public String desensitive(String text, boolean longText) {
		if (text == null || "".equals(text)) return "";
		if(longText) {

//		DaemonSyncHandler.setCallDictSummary(this.dictID);

			//说明没有加载字典
			if (acDATMap.size() == 0) return text;

			List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acDATMap.get(dicTag + dictID).parseText(text.toLowerCase());
			ArrayList<HIT> res = new ArrayList<HIT>();
			//System.out.println(wordList);
			//最长匹配逻辑：Index=>String Length
			TreeMap<Integer, Integer> statusMap = new TreeMap<Integer, Integer>();
			//建表
			for (AhoCorasickDoubleArrayTrie.Hit<String> word : wordList) {
				for (int index = word.begin; index <= word.end; index++) {
					Integer length = statusMap.get(index);
					if (length == null) length = 0;
					int wordLen = word.end - word.begin;
					if (length < wordLen) {
						statusMap.put(index, wordLen);
					}
				}
			}
			//查表
			for (AhoCorasickDoubleArrayTrie.Hit<String> word : wordList) {
				for (int index = word.begin; index <= word.end; index++) {
					Integer length = statusMap.get(index);
					if (length == null) length = 0;
					int wordLen = word.end - word.begin;
					if (length == wordLen) {
						//说明匹配，保留当前词
						HIT h = new HIT();
						h.start = word.begin;
						h.end = word.end;
						h.source = word.value;
						h.target = target(word.value);
						res.add(h);
						break;
					}
				}
			}
			//System.out.println(res);
			return processHIT(res, text);
		} else {
			return this.target(text);
		}
	}

	public  List<HIT> getHit( String text) {
		if(text == null || "".equals(text)) return null;
		//说明没有加载字典
		if(acDATMap.size() == 0) return null;

		List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acDATMap.get(dicTag + dictID).parseText(text.toLowerCase());
		ArrayList<HIT> res = new ArrayList<HIT>();
		//System.out.println(wordList);
		//最长匹配逻辑：Index=>String Length
		TreeMap<Integer,Integer> statusMap = new TreeMap<Integer,Integer>();
		//建表
		for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
			for(int index = word.begin; index <= word.end; index++) {
				Integer length = statusMap.get(index);
				if(length == null) length = 0;
				int wordLen = word.end - word.begin;
				if(length < wordLen) {
					statusMap.put(index, wordLen);
				}
			}
		}
		//查表
		for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
			for(int index = word.begin; index <= word.end; index++) {
				Integer length = statusMap.get(index);
				if(length == null) length = 0;
				int wordLen = word.end - word.begin;
				if(length == wordLen) {
					//说明匹配，保留当前词
					HIT h = new HIT();
					h.start = word.begin;
					h.end = word.end;
					h.source = word.value;
					h.target = target(word.value);
					res.add(h);
					break;
				}
			}
		}
		return res;
	}

	private static final Function<String, String> targetFunction = (source) -> {

		String target = source;
		if (SensitivePatternUtil.isHanName(source)) {
			switch (source.length()) {
				case 2:
					target = FamilyNames.getLikelyName(source);
					break;
				case 3:
				case 4:
				case 5:
					target = FamilyNames.getLikelyFullName(source);
					break;
			}
		} else if (SensitivePatternUtil.isSingleEnglishName(source)) {
			//说明不包含空格
			target = FamilyNames.getEnglishSingleName(source);
		} else if (SensitivePatternUtil.isEnglishName(source)) {
			//说明包含空格
			target = FamilyNames.getEnglishSpaceName(source);
		} else {
			//其余的都返回三个汉字
			target = FamilyNames.getLikelyFullName(source);
		}
		return target;
	};

}
