package com.cec.example.Delivery;

import com.alibaba.fastjson2.JSON;
import com.cec.deliver.DataComponentDelivery;
import com.cec.deliver.InnerMemory.DataComponentDeliveryInnerMemory;
import com.cec.deliver.InnerMemory.InnerMemoryDB;
import com.cec.example.DataComponentStandard.CombinatorialDataComponentExample;
import com.cec.example.DataComponentStandard.ComposedDataComponentExample;
import com.cec.example.DataComponentStandard.ModalDataComponentExample;
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
        ModalDataComponentExample modalDataComponentExample = new ModalDataComponentExample("DataComponentDemo/ModalDataComponentDemo.csv");
        ComposedDataComponentExample composedDataComponentExample = new ComposedDataComponentExample("DataComponentDemo/ComposedDataComponentDemo.csv");
        CombinatorialDataComponentExample combinatorialDataComponentExample = new CombinatorialDataComponentExample("DataComponentDemo/CombinatorialDataComponentDemo.csv");
        //写入内存数据库
        List<ModalDataComponent> modalDataComponents = modalDataComponentExample.getDataComponents();
        List<ComposedDataComponent> composedDataComponents = composedDataComponentExample.getDataComponents();
        List<CombinatorialDataComponent> combinatorialDataComponents = combinatorialDataComponentExample.getDataComponents();
        InnerMemoryDB innerMemoryDB = new InnerMemoryDB();
        String modalDataComponentId = "DCA";
        String composedDataComponentId = "DCB";
        String combinatorialDataComponentId = "DCC";
        int i = 0;
        for(DataComponent dc: modalDataComponents) {
            ModalDataComponent dataComponent = (ModalDataComponent)dc;
            String mainKey = (String)dataComponent.getMainKey().getValue();
            innerMemoryDB.putDataCell( modalDataComponentId + mainKey, dataComponent.getValues());
            innerMemoryDB.putDataCell(modalDataComponentId + i++, dataComponent.getValues());
        }
        i = 0;
        for(DataComponent dc: composedDataComponents) {
            ComposedDataComponent dataComponent = (ComposedDataComponent)dc;
            innerMemoryDB.putDataCell(composedDataComponentId + i++, dataComponent.getValues());
        }
        i = 0;
        for(DataComponent dc: combinatorialDataComponents) {
            CombinatorialDataComponent dataComponent = (CombinatorialDataComponent)dc;
            String mainKey = (String)dataComponent.getMainKey().getValue();
            innerMemoryDB.putDataCell(combinatorialDataComponentId + mainKey, dataComponent.getValues());
            innerMemoryDB.putDataCell(combinatorialDataComponentId + i++, dataComponent.getValues());
        }

        System.out.println(JSON.toJSONString(innerMemoryDB.getData()));

        //交付查询例子
        DataComponentDeliveryInnerMemory dataComponentDelivery = new DataComponentDeliveryInnerMemory();
        dataComponentDelivery.setInnerMemoryDB(innerMemoryDB);
        //通过元件ID、主体标识查询模态元件
        ModalDataComponent modalDataComponent = dataComponentDelivery.getModalDataComponent(modalDataComponentId, "900000000000000000");
        System.out.println("通过元件ID、主体标识查询模态元件");
        System.out.println(JSON.toJSONString(modalDataComponent));
        //通过元件ID查询组态元件
        List<ComposedDataComponent> composedDataComponent = dataComponentDelivery.getComposedDataComponent(composedDataComponentId, 0,1);
        System.out.println("通过元件ID查询组态元件");
        System.out.println(JSON.toJSONString(composedDataComponent));
        //通过元件ID、主体标识查询组合态元件
        CombinatorialDataComponent combinatorialDataComponent = dataComponentDelivery.getCombinatorialDataComponentByMainKey(combinatorialDataComponentId, "900000000000000000");
        System.out.println("通过元件ID、主体标识查询组合态元件");
        System.out.println(JSON.toJSONString(combinatorialDataComponent));
        //批量获取元件
        List<DataComponent> composedDataComponentList = dataComponentDelivery.getDataComponentBath(modalDataComponentId,0,2);
        System.out.println("批量获取元件");
        System.out.println(JSON.toJSONString(composedDataComponentList));
        //通过列查询组合态元件
        Map<String,String> query = new HashMap<>();
        List<CombinatorialDataComponent> combinatorialDataComponentList = dataComponentDelivery.getCombinatorialDataComponentByQueryColumns(combinatorialDataComponentId,query,0,2);
        System.out.println("通过列查询组合态元件(内存版本不实现)");
        System.out.println(JSON.toJSONString(combinatorialDataComponentList));
    }
}
