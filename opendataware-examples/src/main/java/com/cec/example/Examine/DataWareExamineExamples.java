package com.cec.example.Examine;
import com.cec.examine.template.ComponentProductionCheckTemplate;
import com.cec.flink.DataWareConvert;
import com.cec.modeling.CombinatorialDataWare;
import com.cec.modeling.ComposedDataWare;
import com.cec.modeling.ModalDataWare;
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

public class DataWareExamineExamples {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //如果是流式数据则需要设置checkpoint
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_CONSISTENCY_MODE, CheckpointingMode.EXACTLY_ONCE);
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(10));

        //加载创建catalog的SQL语句
        //TODO：如果无法正确加载资源表数据，那么请核对程序运行目录，在resource/DataWareModelDemo/TableCatalogs/createResourceTableCatalog.sql 中修改正确的path即可。
        ResourceFile resourceFile = new ResourceFile("/DataWareModelDemo/TableCatalogs");
        tableEnv.executeSql(resourceFile.getContent("createCsvResourceTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createModalDataWareTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createComposedDataWareTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createCombinatorialDataWareTableCatalog.sql"));
        ResourceFile resourceFileProduction = new ResourceFile("/DataWareModelDemo/ProductionModels");
        //模态元件生产
        Table dc1 = tableEnv.sqlQuery(resourceFileProduction.getContent("ModalDataWareProductionModel.sql"));
        DataWareConvert DataWareConvert1 = new DataWareConvert(dc1.execute());
        List<ModalDataWare> modalDataWares = DataWareConvert1.getModalDataWares("enterprise_code");
        //组态元件生产
        Table dc2 = tableEnv.sqlQuery(resourceFileProduction.getContent("ComposedDataWareProductionModel.sql"));
        DataWareConvert DataWareConvert2 = new DataWareConvert(dc2.execute());
        List<ComposedDataWare> composedDataWares = DataWareConvert2.getComposedDataWares();
        //组合态元件生产
        Table dc3 = tableEnv.sqlQuery(resourceFileProduction.getContent("CombinatorialDataWareProductionModel.sql"));
        DataWareConvert DataWareConvert3 = new DataWareConvert(dc3.execute());
        List<String> queryCols = new ArrayList<>();
        queryCols.add("ratio");
        List<CombinatorialDataWare> combinatorialDataWares = DataWareConvert3.getCombinatorialDataWares("enterprise_code", queryCols);
        //元件生产审核
        long start = System.currentTimeMillis();
        Map<String, Object> result1 = ComponentProductionCheckTemplate.checkModalDataWares(modalDataWares);
        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms.");
        System.out.println(result1);

        start = System.currentTimeMillis();
        Map<String, Object> result2 = ComponentProductionCheckTemplate.checkComposedDataWares(composedDataWares);
        end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms.");
        System.out.println(result2);
    }
}
