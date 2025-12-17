package com.cec.modeling;

import java.util.List;

/**
 * 数据元件数据结构基类
 * @author koala
 */
public class DataComponent {

    /**
     * 数据记录
     */
    private List<DataCell> values;

    /**
     * 获取数据记录
     * @return 数据记录
     */
    public List<DataCell> getValues() {
        return values;
    }

    /**
     * 设置数据记录
     * @param values 数据记录
     */
    public void setValues(List<DataCell> values) {
        this.values = values;
    }

    /**
     * 获取元件元数据
     * @return 元件元数据
     */
    public DataComponentMeta getDataComponentMeta() {
        return dataComponentMeta;
    }

    /**
     * 设置元件元数据
     * @param dataComponentMeta 元件元数据
     */
    public void setDataComponentMeta(DataComponentMeta dataComponentMeta) {
        this.dataComponentMeta = dataComponentMeta;
    }

    /**
     * 元件元数据
     */
    protected DataComponentMeta dataComponentMeta;
}