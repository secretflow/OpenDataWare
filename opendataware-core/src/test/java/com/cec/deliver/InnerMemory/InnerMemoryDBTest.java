package com.cec.deliver.InnerMemory;

import com.cec.modeling.DataCell;
import com.cec.modeling.DataWareDataType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InnerMemoryDBTest {

    @Test
    public void testInnerMemoryDB() {
        DataCell dataCell = new DataCell();
        dataCell.setValueColumn("c1");
        dataCell.setValue("v1");
        dataCell.setValueDataType(DataWareDataType.STRING);
        ArrayList<DataCell> a = new ArrayList<>();
        a.add(dataCell);
        InnerMemoryDB innerMemoryDB = new InnerMemoryDB();
        innerMemoryDB.putDataCell("k1",a);
        assertEquals("v1", innerMemoryDB.getDataCells("k1").get(0).getValue());
    }
}
