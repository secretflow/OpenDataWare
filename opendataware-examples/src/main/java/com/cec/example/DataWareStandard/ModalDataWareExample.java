package com.cec.example.DataWareStandard;
import com.cec.modeling.DataCell;
import com.cec.modeling.DataWareDataType;
import com.cec.modeling.ModalDataWare;
import java.util.ArrayList;
import java.util.List;

/**
 * 模态元件
 * 身份证号，信用分数
 * 110107199901019823，482
 */
public class ModalDataWareExample extends DCExample<ModalDataWare> {

    public ModalDataWareExample(String resourceFileName) {
        super(resourceFileName);
    }

    @Override
    public List<ModalDataWare> getDataWares() {
        List<ModalDataWare> result = new ArrayList<>();
        String [] lines = this.fileContent.split(System.lineSeparator());
        String [] col = lines[0].split(",");
        for(int i = 1; i<lines.length; i++){
            String [] cells = lines[i].split(",",-1);
            ModalDataWare modalDataWare = new ModalDataWare();
            //第一列的值是MainKey，主体标识
            DataCell dataCell0 = new DataCell();
            dataCell0.setValueDataType(DataWareDataType.STRING);
            dataCell0.setValueColumn(col[0]);
            dataCell0.setValue(cells[0]);
            modalDataWare.setMainKey(dataCell0);
            //第二列是特征值
            DataCell dataCell1 = new DataCell();
            dataCell1.setValueDataType(DataWareDataType.STRING);
            dataCell1.setValueColumn(col[1]);
            dataCell1.setValue(cells[1]);
            modalDataWare.setValue(dataCell1);
            List<DataCell> dataCells = new ArrayList<>();
            dataCells.add(dataCell0);
            dataCells.add(dataCell1);
            modalDataWare.setValues(dataCells);
            result.add(modalDataWare);
        }
        return result;
    }
}