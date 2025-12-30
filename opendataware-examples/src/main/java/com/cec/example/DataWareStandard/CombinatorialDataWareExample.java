package com.cec.example.DataWareStandard;

import com.cec.modeling.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合态元件
 *  c0（主体字段），c1（查询列），c2（数据列），c3（数据列）
 *  k1，v11，v12，v13
 *  k2，v21，v22，v23
 *  k3，v21，v32，v33
 *  调用条件可以是：c0，c0+c1，c1
 *  当查询为c0（k1）时，返回v12，v13
 *  当查询为c0+c1（k2，v21）时，返回v22，v23
 *  当查询为c1（v21）时，返回
 *  k2，v22，v23
 *  k3，v32，v33
 */
public class CombinatorialDataWareExample extends DCExample<CombinatorialDataWare> {

    public CombinatorialDataWareExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<CombinatorialDataWare> getDataWares() {
        List<CombinatorialDataWare> result = new ArrayList<>();
        List<DataCell> dataCells = new ArrayList<>();
        String [] lines = this.fileContent.split(System.lineSeparator());
        String [] col = lines[0].split(",");
        List<String> QueryColumns = new ArrayList<>();
        QueryColumns.add(col[3]);
        for(int i = 1; i<lines.length; i++){
            CombinatorialDataWare combinatorialDataWare = new CombinatorialDataWare();
            combinatorialDataWare.setIndex(i);
            combinatorialDataWare.setQueryColumn(QueryColumns);
            String [] cells = lines[i].split(",",-1);
            for(int j=0;j<cells.length;j++){
                DataCell dataCell = new DataCell();
                if(j==0)
                combinatorialDataWare.setMainKey(dataCell);
                dataCell.setValueDataType(DataWareDataType.STRING);
                dataCell.setValueColumn(col[j]);
                dataCell.setValue(cells[j]);
                dataCells.add(dataCell);
            }
            combinatorialDataWare.setValues(dataCells);
            result.add(combinatorialDataWare);
        }
        return result;
    }

}