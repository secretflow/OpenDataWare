package com.cec.examine.qi;

import com.cec.examine.recognize.RecognizeDictionary;
import com.cec.examine.template.TableCellPojo;

import java.util.List;
import java.util.Map;

public class QiOperatorEnumValue extends QiOperator {

    RecognizeDictionary recognizeDictionary;

    public QiOperatorEnumValue(List<String> words) {
        recognizeDictionary = new RecognizeDictionary("", "值域有效性", words);
    }


    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        String cell = String.valueOf(line.get(column).getValue());
        this.countOfCells ++;
        String type = recognizeDictionary.getSingleType(cell);
        if(type == null) countOfProblems ++;
    }

    @Override
    public void execute() {

    }

    @Override
    public void put(Map<String, TableCellPojo> line) {

    }

}
