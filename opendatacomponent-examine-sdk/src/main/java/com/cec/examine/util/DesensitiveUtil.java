package com.cec.examine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cec.examine.dict.AhoCorasickDoubleArrayTrie;
import com.cec.examine.dict.HIT;
import com.cec.examine.util.name.CheckNameUtil;
public class DesensitiveUtil {
	/**
	 * 使用字典过滤人名
	 * @param text
	 * @return
	 */
	public static String desensitiveNamesByDict(String text) {
		if(text == null || "".equals(text)) return "";
		if(!DesensitiveSimulationUtil.hasacdat) return text;
		List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = DesensitiveSimulationUtil.acdat.parseText(text.toLowerCase());
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
						h.target =  DesensitiveUtil.desensitiveNamesAnyway(lastWord.value);
					} else {
						h.target =  DesensitiveUtil.desensitiveNamesAnyway(lastWord.value);
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
				h.target = DesensitiveUtil.desensitiveNamesAnyway(lastWord.value);
			} else {
				h.target =  DesensitiveUtil.desensitiveNamesAnyway(lastWord.value);
			}
			res.add(h);
		}
		return DesensitiveSimulationUtil.processHIT(res,text);
	}
	
	public static void loadNames(List<String> names) {
		DesensitiveSimulationUtil.loadNames(names);
	}
	
	/**
	 * 将@某人 替换为假名
	 * @param text
	 * @return
	 */
	public static String desensitiveAt(String text) {
		return DesensitiveSimulationUtil.desensitiveAt(text);
	}
	
	/**
	 * 将手机号脱敏
	 * @param text
	 * @return
	 */
	public static String desensitivePhoneNum(String text) {
		if(text == null || "".equals(text)) return "";
        String pattern = "(\\D|\\b)([1])([3][0-9]|[4][5-9]|[5][0-3,5-9]|[6][5,6]|[7][0-8]|[8][0-9]|[9][1,8,9])([0-9]{4})([0-9]{4})(\\D|\\b)";
        text = text.replaceAll(pattern, "$1$2$3****$5$6");
        return text;
	}
	
	public static String desensitiveID(String text) {
		if(text == null || "".equals(text)) return "";
		String pattern = "(\\D|\\b)([1-9])(\\d{5})(\\d{8})(\\d{3}[0-9Xx])(\\D|\\b)";
		text = text.replaceAll(pattern, "$1$2$3********$5$6");
        return text;
	}
	
	public static String desensitiveEmail(String text) {
		if(text == null || "".equals(text)) return "";
		String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
		text = text.replaceAll(pattern, "***@***");
        return text;
	}
	
	public static String desensitivePassword(String text) {
		return DesensitiveSimulationUtil.desensitivePassword(text);
	}
	
	public static String desensitiveAnyway(String text) {
		return DesensitiveSimulationUtil.desensitiveAnyway(text);
	}

	public static String desensitiveNamesAnyway(String fetch) {
		if (fetch == null || "".equals(fetch))
			return "";

		if (!CheckNameUtil.checkBlank(fetch)) {
			StringBuilder sb = new StringBuilder(fetch);
			int lens = fetch.length();

			if (lens == 2)
				sb.setCharAt(1, '*');
			if (lens == 1)
				sb.append("*");
			else
				for (int i = 1; i < lens - 1; i++) {
					sb.setCharAt(i, '*');
				}
			fetch = sb.toString();
		} else {
			String[] strs = fetch.split(" ");
			StringBuilder buf = new StringBuilder();

			for (int i = 0; i < strs.length; i++) {
				StringBuilder sb = new StringBuilder(strs[i]);

				int lens = strs[i].length();
				if (lens == 2)
					sb.setCharAt(1, '*');
				if (lens == 1)
					sb.append("*");
				else
					for (int j = 1; j < lens - 1; j++) {
						sb.setCharAt(j, '*');
					}
				buf.append(sb + " ");
			}
			fetch = (buf.substring(0, buf.length() - 1)).toString();
		}

		return fetch;

	}
	
	/**
	 * 脱敏成假名拼音
	 * @param fetch
	 * @return
	 */
	public static String desensitiveNamesAnywayToPinyin(String fetch) {
		return DesensitiveSimulationUtil.desensitiveNamesAnywayToPinyin(fetch);
	}
	
	/**
	 * 脱敏成假名-名字和拼音
	 * @param fetch
	 * @return
	 */
	public static String []  desensitiveNamesAnywayToNameAndPinyin(String fetch) {
		return DesensitiveSimulationUtil.desensitiveNamesAnywayToNameAndPinyin(fetch);
	}
	
	public static String desensitiveAllInOne( String text ) {
		//依次脱敏
		text = DesensitiveUtil.desensitiveID(text);
		text = DesensitiveUtil.desensitivePhoneNum(text);
		text = DesensitiveUtil.desensitiveEmail(text);
		text = DesensitiveUtil.desensitiveNamesByDict(text);
		text = DesensitiveUtil.desensitiveIP(text);
		text = DesensitiveUtil.desensitiveAt(text);
		return text;
	}
	
	public static String desensitiveAllInOneWithOutAt( String text ) {
		//依次脱敏
		text = DesensitiveUtil.desensitiveID(text);
		text = DesensitiveUtil.desensitivePhoneNum(text);
		text = DesensitiveUtil.desensitiveEmail(text);
		text = DesensitiveUtil.desensitiveNamesByDict(text);
		text = DesensitiveUtil.desensitiveIP(text);
		return text;
	}
	
	/**
	 * IP脱敏，将符合IP格式的字符串变为***.***.***.***
	 * @param text
	 * @return
	 */
	public static String desensitiveIP(String text) {
		if (text == null || "".equals(text)) return "";
        //ipv4预览脱敏正则
		String pattern_ipv4 = "(?<=(\\b|\\D))((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?=(\\b|\\D))";
		//ipv6预览脱敏正则
		String pattern_ipv6 = "(?<=(\\b|\\D))(([0-9a-fA-F]{1,4})\\:){7}(([0-9a-fA-F]){1,4})|" +
				"(([0-9a-fA-F]{1,4}))?\\::(([0-9a-fA-F]{1,4})\\:){5}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,2})\\::(([0-9a-fA-F]{1,4})\\:){4}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,3})\\::(([0-9a-fA-F]{1,4})\\:){3}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,4})\\::(([0-9a-fA-F]{1,4})\\:){2}(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,5})\\::(([0-9a-fA-F]{1,4})\\:)(([0-9a-fA-F]{1,4}))|" +
				"(([0-9a-fA-F]{1,4}){0,6})\\::(([0-9a-fA-F]{1,4}))|" +
				"(([:]{2}))";
		//先将符合ipv4规则的字符串替换为***.***.***.***
		text = text.replaceAll(pattern_ipv4, "***.***.***.***");
		//最后将符合ipv6规则的字符串替换为****:****:****:****:****:****:****:****
		text = text.replaceAll(pattern_ipv6, "****:****:****:****:****:****:****:****");
		return text;
	}
	/**
	 * 座机号码匹配，支持国内国际格式
	 * @param text
	 * @return
	 */
	public static String desensitiveLandLine(String text) {
		if (text == null || "".equals(text)) return "";
        //匹配国内座机号
//		String pattern = "(?<=(\\b||\\D))(\\(?0[0-9]{2,3}\\)?-?)?([0-9][0-9])[0-9]{3,4}([0-9]{2})(?=(\\b|\\D))";

		//匹配国内、国际（美国、日本、欧洲）座机号
		String pattern = "(?<=(\\b||\\D))(\\(?[0-9]\\)?[0-9]{1,3}\\)?-?)?([0-9][0-9])[0-9]{3,4}([0-9]{2})(?=(\\b|\\D))";
		text = text.replaceAll(pattern, "$2$3****$4");
		return text;
	}

	public static String desensitiveBankCard(String text) {
        if (text == null || "".equals(text.trim())) {
            return "";
        }
        String pattern = "([\b|\\D](\\d{19}|\\d{16})[\b|\\D])|([\b|\\D](\\d{19}|\\d{16})$)|(^(\\d{19}|\\d{16})[\b|\\D])|(^(\\d{19}|\\d{16})$)";
        Pattern compiler = Pattern.compile(pattern);
        Matcher matcher = compiler.matcher(text);
        String textCopy = text;
        while (matcher.find()) {
//            String  originKeyWord = matcher.group(1);
            String originKeyWord = matcher.group();
            String cleanKeyWord = originKeyWord.replaceAll("^\\D", "")
                    .replaceAll("\\D$", "");
            if (DesensitiveSimulationUtil.checkLuhn(cleanKeyWord)) {
                int from  = matcher.start();
                int to = matcher.end();
                if (Objects.equals(originKeyWord.length() - 2, cleanKeyWord.length())) {
                    from = from + 1;
                    to = to - 1;
                } else if (Objects.equals(originKeyWord.length() - 1, cleanKeyWord.length())) {
                    if (originKeyWord.indexOf(cleanKeyWord) == 0) {
                        to = to - 1;
                    } else if (originKeyWord.indexOf(cleanKeyWord) == 1) {
                        from = from + 1;
                    }
                }
                if (cleanKeyWord.length() == 16) {
                    cleanKeyWord = cleanKeyWord.substring(0, 6) + "******" + cleanKeyWord.substring(12);
                } else if (cleanKeyWord.length() == 19) {
                    cleanKeyWord = cleanKeyWord.substring(0, 6) + "*********" + cleanKeyWord.substring(15);
                }
                textCopy = textCopy.substring(0, from) + cleanKeyWord + textCopy.substring(to);
            }
        }
        return textCopy;
    }
}
