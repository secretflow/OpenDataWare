package com.cec.examine.qi;

import com.cec.examine.recognize.Recognize;
import com.cec.examine.template.TableCellPojo;

import java.util.Map;

/**
 * 如果采用SDK，能够绑定规则的的一定是经过了安全识别的，所以这个算子检查完一定是正常的。
 */
public class QiOperatorFormatCheck extends QiOperator {

    private Recognize recognize;
    private String basicLabelName;

    public QiOperatorFormatCheck(String basicLabelName, Recognize recognize) {
        this.recognize = recognize;
        this.basicLabelName = basicLabelName;
    }
    @Override
    public void put(String column, Map<String, TableCellPojo> line) {
        this.countOfCells++;
    }

    @Override
    public void put(Map<String, TableCellPojo> line) {
        this.countOfCells++;
    }

    @Override
    public void execute() {

    }

}
