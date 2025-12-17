package com.cec.examine.watermark;

import java.util.List;
import java.util.Set;

public abstract class WaterMark {


    /**
     * 批量加水印
     * @param source 原始批量CSV数据
     * @param separator 分隔符
     * @param waterMark 水印
     * @param indexOfText 需要加水印的索引
     * @return 加好水印的结果
     */
    public abstract List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Set<Integer> indexOfText, Integer percent);

    public abstract List<String> addBatchWaterMark(List<String> source, String separator, String waterMark, Integer percent);

    public abstract boolean valid(String waterMark, Integer percent, List<String> result, String separator, Set<Integer> indexOfText);
}
