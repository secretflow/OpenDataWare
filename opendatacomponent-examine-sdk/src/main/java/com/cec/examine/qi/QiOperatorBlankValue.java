package com.cec.examine.qi;

import com.cec.examine.template.TableCellPojo;

import java.util.Map;

/**
 * 空值检测算子
 */
public class QiOperatorBlankValue extends QiOperator {


    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        String cell = String.valueOf(line.get(column).getValue());
        this.countOfCells ++;
        if(cell == null || "null".equals(cell.trim()) || "".equals(cell.trim()) || "NULL".equals(cell.trim()))
            this.countOfProblems++;
    }

    @Override
    public void put(Map<String, TableCellPojo> line) {

    }

    @Override
    public void execute() {

    }


}
