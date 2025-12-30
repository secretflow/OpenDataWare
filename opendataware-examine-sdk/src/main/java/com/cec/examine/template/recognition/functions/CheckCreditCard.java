package com.cec.examine.template.recognition.functions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckCreditCard {

    public static boolean checkCreditCard(String card) {
        String regex = "(?<visa>4\\d{12}(?:\\d{3})?)|(?<mastercard>5[1-5]\\d{14})|(?<discover>6(?:011|5\\d{2})\\d{12})|(?<" +
                "amex>3[47]\\d{13})|(?<diners>3(?:0[0-5]|[68]\\d)?\\d{11})|(?<jcb>(?:2131|1800|35\\d{3})\\d{11})|(?<unionpay>62\\d{14})";
        Pattern pattern = Pattern.compile(regex);
        card = card.replaceAll("-", "");
        Matcher matcher = pattern.matcher(card);
        if (matcher.matches()) {
            //If card is valid then verify which group it belong
           return check(card);
        }
        return false;
    }

    public static boolean check(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static boolean checkCreditCard(List<String> content, long thresholdCount) {
        int i = 0;
        for (String str : content) {
            if(checkCreditCard(str)){
                i++;
            }
            if(i>=thresholdCount){
                return true;
            }
        }
        return false;
    }
}
