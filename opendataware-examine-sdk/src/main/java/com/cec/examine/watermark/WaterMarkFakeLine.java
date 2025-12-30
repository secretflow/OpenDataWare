package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WaterMarkFakeLine extends WaterMark {

    private boolean newInsert;//true 伪行是否是新插入 false 伪行是覆盖原始数据
    private int minLineNum = 0;//最低的限制行数
    public  WaterMarkFakeLine(boolean newInsert) {

        this.newInsert = newInsert;
    }
    public  WaterMarkFakeLine(boolean newInsert, int minLineNum) {
        this.minLineNum = minLineNum;
        this.newInsert = newInsert;
    }
    /**
     *
     * @param source 原始批量CSV数据
     * @param separator 分隔符
     * @param waterMark 水印
     * @param indexOfText 需要加水印的索引 为空则代表考虑全列
     * @param percent
     * @return
     */
    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Set<Integer> indexOfText, Integer percent) {
        List<String> result =  new ArrayList<String>();
        if(source == null) return result;
        if(source.size() < this.minLineNum) return source;//如果批次小于设定的最小门槛，那么不加水印，直接返回；
        String ZeroWith = WaterMarkZeroWith.strToZeroWidth(waterMark);
        double startTime = System.currentTimeMillis();
        int count = 0;
        for(String line : source) {
            if( (count + 1) % ( Math.floor (100D /Double.valueOf(percent))) == 0 ) {
                //随机挑选数据并植入水印
                //String [] cells = line.split(separator, -1);
                String [] cells = FileUtilityUtil.split(line, separator);
                int HMACkeyNumPos = 0;//记录水印密钥的当前位置
                StringBuilder fakeNumStrSb = new StringBuilder();//指纹串

                for (int i = 0; i < cells.length; i++) {
                    //单列处理
                    if(i>0) fakeNumStrSb.append(separator);
                    if (indexOfText !=null && !indexOfText.contains(i)) {
                        //排除掉的列不考虑
                        fakeNumStrSb.append(cells[i]);
                        continue;
                    }
                    //伪行中任何列都加水印信息
                    cells[i] = ZeroWith + '\uFEFF' + cells[i];

                    //将所有数字替换为指纹
                    for(int j = 0; j<cells[i].length() ; j++) {
                        if(HMACkeyNumPos < waterMark.length() && (int)cells[i].charAt(j) >=48 && (int)cells[i].charAt(j) <=57) {
                            //说明是数字字符
                            fakeNumStrSb.append(waterMark.charAt(HMACkeyNumPos++));
                        } else {
                            fakeNumStrSb.append(cells[i].charAt(j));
                        }
                    }
                }
                result.add((fakeNumStrSb.toString()));
                if(newInsert)
                    result.add((line));
            } else
                //写文件
                result.add((line));
            count++;
        }
        return result;
    }

    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Integer percent) {
        return addBatchWaterMark(source, separator, waterMark,  null, percent);
    }



    @Override
    public boolean valid(String waterMark, Integer percent, List<String> result, String separator, Set<Integer> indexOfText) {
        return waterMark.equals(WaterMarkUtils.extractBatchWaterMark(result, separator, indexOfText));
    }
}
