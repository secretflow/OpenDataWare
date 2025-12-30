package com.cec.examine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.dict.HIT;
import com.cec.examine.util.encryption.MD5Util;
import com.cec.examine.util.name.FamilyNames;
import com.cec.examine.util.pinyin.PinyinFormat;
import com.cec.examine.util.pinyin.PinyinHelper;
/**
 * 仿真脱敏
 * @author wenyiqiu
 *
 */
public class DesensitiveSimulationUtil {
	
	public static AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
	public static boolean hasacdat = false;
	/**
	 * 使用字典过滤人名
	 * @param text
	 * @return
	 */
	public static String desensitiveNamesByDict(String text) {
		if(text == null || "".equals(text)) return "";
		if(!hasacdat) return text;
		List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acdat.parseText(text.toLowerCase());
		//最长匹配名字，过滤掉短名匹配
		ArrayList<HIT> res = new ArrayList<HIT>();
		int indexBegin = 0;//定义游标
		int indexEnd = 0;//定义游标
		AhoCorasickDoubleArrayTrie.Hit<String> lastWord = null;
		for(AhoCorasickDoubleArrayTrie.Hit<String> word: wordList) {
			if(word.begin > indexEnd) {		
				if(lastWord != null) {
					HIT h = new HIT();
					h.start = lastWord.begin;
					h.end = lastWord.end;
					h.source = lastWord.value;
					if(lastWord.value.length() == 2 
							&& Character.UnicodeScript.of(lastWord.value.charAt(0)) == Character.UnicodeScript.HAN
							&& Character.UnicodeScript.of(lastWord.value.charAt(1)) == Character.UnicodeScript.HAN) {
						h.target =  FamilyNames.getFakeName(lastWord.value);
					} else {
						h.target =  FamilyNames.getFakeFullName(lastWord.value);
					}
					res.add(h);
				}
				indexBegin = word.begin;
				indexEnd = word.end;
				lastWord = word;
			} else if(word.begin <= indexEnd){
				lastWord = word.end - word.begin > indexEnd - indexBegin? word: lastWord;
				indexBegin = indexBegin > word.begin ? indexBegin: word.begin;
				indexEnd = indexEnd > word.end ? indexEnd: word.end;
			}
		}
		if (lastWord != null) {
			HIT h = new HIT();
			h.start = lastWord.begin;
			h.end = lastWord.end;
			h.source = lastWord.value;
			if (lastWord.value.length() == 2
					&& Character.UnicodeScript.of(lastWord.value.charAt(0)) == Character.UnicodeScript.HAN
					&& Character.UnicodeScript.of(lastWord.value.charAt(1)) == Character.UnicodeScript.HAN) {
				h.target = FamilyNames.getFakeName(lastWord.value);
			} else {
				h.target = FamilyNames.getFakeFullName(lastWord.value);
			}
			res.add(h);
		}
		return processHIT(res,text);
	}
	public static void loadNames(List<String> names) {
		buildNameAndPinyinMap(names);
        TreeMap<String, String> map = new TreeMap<String, String>();
        for(String name : names) {
        	if(name.length() == 3 || name.length() == 4) {
        		//判断超过三个字的汉字姓名
        		boolean allIsHan = true;
        		for(int i = 0; i< name.length();i++) {
        			if(Character.UnicodeScript.of(name.charAt(i)) != Character.UnicodeScript.HAN) {
        				allIsHan = false;
        				break;
        			}
        		}
        		if(allIsHan) {
        			String subName = name.toLowerCase().substring(name.length()-2, name.length());
            		map.put(subName, subName);
            		map.put(nameToPinyinString(subName), nameToPinyinString(subName));
        		}
        	}
        	map.put(name.toLowerCase(), name.toLowerCase());
			map.put(nameToPinyinString(name.toLowerCase()), nameToPinyinString(name.toLowerCase()));
        }
		acdat.build(map);
		hasacdat = true;
	}
	/**
	 * 将@某人 替换为假名
	 * @param text
	 * @return
	 */
	public static String desensitiveAt(String text) {
		if(text == null || "".equals(text)) return "";
		Pattern r1 = Pattern.compile("@([\\u4e00-\\u9fa5A-Za-z]{1,10}(\\([A-Za-z\\s]{1,20}\\)){0,1})(\\s|@|$|;|,)");
        Matcher m1 = r1.matcher(text);
        ArrayList<HIT> res = new ArrayList<HIT>();
        int state = 0;
        while (m1.find(state)) {
        	//System.out.println("xx=" + m1.group(1) + "," + m1.group(3));
            String fetch = m1.group(1);
            int start = m1.start() + 1;
            int end = fetch.length() + start;
            String target = fetch;
            state = m1.end() - 2;
            //如果人名是两个汉字，那么不显示姓
			if(fetch.length() == 2 
					&& Character.UnicodeScript.of(fetch.charAt(0)) == Character.UnicodeScript.HAN
					&& Character.UnicodeScript.of(fetch.charAt(1)) == Character.UnicodeScript.HAN) {
				target = FamilyNames.getFakeName(fetch.substring(0,fetch.length()));
			} else {
				target = FamilyNames.getFakeFullName(fetch.substring(0,fetch.length()));
			}
			
			//封装结果
			HIT hit = new HIT();
			hit.start = start;
			hit.end = end;
			hit.source = fetch;
			hit.target = target;
			res.add(hit);
        }
        text = processHIT(res,text);
        return 	text;
	}
	
