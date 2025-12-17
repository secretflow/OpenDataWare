package com.cec.examine.template.quality;

import com.cec.examine.reversibility.ReversibilityResult;

import java.util.Set;

public class QiConfig extends ReversibilityResult {

    String qiType;//质检算子类型

    public String getQiType() {
        return qiType;
    }

    public void setQiType(String qiType) {
        this.qiType = qiType;
    }

    public String getQiCode() {
        return qiCode;
    }

    public void setQiCode(String qiCode) {
        this.qiCode = qiCode;
    }

    public String getQiName() {
        return qiName;
    }

    public void setQiName(String qiName) {
        this.qiName = qiName;
    }

    public String getBasicLableName() {
        return basicLableName;
    }

    public void setBasicLableName(String basicLableName) {
        this.basicLableName = basicLableName;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(String standardCode) {
        this.standardCode = standardCode;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    String qiCode;//质检算子编码
    String qiName;//质检算子名称
    String qiProblemDesc;//质检算子名称

    public String getQiProblemDesc() {
        return qiProblemDesc;
    }

    public void setQiProblemDesc(String qiProblemDesc) {
        this.qiProblemDesc = qiProblemDesc;
    }

    String basicLableName;//基础信息项
    String standardCode;//值域标准码
    Set<String> values;//值域
}
