package com.cec.examine.template.quality;

import com.cec.examine.reversibility.ReversibilityResult;

/**
 * 列级别质检详情信息
 */
public class QiColumnResult extends ReversibilityResult {
    public String getColChName() {
        return colChName;
    }

    public void setColChName(String colChName) {
        this.colChName = colChName;
    }

    public String getColEnName() {
        return colEnName;
    }

    public void setColEnName(String colEnName) {
        this.colEnName = colEnName;
    }

    public Integer getProblemNum() {
        return problemNum;
    }

    public void setProblemNum(Integer problemNum) {
        this.problemNum = problemNum;
    }

    public Integer getQiDataNum() {
        return qiDataNum;
    }

    public void setQiDataNum(Integer qiDataNum) {
        this.qiDataNum = qiDataNum;
    }

    public String getProblemRate() {
        return problemRate;
    }

    public void setProblemRate(String problemRate) {
        this.problemRate = problemRate;
    }

    /**
     * 列中文名称
     */
    private String colChName;
    /**
     * 列英文名称
     */
    private String colEnName;

    /**
     * 问题数量
     */
    private Integer problemNum;

    /**
     * 质检数据量
     */
    private Integer qiDataNum;

    /**
     * 异常率，eg: 10.25%
     */
    private String problemRate;

    String qiProblemDesc;//质检算子名称

    public String getQiProblemDesc() {
        return qiProblemDesc;
    }

    public void setQiProblemDesc(String qiProblemDesc) {
        this.qiProblemDesc = qiProblemDesc;
    }


}
