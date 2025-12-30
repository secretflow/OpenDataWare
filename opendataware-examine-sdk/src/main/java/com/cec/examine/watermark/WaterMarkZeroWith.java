package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;
import com.cec.examine.util.StringUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *零宽字符
 **/
public class WaterMarkZeroWith extends WaterMark {
    public WaterMarkZeroWith() {}
    public WaterMarkZeroWith(int cellLengthThreshold) {
        this.cellLengthThreshold = cellLengthThreshold;
    }
    public int getCellLengthThreshold() {
        return cellLengthThreshold;
    }
    protected int cellLengthThreshold = 20;//单元格添加水印默认最短长度，低于这个值就不加水印信息
    /**
     * 编码前最大的接收长度
     */
    private int waterMarkDataMaxLength = 10;

    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Set<Integer> indexOfText, Integer percent) {
        String ZeroWith = WaterMarkZeroWith.strToZeroWidth(waterMark);
        List<String> result =  new ArrayList<String>();
        int count = 0;
        for(String line: source) {
            //String [] cells = line.split(separator,-1);
            String [] cells = FileUtilityUtil.split(line, separator);
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i<cells.length; i++) {
                String cell = cells[i];
                if(((count + 1) % ( Math.floor (100D /Double.valueOf(percent))) == 0)
                        && cell.length() >= this.cellLengthThreshold ) {
                    cell = ZeroWith + "\uFEFF" + cells[i];
                }
                if(i != 0) stringBuffer.append(separator);
                stringBuffer.append(cell);
            }
            result.add(stringBuffer.toString());
            count++;
        }
        return result;
    }

    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Integer percent) {
        return addBatchWaterMark(source, separator, waterMark, getIndexOfText(source, separator) ,percent);
    }
    /**
     * 判断列内容是否是文本
     * @param str
     * @return
     */
    public static boolean isText(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5\\s]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 采样获取文本列
     * @param source
     * @param separator
     * @return 文本列索引
     */
    public static Set<Integer> getIndexOfText(List<String> source, String separator) {
        HashMap<Integer, Integer> indexOfTextCounter = new HashMap<Integer, Integer>();//index -> text feq
        Integer sampleNum = 10;
        for(int j = 1; j < sampleNum && j < source.size(); j++) {
            String line = source.get(j);
            //String [] cells = line.split(separator, -1);
            String [] cells = FileUtilityUtil.split(line, separator);
            for (int i = 0; i<cells.length; i++) {
                if(isText(cells[i])) {
                    //计数
                    Integer cnt = indexOfTextCounter.get(i);
                    if(cnt == null) indexOfTextCounter.put(i,1);
                    else indexOfTextCounter.put(i,++cnt);
                }
            }
        }
        //去掉小于50%的样本的索引
        Set<Integer> indexOfText = new HashSet<Integer>();
        for(Integer key: indexOfTextCounter.keySet()) {
            if( indexOfTextCounter.get(key) >= sampleNum/2 ) indexOfText.add(key);
        }
        return indexOfText;
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

    @Override
    public boolean valid(String waterMark, Integer percent, List<String> result, String separator, Set<Integer> indexOfText) {
        return waterMark.equals(WaterMarkUtils.extractBatchWaterMark(result, separator, indexOfText));
    }

}
