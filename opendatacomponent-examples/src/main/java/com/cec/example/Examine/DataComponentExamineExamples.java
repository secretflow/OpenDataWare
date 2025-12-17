package com.cec.example.Examine;
import com.cec.examine.template.ComponentProductionCheckTemplate;
import com.cec.flink.DataComponentConvert;
import com.cec.modeling.CombinatorialDataComponent;
import com.cec.modeling.ComposedDataComponent;
import com.cec.modeling.ModalDataComponent;
import com.cec.utils.ResourceFile;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.core.execution.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataComponentExamineExamples {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //如果是流式数据则需要设置checkpoint
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_CONSISTENCY_MODE, CheckpointingMode.EXACTLY_ONCE);
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(10));

        //加载创建catalog的SQL语句
        //TODO：如果无法正确加载资源表数据，那么请核对程序运行目录，在resource/DataComponentModelDemo/TableCatalogs/createResourceTableCatalog.sql 中修改正确的path即可。
        ResourceFile resourceFile = new ResourceFile("/DataComponentModelDemo/TableCatalogs");
        tableEnv.executeSql(resourceFile.getContent("createCsvResourceTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createModalDataComponentTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createComposedDataComponentTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createCombinatorialDataComponentTableCatalog.sql"));
        ResourceFile resourceFileProduction = new ResourceFile("/DataComponentModelDemo/ProductionModels");
        //模态元件生产
        Table dc1 = tableEnv.sqlQuery(resourceFileProduction.getContent("ModalDataComponentProductionModel.sql"));
        DataComponentConvert dataComponentConvert1 = new DataComponentConvert(dc1.execute());
        List<ModalDataComponent> modalDataComponents = dataComponentConvert1.getModalDataComponents("enterprise_code");
        //组态元件生产
        Table dc2 = tableEnv.sqlQuery(resourceFileProduction.getContent("ComposedDataComponentProductionModel.sql"));
        DataComponentConvert dataComponentConvert2 = new DataComponentConvert(dc2.execute());
        List<ComposedDataComponent> composedDataComponents = dataComponentConvert2.getComposedDataComponents();
        //组合态元件生产
        Table dc3 = tableEnv.sqlQuery(resourceFileProduction.getContent("CombinatorialDataComponentProductionModel.sql"));
        DataComponentConvert dataComponentConvert3 = new DataComponentConvert(dc3.execute());
        List<String> queryCols = new ArrayList<>();
        queryCols.add("ratio");
        List<CombinatorialDataComponent> combinatorialDataComponents = dataComponentConvert3.getCombinatorialDataComponents("enterprise_code", queryCols);
        //元件生产审核
        long start = System.currentTimeMillis();
        Map<String, Object> result1 = ComponentProductionCheckTemplate.checkModalDataComponents(modalDataComponents);
        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms.");
        System.out.println(result1);

        start = System.currentTimeMillis();
        Map<String, Object> result2 = ComponentProductionCheckTemplate.checkComposedDataComponents(composedDataComponents);
        end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms.");
        System.out.println(result2);
    }
}
