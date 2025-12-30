package com.cec.examine.watermark;

import com.cec.examine.util.FileUtilityUtil;
import com.cec.examine.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 组态噪音算法，模态不包含
 * 浮点数噪音：通过尾数存储信息
 * 通过与浮点数进行运算，将奇偶值进行修改写入尾数；
 */
public class WaterMarkLSB extends WaterMark {

    public WaterMarkLSB() {

    }

    /**
     *
     * @param source 原始批量CSV数据
     * @param separator 分隔符
     * @param waterMark 水印
     * @param indexOfText 需要考虑加水印的索引范围
     * @param percent
     * @return
     */
    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Set<Integer> indexOfText, Integer percent) {
        List<String> result =  new ArrayList<String>();
        double startTime = System.currentTimeMillis();
        int cnt = 0;
        for(String line : source) {
            //随机挑选数据并植入水印
            StringBuilder newLine = new StringBuilder();//指纹串
            //String[] cells = line.split(separator, -1);
            String [] cells = FileUtilityUtil.split(line, separator);
            for (int i = 0; i < cells.length; i++) {
                if(i>0) newLine.append(separator);
                if (indexOfText !=null && !indexOfText.contains(i)) {
                    //排除掉的列不考虑
                    continue;
                }
                //如果列存在浮点数
                if(StringUtil.isNumerical(cells[i])) {
                    String tmp = cells[i].substring(0,cells[i].length()-1);
                    String newCell = cells[i];//默认不变
                    int checksum = (waterMark.hashCode() & tmp.hashCode()) % 10;
                    if (cnt % 100 >= 100 - percent) {
                        //挑选一定百分比设置正确的校验和
                        newCell = tmp + checksum;
                    } else {
                        //其余设置为错误的校验和
                        if(Integer.parseInt(cells[i].substring(cells[i].length()-1, cells[i].length())) == checksum) {
                            //System.out.println(cells[i] + "->" +  newCell);
                            newCell = tmp + (checksum + 1) % 10;
                            //System.out.println(cells[i] + "->" +  newCell);
                        }
                    }
                    //System.out.println(cells[i] + "->" + newCell);
                    newLine.append(newCell);
                } else {
                    newLine.append(cells[i]);
                }
            }
            cnt++;
            result.add(newLine.toString());
        }
        double endTime = System.currentTimeMillis();
        return result;
    }

    @Override
    public List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Integer percent) {
        return addBatchWaterMark(source,separator,waterMark,null,percent);
    }

    @Override
    public boolean valid(String waterMark, Integer percent, List<String> result, String separator, Set<Integer> indexOfText) {
        int right = 0;
        for(String line : result) {
            boolean has = false;
            //String[] cells = line.split(separator, -1);
            String [] cells = FileUtilityUtil.split(line, separator);
            for (int i = 0; i < cells.length; i++) {
                if (indexOfText !=null && !indexOfText.contains(i)) {
                    //排除掉的列不考虑
                    continue;
                }
                //如果列存在浮点数
                if(StringUtil.isNumerical(cells[i])) {
                    String tmp = cells[i].substring(0,cells[i].length()-1);
                    String newCell = tmp + ((waterMark.hashCode() & tmp.hashCode()) % 10);
                    if(cells[i].equals(newCell)) {
                        has = true;
                        break;
                    }
                }
            }
            if(has) right++;
        }
        //System.out.println(right * 100 + "," + result.size() + "," + right * 100 / result.size() + "," + percent);
        if(right * 100 / result.size() >= percent - 1) return true;
        else  return  false;
    }
}