	public static String processHIT(List<HIT> res, String text) {
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		for(HIT h: res) {
			if(i < h.start) {
				sb.append(text.subSequence(i, h.start));
				i = h.start;
			}
			if (i == h.start) {
				sb.append(h.target);
				i = h.end;
			}
		}
		
		if(i < text.length()) {
			sb.append(text.subSequence(i,text.length()));
		}
		
		return sb.toString();
	}
	
	public static String padding(int size) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<size;i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	
	public static String desensitivePhoneNum(String text) {
		if(text == null || "".equals(text)) return "";
        String pattern = "(\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(\\D|\\b)";
     
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        String rep = "0000";
        List<HIT> res = new ArrayList<HIT>();
        while(m.find()) {
        	String source = m.group(0);
        	////生成Target，先生成仿真码
			Integer temp = Integer.parseInt(m.group(5)) * 13;
        	rep = String.valueOf(temp);
        	if(rep.length() > 4) rep = rep.substring(rep.length()-2, rep.length()) + rep.substring(0, 2);
        	if(rep.length() < 4) rep = padding(4-rep.length()) + rep; 
        	String target = m.group(1) + m.group(2) + m.group(3) + m.group(4) + rep + m.group(6);
        	//组装替换结果
        	HIT hit = new HIT();
        	hit.start = m.start();
        	hit.end = m.end();
        	hit.source = source;
        	hit.target = target;
        	res.add(hit);
        }
        return DesensitiveSimulationUtil.processHIT(res,text);
	}
	
	public static String desensitiveID(String text) {
		if(text == null || "".equals(text)) return "";
		String pattern = "(\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(\\D|\\b)";
		
		//将末尾四位数字处理成其他四位数字
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        String rep = "0000";
        List<HIT> res = new ArrayList<HIT>();
        while(m.find()) {
        	String source = m.group(0);			
        	////生成Target，先生成仿真码
        	Integer temp = Integer.parseInt(m.group(5)) * 17;
        	rep = String.valueOf(temp);
        	if(rep.length() > 4) rep = rep.substring(rep.length()-2, rep.length()) + rep.substring(0, 2);
        	if(rep.length() < 4) rep = DesensitiveSimulationUtil.padding(4-rep.length()) + rep;
        	String target = m.group(1) + m.group(2) + m.group(3) + m.group(4) + rep + m.group(6);
        	//组装替换结果
        	HIT hit = new HIT();
        	hit.start = m.start();
        	hit.end = m.end();
        	hit.source = source;
        	hit.target = target;
        	res.add(hit);
        }
        return DesensitiveSimulationUtil.processHIT(res,text);
	}
	
