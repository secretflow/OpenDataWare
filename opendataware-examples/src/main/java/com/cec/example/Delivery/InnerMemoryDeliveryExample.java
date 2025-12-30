package com.cec.example.Delivery;

import com.alibaba.fastjson2.JSON;
import com.cec.deliver.DataWareDelivery;
import com.cec.deliver.InnerMemory.DataWareDeliveryInnerMemory;
import com.cec.deliver.InnerMemory.InnerMemoryDB;
import com.cec.example.DataWareStandard.CombinatorialDataWareExample;
import com.cec.example.DataWareStandard.ComposedDataWareExample;
import com.cec.example.DataWareStandard.ModalDataWareExample;
import com.cec.modeling.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用内置内存数据库的交付方式作为交付样例
 */
public class InnerMemoryDeliveryExample {

    public static void main(String[] args) {
        //读取元件样例文件
        ModalDataWareExample modalDataWareExample = new ModalDataWareExample("DataWareDemo/ModalDataWareDemo.csv");
        ComposedDataWareExample composedDataWareExample = new ComposedDataWareExample("DataWareDemo/ComposedDataWareDemo.csv");
        CombinatorialDataWareExample combinatorialDataWareExample = new CombinatorialDataWareExample("DataWareDemo/CombinatorialDataWareDemo.csv");
        //写入内存数据库
        List<ModalDataWare> modalDataWares = modalDataWareExample.getDataWares();
        List<ComposedDataWare> composedDataWares = composedDataWareExample.getDataWares();
        List<CombinatorialDataWare> combinatorialDataWares = combinatorialDataWareExample.getDataWares();
        InnerMemoryDB innerMemoryDB = new InnerMemoryDB();
        String modalDataWareId = "DCA";
        String composedDataWareId = "DCB";
        String combinatorialDataWareId = "DCC";
        int i = 0;
        for(DataWare dc: modalDataWares) {
            ModalDataWare DataWare = (ModalDataWare)dc;
            String mainKey = (String)DataWare.getMainKey().getValue();
            innerMemoryDB.putDataCell( modalDataWareId + mainKey, DataWare.getValues());
            innerMemoryDB.putDataCell(modalDataWareId + i++, DataWare.getValues());
        }
        i = 0;
        for(DataWare dc: composedDataWares) {
            ComposedDataWare DataWare = (ComposedDataWare)dc;
            innerMemoryDB.putDataCell(composedDataWareId + i++, DataWare.getValues());
        }
        i = 0;
        for(DataWare dc: combinatorialDataWares) {
            CombinatorialDataWare DataWare = (CombinatorialDataWare)dc;
            String mainKey = (String)DataWare.getMainKey().getValue();
            innerMemoryDB.putDataCell(combinatorialDataWareId + mainKey, DataWare.getValues());
            innerMemoryDB.putDataCell(combinatorialDataWareId + i++, DataWare.getValues());
        }

        System.out.println(JSON.toJSONString(innerMemoryDB.getData()));

        //交付查询例子
        DataWareDeliveryInnerMemory DataWareDelivery = new DataWareDeliveryInnerMemory();
        DataWareDelivery.setInnerMemoryDB(innerMemoryDB);
        //通过元件ID、主体标识查询模态元件
        ModalDataWare modalDataWare = DataWareDelivery.getModalDataWare(modalDataWareId, "900000000000000000");
        System.out.println("通过元件ID、主体标识查询模态元件");
        System.out.println(JSON.toJSONString(modalDataWare));
        //通过元件ID查询组态元件
        List<ComposedDataWare> composedDataWare = DataWareDelivery.getComposedDataWare(composedDataWareId, 0,1);
        System.out.println("通过元件ID查询组态元件");
        System.out.println(JSON.toJSONString(composedDataWare));
        //通过元件ID、主体标识查询组合态元件
        CombinatorialDataWare combinatorialDataWare = DataWareDelivery.getCombinatorialDataWareByMainKey(combinatorialDataWareId, "900000000000000000");
        System.out.println("通过元件ID、主体标识查询组合态元件");
        System.out.println(JSON.toJSONString(combinatorialDataWare));
        //批量获取元件
        List<DataWare> composedDataWareList = DataWareDelivery.getDataWareBath(modalDataWareId,0,2);
        System.out.println("批量获取元件");
        System.out.println(JSON.toJSONString(composedDataWareList));
        //通过列查询组合态元件
        Map<String,String> query = new HashMap<>();
        List<CombinatorialDataWare> combinatorialDataWareList = DataWareDelivery.getCombinatorialDataWareByQueryColumns(combinatorialDataWareId,query,0,2);
        System.out.println("通过列查询组合态元件(内存版本不实现)");
        System.out.println(JSON.toJSONString(combinatorialDataWareList));
    }
}
