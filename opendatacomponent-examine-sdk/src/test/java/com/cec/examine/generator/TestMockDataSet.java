package com.cec.examine.generator;

import com.cec.examine.template.TableCellPojo;
import com.cec.examine.util.MockDataSet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;


/**
 * 固定仿真测试
 */
public class TestMockDataSet {

    @Test
    public void testMockDataExample(){
        String r = new MockDataSet(MockDataSet.getExampleBuilder()).dataMockJSON(2);
        System.out.println(r);
    }

    @Test
    public void testMockDataPrint(){
        List<Map<String, TableCellPojo>> r = new MockDataSet(MockDataSet.getExampleBuilder()).mockListMap(20);
        MockDataSet.print(r);
    }


}