	public static String desensitiveEmail(String text) {
		if(text == null || "".equals(text)) return "";
		String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
		
		//将邮箱前面提取出来MD5处理拼接回去
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        String rep = MD5Util.textToMD5L32("1");
        List<HIT> res = new ArrayList<HIT>();
        while(m.find()) {
        	rep = MD5Util.textToMD5L32(m.group(1));
        	HIT hit = new HIT();
        	hit.start = m.start();
        	hit.end = m.end();
        	hit.source = m.group(0);
        	hit.target = rep + "@" + m.group(3);
        	res.add(hit);
        }
        //System.out.println(res);

        return processHIT(res,text);
	}
	
	public static String desensitivePassword(String text) {
		if(text == null || "".equals(text)) return "";
		if(text.indexOf("密码")>=0 || text.toLowerCase().indexOf("password") >=0)
			return "***";
		else return text;
	}
	
	public static String desensitiveAnyway(String text) {
		if(text == null || "".equals(text)) return "";
		else return "***";
	}

	public static String desensitiveNamesAnyway(String fetch) {
		if(fetch == null || "".equals(fetch)) return "";
		else if(fetch.length() == 2
				&& Character.UnicodeScript.of(fetch.charAt(0)) == Character.UnicodeScript.HAN
				&& Character.UnicodeScript.of(fetch.charAt(1)) == Character.UnicodeScript.HAN) {
			fetch = FamilyNames.getFakeName(fetch);
		} else {
			fetch = FamilyNames.getFakeFullName(fetch);
		}
		return fetch;
	}
	
	/**
	 * 脱敏成假名拼音
	 * @param fetch
	 * @return
	 */
	public static String desensitiveNamesAnywayToPinyin(String fetch) {
		String name = desensitiveNamesAnyway(fetch);
		String py = PinyinHelper.convertToPinyinString(name, "", PinyinFormat.WITHOUT_TONE);
		return py;
	}
	
	/**
	 * 脱敏成假名-名字和拼音
	 * @param fetch
	 * @return
	 */
	public static String []  desensitiveNamesAnywayToNameAndPinyin(String fetch) {
		String [] r = new String[2];
		r[0] = desensitiveNamesAnyway(fetch);
		r[1] = PinyinHelper.convertToPinyinString(r[0], "", PinyinFormat.WITHOUT_TONE);
		return r;
	}

	public static String desensitiveAllInOne( String text ) {
		//依次脱敏
		text = DesensitiveSimulationUtil.desensitiveID(text);
		text = DesensitiveSimulationUtil.desensitivePhoneNum(text);
		text = DesensitiveSimulationUtil.desensitiveEmail(text);
		text = DesensitiveSimulationUtil.desensitiveNamesByDict(text);
		text = DesensitiveSimulationUtil.desensitiveIP(text);
		text = DesensitiveSimulationUtil.desensitiveAt(text);
		return text;
	}
	
	public static String desensitiveAllInOneWithOutAt( String text ) {
		//依次脱敏
		text = DesensitiveSimulationUtil.desensitiveID(text);
		text = DesensitiveSimulationUtil.desensitivePhoneNum(text);
		text = DesensitiveSimulationUtil.desensitiveEmail(text);
		text = DesensitiveSimulationUtil.desensitiveNamesByDict(text);
		text = DesensitiveSimulationUtil.desensitiveIP(text);
		return text;
	}
	
