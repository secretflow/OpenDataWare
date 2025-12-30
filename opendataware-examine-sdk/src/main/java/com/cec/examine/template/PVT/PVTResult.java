package com.cec.examine.template.PVT;

import com.cec.examine.reversibility.ReversibilityResult;

/**
 * 黄暴恐检查，一个列刚碰到敏感词就会跳出来
 */
public class PVTResult extends ReversibilityResult {

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getHitWords() {
        return hitWords;
    }

    public void setHitWords(String hitWords) {
        this.hitWords = hitWords;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    private String columnName;//列名
    private String hitWords;//关键词
    private Long lineNumber;//行号
}
