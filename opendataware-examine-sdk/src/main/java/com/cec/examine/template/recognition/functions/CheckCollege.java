package com.cec.examine.template.recognition.functions;

import java.util.List;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckCollege.java
 * @Description 院系检查
 * @createTime 2022/12/19
 */
public class CheckCollege {
    public static boolean isCollege(String str){
        String regex = "学院|系";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.endsWith(rege)){
                return true;
            }
        }
        return false;

    }

    public static boolean checkCollege(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(isCollege(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String str = "自动化学院";
        System.out.println(isCollege("自动化"));
        System.out.println(isCollege(str));
    }
}
