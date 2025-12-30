package com.cec.examine.template.recognition;

import com.cec.examine.reversibility.ReversibilityResult;

/**
 * 安全识别结果单元，描述列名对应的信息项
 */
public class RecognitionPair extends ReversibilityResult {

    String columName;//列

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName;
    }

    public String getBasicLableName() {
        return basicLableName;
    }

    public void setBasicLableName(String basicLableName) {
        this.basicLableName = basicLableName;
    }

    public Long getCountOfPositive() {
        return countOfPositive;
    }

    public void setCountOfPositive(Long countOfPositive) {
        this.countOfPositive = countOfPositive;
    }

    public Long getCountOfRecords() {
        return countOfRecords;
    }

    public void setCountOfRecords(Long countOfRecords) {
        this.countOfRecords = countOfRecords;
    }

    String basicLableName;//最终信息项

    Long countOfPositive;//信息项正例个数

    Long countOfRecords;//总样本条数

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    private String pattern;//正则表达式

    private String code;//识别规则的编码

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String taskType;//1：个人数据   2：行业数据'

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIndustryTypeStr() {
        return industryTypeStr;
    }

    public void setIndustryTypeStr(String industryTypeStr) {
        this.industryTypeStr = industryTypeStr;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getParentNames() {
        return parentNames;
    }

    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }

    public String getSensitivePersonalInformationStatus() {
        return sensitivePersonalInformationStatus;
    }

    public void setSensitivePersonalInformationStatus(String sensitivePersonalInformationStatus) {
        this.sensitivePersonalInformationStatus = sensitivePersonalInformationStatus;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    private String dataType;//1.公共数据  2.企业数据',

    private String industryTypeStr;//所属行业
    private String labelName;//信息标签

    private String parentNames;//类别/分类拼接而成

    private String sensitivePersonalInformationStatus;//1：是，0：否'
    private String securityLevel;//等级

    private String comments;//注释

    public String getComments() {
        return comments;
    }

    public void setComments(String comment) {
        this.comments = comment;
    }

}
