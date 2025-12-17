package com.cec.modeling;
import java.util.List;

/**
 * 组合态元件数据结构<p/>
 *  c0（主体字段），c1（查询列），c2（数据列），c3（数据列）<p/>
 *  k1，v11，v12，v13<p/>
 *  k2，v21，v22，v23<p/>
 *  k3，v21，v32，v33<p/>
 *  调用条件可以是：c0，c0+c1，c1<p/>
 *  当查询为c0（k1）时，返回v12，v13<p/>
 *  当查询为c0+c1（k2，v21）时，返回v22，v23<p/>
 *  当查询为c1（v21）时，返回<p/>
 *  k2，v22，v23<p/>
 *  k3，v32，v33<p/>
 *  @author koala
 *  @see com.cec.modeling.DataComponent
 */
public class CombinatorialDataComponent extends DataComponent {

    /**
     * 记录起始索引值
     */
    private int index;

    /**
     * 获取记录起始索引值
     * @return 记录起始索引值
     */
    public int getIndex() {
        return index;
    }

    /**
     * 设置记录起始索引值
     * @param index 记录起始索引值
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 获取可查询字段列表
     * @return 可查询字段列表
     */
    public List<String> getQueryColumn() {
        return QueryColumn;
    }

    /**
     * 设置可查询字段列表
     * @param queryColumn 可查询字段列表
     */
    public void setQueryColumn(List<String> queryColumn) {
        QueryColumn = queryColumn;
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
     * 可查询列
     */
    private List<String> QueryColumn;
}