package com.cec.examine.template.recognition.functions;

import java.util.List;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckCollege.java
 * @Description 职位检查
 * @createTime 2022/12/19
 */
public class CheckPosition {
    public static boolean isPosition(String str){
        String regex = "销售总监|销售经理|区域销售经理|销售主管|销售工程师|销售代表|" +
                "销售助理|医药销售代表|电话销售|" +
                "渠道管理|分销管理|渠道专员|分销专员|经销商|" +
                "客户经理|客户主管|客户代表";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.contains(rege)){
                return true;
            }
        }
        return false;

    }

    public static boolean checkPosition(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(isPosition(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
