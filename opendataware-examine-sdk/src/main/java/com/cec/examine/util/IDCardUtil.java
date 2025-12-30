package com.cec.examine.util;

import java.util.Calendar;

/**
 * 提取身份证中的年龄、性别、身份、城市信息
 */
public class IDCardUtil {

    /**
     * 解析年龄
     * @param ID
     * @return
     */
    public static Integer getAge(String ID) {
        String YEAR = ID.substring(6, 10);
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        return curYear - Integer.parseInt(YEAR);
    }

    /**
     * 解析性别
     * @param ID
     * @return
     */
    public static String getGender(String ID) {
        String S15 = ID.substring(14, 15);
        Integer I15 = Integer.parseInt(S15);
        if(I15 % 2 == 0) return "F";
        else return "M";
    }
}
