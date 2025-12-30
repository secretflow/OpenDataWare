package com.cec.examine.template.recognition.functions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckAge {
    /**
     * 校验年龄
     */
    public static boolean checkAge(String str) {

        String regex = "[1-9]\\d?|1[01]\\d|120";
        str = str.replace("岁","");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean checkByFields(String colName, String comment){
        String regex = "age|nl|年龄";
        colName = colName.toLowerCase();
        comment = comment.toLowerCase();
        return regex.contains(colName) || regex.contains(comment);
    }


    public static void main(String[] args) {
        String str = "121岁";
        System.out.println(checkAge(str));
    }

    public static boolean checkAge(String colName, String comment, List<String> content, long thresholdCount) {
        if(checkByFields(colName,comment)){
            int i = 0;
            for (String str : content) {
                if(checkAge(str)){
                    i++;
                }
                if(i>=thresholdCount){
                    return true;
                }
            }
        }

        return false;
    }
}
