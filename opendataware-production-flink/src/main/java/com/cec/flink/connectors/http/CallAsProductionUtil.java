package com.cec.flink.connectors.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 调用即生产工具类
 * @author koala
 */
public class CallAsProductionUtil {

    private static StreamTableEnvironment tableEnv;
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    /**
     * 配置环境
     */
    public static void configEnv() {
        StreamExecutionEnvironment env;
        EnvironmentSettings settings;
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();
        tableEnv = StreamTableEnvironment.create(env, settings);
        Configuration configuration = tableEnv.getConfig().getConfiguration();
        configuration.setString("table.exec.state.ttl", "5000");//设置状态存活时间
        //如果是流式数据则需要设置checkpoint，Http调用模式不需要开启，CHECKPOINTING_INTERVAL设置过长会影响实时性，但设置过短会影响快照性能
        //tableEnv.getConfig().getConfiguration().set(CheckpointingOptions.CHECKPOINTING_CONSISTENCY_MODE, CheckpointingMode.EXACTLY_ONCE);
        //tableEnv.getConfig().getConfiguration().set(CheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(1));

    }

    //批量建catalogs
    public static void createCatalogs(List<String> catalogSQLs) {
        for (String catalogSQL : catalogSQLs) {
            tableEnv.executeSql(catalogSQL);
        }
    }

    /**
     * 提交SQL
     * @param sql 生产SQL
     */
    public static void submitSql(String sql) {
        Table table = tableEnv.sqlQuery(sql);
        TableResult tableResult = table.execute();
        SQLTask sqlTask = new SQLTask(tableResult);
        sqlTask.start();
    }

    /**
     * 采用Sink的方式
     */
    public static void submitSql(String sql, String sinkTable) {
        Table table = tableEnv.sqlQuery(sql);
        table.executeInsert(sinkTable);
    }

    public static class SQLTask extends Thread {
        private TableResult tableResult;
        public SQLTask(TableResult tableResult) {
            this.tableResult = tableResult;
        }

        public void run() {
            try (CloseableIterator<Row> iterator = tableResult.collect()) {
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    // 将数据放入队列
                    Set<String> fieldNames = row.getFieldNames(true);
                    JSONObject resultJSON = new JSONObject();
                    String requestId = null;
                    if (fieldNames != null) {
                        for (String fieldName : fieldNames) {
                            String value = row.getField(fieldName).toString();
                            if (fieldName.equals("requestId")) {
                                requestId = value;
                            }
                            resultJSON.put(fieldName, value);
                        }
                    }
                    //先清空在写入
                    if(requestId != null && requestId.hashCode() % 5 == 0) {
                        CallEventListener.clearResultJSONStrings();
                    }
                    CallEventListener.putResultJSONString(requestId, resultJSON.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
