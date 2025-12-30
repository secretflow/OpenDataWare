package com.cec.examine.qi;

import com.cec.examine.template.TableCellPojo;

import java.util.Map;

public class QiOperatorDataType extends QiOperator {


    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        String cell = String.valueOf(line.get(column).getValue());

    }

    @Override
    public void execute() {

    }

    @Override
    public void put(Map<String, TableCellPojo> line) {

    }

}
