package com.cec.examine.template.desensitization;

import com.cec.examine.util.jsonparser.model.JsonObject;

/**
 * 脱敏样本生成配置
 */
public class DesensitizationConfig {
    private String colNameCn;
    private String colType;
    private String comment;
    private String identifyLabel;
    private String maskMethod;
    private String maskType;
    private String maskTag;
    //探查结果JSON
    private String explorationJson;
    private Integer sensitivePersonalInformationStatus;
    private boolean isCodeMark;

    public boolean isCoverEmptyValue() {
        return coverEmptyValue;
    }

    public void setCoverEmptyValue(boolean coverEmptyValue) {
        this.coverEmptyValue = coverEmptyValue;
    }

    public boolean isCoverPeekValue() {
        return coverPeekValue;
    }

    public void setCoverPeekValue(boolean coverPeekValue) {
        this.coverPeekValue = coverPeekValue;
    }

    public boolean isCoverAllEnum() {
        return coverAllEnum;
    }

    public void setCoverAllEnum(boolean coverAllEnum) {
        this.coverAllEnum = coverAllEnum;
    }

    //覆盖空值占比
    private boolean coverEmptyValue;
    //覆盖极值
    private boolean coverPeekValue;
    //覆盖所有枚举值
    private boolean coverAllEnum;
    private maskMethodDetail maskMethodDetail;

    public DesensitizationConfig(JsonObject jsonObject) {
        if (jsonObject != null) {
            Object o = null;
            colName = (o = jsonObject.get("colName")) == null ? null : String.valueOf(o);
            colNameCn = (o = jsonObject.get("colNameCn")) == null ? null : String.valueOf(o);
            colType = (o = jsonObject.get("colType")) == null ? null : String.valueOf(o);
            comment = (o = jsonObject.get("comment")) == null ? null : String.valueOf(o);
            identifyLabel = (o = jsonObject.get("identifyLabel")) == null ? null : String.valueOf(o);
            maskMethod = (o = jsonObject.get("maskMethod")) == null ? null : String.valueOf(o);
            maskType = (o = jsonObject.get("maskType")) == null ? null : String.valueOf(o);
            maskTag = (o = jsonObject.get("maskTag")) == null ? null : String.valueOf(o);
            explorationJson = (o = jsonObject.get("explorationJson")) == null ? null : String.valueOf(o);
            sensitivePersonalInformationStatus = (o = jsonObject.get("sensitivePersonalInformationStatus")) == null ? null : (Integer) o;
            isCodeMark = (o = jsonObject.get("isCodeMark")) != null && (Boolean) o;
            coverAllEnum = (o = jsonObject.get("coverAllEnum")) != null && (Boolean) o;
            coverEmptyValue = (o = jsonObject.get("coverEmptyValue")) != null && (Boolean) o;
            coverPeekValue = (o = jsonObject.get("coverPeekValue")) != null && (Boolean) o;
            JsonObject maskMethodDetailObject = (o = jsonObject.get("maskMethodDetail")) == null ? null : (JsonObject) o;
            if (maskMethodDetailObject != null) {
                maskMethodDetail = new maskMethodDetail();
                maskMethodDetail.setMaskChar((o = maskMethodDetailObject.get("maskChar")) == null ? null : String.valueOf(o).charAt(0));
                maskMethodDetail.setMode((o = maskMethodDetailObject.get("mode")) == null ? null : String.valueOf(o));
                maskMethodDetail.setBeginIndex((o = maskMethodDetailObject.get("beginIndex")) == null ? null : (Integer) o);
                maskMethodDetail.setEndAfter((o = maskMethodDetailObject.get("endAfter")) == null ? null : (Integer) o);
            }
        }
    }

    private String colName;
    public String getExplorationJson() {
        return explorationJson;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColNameCn() {
        return colNameCn;
    }

    public void setColNameCn(String colNameCn) {
        this.colNameCn = colNameCn;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdentifyLabel() {
        return identifyLabel;
    }

    public void setIdentifyLabel(String identifyLabel) {
        this.identifyLabel = identifyLabel;
    }

    public String getMaskMethod() {
        return maskMethod;
    }

    public void setMaskMethod(String maskMethod) {
        this.maskMethod = maskMethod;
    }

    public String getMaskType() {
        return maskType;
    }

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public String getMaskTag() {
        return maskTag;
    }

    public void setMaskTag(String maskTag) {
        this.maskTag = maskTag;
    }

    public Integer getSensitivePersonalInformationStatus() {
        return sensitivePersonalInformationStatus;
    }

    public void setSensitivePersonalInformationStatus(Integer sensitivePersonalInformationStatus) {
        this.sensitivePersonalInformationStatus = sensitivePersonalInformationStatus;
    }

    public boolean isCodeMark() {
        return isCodeMark;
    }

    public void setCodeMark(boolean codeMark) {
        isCodeMark = codeMark;
    }

    public maskMethodDetail getMaskMethodDetail() {
        return maskMethodDetail;
    }

    public void setMaskMethodDetail(maskMethodDetail maskMethodDetail) {
        this.maskMethodDetail = maskMethodDetail;
    }

    public static class maskMethodDetail{
        private String mode;
        private Integer beginIndex;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Integer getBeginIndex() {
            return beginIndex;
        }

        public void setBeginIndex(Integer beginIndex) {
            this.beginIndex = beginIndex;
        }

        public Integer getEndAfter() {
            return endAfter;
        }

        public void setEndAfter(Integer endAfter) {
            this.endAfter = endAfter;
        }

        public char getMaskChar() {
            return maskChar;
        }

        public void setMaskChar(char maskChar) {
            this.maskChar = maskChar;
        }

        private Integer endAfter;
        private char maskChar;
    }
}
