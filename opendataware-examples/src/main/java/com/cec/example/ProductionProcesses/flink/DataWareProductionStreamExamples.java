package com.cec.example.ProductionProcesses.flink;
import com.cec.utils.ResourceFile;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.core.execution.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;

public class DataWareProductionStreamExamples {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);
        //如果是流式数据则需要设置checkpoint
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_CONSISTENCY_MODE, CheckpointingMode.EXACTLY_ONCE);
        tableEnv.getConfig().getConfiguration().set(
                CheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(10));

        //加载创建catalog的SQL语句
        //TODO：如果无法正确加载资源表数据，那么请核对程序运行目录，在resource/DataWareModelDemo/TableCatalogs/createResourceTableCatalog.sql 中修改正确的path即可。
        ResourceFile resourceFile = new ResourceFile("/DataWareModelDemo/TableCatalogs");
        tableEnv.executeSql(resourceFile.getContent("createDataGenResourceTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createModalDataWareTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createComposedDataWareTableCatalog.sql"));
        tableEnv.executeSql(resourceFile.getContent("createCombinatorialDataWareTableCatalog.sql"));
        ResourceFile resourceFileProduction = new ResourceFile("/DataWareModelDemo/ProductionModels");
        //模态元件生产
        Table dc1 = tableEnv.sqlQuery(resourceFileProduction.getContent("ModalDataWareProductionModel.sql"));
        dc1.executeInsert("dc1");

        //组态元件生产
        Table dc2 = tableEnv.sqlQuery(resourceFileProduction.getContent("ComposedDataWareProductionModel.sql"));
        dc2.executeInsert("dc2");

        //组合态元件生产
        Table dc3 = tableEnv.sqlQuery(resourceFileProduction.getContent("CombinatorialDataWareProductionModel.sql"));
        dc3.executeInsert("dc3");

    }
}
