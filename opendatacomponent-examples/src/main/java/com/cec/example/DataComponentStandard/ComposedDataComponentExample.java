package com.cec.example.DataComponentStandard;

import com.cec.modeling.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 组态元件
 * c1，c2，c3
 * v11，v12，v13
 * v21，v22，v23
 */
public class ComposedDataComponentExample  extends DCExample<ComposedDataComponent>{

    public ComposedDataComponentExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<ComposedDataComponent> getDataComponents() {
        List<ComposedDataComponent> result = new ArrayList<>();
        List<DataCell> dataCells = new ArrayList<>();
        String [] lines = this.fileContent.split(System.lineSeparator());
        String [] col = lines[0].split(",");
        for(int i = 1; i<lines.length; i++){
            ComposedDataComponent composedDataComponent = new ComposedDataComponent();
            composedDataComponent.setIndex(i);
            String [] cells = lines[i].split(",",-1);
            for(int j=0;j<cells.length;j++){
                DataCell dataCell = new DataCell();
                dataCell.setValueDataType(DataComponentDataType.STRING);
                dataCell.setValueColumn(col[j]);
                dataCell.setValue(cells[j]);
                dataCells.add(dataCell);
            }
            composedDataComponent.setValues(dataCells);
            result.add(composedDataComponent);
        }
        return result;
    }
}