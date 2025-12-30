package com.cec.examine.template.recognition.functions;

import java.util.List;

public class CheckGender {
    /**
     * 校验性别
     */
    public static boolean checkGender(String str) {

        String regex = "M|F|f|m|0|1|M性|F性|male|female";
        str = str.toLowerCase();
        return regex.contains(str);
    }

    public static boolean checkByFields(String colName, String comment){
        String regex = "sex|gender|xb|xingbie|性别";
        colName = colName.toLowerCase();
        comment = comment.toLowerCase();
        return regex.contains(colName) || regex.contains(comment);
    }

    public static boolean checkGender(String colName, String comment, List<String> content, long thresholdCount) {
        if(checkByFields(colName,comment)){
            int i = 0;
            for (String str : content) {
                if(checkGender(str)){
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
