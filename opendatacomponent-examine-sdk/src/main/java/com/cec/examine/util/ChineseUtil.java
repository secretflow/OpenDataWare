package com.cec.examine.util;

public class ChineseUtil {

    public static boolean isChineseCharacter(char c) {
        int unicode = (int)c;
        return (unicode >= 19968 && unicode <= 40959);
    }

    public static boolean containChineseCharacter(String s) {
        for(int i = 0; i<s.length(); i++) {
            if(isChineseCharacter(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
