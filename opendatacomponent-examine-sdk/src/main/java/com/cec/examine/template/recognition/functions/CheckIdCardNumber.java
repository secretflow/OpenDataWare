package com.cec.examine.template.recognition.functions;

import java.util.List;

public class CheckIdCardNumber {
    /**
     * 验证身份证真假
     * @param idCardNumber 身份证号
     * @return boolean
     */
    public static boolean checkIdCardNumber(String idCardNumber) {
        //判断输入身份证号长度是否合法
        if (idCardNumber == null || idCardNumber.length() != 18) {
            return false;
        }

        //String regex = "[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(0[1-9]|([1|2]\\d)|3[0-1])((\\d{4})|\\d{3}X)";
        //Pattern pattern = Pattern.compile(regex);
        //Matcher matcher = pattern.matcher(idCardNumber);
        //return matcher.matches();
        //校验身份证真假
        int sum = 0;
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//将加权因子定义为数组
        //遍历weight数组 求身份证前17项系数和
        for (int i = 0; i < weight.length; i++) {
            int n = idCardNumber.charAt(i) - 48;//获取 身份证对应数
            int w = weight[i];
            sum += w * n;
        }
        //对11求余
        int index = sum % 11;
        //校验码
        String m = "10X98765432";
        idCardNumber = idCardNumber.toUpperCase();
        //获取身份证最后一位进行比对
        return m.charAt(index) == idCardNumber.charAt(17);
    }

    public static boolean checkIdCardNumber(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(checkIdCardNumber(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
