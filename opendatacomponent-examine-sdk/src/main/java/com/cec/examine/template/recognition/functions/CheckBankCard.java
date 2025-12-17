package com.cec.examine.template.recognition.functions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckBankCard {
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(!checCard(bankCard)){
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    public static boolean checCard(String card) {
        String regex = "([4-6])(\\d{15}|\\d{16}|\\d{18})";
        Pattern pattern = Pattern.compile(regex);
        card = card.replaceAll("-", "");
        card = card.replaceAll(" ","");
        Matcher matcher = pattern.matcher(card);
        return matcher.matches();
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0 || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean checkBankCard(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(checkBankCard(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
