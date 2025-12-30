package com.cec.examine.template.recognition.functions;

import java.util.List;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckCollege.java
 * @Description 专业检查
 * @createTime 2022/12/19
 */
public class CheckMajor {
    public static boolean isMajor(String str){
        String regex = "专业|物理|化学|自动化|电子|生物|人工智能";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.contains(rege)){
                return true;
            }
        }
        return false;

    }

    public static void main(String[] args) {
        String str = "自动化专业";
        System.out.println(isMajor(str));
    }

    public static boolean checkMajor(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(isMajor(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
