package com.cec.examine.template;
import com.cec.examine.util.jsonparser.model.JsonObject;
import com.cec.modeling.DataCell;
import com.cec.modeling.DataWare;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个代表一个结构化表格的一个单元格
 */
public class TableCellPojo {
    /**
     * 针对元件的包裹类
     * @param DataWare
     * @return
     */
    public static Map<String,TableCellPojo> tableCellPojoMapWrapper(DataWare DataWare) {
        List<DataCell> dataCells = DataWare.getValues();
        Map<String,TableCellPojo> tableCellPojoMap = new LinkedHashMap<>();
        for(DataCell dataCell: dataCells){
            TableCellPojo tableCellPojo = new TableCellPojo();
            tableCellPojo.setValue(dataCell.getValue());
            tableCellPojo.setDataType(String.valueOf(dataCell.getValueDataType()));
            tableCellPojoMap.put(dataCell.getValueColumn(), tableCellPojo);
        }
        return tableCellPojoMap;
    }
    /**
     * 包装方法
     * 目前支持特定的json格式，仅两种
     * 1. {"id": "10","stu_idcard": "x","score": "80"}
     * 2. {"id": { "englishColumnName":"XXX", "chineseColumnName":"XXX","comments":"XXX","value":"XXX" },"stu_idcard": "x","score": "80"}
     * @param jsonObject
     * @return
     */
    public static Map<String,TableCellPojo> tableCellPojoMapWrapper(JsonObject jsonObject) {
        Map<String,TableCellPojo> r = new LinkedHashMap<>();
        for(String key: jsonObject.keySet()) {
            Object cell = jsonObject.get(key);
            if(cell instanceof JsonObject) {
                String englishColumnName =  ((JsonObject) cell).containsKey("englishColumnName") ? String.valueOf(((JsonObject) cell).get("englishColumnName")) : null;
                String chineseColumnName = ((JsonObject) cell).containsKey("chineseColumnName") ? String.valueOf(((JsonObject) cell).get("chineseColumnName")) : null;
                String comments = ((JsonObject) cell).containsKey("comments") ? String.valueOf(((JsonObject) cell).get("comments")) : null;
                Object value = ((JsonObject) cell).containsKey("value") ? ((JsonObject) cell).get("value") : null;
                TableCellPojo tableCellPojo = new TableCellPojo();
                tableCellPojo.setEnglishColumnName(englishColumnName);
                tableCellPojo.setComments(comments);
                tableCellPojo.setChineseColumnName(chineseColumnName);
                //目前系统仅仅会使用注释作为中文字段名称，所以直接赋值给中文字段，下游也处理中文字段即可。
                if(comments != null)
                    tableCellPojo.setChineseColumnName(comments);
                tableCellPojo.setValue(value);
                r.put(key, tableCellPojo);
            } else {
                //如果只有一个值，那么就直接赋值即可
                TableCellPojo tableCellPojo = new TableCellPojo();
                tableCellPojo.setValue(cell);
                r.put(key, tableCellPojo);
            }
        }
        return r;
    }
    String englishColumnName;//列英文名称

    public String getEnglishColumnName() {
        return englishColumnName;
    }

    public void setEnglishColumnName(String englishColumnName) {
        this.englishColumnName = englishColumnName;
    }

    public String getChineseColumnName() {
        return chineseColumnName;
    }

    public void setChineseColumnName(String chineseColumnName) {
        this.chineseColumnName = chineseColumnName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    String chineseColumnName;//列中文名称
    String comments;//注释
    String dataType;//数据库类型
    Object value;//值
}
