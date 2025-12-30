package com.cec.deliver.InnerMemory;
import com.cec.modeling.DataCell;

import java.util.*;

/**
 * 模拟存储元件的内存数据库
 * <p/>
 * 存储结构：
 * <p/>
 * 模态元件：
 * <p/>
 * 元件ID+MainKey=>行数据;
 * <p/>
 * 元件ID+index=>多行数据;
 * <p/>
 * 组态元件：
 * <p/>
 * 元件ID+index=>多行数据;
 * <p/>
 * 组合态元件：
 * <p/>
 * 元件ID+MainKey=>行数据;
 * <p/>
 * 元件ID+index=>多行数据;
 * <p/>
 * @author koala
 */
public class InnerMemoryDB {

    private Map<String, List<DataCell>> dataIndex;
    public InnerMemoryDB() {
        this.dataIndex = new HashMap<>();
    }

    /**
     * 写入一行数据
     * @param key
     * @param dataCell
     */
    public void putDataCell(String key, List<DataCell> dataCell) {
         List<DataCell> dataCells = dataIndex.get(key);
         if(dataCells == null) {
             dataCells = new ArrayList<>();
         }
        dataIndex.put(key, dataCell);
    }

    /**
     * 读取一行元件
     * @param key
     */
    public List<DataCell> getDataCells(String key) {
        return dataIndex.get(key);
    }


    public Map<String, List<DataCell>> getData() {
        return this.dataIndex;
    }

}
