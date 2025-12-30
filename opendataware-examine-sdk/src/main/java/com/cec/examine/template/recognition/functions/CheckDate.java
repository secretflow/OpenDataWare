package com.cec.examine.template.recognition.functions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDate {
    /**
     * 校验日期
     */
    public static boolean checkDate(String str) {
        String regex = "(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0[1-9]|1[0-2])([-/.]?)(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])([-/.]?)(?:29|30)|(?:0[13578]|1[02])([-/.]?)31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)02([-/.]?)29)([\\s]?)+(([01][0-9]|2[0-3])?)([:]?)(([0-5][0-9])?)([:]?)(([0-5][0-9])?)([.]?)(([0-9]){0,3})";
        return replaceCharacter(str, regex);
    }

    private static boolean replaceCharacter(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        str = str.replace("年","");
        str = str.replace("月","");
        str = str.replace("日","");
        str = str.replace("时","");
        str = str.replace("分","");
        str = str.replace("秒","");

        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean checkDate(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(checkDate(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
