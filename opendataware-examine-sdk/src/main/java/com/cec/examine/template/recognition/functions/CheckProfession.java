package com.cec.examine.template.recognition.functions;

import java.util.List;

/**
 * @author ZGH
 * @version 1.0.0
 * @ClassName CheckCollege.java
 * @Description 职业检查
 * @createTime 2022/12/19
 */
public class CheckProfession {
    public static boolean isProfession(String str){
        String regex = "农、林、牧、渔、水利业|工业|地质普查和勘探业|建筑业|交通运输业|邮电通信业|商业、公共饮食业、物资供应和仓储业|房地产管理|公用事业|居民服务和咨询服务业" +
                "卫生、体育和社会福利事业|教育、文化艺术和广播电视业|科学研究和综合技术服务业|金融业|保险业|国家机关、党政机关和社会团体";
        String[] regexs = regex.split("\\|");
        for(String rege:regexs){
            if(str.contains(rege)){
                return true;
            }
        }
        return false;

    }

    public static void main(String[] args) {
        String str = "金融业";
        System.out.println(isProfession("科技大学"));
        System.out.println(isProfession(str));
    }

    public static boolean checkProfession(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(isProfession(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
