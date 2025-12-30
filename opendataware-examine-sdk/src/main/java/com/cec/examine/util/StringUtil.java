package com.cec.examine.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


    public static boolean isBlank(String source) {

        return Objects.isNull(source)  || source.isEmpty();

    }

    public static boolean notBlank(String source) {

        return !isBlank(source);

    }

    public static String join(Iterable<?> iterable, String separator) {
        return iterable == null ? null : join((Iterable<?>) iterable.iterator(), separator);
    }

    public static boolean isNumerical(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[-+]?\\d*\\.?\\d+");
        return pattern.matcher(str).matches();
    }

    //存在数字就行
    public static boolean hasNumerical(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile(".*\\d+.*");
        return pattern.matcher(str).matches();
    }

    public static boolean isText(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
