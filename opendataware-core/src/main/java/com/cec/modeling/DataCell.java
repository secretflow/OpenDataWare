package com.cec.modeling;
/**
 * 数据元件业务包装类
 */
public class DataCell {

    /**
     * 获取列名
     * @return 列名
     */
    public String getValueColumn() {
        return valueColumn;
    }

    /**
     * 设置列名
     * @param valueColumn 列名
     */
    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    /**
     * 获取值
     * @return 值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置值
     * @param value 值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 获取值类型
     * @return 值类型
     */
    public DataWareDataType getValueDataType() {
        return valueDataType;
    }

    /**
     * 设置值类型
     * @param valueDataType 值类型
     */
    public void setValueDataType(DataWareDataType valueDataType) {
        this.valueDataType = valueDataType;
    }

    /**
     * 列名
     */
    private String valueColumn;
    /**
     * 值
     */
    private Object value;
    /**
     * 值类型
     */
    private DataWareDataType valueDataType;

}