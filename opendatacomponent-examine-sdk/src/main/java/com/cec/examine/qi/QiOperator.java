package com.cec.examine.qi;

import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.jsonparser.ObjectToJSON;

import java.util.Map;

public abstract class QiOperator {

    /**
     * 单元格总数
     */
    protected Integer countOfCells = 0;

    /**
     * 存在问题的单元格数量
     */
    protected Integer countOfProblems = 0;

    protected String qiCode;

    public String getQiName() {
        return qiName;
    }

    public void setQiName(String qiName) {
        this.qiName = qiName;
    }

    public String getQiType() {
        return qiType;
    }

    public void setQiType(String qiType) {
        this.qiType = qiType;
    }

    protected String qiName;
    protected String qiType;


    public Integer getCountOfCells() {
        return countOfCells;
    }

    public Integer getCountOfProblems() {
        return countOfProblems;
    }

    public String getQiCode() {
        return qiCode;
    }

    public void setQiCode(String qiCode) {
        this.qiCode = qiCode;
    }

    /**
     * 输入单元格
     * @param
     */
    public abstract void put(String column, Map<String, TableCellPojo> line);

    /**
     * 输入一行数据
     * @param line
     */
    public abstract void put(Map<String, TableCellPojo> line);

    /**
     * 执行质检算子
     * @return
     */
    public abstract void execute();
    public String toString() {
        return ObjectToJSON.toJSONString(this);
    }
}
