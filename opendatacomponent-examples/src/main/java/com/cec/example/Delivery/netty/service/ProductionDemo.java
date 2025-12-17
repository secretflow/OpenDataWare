package com.cec.example.Delivery.netty.service;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.example.DataComponentStandard.CombinatorialDataComponentExample;
import com.cec.example.DataComponentStandard.ComposedDataComponentExample;
import com.cec.example.DataComponentStandard.ModalDataComponentExample;
import com.cec.flink.ProductionManager;
import com.cec.modeling.CombinatorialDataComponent;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.DataComponent;
import com.cec.modeling.ModalDataComponent;
import com.cec.production.ProductionModel;
import com.cec.utils.ResourceFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionDemo extends Thread {

    public static void initCallAsProduction() {
        String DC_ID = "001";
        ProductionModel model = new ProductionModel();
        ResourceFile resourceFileSQL = new ResourceFile("/DataComponentModelDemo/ProductionModels");
        String sql = resourceFileSQL.getContent("CallAsProductionDemoModel.sql");
        model.setSql(sql);
        ResourceFile resourceFile = new ResourceFile("/DataComponentModelDemo/TableCatalogs");
        Map<String, String> tableCataLogs = new HashMap<>();
        tableCataLogs.put("enterprise_info_tbl",resourceFile.getContent("createHttpResourceTableListenEventCatalog.sql"));
        tableCataLogs.put("enterprise_city_tbl",resourceFile.getContent("createHttpResourceDimensionTableListenEventCatalog.sql"));
        tableCataLogs.put("request_tbl",resourceFile.getContent("createRequestTableCatalog.sql"));
        model.setSourceTableCataLogs(tableCataLogs);
        //设置Sink表信息
        model.setSinkTableName("dc001_tbl");
        model.setSinkTableCataLogs(resourceFile.getContent("createCallAsProductionModalDataComponentTableCatalog.sql"));
        ProductionManager.putDcModel(DC_ID, model);
        ProductionManager.startAll();
    }

    public static void initInMemoryDelivery() {
        //读取元件样例文件
        ModalDataComponentExample modalDataComponentExample = new ModalDataComponentExample("DataComponentDemo/ModalDataComponentDemo.csv");
        ComposedDataComponentExample composedDataComponentExample = new ComposedDataComponentExample("DataComponentDemo/ComposedDataComponentDemo.csv");
        CombinatorialDataComponentExample combinatorialDataComponentExample = new CombinatorialDataComponentExample("DataComponentDemo/CombinatorialDataComponentDemo.csv");
        //写入内存数据库
        List<ModalDataComponent> modalDataComponents = modalDataComponentExample.getDataComponents();
        List<ComposedDataComponent> composedDataComponents = composedDataComponentExample.getDataComponents();
        List<CombinatorialDataComponent> combinatorialDataComponents = combinatorialDataComponentExample.getDataComponents();
        String modalDataComponentId = "DCA";
        String composedDataComponentId = "DCB";
        String combinatorialDataComponentId = "DCC";
        //用内存交付插件加载数据
        InnerMemoryDeliveryPlugin.loadModalDataComponents(modalDataComponentId,modalDataComponents);
        InnerMemoryDeliveryPlugin.loadComposedDataComponents(composedDataComponentId,composedDataComponents);
        InnerMemoryDeliveryPlugin.loadCombinatorialDataComponents(combinatorialDataComponentId,combinatorialDataComponents);
    }
}
