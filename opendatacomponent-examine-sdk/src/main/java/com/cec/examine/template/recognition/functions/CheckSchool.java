package com.cec.examine.template.recognition.functions;

import java.util.List;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckSchool.java
 * @Description 检查学校
 * @createTime 2022/12/19
 */
public class CheckSchool {
    public static boolean isSchool(String str){
        String regex = "大学|中学|小学|专科|学校|学院|University";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.contains(rege)){
                return true;
            }
        }
        return false;

    }

    public static void main(String[] args) {
        String str = "北京大学";
        System.out.println(isSchool("金融学院"));
        System.out.println(isSchool("金融"));
        System.out.println(isSchool(str));
    }

    public static boolean checkSchool(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(isSchool(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
