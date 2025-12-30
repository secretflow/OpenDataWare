package com.cec.example.Delivery.netty.service;
import com.cec.deliver.netty.plugin.InnerMemoryDeliveryPlugin;
import com.cec.example.DataWareStandard.CombinatorialDataWareExample;
import com.cec.example.DataWareStandard.ComposedDataWareExample;
import com.cec.example.DataWareStandard.ModalDataWareExample;
import com.cec.flink.ProductionManager;
import com.cec.modeling.CombinatorialDataWare;
import com.cec.modeling.ComposedDataWare;
import com.cec.modeling.DataWare;
import com.cec.modeling.ModalDataWare;
import com.cec.production.ProductionModel;
import com.cec.utils.ResourceFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionDemo extends Thread {

    public static void initCallAsProduction() {
        String DC_ID = "001";
        ProductionModel model = new ProductionModel();
        ResourceFile resourceFileSQL = new ResourceFile("/DataWareModelDemo/ProductionModels");
        String sql = resourceFileSQL.getContent("CallAsProductionDemoModel.sql");
        model.setSql(sql);
        ResourceFile resourceFile = new ResourceFile("/DataWareModelDemo/TableCatalogs");
        Map<String, String> tableCataLogs = new HashMap<>();
        tableCataLogs.put("enterprise_info_tbl",resourceFile.getContent("createHttpResourceTableListenEventCatalog.sql"));
        tableCataLogs.put("enterprise_city_tbl",resourceFile.getContent("createHttpResourceDimensionTableListenEventCatalog.sql"));
        tableCataLogs.put("request_tbl",resourceFile.getContent("createRequestTableCatalog.sql"));
        model.setSourceTableCataLogs(tableCataLogs);
        //设置Sink表信息
        model.setSinkTableName("dc001_tbl");
        model.setSinkTableCataLogs(resourceFile.getContent("createCallAsProductionModalDataWareTableCatalog.sql"));
        ProductionManager.putDcModel(DC_ID, model);
        ProductionManager.startAll();
    }

    public static void initInMemoryDelivery() {
        //读取元件样例文件
        ModalDataWareExample modalDataWareExample = new ModalDataWareExample("DataWareDemo/ModalDataWareDemo.csv");
        ComposedDataWareExample composedDataWareExample = new ComposedDataWareExample("DataWareDemo/ComposedDataWareDemo.csv");
        CombinatorialDataWareExample combinatorialDataWareExample = new CombinatorialDataWareExample("DataWareDemo/CombinatorialDataWareDemo.csv");
        //写入内存数据库
        List<ModalDataWare> modalDataWares = modalDataWareExample.getDataWares();
        List<ComposedDataWare> composedDataWares = composedDataWareExample.getDataWares();
        List<CombinatorialDataWare> combinatorialDataWares = combinatorialDataWareExample.getDataWares();
        String modalDataWareId = "DCA";
        String composedDataWareId = "DCB";
        String combinatorialDataWareId = "DCC";
        //用内存交付插件加载数据
        InnerMemoryDeliveryPlugin.loadModalDataWares(modalDataWareId,modalDataWares);
        InnerMemoryDeliveryPlugin.loadComposedDataWares(composedDataWareId,composedDataWares);
        InnerMemoryDeliveryPlugin.loadCombinatorialDataWares(combinatorialDataWareId,combinatorialDataWares);
    }
}