	public static String desensitiveIP(String text) {
		if (text == null || "".equals(text)) return "";
		//ipv4预览脱敏正则
		String pattern = "(?<=(\\b|\\D))(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.)(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))";
		//ipv6脱敏正则
		/*
		String pattern_ipv6 = "(?<=(\\b|\\D))(([0-9a-fA-F]{1,4})\\:){7}(([0-9a-fA-F]){1,4})|" +
				"(([0-9a-fA-F]{1,4}))?\\::(([0-9a-fA-F]{1,4})\\:){5}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,2})\\::(([0-9a-fA-F]{1,4})\\:){4}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,3})\\::(([0-9a-fA-F]{1,4})\\:){3}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,4})\\::(([0-9a-fA-F]{1,4})\\:){2}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,5})\\::(([0-9a-fA-F]{1,4})\\:)(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,6})\\::(([0-9a-fA-F]{1,4}))|" +
				"(([:]{2}))";
		*/
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		List<HIT> res = new ArrayList<HIT>();
		while(m.find()) {
			HIT hit = new HIT();
			hit.start = m.start();
			hit.end = m.end();
			hit.source = m.group(0);
			hit.target =  m.group(2) + m.group(3) + m.group(4) +m.group(5) + map_ipv4(m.group(6)) + m.group(7) + map_ipv4(m.group(8));
			res.add(hit);
		}
		return processHIT(res,text);
	}
	//映射函数,将IPv4需要脱敏位置的数映射为一个新的数
	private static String map_ipv4(String text) {
		Integer val = Integer.valueOf(text);
		return String.valueOf( (val * 5 + 127 ) % 255);
	}

	//映射函数,将IPv6需要脱敏位置的数映射为一个新的数
	//private static String map_ipv6(String text) {
	//	Integer val = Integer.valueOf(text);
	//	return String.valueOf( (val * 5 + 127) % 9999);
	//}

	
	public static String desensitiveLandLine(String text) {
		if (text == null || "".equals(text)) return "";
		//匹配国内、国际（美国、日本、欧洲）座机号
		String pattern = "(?<=(\\b||\\D))(\\(?[0-9]\\)?[0-9]{1,3})?(\\)?-?)([0-9]{3,4})([0-9]{4})(?=(\\b|\\D))";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);

		List<HIT> res = new ArrayList<HIT>();

