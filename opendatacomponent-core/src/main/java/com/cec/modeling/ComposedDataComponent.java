package com.cec.modeling;

/**
 * 组态元件数据结构<p/>
 * c1，c2，c3<p/>
 * v11，v12，v13<p/>
 * v21，v22，v23<p/>
 * @author koala
 * @see com.cec.modeling.DataComponent
 */
public class ComposedDataComponent extends DataComponent {
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
     * 记录起始索引值
     */
    private int index;//这一行的索引值
}