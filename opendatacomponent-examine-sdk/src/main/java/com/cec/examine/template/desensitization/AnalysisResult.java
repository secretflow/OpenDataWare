package com.cec.examine.template.desensitization;

/**
 * 分析结果的抽象类
 */
public abstract class AnalysisResult {

    public double getEmptyValuePercent() {
        return emptyValuePercent;
    }

    public void setEmptyValuePercent(double emptyValuePercent) {
        this.emptyValuePercent = emptyValuePercent;
    }

    /**
     * 每一个分析结果都必须包含空值分布
     */
    protected double emptyValuePercent = 0D;
    protected Integer totalSize = 0;

    /**
     * 获取数据量
     */
    public long getCount() {
        return this.totalSize;
    }



}