		while(m.find()) {
//			System.out.println("0-"+m.group(0));
//			System.out.println("1-"+m.group(1));
//			System.out.println("2-"+m.group(2));
//			System.out.println("3-"+m.group(3));
//			System.out.println("4-"+m.group(4));
//			System.out.println("5-"+m.group(5));

			HIT hit = new HIT();
			hit.start = m.start();
			hit.end = m.end();
			hit.source = m.group(0);
			if(m.group(2) == null) {
				hit.target =  m.group(1) + m.group(3) + m.group(4) + mapForLandLine(m.group(5));
				res.add(hit);
			}else {
				hit.target =  m.group(1) + m.group(2) + m.group(3) + m.group(4)+ mapForLandLine(m.group(5));
				res.add(hit);
			}
		}
		return processHIT(res,text);
	}
	//映射函数,将座机号中需要脱敏位置的数映射为一个新的数
	private static String mapForLandLine(String text) {
		Integer val = Integer.valueOf(text);
		assert val <=9999;
		return String.valueOf( (val * 5 + 8888 ) % 9999);
	}

	//调用PinyinHelper类里汉字转拼音方法，实现将names字典里维护的汉字转为拼音
		public static String nameToPinyinString(String str) {
			return PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITHOUT_TONE);
		}

		/**
	     *
	     * 对银行卡号进行仿真脱敏
	     * 目前银行卡号的长度只能是16或者19，其他长度的不视为银行卡号
		 * 不处理 **** **** **** **** 这种中间有空格的情况
	     *
	     * @param text
	     * @return 返回脱敏后的数据
	     */
		public static String desensitiveBankCard(String text) {
			if (text == null || "".equals(text.trim())) {
				return "";
			}

			String pattern = "([\b|\\D](\\d{19}|\\d{16})[\b|\\D])|([\b|\\D](\\d{19}|\\d{16})$)|(^(\\d{19}|\\d{16})[\b|\\D])|(^(\\d{19}|\\d{16})$)";

			Pattern compiler = Pattern.compile(pattern);

			Matcher matcher = compiler.matcher(text);

			List<HIT> hits = new ArrayList<>(10);

			while (matcher.find()) {
//	            String  originKeyWord = matcher.group(1);

				String originKeyWord = matcher.group();

				String cleanKeyWord = originKeyWord.replaceAll("^\\D", "")
						.replaceAll("\\D$", "");

				if (checkLuhn(cleanKeyWord)) {

					// TODO 增加国外类型银联的前缀
//					if (cleanKeyWord.startsWith("62") ||
//							cleanKeyWord.startsWith("88")) {

					HIT hit = new HIT();
					hit.source = cleanKeyWord;
					hit.start = matcher.start();
					hit.end = matcher.end();

					if (Objects.equals(originKeyWord.length() - 2, cleanKeyWord.length())) {
						hit.start = hit.start + 1;
						hit.end = hit.end - 1;
					} else if (Objects.equals(originKeyWord.length() - 1, cleanKeyWord.length())) {
						if (originKeyWord.indexOf(cleanKeyWord) == 0) {
							hit.end = hit.end - 1;
						} else if (originKeyWord.indexOf(cleanKeyWord) == 1) {
							hit.start = hit.start + 1;
						}
					}

					String pre = "";
					String mid = "";
					String last = "";
					if (hit.source.length() == 16) {
						pre = hit.source.substring(0, 6);
						mid = hit.source.substring(6, 12);
						last = hit.source.substring(12);
						String mask = String.valueOf(mid.hashCode());
						int maskLen = mask.length();
						if (maskLen < 6) {
							mid = "000000";
						} else {
							mid = mask.substring(maskLen - 6, maskLen);
						}
					} else if (hit.source.length() == 19) {
						pre = hit.source.substring(0, 6);
						mid = hit.source.substring(6, 15);
						last = hit.source.substring(15);
						String mask = String.valueOf(mid.hashCode());
						int maskLen = mask.length();
						if (maskLen < 9) {
							mid = "000000000";
						} else {
							mid = mask.substring(maskLen - 9, maskLen);
						}
					}
					hit.target = pre + mid + last;
					hits.add(hit);
				}
			}
			return processHIT(hits, text);
		}
		
		public static boolean checkLuhn(String cardNo) {
	        int nDigits = cardNo.length();
	        int nSum = 0;
	        boolean isSecond = false;
	        for (int i = nDigits - 1; i >= 0; i--) {
	            int d = cardNo.charAt(i) - '0';
	            if (isSecond == true)
	                d = d * 2;
	            // We add two digits to handle
	            // cases that make two digits
	            // after doubling
	            nSum += d / 10;
	            nSum += d % 10;
	            isSecond = !isSecond;
	        }
	        return (nSum % 10 == 0);
	    }

		public static String getNameWithPinyin(String pinyin){
			return nameAndPinyinMap.get(pinyin).toString();
		}
		private static HashMap<String, String> nameAndPinyinMap = new HashMap<String, String>();

		/**
		 * 将加载的姓名和拼音维护成map
		 * @param names
		 */
		public static void buildNameAndPinyinMap(List<String> names) {

			for(String name : names) {
				if(name.length() == 3 || name.length() == 4) {
					//判断超过三个字的汉字姓名
					boolean allIsHan = true;
					for(int i = 0; i< name.length();i++) {
						if(Character.UnicodeScript.of(name.charAt(i)) != Character.UnicodeScript.HAN) {
							allIsHan = false;
							break;
						}
					}
					if(allIsHan) {
						String subName = name.toLowerCase().substring(name.length()-2, name.length());
						nameAndPinyinMap.put(nameToPinyinString(subName), subName);
					}
				}
				nameAndPinyinMap.put(nameToPinyinString(name.toLowerCase()), name.toLowerCase());
			}

//			System.out.println(nameAndPinyinMap);
		}




}
