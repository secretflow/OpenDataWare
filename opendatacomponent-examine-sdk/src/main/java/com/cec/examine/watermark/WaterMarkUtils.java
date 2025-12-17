package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;
import com.cec.examine.util.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/*
业务数据JSON->sha-2/md5->取最长10个字符转10进制->零宽
 */
public class WaterMarkUtils {

    /**
     * sha-2/md5->取最长10个字符转10进制
     * @param key
     * @return
     */
    public static String encodeWaterMark(String key) {
        int maxLen = 10;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<key.length() && i < maxLen ;i++) {
            String formattedNum = String.format("%03d", (int)key.charAt(i));
            sb.append(formattedNum);
        }
        return sb.toString();
    }

    /**
     * 取最长10个字符转10进制->sha-2/md5
     * @param waterMark
     * @return
     */
    public static String decodeWaterMark(String waterMark) {
        //伪行水印的样式必须是数字的，否则就不属于伪行类型的水印
        if(StringUtil.isNumerical(waterMark)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i + 3 < waterMark.length(); i += 3) {
                String asciiCode = waterMark.substring(i, i + 3);
                sb.append((char) Integer.parseInt(asciiCode));
            }
            return sb.toString();
        } else return null;
    }

    // String -> 零宽字符
    public static String strToZeroWidth(String str){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            String s = Integer.toBinaryString(str.charAt(i));
            for(int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if('1' == c) {
                    sb.append("\u200b");
                } else if ( '0' == c ) {
                    sb.append("\u200c");
                } else {
                    sb.append("\u200d");
                }
            }// for
            sb.append("\u200e");//分隔符
        }// for
        return sb.toString();
    }

    // 零宽字符 -> String
    public static String zeroWidthToStr(String str){
        StringBuilder sb = new StringBuilder();
        str = str.replaceAll("[^\u200b-\u200f\uFEFF\u202a-\u202e]", "");
        String [] ss = str.split("\u200e",-1);
        for(String item:ss) {
            if(item.length() == 0) continue;
            StringBuilder BinaryItem = new StringBuilder();
            for(int i = 0; i < item.length(); i++) {
                char c = item.charAt(i);
                if('\u200b' == c) {
                    BinaryItem.append("1");
                } else if ( '\u200c' == c ) {
                    BinaryItem.append("0");
                } else {
                    BinaryItem.append("");
                }
            }
            String C = String.valueOf((char) Integer.parseInt(BinaryItem.toString(), 2));
            sb.append(C);
        }
        return sb.toString();
    }

    public static String extractBatchWaterMark(List<String> result, String separator, Set<Integer> indexOfText) {
        String waterMark = null;
        for(int i = 0; waterMark == null && i < result.size();i++) {
            String line = result.get(i);
            //String waterMark2 = line.replaceAll("\\D*", "");
            //String [] cells = line.split(separator,-1);
            String [] cells = FileUtilityUtil.split(line, separator);
            for(String cell: cells) {
                int end = cell.indexOf("\uFEFF");
                if(end > 0) {
                    waterMark = cell.substring(0, end);
                }
            }
        }
        if(!Objects.isNull(waterMark)){
            waterMark = WaterMarkZeroWith.zeroWidthToStr(waterMark);
            waterMark = decodeWaterMark(waterMark);
        }
        return  waterMark;
    }
}
