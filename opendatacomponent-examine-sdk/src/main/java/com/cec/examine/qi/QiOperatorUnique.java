package com.cec.examine.qi;

import com.cec.examine.template.TableCellPojo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QiOperatorUnique extends QiOperator {

    private Set<String> values = new HashSet<>();
    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        String cell = String.valueOf(line.get(column));
        this.countOfCells++;
        if(values.contains(cell)) {
            this.countOfProblems++;
            values.add(cell);
        }
    }

    @Override
    public void put(Map<String, TableCellPojo> line) {
        String cell = "";
        for(String key: line.keySet()) {
            cell += line.get(key).getValue();
        }
        this.countOfCells++;
        if(values.contains(cell)) {
            this.countOfProblems++;
            values.add(cell);
        }
    }

    @Override
    public void execute() {

    }

}
