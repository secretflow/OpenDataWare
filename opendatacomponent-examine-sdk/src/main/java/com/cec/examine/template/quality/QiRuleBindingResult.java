package com.cec.examine.template.quality;

import com.cec.examine.recognize.Recognize;
import com.cec.examine.util.jsonparser.ObjectToJSON;

import java.util.Map;

/**
 * 列->规则的配置对
 */
public class QiRuleBindingResult {

    public static String ALL_COLUMN = "all_column";//所有列适用
    public static String TABLE_LEVEL = "table_level";//表级别适用

    public Recognize getRecognize() {
        return recognize;
    }

    public void setRecognize(Recognize recognize) {
        this.recognize = recognize;
    }

    private Recognize recognize;

    public static Map<String, String> getBasicLabelNameToColumnName() {
        return basicLabelNameToColumnName;
    }

    public static void setBasicLabelNameToColumnName(Map<String, String> basicLabelNameToColumnName) {
        QiRuleBindingResult.basicLabelNameToColumnName = basicLabelNameToColumnName;
    }

    private static Map<String, String> basicLabelNameToColumnName;

    String columnName;//列名称

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public QiConfig getQiConfig() {
        return qiConfig;
    }

    public void setQiConfig(QiConfig qiConfig) {
        this.qiConfig = qiConfig;
    }

    QiConfig qiConfig;//绑定的质检规则配置

    public String toString() {
        return ObjectToJSON.toJSONString(this);
    }
}
