package com.cec.examine.template.recognition;

import com.cec.examine.reversibility.ReversibilityResult;

public class RecognitionCofig extends ReversibilityResult {

    private String code;//识别代码
    private String category;//类别
    private String classification;//分类
    private String lableName;//信息项
    private Integer priority;//优先级
    private String EnglishColumName;//英文列名称
    private String ChineseColumName;//中文列名称
    private String regexPattern;//正则表达式
    private String nerCode;//NER识别的代码
    private String javaFunctionName;//Java函数
    private String strategy;//1.字段识别 2.内容识别：格式+ 内容 3.内容+字段识别
    private boolean isPersonal ;//个人信息：1 是， 0 否
    private String level; // 平台级别：3  核心 2  重要 1  一般
    private String basicLableName;//基础信息项

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getEnglishColumName() {
        return EnglishColumName;
    }

    public void setEnglishColumName(String englishColumName) {
        EnglishColumName = englishColumName;
    }

    public String getChineseColumName() {
        return ChineseColumName;
    }

    public void setChineseColumName(String chineseColumName) {
        ChineseColumName = chineseColumName;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getNerCode() {
        return nerCode;
    }

    public void setNerCode(String nerCode) {
        this.nerCode = nerCode;
    }

    public String getJavaFunctionName() {
        return javaFunctionName;
    }

    public void setJavaFunctionName(String javaFunctionName) {
        this.javaFunctionName = javaFunctionName;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public void setPersonal(boolean personal) {
        isPersonal = personal;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBasicLableName() {
        return basicLableName;
    }

    public void setBasicLableName(String basicLableName) {
        this.basicLableName = basicLableName;
    }

}
