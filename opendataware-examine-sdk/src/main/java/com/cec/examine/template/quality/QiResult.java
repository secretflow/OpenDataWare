package com.cec.examine.template.quality;
import com.cec.examine.reversibility.ReversibilityResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对一个质检规则，表级别质检或者所有列级别汇总信息
 */
public class QiResult extends ReversibilityResult {

    public boolean isTableLevel() {
        return isTableLevel;
    }

    public void setTableLevel(boolean tableLevel) {
        isTableLevel = tableLevel;
    }

    //是否是表级别的结果
    private boolean isTableLevel = false;

    public String getQiCode() {
        return qiCode;
    }

    public void setQiCode(String qiCode) {
        this.qiCode = qiCode;
    }

    /**
     * qiCode 质检规则编码
     */
    private String qiCode;

    public String getQiName() {
        return qiName;
    }

    public void setQiName(String qiName) {
        this.qiName = qiName;
        this.qiProblemDesc = qiName + "有误";
    }

    public String getQiType() {
        return qiType;
    }

    public void setQiType(String qiType) {
        this.qiType = qiType;
    }

    private String qiName;
    private String qiType;

    /**
     质检总数据量（质检数据量检查项）
     problemNum的分母，被检查单元格的个数
     */
    private Integer qiDataTotal = 0;
    /**
     ● 问题数量（每个检查项问题数量和）
     */
    private Integer problemNum = 0;

    /**
     ● 质检数据量（不同于质检总数据量，总数量可能大于单个规则检查项的质检数量）
     实际质检的总行数，一般是行数，除非特殊例子，比如南京-江苏这种级联规则，那就是所有江苏的行数作为总行数了。
     */
    private Integer qiDataNum = 0;

    /**
     ● 质检异常信息说明
     */
    private String problemDesc;

    /**
     ● 资源表总数据量（增量质检新加，用于代替qi_data_num取最大值来作为表数据量的展示）
     */
    private Integer tableDataCount = 0;

    public Integer getQiDataTotal() {
        return qiDataTotal;
    }

    public void setQiDataTotal(Integer qiDataTotal) {
        this.qiDataTotal = qiDataTotal;
    }
    public void addQiDataTotal(Integer qiDataTotal) {
        this.qiDataTotal += qiDataTotal;
    }


    public Integer getProblemNum() {
        return problemNum;
    }

    public void setProblemNum(Integer problemNum) {
        this.problemNum = problemNum;
    }

    public void addProblemNum(Integer problemNum) {
        this.problemNum += problemNum;
    }

    public Integer getQiDataNum() {
        return qiDataNum;
    }

    public void setQiDataNum(Integer qiDataNum) {
        this.qiDataNum = qiDataNum;
    }
    public void maxQiDataNum(Integer qiDataNum) {
        if(this.qiDataNum < qiDataNum) this.qiDataNum = qiDataNum;
    }


    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public Integer getTableDataCount() {
        return tableDataCount;
    }

    public void setTableDataCount(Integer tableDataCount) {
        this.tableDataCount = tableDataCount;
    }

    String qiProblemDesc;//质检算子名称

    public String getQiProblemDesc() {
        return qiProblemDesc;
    }

    public void setQiProblemDesc(String qiProblemDesc) {
        this.qiProblemDesc = qiProblemDesc;
    }

    private Map<String,QiColumnResult> qiColumnResults = new HashMap<>();

    public void addQiColumnResults(QiColumnResult qiColumnResult) {
        //累加合并QiColumnResult的逻辑
        String colName = qiColumnResult.getColEnName() + "-" + qiColumnResult.getColChName();
        qiColumnResults.put(colName, qiColumnResult);
    }

    private List<QiColumnResult> ruleCheckResultDetailDTOList;

    //表级别的保存对象
    private RuleCheckResultDetailDTO ruleCheckResultDetailDTO;
    public RuleCheckResultDetailDTO getRuleCheckResultDetailDTO() {
        return ruleCheckResultDetailDTO;
    }
    public void setRuleCheckResultDetailDTO(RuleCheckResultDetailDTO ruleCheckResultDetailDTO) {
        this.ruleCheckResultDetailDTO = ruleCheckResultDetailDTO;
    }

    /**
     * 整合列级别质检结果
     */
    public void calculateQiColumnResultDetailDTO() {
        ruleCheckResultDetailDTOList = new ArrayList<>();
        for(String colName : qiColumnResults.keySet()) {
            QiColumnResult qiColumnResult = qiColumnResults.get(colName);
            Integer problemNum = qiColumnResult.getProblemNum();
            if(problemNum == 0) {
                //如果发现没有质检问题，那么就需要把质检问题描述设置为空
                qiColumnResult.setQiProblemDesc("");
            }
            ruleCheckResultDetailDTOList.add(qiColumnResult);
        }
    }

    /**
     * 整合表级别的质检结果
     */
    public void calculateQiTableResultDetailDTO() {
        this.ruleCheckResultDetailDTO = new RuleCheckResultDetailDTO();
        this.ruleCheckResultDetailDTO.setQiDataTotal(this.qiDataTotal);
        this.ruleCheckResultDetailDTO.setQiDataNum(this.qiDataNum);
        this.ruleCheckResultDetailDTO.setProblemNum(this.problemNum);
        if(this.problemNum > 0) {
            this.ruleCheckResultDetailDTO.setProblemDesc(this.problemDesc);
        } else {
            this.ruleCheckResultDetailDTO.setProblemDesc("");
        }
    }

    /**
     * 表级别的结果字段定义
     */
    public static class RuleCheckResultDetailDTO {
        String problemDesc;
        Integer problemNum;
        Integer qiDataNum;
        Integer qiDataTotal;

        public String getProblemDesc() {
            return problemDesc;
        }

        public void setProblemDesc(String problemDesc) {
            this.problemDesc = problemDesc;
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

        public Integer getQiDataTotal() {
            return qiDataTotal;
        }

        public void setQiDataTotal(Integer qiDataTotal) {
            this.qiDataTotal = qiDataTotal;
        }

    }


}
