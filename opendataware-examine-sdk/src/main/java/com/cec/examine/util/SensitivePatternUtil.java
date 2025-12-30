package com.cec.examine.util;

import com.cec.examine.comm.OpType;
import com.cec.examine.comm.SymbolOperator;
import com.cec.examine.desensitization.SensitivePattern;
import com.cec.examine.util.pinyin.PinyinFormat;
import com.cec.examine.util.pinyin.PinyinHelper;
import com.cec.examine.template.Config;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensitivePatternUtil {

    /**
     * 填充
     *
     * @param size
     * @return
     */
    public static String padding(int size, char tag) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(tag);
        }
        return sb.toString();
    }


    public static String maskWithConfig(String source, Config config) {

        return mask(source,
                config.getStart(),
                config.getEnd(),
                // 替换符不能为空，且不能是多个
                config.getPlaceHolder().charAt(0),
                config.getSymbol(),
                config.getSymbotOp(),
                config.getOrder(),
                config.getOp());

    }

    public static String mask(String source, int start, int end, char placeholder, String symbol, SymbolOperator operator, int order, OpType op) {

        if (Objects.equals(op, OpType.OR)) {
            int from = 0;
            int to = start;

            source = mask(source, from, to, placeholder, symbol, operator, order, OpType.AND);

            int length = source.length();

            from = length - Math.abs(end);
            to = 100000;

            source = mask(source, from, to, placeholder, symbol, operator, order, OpType.AND);

            return source;

        } else {

            if (StringUtil.notBlank(symbol)) {

                if (Objects.equals(operator, SymbolOperator.SKIP)) {

                    return maskWithSkipSymbol(source, start, end, placeholder, symbol.charAt(0));

                } else if (Objects.equals(operator, SymbolOperator.SPLIT)) {

                    return maskWithSplitor(source, start, end, placeholder, symbol, order);

                }
            }
        }

        return maskNormal(source, start, end, placeholder);
    }


    protected static String maskWithSplitor(String source, int start, int end, char placeholder, String symbol, int order) {

        String wrapSymbol = symbol;

        if (Objects.equals(".", symbol)) {

            wrapSymbol = "\\.";
        }
        if (Objects.equals("^", symbol)) {
            wrapSymbol = "\\^";
        }
        if (Objects.equals("$", symbol)) {
            wrapSymbol = "\\$";
        }
        if (Objects.equals("*", symbol)) {
            wrapSymbol = wrapSymbol;
        }

        String[] split = source.split(wrapSymbol, -1);

        int len = split.length;

        if (len > order) {

            String maskPart = split[order];

            String postMask = maskNormal(maskPart, start, end, placeholder);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < len; i++) {

                if (Objects.equals(order, i)) {

                    sb.append(postMask);

                } else {

                    sb.append(split[i]);
                }

                if (i < len - 1) {

                    sb.append(symbol);

                }
            }

            return sb.toString();

        }

        // 如果指定的脱敏的位置没有对应的config，那么返回原始数据，没办法脱敏
        return source;
    }

    /**
     * 跳过分隔符的遮蔽，默认symbolt 不为空
     * 如果end大于最大长度，这个情况可以无视
     *
     * @param source
     * @param start
     * @param end
     * @param placeholder
     * @param symbol
     * @return
     */
    protected static String maskWithSkipSymbol(String source, int start, int end, char placeholder, char symbol) {

        int len = source.length();

        if (Math.abs(end) > len) {
            end = len;
        }

        if (end < 0) {
            end = len + end;
        }
        // 在这之后，end必须为正

        if (start >= end) {
            end = len;
        }

        if (start >= end || start < 0) {

            return source;
        }

        char[] chars = source.toCharArray();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {

            char ele = chars[i];

            if (i >= start && i < end) {

                if (Objects.equals(ele, symbol)) {

                    sb.append(ele);

                } else {

                    sb.append(placeholder);

                }

            } else {

                sb.append(ele);
            }

        }
        return sb.toString();
    }

    public static String maskNormal(String source, int start, int end, char placeholder) {

        int len = source.length();

        if (Objects.equals(start, end)) {
            return source;
        }

        if (start > len - 1) {
            return source;
        }

        // 用户传来的是负数 -1 代表最后一个
        if (end < 0) {

            end = len + end;

            // 如果负值的end 超过了总长度，放弃指定的end，使用长度作为end，也就是不保留后一部分
            if (end < 0 || end <= start) {
                end = len;
            }

        } else {

            end = end > len - 1 ? len : end;
        }

        char[] chars = source.toCharArray();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {

            if (i >= start && i < end) {
                sb.append(placeholder);
            } else {
                sb.append(chars[i]);
            }
        }

        return sb.toString();

    }

    /**
     * 硬遮蔽
     *
     * @param source
     * @param maskStart
     * @param maskLength
     * @param maskTag
     * @return
     */
    public static String mask(String source, int maskStart, int maskLength, char maskTag) {
        if(source == null) return null;
        StringBuilder sourceB = new StringBuilder(source);
        for (int i = maskStart; i < source.length() && i < maskStart + maskLength; i++) {
            sourceB.setCharAt(i, maskTag);
        }
        return sourceB.toString();
    }

    /**
     * 0是第一个，-1是最后一个 -2 是倒数第二个，-3是倒数第三个
     *
     * @param source
     * @param start
     * @param end
     * @param palceholder
     * @return
     */
    public static String maskUntil(String source, int start, int end, char palceholder) {
        // v  i  n  c  e  n  t
        // 0  1  2  3  4  5  6
        //-7 -6 -5 -4 -3 -2 -1
        int len = source.length();

//        1 -2
        // 如果 倒数的数已经超过的总长度，那么直接返回
        if (Math.abs(end) > len) {
            return source;
        }
        if (start > len) {
            return source;
        }
        if (Objects.equals(len, 0)) {
            return source;
        }
        return maskNormal(source, start, len + end + 1, palceholder);
    }

    /**
     * string1 s2 -> s*****1 s*
     *
     * @param source
     * @param limit  最长遮蔽limit个tag
     * @return
     */
    public static String maskMiddle(String source, char tag, int limit) {
        String[] strs = source.split(" ", -1);
        StringBuilder target = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            StringBuilder sb = new StringBuilder(strs[i]);
            int lens = strs[i].length();
            if (lens == 2)
                sb.setCharAt(1, tag);
            if (lens == 1)
                sb.append(tag);
            else {
                int counter = 0;
                for (int j = 1; j < lens - 1; j++) {
                    if (counter >= limit) {
                        sb.setCharAt(j, '\0');
                    } else {
                        sb.setCharAt(j, tag);
                        counter++;
                    }
                }
            }
            target.append(sb + " ");
        }
        source = (target.substring(0, target.length() - 1)).toString();
        return source;
    }

    /**
     * 等长数字字符串混淆
     *
     * @param numStr
     * @return
     */
    public static String getEqLenNumStr(String numStr) {
        Integer temp = Math.abs(numStr.hashCode());
        String rep = String.valueOf(temp);
        if (rep.length() > numStr.length())
            rep = rep.substring(0, numStr.length());
        if (rep.length() < numStr.length())
            rep = padding(numStr.length() - rep.length(), '0') + rep;
        return rep;
    }

    /**
     * 银行卡ISO 2894中支付卡校验位的算法 The Luhn Mod-10 Method
     *
     * @param cardNo
     * @return
     */
    public static boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNo.charAt(i) - '0';
            if (isSecond == true)
                d = d * 2;
            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    /**
     * 通过不带校验位的号码生成校验位
     *
     * @param cardNo
     * @return
     */
    public static int getLuhnCode(String cardNo) {
        int nSum = 0;
        for (int i = 0; i < cardNo.length(); i++) {
            int d = cardNo.charAt(i) - '0';
            nSum += i % 2 == 0 ? 2 * d : d;
        }
        return nSum / 10 % 5;
    }

    //调用PinyinHelper类里汉字转拼音方法，实现将names字典里维护的汉字转为拼音
    public static String HanToPinyin(String str) {
        return PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITHOUT_TONE);
    }

    /**
     * 判断是否是汉字名字
     *
     * @param name
     * @return
     */
    public static boolean isHanName(String name) {
        boolean isHan = false;
        if (name.length() >= 2 && name.length() <= 5) {
            isHan = true;
            for (int i = 0; i < name.length(); i++) {
                if (Character.UnicodeScript.of(name.charAt(i)) != Character.UnicodeScript.HAN) {
                    isHan = false;
                    break;
                }
            }
        }
        return isHan;
    }

    /**
     * 判断英文人名，长度2-30
     *
     * @param name
     * @return
     */
    public static boolean isEnglishName(String name) {
        String pattern = "^[a-zA-Z\\.\\s]{2,30}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(name);
        return m.find();
    }

    /**
     * 纯粹英文
     *
     * @param name
     * @return
     */
    public static boolean isSingleEnglishName(String name) {
        String pattern = "^[a-zA-Z]{2,30}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(name);
        return m.find();
    }

    /**
     * 多个脱敏函数作用
     *
     * @param text
     * @param func
     * @return
     */
    public static String processMultiPattern(String text, List<SensitivePattern> func) {
        for (SensitivePattern sp : func) {
        }
        return text;
    }

    /**
     * 判断字符串是否为null 或者空串或者空格串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        boolean flag = false;

        if (str == null || str.trim().length() == 0) {
            flag = true;
        }
        return flag;
    }


    /**
     *根据输入数字长度随机生成相同长度的数字
     **/
    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        // 生成随机数
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 生成随机数字（0-9）并添加到字符串中
            int randomNumber = random.nextInt(10);
            stringBuilder.append(randomNumber);
        }

        return stringBuilder.toString();
    }

}
