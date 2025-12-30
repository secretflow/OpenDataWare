package com.cec.example.DataWareStandard;

import com.cec.modeling.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 组态元件
 * c1，c2，c3
 * v11，v12，v13
 * v21，v22，v23
 */
public class ComposedDataWareExample  extends DCExample<ComposedDataWare>{

    public ComposedDataWareExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<ComposedDataWare> getDataWares() {
        List<ComposedDataWare> result = new ArrayList<>();
        List<DataCell> dataCells = new ArrayList<>();
        String [] lines = this.fileContent.split(System.lineSeparator());
        String [] col = lines[0].split(",");
        for(int i = 1; i<lines.length; i++){
            ComposedDataWare composedDataWare = new ComposedDataWare();
            composedDataWare.setIndex(i);
            String [] cells = lines[i].split(",",-1);
            for(int j=0;j<cells.length;j++){
                DataCell dataCell = new DataCell();
                dataCell.setValueDataType(DataWareDataType.STRING);
                dataCell.setValueColumn(col[j]);
                dataCell.setValue(cells[j]);
                dataCells.add(dataCell);
            }
            composedDataWare.setValues(dataCells);
            result.add(composedDataWare);
        }
        return result;
    }
}