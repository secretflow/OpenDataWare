package com.cec.examine.util.name;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断名字工具类
 */
public class CheckNameUtil {


    /**
     * 判读名字中是否含有汉字
     * @param name
     * @return
     */
    public static boolean isContainChinese(String name) {
        boolean flag = false;

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(name);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断单个字符是否是汉字
     * @param character
     * @return
     */
    public static boolean charIsChinese(Character character) {
        boolean flag = false;
        if (Character.UnicodeScript.of(character) == Character.UnicodeScript.HAN) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断字符串中是否含有空格
     * @param name
     * @return
     */
    public static boolean checkBlank(String name) {
        boolean flag = false;

        Pattern p =  Pattern.compile("[\\s]+");
        Matcher m = p.matcher(name);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }


}
