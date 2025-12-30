package com.cec.modeling;
/**
 * 模态元件数据结构<p/>
 * 身份证号，信用分数
 * 110107199901019823，482
 * @author koala
 * @see com.cec.modeling.DataWare
 */
public class ModalDataWare extends DataWare {

    /**
     * 获取标签值
     * @return 标签值
     */
    public DataCell getValue() {
        return value;
    }

    /**
     * 设置标签值
     * @param value 标签值
     */
    public void setValue(DataCell value) {
        this.value = value;
    }
    /**
     * 获取主体字段
     * @return 主体字段
     */
    public DataCell getMainKey() {
        return mainKey;
    }
    /**
     * 设置主体字段
     * @param mainKey 主体字段
     */
    public void setMainKey(DataCell mainKey) {
        this.mainKey = mainKey;
    }
    /**
     * 主体字段
     */
    private DataCell mainKey;
    /**
     * 标签值
     */
    private DataCell value;
}